package com.jary.eval.heuralg;

import com.google.common.collect.Lists;
import com.jary.eval.constant.URAPConstant;
import com.jary.eval.entity.AlgTypeEnum;
import com.jary.eval.entity.Solution;
import com.jary.eval.entity.StatisticalResult;
import com.jary.eval.exception.AlgException;
import com.jary.eval.problem.Siap;
import com.jary.eval.utils.DateUtils;
import com.jary.eval.utils.FileUtils;
import com.jary.eval.utils.excel.Common;
import com.jary.eval.utils.excel.ExcelUtil;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Fantasy
 * @date 2018/3/1 14:09
 * @description
 */
public class SiapSolve implements Runnable {

    private int instanceNo;//问题实例编号
    private int runtime;//运行次数
    private Siap problem;
    private AlgTypeEnum algType;

    public SiapSolve(){

    }

    public SiapSolve(int instanceNo, int runtime, Siap problem, AlgTypeEnum algType){
        this.instanceNo = instanceNo;
        this.runtime = runtime;
        this.problem = problem;
        this.algType = algType;
    }

    public void Start(int instanceNo, int runtime, Siap problem, AlgTypeEnum algType){

        /************ 运行结果 *****************/
        AbstractAPopAlg alg = null;
        List<Solution> results = Lists.newArrayList();
        long start = System.currentTimeMillis();
        for(int i=0;i<runtime;i++){
            if(algType.getCode() == AlgTypeEnum.DE.getCode()){
                alg = new DEAlg(instanceNo,problem);
            }else if(algType.getCode() == AlgTypeEnum.WWO.getCode()){
                alg = new WWOAlg(instanceNo,problem);
            }else if(algType.getCode() == AlgTypeEnum.FADE.getCode()){
                alg = new FADEAlg(instanceNo,problem);
            }else if(algType.getCode() == AlgTypeEnum.DNSPSO.getCode()){
                alg = new DNSPSOAlg(instanceNo,problem);
            }else if(algType.getCode() == AlgTypeEnum.DEDNSPSO.getCode()){
                alg = new DEDNSPSOAlg(instanceNo,problem);
            }else if(algType.getCode() == AlgTypeEnum.PSO.getCode()){
                alg = new PSOAlg(instanceNo,problem);
            }

            Solution sol = alg.SolveF();
            results.add(sol);
        }
        long end = System.currentTimeMillis();

        /************ 统计结果 *************/
        double sum = 0.0;//适应度和
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double mean = 0;
        double std = 0;
        double totaltime = (end - start) / 1000.0;//秒
        double avgtime = totaltime / results.size();

        Solution best = null, worst = null;
        for(int i=0;i<results.size();i++){
            Solution sol = results.get(i);
            sum += sol.getValue();

            if(Double.compare(min, sol.getValue())>0){
                min = sol.getValue();
                worst = sol;
            }
            if(Double.compare(max, sol.getValue())<0){
                max = sol.getValue();
                best = sol;
            }
        }

        mean = sum / results.size();
        for(int i=0;i<results.size();i++){
            Solution sol = results.get(i);
            std += (sol.getValue() - mean) * (sol.getValue() - mean);
        }
        std = Math.sqrt(std / (results.size() - 1));

        /************** 结果保存 **************/
        StringBuffer sb = new StringBuffer();
        sb.append("\n").append(alg.name).append("统计信息：").append("\n");
        sb.append("size = ").append(alg.size).append("\n");
        sb.append("iters = ").append(alg.iters).append("\n");
        sb.append("nfes = ").append(alg.nfes).append("\n");
        sb.append("sum = ").append(sum).append("\n");
        sb.append("min = ").append(min).append("\n");
        sb.append("max = ").append(max).append("\n");
        sb.append("mean = ").append(mean).append("\n");
        sb.append("std = ").append(std).append("\n");
        sb.append("totaltime = ").append(totaltime).append("s\n");
        sb.append("avgtime = ").append(avgtime).append("s\n");
        sb.append("最优解：").append("\n");
        sb.append(best).append("\n");
        sb.append("最差解：").append("\n");
        sb.append(worst).append("\n");
        System.out.println(sb.toString());
        String path = URAPConstant.RESULT_PATH + String.format("%02d",instanceNo) + problem.res_subpath;
        String filename = alg.name+"_"+ DateUtils.formatDate(new Date(), "yyyyMMdd") + ".txt";
        FileUtils.writeAsStr(path,filename,sb.toString());

        StatisticalResult result = new StatisticalResult(min,max,mean,std,avgtime,alg.size,alg.iters,alg.nfes);
        List<StatisticalResult> list = Lists.newArrayList();
        list.add(result);
        path = URAPConstant.RESULT_PATH  + String.format("%02d",instanceNo) + problem.res_subpath + alg.name + "_" + DateUtils.formatDate(new Date(),"yyyyMMdd") + Common.POINT + Common.OFFICE_EXCEL_2003_POSTFIX;
        try {
            new ExcelUtil().writeExcel(list,path,alg.name);
        } catch (Exception e) {
            throw new AlgException(alg.name+"结果保存异常！");
        }
    }

    @Override
    public void run() {
        Start(instanceNo,runtime,problem,algType);
    }

    public static void main(String[] args){
        int instanceNo = 2;
        int runtime = 30;
        Siap problem = Siap.generateProblem(instanceNo);
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        long start = System.currentTimeMillis();
        try{
            threadPool.execute(new SiapSolve(instanceNo,runtime,problem,AlgTypeEnum.DE));
            threadPool.execute(new SiapSolve(instanceNo,runtime,problem,AlgTypeEnum.WWO));
            threadPool.execute(new SiapSolve(instanceNo,runtime,problem,AlgTypeEnum.FADE));
            threadPool.execute(new SiapSolve(instanceNo,runtime,problem,AlgTypeEnum.DNSPSO));
            threadPool.execute(new SiapSolve(instanceNo,runtime,problem,AlgTypeEnum.DEDNSPSO));
            threadPool.execute(new SiapSolve(instanceNo,runtime,problem,AlgTypeEnum.PSO));
        }finally {
            threadPool.shutdown();

            while(true){
                if(threadPool.isTerminated()){
                    long end = System.currentTimeMillis();
                    System.out.println("总耗时："+DateUtils.AsTimeStr(end - start));
                    break;
                }
            }
        }
    }
}
