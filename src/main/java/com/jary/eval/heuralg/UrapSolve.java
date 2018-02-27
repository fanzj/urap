package com.jary.eval.heuralg;

import com.google.common.collect.Lists;
import com.jary.eval.constant.URAPConstant;
import com.jary.eval.entity.Solution;
import com.jary.eval.entity.StatisticalResult;
import com.jary.eval.entity.Wave;
import com.jary.eval.problem.Siap;
import com.jary.eval.utils.DateUtils;
import com.jary.eval.utils.FileUtils;
import com.jary.eval.utils.excel.Common;
import com.jary.eval.utils.excel.ExcelUtil;

import java.util.Date;
import java.util.List;

/**
 * @author Fantasy
 * @date 2018/2/24 18:14
 * @description 问题求解
 */
public class UrapSolve {



    public static void main(String[] args) throws Exception {
        //DEAlgSolve();
        WWOAlgSolve();


    }

    private static void DEAlgSolve() throws Exception {
        int instanceNo = 1;
        int runtime = 10;
        Siap problem = Siap.generateProblem(instanceNo);
        AbstractAPopAlg alg = null;

        /************ 运行结果 *****************/
        List<Solution> results = Lists.newArrayList();
        long start = System.currentTimeMillis();
        for(int i=0;i<runtime;i++){
            alg = new DEAlg(instanceNo,problem);
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
        sb.append("\n").append("统计信息：").append("\n");
        sb.append("sum = ").append(sum).append("\n");
        sb.append("min = ").append(min).append("\n");
        sb.append("max = ").append(max).append("\n");
        sb.append("mean = ").append(mean).append("\n");
        sb.append("std = ").append(std).append("\n");
        sb.append("totaltime = ").append(totaltime).append("\n");
        sb.append("avgtime = ").append(avgtime).append("\n");
        sb.append("最优解：").append("\n");
        sb.append(best).append("\n");
        sb.append("最差解：").append("\n");
        sb.append(worst).append("\n");
        System.out.println(sb.toString());
        String path = URAPConstant.RESULT_PATH + String.format("%02d",instanceNo) + problem.res_subpath;
        String filename = alg.name+"_"+ DateUtils.formatDate(new Date(), "yyyyMMdd") + ".txt";
        FileUtils.writeAsStr(path,filename,sb.toString());

        StatisticalResult result = new StatisticalResult(min,max,mean,std,avgtime);
        List<StatisticalResult> list = Lists.newArrayList();
        list.add(result);
        path = URAPConstant.RESULT_PATH  + String.format("%02d",1) + problem.res_subpath + alg.name + "_" + DateUtils.formatDate(new Date(),"yyyyMMdd") + Common.POINT + Common.OFFICE_EXCEL_2003_POSTFIX;
        new ExcelUtil().writeExcel(list,path,alg.name);
    }

    private static void WWOAlgSolve() throws Exception {
        int instanceNo = 1;
        int runtime = 10;
        Siap problem = Siap.generateProblem(instanceNo);
        WWOAlg alg = null;

        /************ 运行结果 *****************/
        List<Wave> results = Lists.newArrayList();
        long start = System.currentTimeMillis();
        for(int i=0;i<runtime;i++){
            alg = new WWOAlg(instanceNo,problem);
            Wave sol = alg.SolveF();
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

        Wave best = null, worst = null;
        for(int i=0;i<results.size();i++){
            Wave sol = results.get(i);
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
            Wave sol = results.get(i);
            std += (sol.getValue() - mean) * (sol.getValue() - mean);
        }
        std = Math.sqrt(std / (results.size() - 1));

        /************** 结果保存 **************/
        StringBuffer sb = new StringBuffer();
        sb.append("\n").append("统计信息：").append("\n");
        sb.append("sum = ").append(sum).append("\n");
        sb.append("min = ").append(min).append("\n");
        sb.append("max = ").append(max).append("\n");
        sb.append("mean = ").append(mean).append("\n");
        sb.append("std = ").append(std).append("\n");
        sb.append("totaltime = ").append(totaltime).append("\n");
        sb.append("avgtime = ").append(avgtime).append("\n");
        sb.append("最优解：").append("\n");
        sb.append(best).append("\n");
        sb.append("最差解：").append("\n");
        sb.append(worst).append("\n");
        System.out.println(sb.toString());
        String path = URAPConstant.RESULT_PATH + String.format("%02d",instanceNo) + problem.res_subpath;
        String filename = alg.name+"_"+ DateUtils.formatDate(new Date(), "yyyyMMdd") + ".txt";
        FileUtils.writeAsStr(path,filename,sb.toString());

        StatisticalResult result = new StatisticalResult(min,max,mean,std,avgtime);
        List<StatisticalResult> list = Lists.newArrayList();
        list.add(result);
        path = URAPConstant.RESULT_PATH  + String.format("%02d",1) + problem.res_subpath + alg.name + "_" + DateUtils.formatDate(new Date(),"yyyyMMdd") + Common.POINT + Common.OFFICE_EXCEL_2003_POSTFIX;
        new ExcelUtil().writeExcel(list,path,alg.name);
    }
}
