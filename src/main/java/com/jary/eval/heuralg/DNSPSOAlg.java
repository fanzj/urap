package com.jary.eval.heuralg;

import com.jary.eval.entity.FourTuple;
import com.jary.eval.entity.Particle;
import com.jary.eval.entity.Solution;
import com.jary.eval.entity.TwoTuple;
import com.jary.eval.problem.MSiap;
import com.jary.eval.problem.Siap;
import com.jary.eval.utils.MathUtils;

import java.util.Random;

/**
 * @author Fantasy
 * @date 2018/2/28 14:15
 * @description
 */
public class DNSPSOAlg extends PSOAlg{
    protected double pr;//控制种群多样性
    protected double pns;//邻域搜索
    protected int MK;//邻域K值
    protected double r1,r2,r3,r4,r5,r6;

    public DNSPSOAlg(){
        super();
    }

    public DNSPSOAlg(int instanceNo, MSiap problem){
        super(instanceNo, problem);
    }

    @Override
    public void SetParameters() {
        super.SetParameters();
        this.name = "DNSPSO";
        this.cRate1 = 1.49445;
        this.cRate2 = 1.49445;
        this.pr = 0.9;
        this.pns = 0.6;
        this.MK = 2;
    }

    @Override
    public void Evolve() {
        this.Calculate();
        for(int i=0;i<pop.length;i++){
            this.Move(pop[i],i);
        }
        this.RandomR();
        for(int i=0;i<pop.length;i++){
            //this.RandomR();
            this.NeighborSearch(pop[i],i);
        }
    }

    /**
     * 邻域搜索策略
     * @param Pi
     * @param index
     */
    protected void NeighborSearch(Particle Pi,int index){
        double r = Rand.nextDouble();
        if (Double.compare(r,pns)<=0)
        {
            Particle Li = LNS(Pi, index);
            Particle Gi = GNS(Pi, index);

            //从Pi,Li,Gi中选出最好的作为Pi
            if (Pi.compareTo(Li) < 0 || Pi.compareTo(Gi) < 0) {
                if (Li.compareTo(Gi) > 0) {
                    Pi = Li;
                } else {
                    Pi = Gi;
                }
            }
            pop[index] = Pi;
        }

        //更新pbest和pbest
        if (Pi.compareTo(Pi.pBest) > 0)
        {
            Solution pbest = new Solution();
            pbest.content = Pi.content;
            pbest.setValue(Pi.getValue());
            Pi.pBest = pbest;
            if (Pi.compareTo(best) > 0)
                best = Pi.clone();
        }
    }

    /// <summary>
    /// 局部邻域搜索策略LNS
    /// </summary>
    /// <param name="s"></param>
    /// <param name="index"></param>
    /// <returns></returns>
    protected Particle LNS(Particle s, int index)
    {
        //找出k邻域集
        int[] kneighbor = new int[2 * MK];
        int k = 0;
        for (int i = index - MK; i <= (index + MK); i++)
        {
            int pos = (i + pop.length) % pop.length;
            if(pos != index)
            {
                kneighbor[k++] = pos;
            }
        }

        TwoTuple<Integer,Integer> tuple = RandomSelectTwoIndicies(k);
        int c = tuple.first, d = tuple.second;
        Particle Li = s.clone();
        for (int j = 0; j < dimension; j++)
        {
            Li.velocity[j] = s.velocity[j];
            Li.content[j] = (int) Math.round(r1 * s.content[j] + r2 * s.pBest.content[j] + r3 * (pop[kneighbor[c]].content[j] - pop[kneighbor[d]].content[j]));

            if (Li.content[j] < problem.lowers[j] || Li.content[j] > problem.uppers[j]) {
                Li.content[j] = (int) Math.round(problem.lowers[j] + Rand.nextDouble() * (problem.uppers[j] - problem.lowers[j]));
                Li.velocity[j] = (int) Math.round(0.5 * (Rand.nextDouble() * (problem.uppers[j] - problem.lowers[j]) - s.content[j]));
            }
        }
        Evaluate(Li);
        return Li;

    }

    /// <summary>
    /// 全局邻域搜索策略
    /// </summary>
    /// <param name="s"></param>
    /// <param name="index"></param>
    /// <returns></returns>
    protected Particle GNS(Particle s, int index)
    {
        TwoTuple<Integer, Integer> tuple = RandomSelectTwoIndicies(pop.length, index);
        int e = tuple.first, f = tuple.second;
        Particle Gi = s.clone();
        for (int j = 0; j < dimension; j++)
        {
            Gi.velocity[j] = s.velocity[j];
            Gi.content[j] = (int) Math.round(r4 * s.content[j] + r5 * best.content[j] + r6 * (pop[e].content[j] - pop[f].content[j]));

            if (Gi.content[j] < problem.lowers[j] || Gi.content[j] > problem.uppers[j]) {
                Gi.content[j] = (int) Math.round(problem.lowers[j] + Rand.nextDouble() * (problem.uppers[j] - problem.lowers[j]));
                Gi.velocity[j] = (int) Math.round(0.5 * (Rand.nextDouble() * (problem.uppers[j] - problem.lowers[j]) - s.content[j]));
            }
        }
        Evaluate(Gi);
        return Gi;
    }

    @Override
    public Particle Move(Particle s, int index) {
        Particle pit2 = pop[index].clone();
        super.Move(pit2,index);


        //生成tp粒子
        Particle tp = getParticleTp(index,pit2);

        //从pit2和tp中选择好的一个
        //s = SelectBetterOne(pit2, tp,s);
        if (pit2.compareTo(pop[index])>0 || tp.compareTo(pop[index])>0)
        {
            if (pit2.compareTo(tp) > 0)
            {
                pop[index] = pit2;
            }
            else
            {
                pop[index] = tp;
            }
        }

        //更新pbest和gbest
        if (pop[index].compareTo(pop[index].pBest) > 0)
        {
            Solution pbest = new Solution();
            pbest.setValue(pop[index].getValue());
            pbest.content = pop[index].content.clone();
            pop[index].pBest = pbest;
            if (pop[index].compareTo(best) > 0)
                best = pop[index].clone();
        }
        return pop[index];
    }

    private Particle SelectBetterOne(Particle pit2, Particle tp,Particle s) {
        if (pit2.compareTo(s)>0 || tp.compareTo(s)>0)
        {
            if (pit2.compareTo(tp) > 0)
            {
                s = pit2;
            }
            else
            {
                s = tp;
            }
        }
        return s;
    }

    private Particle getParticleTp(int index, Particle pit2) {
        Particle tp = new Particle();
        int[] tx = new int[dimension];
        int[] tv = new int[dimension];
        for (int d = 0; d < dimension; d++)
        {
            double r = Rand.nextDouble();
            if (r < pr)
            {
                tx[d] = pit2.content[d];
            }
            else
            {
                tx[d] = pop[index].content[d];
            }
            tv[d] = pit2.velocity[d];
        }
        tp.content = tx;
        tp.velocity = tv;
        Evaluate(tp);
        Solution tppbest = new Solution();
        tppbest.content = tx;
        tppbest.setValue(tp.getValue());
        tp.pBest = tppbest;
        return tp;
    }

    /// <summary>
    /// 生成邻域搜索策略的随机数，每代生成一次
    /// </summary>
    protected void RandomR()
    {
        r1 = Rand.nextDouble();
        r2 = Rand.nextDouble();
        while ((r1 + r2) > 1)
        {
            r2 = Rand.nextDouble();
        }
        r3 = 1 - r1 - r2;

        r4 = Rand.nextDouble();
        r5 = Rand.nextDouble();
        while ((r4 + r5)>1)
        {
            r5 = Rand.nextDouble();
        }
        r6 = 1 - r4 - r5;

      /*  r1 = Rand.nextDouble();
        r2 = 1 - r1;
        r3 = Rand.nextGaussian();
        r3 = MathUtils.doubleAToB(-2,2);

        r4 = Rand.nextDouble();
        r5 = 1 - r4;
        r6 = Rand.nextGaussian();
        r6 = MathUtils.doubleAToB(-2,2);
*/


    }

    public static void main(String[] args){
        System.out.println("DNSPSO算法测试");
        MSiap problem = MSiap.generateProblem(1);
        PSOAlg psoAlg = new DNSPSOAlg(1,problem);
        psoAlg.SolveF();
        psoAlg.printAll(psoAlg.pop);
        System.out.println("最优解：");
        psoAlg.print(psoAlg.best);
    }


}
