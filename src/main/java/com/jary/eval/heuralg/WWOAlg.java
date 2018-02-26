package com.jary.eval.heuralg;

import com.jary.eval.entity.FourTuple;
import com.jary.eval.entity.Solution;
import com.jary.eval.entity.Wave;
import com.jary.eval.exception.AlgException;
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
    }

    public WWOAlg(int instanceNo, Siap problem){
        super(instanceNo, problem);
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
        for (int i = 0; i < size; i++) {
            Wave wave = new Wave();
            int[] content = new int[dimension];
            for(int d=0;d<dimension;d++){
                if(d%2==0){//设备
                    content[d] = Rand.nextInt(problem.K) + 1;
                }else{
                    content[d] = Rand.nextInt(problem.Q+1);
                }
            }
            wave.setDimension(dimension);
            wave.setContent(content);
            wave.setId(i);
            pop[i] = wave;
            this.Evaluate(pop[i]);
        }
        if (iters == 0) {
            iters = nfes / size;
        }


        this.beta = betaMax;
        range = new int[dimension];
        srange = new int[dimension];
        for(int i=0;i<range.length;i++){
            if(i%2==0){
                range[i] = problem.upper1 - problem.lower1;
            }else{
                range[i] = problem.upper2 - problem.lower2;
            }
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

    }

    @Override
    public void Calculate() {

    }

    public static void main(String[] args){
        System.out.println("水波优化算法测试");
        Siap problem = Siap.generateProblem(1);
        WWOAlg wwoAlg = new WWOAlg(1,problem);
        wwoAlg.Solve();
        wwoAlg.printAll(wwoAlg.pop);
        System.out.println("最优解：");
        wwoAlg.print(wwoAlg.best);
    }
}
