package com.jary.eval.heuralg;

import com.jary.eval.entity.Particle;
import com.jary.eval.entity.Solution;
import com.jary.eval.entity.ThreeTuple;
import com.jary.eval.entity.TwoTuple;
import com.jary.eval.problem.MSiap;
import com.jary.eval.problem.Siap;

/**
 * @author Fantasy
 * @date 2018/3/1 11:01
 * @description
 */
public class DEDNSPSOAlg extends DNSPSOAlg {

    private double scalingF;//变异概率
    private double crossRate;//交叉概率

    @Override
    public void SetParameters() {
        super.SetParameters();
        this.name = "DEDNSPSO";
        this.scalingF = 0.5;
        this.crossRate = 0.9;
    }

    public DEDNSPSOAlg(){
        super();
    }

    public DEDNSPSOAlg(int instanceNo, MSiap problem){
        super(instanceNo, problem);
    }


    @Override
    public Particle Move(Particle s, int index) {
        ThreeTuple<Integer,Integer,Integer> ind = RandomSelectThreeIndices(pop.length, index);
        int p1 = ind.first, p2 = ind.second, p3 = ind.third;
        Particle u = s.clone();
        int j = Rand.nextInt(dimension);
        for(int d=0;d<dimension;d++){
            if(Rand.nextDouble() < crossRate || j==d){
                u.content[d] = (int) Math.round(pop[p1].content[d] + scalingF * (pop[p2].content[d] - pop[p3].content[d]));
                if(u.content[d] < problem.lowers[d] || u.content[d] > problem.uppers[d]){
                    u.content[d] = (int) Math.round(problem.lowers[d] + Rand.nextDouble() * (problem.uppers[d] - problem.lowers[d]));
                    u.velocity[d] = (int) Math.round(0.5 * (Rand.nextDouble() * (problem.uppers[d] - problem.lowers[d]) - u.content[d]));
                }
            }
        }
        Evaluate(u);
        if(u.compareTo(s)>0){
            s = u;
        }

        //更新pbest和gbest
        if(s.compareTo(s.pBest)>0){
            Solution pbest = new Solution();
            pbest.content = s.content.clone();
            pbest.setValue(s.getValue());
            s.pBest = pbest;
            if(s.compareTo(best)>0){
                best = s.clone();
            }
        }
        return s;
    }

    @Override
    protected Particle LNS(Particle s, int index) {
        Particle Li = s.clone();
        for (int j = 0; j < dimension; j++)
        {
            /*Li.velocity[j] = (int) Math.round(wInertia * s.velocity[j] + Rand.nextDouble() * cRate1 * (s.pBest.content[j] - s.content[j]) + Rand.nextDouble() * cRate2 * (best.content[j] - s.content[j]));
            Li.content[j] += Li.velocity[j];*/
            Li.velocity[j] = s.velocity[j];
            Li.content[j] = (int) Math.round((s.content[j] + s.pBest.content[j]) / 2.0 + Rand.nextGaussian() * (s.pBest.content[j] - s.content[j]) / 2.0);

            if (Li.content[j] < problem.lowers[j] || Li.content[j] > problem.uppers[j]) {
                Li.content[j] = (int) Math.round(problem.lowers[j] + Rand.nextDouble() * (problem.uppers[j] - problem.lowers[j]));
                Li.velocity[j] = (int) Math.round(0.5 * (Rand.nextDouble() * (problem.uppers[j] - problem.lowers[j]) - s.content[j]));
            }
        }

        Evaluate(Li);
        return Li;
    }

    @Override
    protected Particle GNS(Particle s, int index) {
        Particle Gi = s.clone();
        for (int j = 0; j < dimension; j++)
        {
            //Gi.velocity[j] = (int) Math.round(wInertia * s.velocity[j] + Rand.nextDouble() * cRate1 * (s.pBest.content[j] - s.content[j]) + Rand.nextDouble() * cRate2 * (best.content[j] - s.content[j]));
            //Gi.content[j] += Gi.velocity[j];
            Gi.velocity[j] = s.velocity[j];
            Gi.content[j] = (int) Math.round((s.content[j] + best.content[j]) / 2.0 + Rand.nextGaussian() * (best.content[j] - s.content[j]) / 2.0);

            if (Gi.content[j] < problem.lowers[j] || Gi.content[j] > problem.uppers[j]) {
                Gi.content[j] = (int) Math.round(problem.lowers[j] + Rand.nextDouble() * (problem.uppers[j] - problem.lowers[j]));
                Gi.velocity[j] = (int) Math.round(0.5 * (Rand.nextDouble() * (problem.uppers[j] - problem.lowers[j]) - s.content[j]));
            }
        }
        Evaluate(Gi);
        return Gi;
    }

    public static void main(String[] args){
        System.out.println("DEDNSPSO算法测试");
        MSiap problem = MSiap.generateProblem(1);
        DEDNSPSOAlg psoAlg = new DEDNSPSOAlg(1,problem);
        psoAlg.SolveF();
        psoAlg.printAll(psoAlg.pop);
        System.out.println("最优解：");
        psoAlg.print(psoAlg.best);
    }
}
