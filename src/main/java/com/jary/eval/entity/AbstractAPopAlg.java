package com.jary.eval.entity;

import com.jary.eval.exception.AlgException;
import com.jary.eval.heuralg.AbstractPopAlg;

import java.util.Random;

/**
 * @author Fantasy
 * @date 2018/2/23 13:34
 * @description
 */
public abstract class AbstractAPopAlg<S extends Solution> extends AbstractPopAlg {

    @Override
    public void SetParameters() {
        this.Rand = new Random();
        this.size = 10;
    }

    /**
     * 种群初始化
     */
    public void Initialize() {
        if (this.size <= 0)
            throw new AlgException("未设置种群大小");
        pop = new Solution[size];
        for (int i = 0; i < size; i++) {
            this.Evaluate(pop[i]);
        }
        if (iters == 0) {
            iters = nfes / size;
        }
    }



    @Override
    public double Evaluate(Solution s) {
        return 0;
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

    public ThreeTuple<Integer,Integer,Integer> RandomSelectThreeIndices(int n,int i){
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
