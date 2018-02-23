package com.jary.eval.heuralg;

import com.jary.eval.entity.Solution;
import com.jary.eval.entity.ThreeTuple;

import java.util.Random;

/**
 * @author Fantasy
 * @date 2018/2/23 14:49
 * @description 差分进化（DE）算法
 */
public class DEAlg extends AbstractAPopAlg{

    private double scalingF;//变异概率

    private double crossRate;//交叉概率

    public DEAlg(){

    }


    @Override
    public void SetParameters() {
        super.SetParameters();
        this.scalingF = 0.5;
        this.crossRate = 0.9;

        this.size = 10;
        this.name = "DE";
        this.nfes = 10000;
        this.iters = 20;
    }

    @Override
    public void Initialize() {
        super.Initialize();
        this.UpdateBest();//更新全局最优best
    }

    public void Evolve() {
        this.Calculate();
        Solution u;
        for(int i=0;i<pop.length;i++){
            u = DiffEvolve(pop[i],i);
            if(u.compareTo(pop[i])>0){
                pop[i] = u;
            }
        }
        this.UpdateBest();
    }

    public void Calculate() {

    }

    public Solution DiffEvolve(Solution s, int index){
        ThreeTuple<Integer,Integer,Integer> ind = RandomSelectThreeIndices(pop.length, index);
        int r1 = ind.first, r2 = ind.second, r3 = ind.third;
        Solution u = s.clone();
        int j = Rand.nextInt(dimension);
        for(int d=0;d<dimension;d++){
            if(Double.compare(Rand.nextDouble(),crossRate)<0 || j==d){
                double x = pop[r1].content[d] + scalingF * (pop[r2].content[d] - pop[r3].content[d]);
                u.content[d] = (int) Math.round(x);
                if(d%2==0){//设备
                    if(u.content[d] < problem.lower1 || u.content[d] > problem.upper1){
                        x = problem.lower1 + Rand.nextDouble()*(problem.upper1 - problem.lower1);
                        u.content[d] = (int) Math.round(x);
                    }
                }else{//安检员
                    if(u.content[d] < problem.lower2 || u.content[d] > problem.upper2){
                        x = problem.lower1 + Rand.nextDouble()*(problem.upper1 - problem.lower1);
                        u.content[d] = (int) Math.round(x);
                    }
                }
            }
        }
        Evaluate(u);
        return u;
    }

    public static void main(String[] args){
        System.out.println("差分进化算法测试");
        DEAlg deAlg = new DEAlg();
        deAlg.Solve(10);
    }
}