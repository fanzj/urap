package com.jary.eval.heuralg;

import com.jary.eval.entity.FourTuple;
import com.jary.eval.entity.Wave;
import com.jary.eval.exception.AlgException;
import com.jary.eval.problem.MSiap;
import com.jary.eval.problem.Siap;
import com.jary.eval.utils.GaussRandom;

/**
 * @author Fantasy
 * @date 2018/2/26 21:22
 * @description
 */
public class WWOAlg extends AbstractAPopAlg<Wave>{

    private GaussRandom gRand;
    private int[] range;//搜索范围
    private int[] srange;//碎浪范围
    private Wave cBest;//当前种群的最优解
    private Wave cWorst;//当前种群的最差解

    private double alpha;//波长递减系数
    private int hmax;//初始波高（振幅）
    private int btimes;//碎浪个数
    private double beta;//初始碎浪范围系数
    private double betaMax;
    private double betaMin;

    public WWOAlg(){
        super();
        this.gRand = new GaussRandom();
    }

    public WWOAlg(int instanceNo, MSiap problem){
        super(instanceNo, problem);
        this.gRand = new GaussRandom();
    }

    @Override
    public void SetParameters() {
        super.SetParameters();
        this.name = "WWO";
        this.alpha = 1.0026;
        this.hmax = 12;
        this.btimes = 12;
        this.betaMax = 0.25;
        this.betaMin = 0.001;
    }

    @Override
    public void Initialize() {
        if (this.size <= 0)
            throw new AlgException("未设置种群大小");
        pop = new Wave[size];
        for (int i = 0; i < size; ) {
            Wave wave = new Wave();
            int[] content = new int[dimension];
            for(int d=0;d<dimension;d++){
                if(d%3==0){//设备
                    content[d] = Rand.nextInt(problem.K) + 1;
                }else if(d%3==1){
                    content[d] = Rand.nextInt(problem.R+1);
                }else{
                    content[d] = Rand.nextInt(problem.Q+1);
                }
            }
            wave.setDimension(dimension);
            wave.setContent(content);
            wave.setId(i);
            pop[i] = wave;
            this.Evaluate(pop[i]);

            if(pop[i].getValue()>0){
                i++;
            }
        }
        if (iters == 0) {
            iters = nfes / size;
        }


        this.beta = betaMax;
        range = new int[dimension];
        srange = new int[dimension];
        for(int i=0;i<range.length;i++){
            range[i] = problem.uppers[i] - problem.lowers[i];
            srange[i] = (int) Math.round(beta * range[i]);
        }

        FourTuple<Wave,Integer,Wave,Integer> tuple = PickBestWorst();
        this.cBest = tuple.first;
        this.cWorst = tuple.third;
        this.best = cBest.clone();
        for (int i = 0; i < pop.length; i++)
        {
            pop[i].amplitude = this.hmax;
        }
    }

    @Override
    public void Evolve() {
        for(int i=0;i<pop.length;i++){
            pop[i] = this.Surge(pop[i], i);
        }
        this.UpdateBest();
        this.Calculate();
    }

    @Override
    public void UpdateBest() {
        cBest = cWorst = pop[0];
        for(int i=1;i<pop.length;i++){
            if(pop[i].getValue() > cBest.getValue()){
                cBest = pop[i];
            }else if(pop[i].getValue() < cWorst.getValue()){
                cWorst = pop[i];
            }
        }
        if(cBest.compareTo(best)>0){
            best =  cBest.clone();
        }
    }

    @Override
    public void Calculate() {
        double r, den = (cWorst.getValue() - cBest.getValue() + epsilon);
        for(int i=0;i<pop.length;i++){
            r = Math.pow(alpha, -(cWorst.getValue() - pop[i].getValue() + epsilon) / den);
            pop[i].length *= r;
        }
        beta = betaMax - (betaMax - betaMin) * nfe / nfes;
        for(int i=0;i<range.length;i++){
            srange[i] = (int) Math.round(beta * range[i]);
        }
    }

    /**
     * 浪涌
     * @param w
     * @param index
     * @return
     */
    private Wave Surge(Wave w, int index){
        Wave w1 = Move(w, index);
        if(w1.getValue() > w.getValue()){
            if(w1.getValue() > best.getValue()){
                int times = Math.min((int)(btimes * best.getValue() / w1.getValue()), dimension / 2);
                w1 = MultiBreak(w1,times);
                best = w1.clone();
            }
            return w1;
        }else {
            if(--w.amplitude ==0 && w.getValue()<best.getValue()){
                w = Refract(w,index);
                if(w.getValue() > best.getValue()){
                    best = w.clone();
                }
            }
            return w;
        }
    }

    /**
     * 传播操作
     * @param w
     * @param index
     * @return
     */
    private Wave Move(Wave w, int index){
        Wave w1 = w.clone();
        int lower, upper;
        for(int d=0;d<dimension;d++){
            double x = (Rand.nextDouble() * 2 - 1) * w1.length * range[d];
            w1.content[d] += (int) Math.round(x);
            CheckBound(w1, d);
        }
        Evaluate(w1);
        w1.amplitude = hmax;
        return w1;
    }

    /**
     * 波浪折射
     * @param w
     * @param index
     * @return
     */
    private Wave Refract(Wave w, int index){
        Wave w1 = w.clone();
        for(int d=0;d<dimension;d++){
            double x = (w.content[d] + best.content[d]) / 2 + gRand.nextDouble() * (best.content[d] - w.content[d]) / 2;
            w1.content[d] = (int) Math.round(x);
            CheckBound(w1, d);
        }
        Evaluate(w1);
        w1.amplitude = hmax;
        w1.length = w.length * w1.getValue() / w.getValue();
        return w1;
    }

    /**
     * 碎浪
     * @param w
     * @return
     */
    private Wave Break(Wave w){
        Wave w1 = w.clone();
        int d = Rand.nextInt(dimension);
        w1.content[d] += Math.round(gRand.nextDouble() * srange[d]);
        CheckBound(w1,d);
        Evaluate(w1);
        if(w1.getValue() > w.getValue()){
            w1.amplitude = hmax;
            w1.length = w.length * w1.getValue() / w.getValue();
            if(beta > 0.003){
                beta = beta * 0.9999;
                for(int i=0;i<range.length;i++){
                    srange[i] = (int) Math.round(beta * range[i]);
                }
            }
            return w1;
        }
        return w;
    }

    /**
     * 多次碎浪
     * @param w
     * @param times
     * @return
     */
    private Wave MultiBreak(Wave w, int times){
        for(int i=0;i<times;i++){
            Wave w1 = Break(w);
            if(w1.getValue() > w.getValue())
                w = w1;
        }
        return w;
    }



    public static void main(String[] args){
        System.out.println("水波优化算法测试");
        MSiap problem = MSiap.generateProblem(1);
        WWOAlg wwoAlg = new WWOAlg(1,problem);
        wwoAlg.SolveF();
        wwoAlg.printAll(wwoAlg.pop);
        System.out.println("最优解：");
        wwoAlg.print(wwoAlg.best);
    }
}
