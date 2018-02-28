package com.jary.eval.heuralg;

import com.jary.eval.entity.Particle;
import com.jary.eval.problem.Siap;

/**
 * @author Fantasy
 * @date 2018/2/28 14:15
 * @description
 */
public class DNSPSOAlg extends PSOAlg{
    private double pr;//控制种群多样性
    private double pns;//邻域搜索
    private int MK;//邻域K值
    private double r1,r2,r3,r4,r5,r6;

    public DNSPSOAlg(){
        super();
    }

    public DNSPSOAlg(int instanceNo, Siap problem){
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
            this.NeighborSearch(pop[i],i);
        }
    }

    /**
     * 邻域搜索策略
     * @param s
     * @param index
     */
    private void NeighborSearch(Particle s,int index){
        double r = Rand.nextDouble();
        if (r <= pns)
        {
            Particle Pi = s.clone();
            Particle Li = LNS(Pi, index);
            Evaluate(Li);

            Particle Gi = GNS(Pi, index);
            Evaluate(Gi);

            //从Pi,Li,Gi中选出最好的作为Pi
            if (Pi.compareTo(Li) < 0 || Pi.compareTo(Gi) < 0)
            {
                if (Li.compareTo(Gi) > 0)
                {
                    Pi = Li;
                }
                else
                {
                    Pi = Gi;
                }
            }
            s = Pi;
        }

        //更新pbest和pbest
        if (s.compareTo(s.pBest) > 0)
        {
            s.pBest = s.clone();
            if (s.compareTo(best) > 0)
                best = s.clone();
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

        int c = Rand.nextInt(k);
        int d = Rand.nextInt(k);
        while (c == d)
        {
            d = Rand.nextInt(k);
        }

        Particle Li = s.clone();
        int[] x = new int[dimension];
        int[] v = new int[dimension];
        for (int j = 0; j < dimension; j++)
        {
            x[j] = (int) Math.round(r1 * s.content[j] + r2 * s.pBest.content[j] + r3 * (pop[kneighbor[c]].content[j] - pop[kneighbor[d]].content[j]));
            v[j] = s.velocity[j];
            if (x[j] < problem.lowers[j])
            {
                x[j] = problem.lowers[j];
                v[j] = 0;
            }
            else if (x[j] > problem.uppers[j])
            {
                x[j] = problem.uppers[j];
                v[j] = 0;
            }

        }
        Li.content = x;
        Li.velocity = v;
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
        int e = Rand.nextInt(pop.length);
        int f = Rand.nextInt(pop.length);
        while(e==index){
            e = Rand.nextInt(pop.length);
        }
        while (f == e || f == index)
        {
            f = Rand.nextInt(pop.length);
        }

        Particle Gi = s.clone();
        int[] x = new int[dimension];
        int[] v = new int[dimension];
        for (int j = 0; j < dimension; j++)
        {
            x[j] = (int) Math.round(r4 * s.content[j] + r5 * best.content[j] + r6 * (pop[e].content[j] - pop[f].content[j]));
            v[j] = s.velocity[j];
            if (x[j] < problem.lowers[j])
            {
                x[j] = problem.lowers[j];
                v[j] = 0;
            }
            else if (x[j] > problem.uppers[j])
            {
                x[j] = problem.uppers[j];
                v[j] = 0;
            }
        }
        Gi.content = x;
        Gi.velocity = v;
        return Gi;
    }

    @Override
    public Particle Move(Particle s, int index) {
        Particle pit2 = s.clone();
        super.Move(pit2,index);

        //生成tp粒子
        Particle tp = getParticleTp(s,pit2);
        //从pit2和tp中选择好的一个
        SelectBetterOne(pit2, tp,s);
        //更新pbest和gbest
        if (s.compareTo(s.pBest) > 0)
        {
            s.pBest = s.clone();
            if (s.compareTo(best) > 0)
                best = s.clone();
        }
        return s;
    }

    private Particle SelectBetterOne(Particle pit2, Particle tp,Particle s) {
        if (pit2.compareTo(s)>0 || tp.compareTo(s)>0)
        {
            if (pit2.compareTo(tp) > 0)
            {
                s = pit2.clone();
            }
            else
            {
                s = tp.clone();
            }
        }
        return s;
    }

    private Particle getParticleTp(Particle s, Particle pit2) {
        Particle tp = pit2.clone();
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
                tx[d] = s.content[d];
            }
            tv[d] = pit2.velocity[d];
        }
        tp.content = tx;
        tp.velocity = tv;
        Evaluate(tp);
        return tp;
    }

    /// <summary>
    /// 生成邻域搜索策略的随机数，每代生成一次
    /// </summary>
    private void RandomR()
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
    }

    public static void main(String[] args){
        System.out.println("DNSPSO算法测试");
        Siap problem = Siap.generateProblem(1);
        DNSPSOAlg psoAlg = new DNSPSOAlg(1,problem);
        psoAlg.SolveF();
        //psoAlg.printAll(psoAlg.pop);
        //System.out.println("最优解：");
        //psoAlg.print(psoAlg.best);
    }


}
