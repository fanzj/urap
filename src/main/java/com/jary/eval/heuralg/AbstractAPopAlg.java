package com.jary.eval.heuralg;

import com.alibaba.fastjson.JSON;
import com.jary.eval.entity.RunConfig;
import com.jary.eval.entity.Solution;
import com.jary.eval.entity.ThreeTuple;
import com.jary.eval.entity.TwoTuple;
import com.jary.eval.exception.AlgException;
import com.jary.eval.problem.Siap;
import com.jary.eval.utils.FileUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

/**
 * @author Fantasy
 * @date 2018/2/23 13:34
 * @description
 */
public abstract class AbstractAPopAlg<S extends Solution> extends AbstractPopAlg<S> {

    public AbstractAPopAlg(){
        super();
    }

    public AbstractAPopAlg(int instanceNo,Siap problem){
        super(instanceNo,problem);
    }



    @Override
    public void SetParameters() {
        this.Rand = new Random();

       /* //问题初始化
        if(instanceNo<=0)
            throw new AlgException("请先设置问题实例规模！");

        problem = new Siap();
        try {
            if(problem.GenerateProblem(instanceNo)){
                System.out.println("问题生成！准备执行。。。");
            }
        } catch (IOException e) {
            throw new AlgException("问题生成失败！");
        }*/

        this.dimension = problem.D;

        //运行参数获取
        try {
            getRunConfig(instanceNo);
        } catch (IOException e) {
            throw new AlgException("运行参数获取失败！");
        }
    }

    /**
     * 根据实例编号获取具体配置信息
     * @param instanceNo
     * @throws IOException
     */
    private void getRunConfig(int instanceNo) throws IOException {
        String runsettingStr = FileUtils.readAsStr("config/runsetting.json");
        //System.out.println(runsettingStr);
        RunConfig runConfig = JSON.parseObject(runsettingStr,RunConfig.class);
        Map<Integer, String> instance = runConfig.getInstance();
        //System.out.println(instance);
        String content = instance.get(instanceNo);
        //System.out.println(content);
        Map<String, Integer> config = JSON.parseObject(content,Map.class);
        size = config.get("size");
        nfes = config.get("nfes");
        iters = config.get("iters");

    }

   /* *//**
     * 种群初始化
     *//*
    public void Initialize() {
        if (this.size <= 0)
            throw new AlgException("未设置种群大小");
        pop = new S[size];
        ///这里开始。。。。。初始化种群
        for (int i = 0; i < size; i++) {
            S sol = new Solution();
            int[] content = new int[dimension];
            for(int d=0;d<dimension;d++){
                if(d%2==0){//设备
                    content[d] = Rand.nextInt(problem.K) + 1;
                }else{
                    content[d] = Rand.nextInt(problem.Q+1);
                }
            }
            sol.setDimension(dimension);
            sol.setContent(content);
            sol.setId(sol.hashCode());
            pop[i] = sol;
            this.Evaluate(pop[i]);
        }
        if (iters == 0) {
            iters = nfes / size;
        }
        //System.out.println("初始种群");
        //printAll(pop);
    }*/


    /**
     * 适应度值的评价
     * @param s
     * @return
     */
    @Override
    public double Evaluate(S s) {
        TwoTuple<Double, Double> evaRes = problem.Evaluate(s.getContent());
        s.setValue(evaRes.first);
        s.setTime(evaRes.second);
        this.nfe ++;
        return evaRes.first;
    }

    /**
     * 边界检验
     * @param  s 解
     * @param d 某一维
     * @return
     */
    public void CheckBound(S s, int d){
        double x = 0.0;
        if(d%2==0){//设备
            if(s.content[d] < problem.lower1 || s.content[d] > problem.upper1){
                x = problem.lower1 + Rand.nextDouble()*(problem.upper1 - problem.lower1);
                s.content[d] = (int) Math.round(x);
            }
        }else{//安检员
            if(s.content[d] < problem.lower2 || s.content[d] > problem.upper2){
                x = problem.lower2 + Rand.nextDouble()*(problem.upper2 - problem.lower2);
                s.content[d] = (int) Math.round(x);
            }
        }

    }







    /********************** 一些工具方法 ***********************/

    public int RandomSelectIndex(int n, int index) {
        int r = Rand.nextInt(n);
        while (r == index) {
            r = Rand.nextInt(n);
        }
        return r;
    }

    public TwoTuple<Integer, Integer> RandomSelectTwoIndicies(int n) {
        int r1 = Rand.nextInt(n);
        int r2 = Rand.nextInt(n);
        while (r1 == r2) {
            r2 = Rand.nextInt(n);
        }
        return new TwoTuple<Integer, Integer>(r1, r2);
    }

    public TwoTuple<Integer, Integer> RandomSelectTwoIndicies(int n, int index) {
        int r1 = Rand.nextInt(n);
        while (r1 == index) {
            r1 = Rand.nextInt(n);
        }
        int r2 = Rand.nextInt(n);
        while (r2 == index || r2 == r1) {
            r2 = Rand.nextInt(n);
        }
        return new TwoTuple<Integer, Integer>(r1,r2);
    }

    /**
     * 生成0~n之间不同于i和j的2个随机整数
     * @param n
     * @param i
     * @param j
     * @return
     */
    public TwoTuple<Integer,Integer> RandomSelectTwoIndicies(int n, int i, int j){
        int r1 = Rand.nextInt(n);
        while(r1 ==i || r1==j){
            r1 = Rand.nextInt(n);
        }
        int r2 = Rand.nextInt(n);
        while(r2==i || r2==j || r2 ==r1){
            r2 = Rand.nextInt(n);
        }
        return new TwoTuple<Integer, Integer>(r1,r2);
    }

    public ThreeTuple<Integer,Integer,Integer> RandomSelectThreeIndices(int n, int i){
        int r1 = Rand.nextInt(n);
        while(r1==i){
            r1 = Rand.nextInt(n);
        }
        int r2 = Rand.nextInt(n);
        while(r2==i || r2==r1){
            r2 = Rand.nextInt(n);
        }
        int r3 = Rand.nextInt(n);
        while(r3==i || r3==r1 || r3==r2){
            r3 = Rand.nextInt(n);
        }
        return new ThreeTuple<Integer, Integer, Integer>(r1,r2,r3);
    }

}
