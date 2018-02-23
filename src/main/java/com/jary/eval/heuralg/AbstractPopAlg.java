package com.jary.eval.heuralg;

import com.jary.eval.entity.FourTuple;
import com.jary.eval.entity.Solution;
import com.jary.eval.entity.TwoTuple;

import java.util.Random;

/**
 * Created by Fantasy on 2018/2/21.
 */
public abstract class AbstractPopAlg implements IAlg {

    public static final double epsilon = 0.00000000001;

    protected String name;//算法名称

    protected int size;//初始种群大小

    protected int nfe;//函数估值次数

    protected int nfes;//最大函数估值次数

    protected int iter;//迭代次数

    protected int iters;//最大迭代次数

    protected Solution[] pop;//种群

    protected Solution best;

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

    public Solution[] getPop() {
        return pop;
    }

    public Solution getBest() {
        return best;
    }

    public abstract void SetParameters();

    public abstract void Calculate();//更新计算控制参数（可选）

    public abstract double Evaluate(Solution s);//nfe++

    public void EvaluateAll(){
        for (Solution s:pop) {
            this.Evaluate(s);
        }
    }

    /**
     * 更新最优解，通常在Initialize或Evolve中调用
     */
    public void UpdateBest(){
        TwoTuple<Solution,Integer> tuple = PickBest();
        Solution cBest = tuple.first;
        if(Double.compare(cBest.getValue(),best.getValue())>0){
            best = cBest.clone();
        }
    }

    /**
     * 返回种群中最优解和其索引
     * @return
     */
    public  TwoTuple<Solution,Integer> PickBest(){
        if(pop==null)
            return new TwoTuple<Solution, Integer>(null,-1);
        Solution s, cBest = pop[0];
        int bId = 0,count = pop.length;
        for(int i=1;i<count;i++){
            s = pop[i];
            if(Double.compare(s.getValue(),cBest.getValue())>0){
                cBest = s;
                bId = i;
            }
        }
        return new TwoTuple<Solution, Integer>(cBest,bId);
    }

    public TwoTuple<Solution,Integer> PickWorst(){
        if(pop==null)
            return new TwoTuple<Solution, Integer>(null,-1);
        Solution s, cWorst = pop[0];
        int bId = 0,count = pop.length;
        for(int i=1;i<count;i++){
            s = pop[i];
            if(Double.compare(s.getValue(),cWorst.getValue())<0){
                cWorst = s;
                bId = i;
            }
        }
        return new TwoTuple<Solution, Integer>(cWorst,bId);
    }

    public FourTuple<Solution,Integer,Solution,Integer> PickBestWorst(){
        if(pop==null)
            return new FourTuple<Solution, Integer, Solution, Integer>(null,-1,null,-1);
        Solution s, cBest = pop[0], cWorst = pop[0];
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
        return new FourTuple<Solution, Integer, Solution, Integer>(cBest,bId,cWorst,wId);
    }

    public Solution Solve(int iters) {
        this.iters = iters;
        this.SetParameters();
        this.Initialize();
        while (iter++ < iters){
            this.Evolve();
        }
        return best;
    }

    public Solution SolveF(int nfes) {
        this.nfes = nfes;
        this.SetParameters();
        this.Initialize();
        while(nfe <= nfes){
            iter++;
            this.Evolve();
        }
        return best;
    }

}
