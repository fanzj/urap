package com.jary.eval.heuralg;

import com.google.common.collect.Lists;
import com.jary.eval.entity.FireSpark;
import com.jary.eval.entity.FourTuple;
import com.jary.eval.exception.AlgException;
import com.jary.eval.problem.Siap;

import java.util.List;

/**
 * @author Fantasy
 * @date 2018/2/27 20:25
 * @description
 */
public class FADEAlg extends AbstractAPopAlg<FireSpark>{

    /***************** FWA操作参数 ******************/
    private int Smin;
    private int Smax;
    private double Ax;
    private int Mx;
    private int Mm;
    private FireSpark cBest;
    private FireSpark cWorst;
    private List<FireSpark> R;//存放烟花及其生成的火星
    private List<FireSpark> P;//
    private int[] Si;//烟花i生成的火星数
    private double[] Ai;//烟花i爆破的振幅
    private boolean[] isSelected;

    /*************** DE操作参数 **********************/
    private double F;//变异概率
    private double Cr;//交叉概率

    public FADEAlg(){
        super();
    }

    public FADEAlg(int intanceNo, Siap problem){
        super(intanceNo, problem);
    }

    @Override
    public void SetParameters() {
        super.SetParameters();
        this.name = "FADE";
        this.Smin = 2;
        this.Smax = 20;
        this.Mm = 25;
        this.Mx = 5;
        this.Ax = Math.min(problem.Q, problem.K-1) / 7.0;
        this.F = 0.5;
        this.Cr = 0.9;
    }

    @Override
    public void Initialize() {
        if (this.size <= 0)
            throw new AlgException("未设置种群大小");
        pop = new FireSpark[size];
        for (int i = 0; i < size; i++) {
            FireSpark spark = new FireSpark();
            int[] content = new int[dimension];
            for(int d=0;d<dimension;d++){
                if(d%2==0){//设备
                    content[d] = Rand.nextInt(problem.K) + 1;
                }else{
                    content[d] = Rand.nextInt(problem.Q+1);
                }
            }
            spark.setDimension(dimension);
            spark.setContent(content);
            spark.setId(i);
            pop[i] = spark;
            this.Evaluate(pop[i]);
        }
        if (iters == 0) {
            iters = nfes / size;
        }

        FourTuple<FireSpark,Integer,FireSpark, Integer> tuple = PickBestWorst();
        this.cBest = tuple.first;
        this.cWorst = tuple.third;
        this.best = cBest.clone();

        R = Lists.newArrayList();
        P = Lists.newArrayList();
        Si = new int[size];
        Ai = new double[size];
        isSelected = new boolean[size];
    }

    @Override
    public void Evolve() {
        R.clear();
        double sumS = 0.0, sumA = 0.0;
        for(int i=0;i<pop.length;i++){//遍历种群中每个烟火
            sumS += (cBest.getValue() - pop[i].getValue());//用于计算Si
            sumA += (pop[i].getValue() - cWorst.getValue());//用于计算Ai
        }

        for(int i=0;i<pop.length;i++){//遍历种群中的每个烟火
            //计算Si和Ai
            double sval = Mm * ((cWorst.getValue() - pop[i].getValue() - epsilon) / (sumS - epsilon));
            Si[i] = (int) Math.round(sval);
            double aval = Ax * ((pop[i].getValue() - cBest.getValue() - epsilon) / (sumA - epsilon));
            Ai[i] = aval;
            if(Si[i] <Smin){
                Si[i] = Smin;
            }else if(Si[i] > Smax){
                Si[i] = Smax;
            }

            //from this position
        }
    }

    @Override
    public void Calculate() {

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
}
