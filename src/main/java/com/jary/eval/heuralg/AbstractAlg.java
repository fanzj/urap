package com.jary.eval.heuralg;

import com.jary.eval.entity.FourTuple;
import com.jary.eval.entity.Solution;
import com.jary.eval.entity.TwoTuple;

import java.util.Random;

/**
 * Created by Fantasy on 2018/2/21.
 */
public abstract class AbstractAlg<S extends Solution> implements IAlg {

    public static final double epsilon = 0.00000000001;

    protected String name;//算法名称

    protected int size;//初始种群大小

    protected int nfe;//函数估值次数

    protected int nfes;//最大函数估值次数

    protected int iter;//迭代次数

    protected int iters;//最大迭代次数

    protected S[] pop;//种群

    protected S best;

    public Random Rand;//随机数发生器

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRand(Random rand) {
        Rand = rand;
    }

    public Random getRand() {
        return Rand;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNfe() {
        return nfe;
    }

    public int getNfes() {
        return nfes;
    }

    public int getIter() {
        return iter;
    }

    public int getIters() {
        return iters;
    }

    public S[] getPop() {
        return pop;
    }

    public S getBest() {
        return best;
    }

    public void SetParameters(){
        this.size = 10;
        this.Rand = new Random();
        //////
    }

    public abstract void Calculate();//更新计算控制参数（可选）

    public abstract double Evaluate(S s);//nfe++

    public void EvaluateAll(){
        for (S s:pop) {
            this.Evaluate(s);
        }
    }

    /**
     * 更新最优解，通常在Initialize或Evolve中调用
     */
    public void UpdateBest(){
        TwoTuple<S,Integer> tuple = PickBest();
        S cBest = tuple.first;
        if(Double.compare(cBest.getValue(),best.getValue())>0){
            best = (S) cBest.clone();
        }
    }

    /**
     * 返回种群中最优解和其索引
     * @return
     */
    public  TwoTuple<S,Integer> PickBest(){
        if(pop==null)
            return new TwoTuple<S, Integer>(null,-1);
        S s, cBest = pop[0];
        int bId = 0,count = pop.length;
        for(int i=1;i<count;i++){
            s = pop[i];
            if(Double.compare(s.getValue(),cBest.getValue())>0){
                cBest = s;
                bId = i;
            }
        }
        return new TwoTuple<S, Integer>(cBest,bId);
    }

    public TwoTuple<S,Integer> PickWorst(){
        if(pop==null)
            return new TwoTuple<S, Integer>(null,-1);
        S s, cWorst = pop[0];
        int bId = 0,count = pop.length;
        for(int i=1;i<count;i++){
            s = pop[i];
            if(Double.compare(s.getValue(),cWorst.getValue())<0){
                cWorst = s;
                bId = i;
            }
        }
        return new TwoTuple<S, Integer>(cWorst,bId);
    }

    public FourTuple<S,Integer,S,Integer> PickBestWorst(){
        if(pop==null)
            return new FourTuple<S, Integer, S, Integer>(null,-1,null,-1);
        S s, cBest = pop[0], cWorst = pop[0];
        int bId = 0, wId = 0, count = pop.length;
        for(int i=1;i<count;i++){
            s = pop[i];
            if(Double.compare(s.getValue(),cBest.getValue())>0){
                cBest = s;
                bId = i;
            } else if(Double.compare(s.getValue(),cWorst.getValue())<0){
                cWorst = s;
                wId = i;
            }
        }
        return new FourTuple<S, Integer, S, Integer>(cBest,bId,cWorst,wId);
    }

    public S Solve(int iters) {
        this.iters = iters;
        while (iter++ < iters){
            this.Evolve();
        }
        return best;
    }

    public S SolveF(int nfes) {
        this.nfes = nfes;
        while(nfe <= nfes){
            iter++;
            this.Evolve();
        }
        return best;
    }

}
