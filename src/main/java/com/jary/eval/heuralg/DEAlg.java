package com.jary.eval.heuralg;

import com.jary.eval.entity.Solution;
import com.jary.eval.entity.ThreeTuple;
import com.jary.eval.exception.AlgException;
import com.jary.eval.problem.Siap;

import java.util.Random;

/**
 * @author Fantasy
 * @date 2018/2/23 14:49
 * @description 差分进化（DE）算法
 */
public class DEAlg extends AbstractAPopAlg<Solution>{

    private double scalingF;//变异概率

    private double crossRate;//交叉概率

    public DEAlg(){
        super();
    }

    public DEAlg(int instanceNo, Siap problem){
        super(instanceNo,problem);
    }





    @Override
    public void SetParameters() {
        super.SetParameters();
        this.name = "DE";
        this.scalingF = 0.5;
        this.crossRate = 0.9;
    }

    @Override
    public void Initialize() {
        if (this.size <= 0)
            throw new AlgException("未设置种群大小");
        pop = new Solution[size];
        ///这里开始。。。。。初始化种群
        for (int i = 0; i < size;) {
            Solution sol = new Solution();
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
            sol.setId(i);
            pop[i] = sol;
            this.Evaluate(pop[i]);
            if(pop[i].getValue()>0){
                i++;
            }
        }
        if (iters == 0) {
            iters = nfes / size;
        }

        this.best = PickBest().first;
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
                CheckBound(u,d);
            }
        }
        Evaluate(u);
        return u;
    }

    public static void main(String[] args){
        System.out.println("差分进化算法测试");
        Siap problem = Siap.generateProblem(1);
        DEAlg deAlg = new DEAlg(1,problem);
        deAlg.SolveF();
        deAlg.printAll(deAlg.pop);
        System.out.println("最优解：");
        deAlg.print(deAlg.best);
    }
}
