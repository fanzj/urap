package com.jary.eval.heuralg;

import com.jary.eval.entity.Particle;
import com.jary.eval.entity.Solution;
import com.jary.eval.exception.AlgException;
import com.jary.eval.problem.MSiap;
import com.jary.eval.problem.Siap;

/**
 * @author Fantasy
 * @date 2018/2/28 14:18
 * @description
 */
public class PSOAlg extends AbstractAPopAlg<Particle> {

    protected double wInertia; // 惯性权重W
    public double wmax;//惯性权重上限
    public double wmin;//惯性权重下限
    public double cRate1;//常数系数C1
    public double cRate2;//常数系数C2

    public PSOAlg(){
        super();
    }

    public PSOAlg(int instanceNo, MSiap problem){
        super(instanceNo, problem);
    }

    @Override
    public void SetParameters() {
        super.SetParameters();
        this.name = "PSO";
        this.wmax = 0.9;
        this.wmin = 0.4;
        this.cRate1 = 2;
        this.cRate2 = 2;
    }

    @Override
    public void Initialize() {
        if (this.size <= 0)
            throw new AlgException("未设置种群大小");
        pop = new Particle[size];
        for (int i = 0; i < size; ) {
            Particle sol = new Particle();
            int[] content = new int[dimension];
            int[] vs = new int[dimension];
            for(int d=0;d<dimension;d++){
                if(d%3==0){//设备
                    content[d] = Rand.nextInt(problem.K) + 1;
                }else if(d%3==1){
                    content[d] = Rand.nextInt(problem.R+1);
                }else{
                    content[d] = Rand.nextInt(problem.Q+1);
                }
                vs[d] = (int) Math.round(0.5 * (Rand.nextDouble() * (problem.uppers[d] - problem.lowers[d]) - content[d]));
            }
            sol.setDimension(dimension);
            sol.setContent(content);
            sol.setId(i);
            sol.velocity = vs;
            pop[i] = sol;
            this.Evaluate(pop[i]);

            Solution pbest = new Solution();
            pbest.content = content.clone();
            pbest.setValue(sol.getValue());
            sol.pBest = pbest;

            if(pop[i].getValue()>0){
                i++;
            }
        }
        if (iters == 0) {
            iters = nfes / size;
        }

        this.best = PickBest().first;
        this.wInertia = wmax;
    }

    @Override
    public void Evolve() {
        this.Calculate();
        for(int i=0;i<pop.length;i++){
            this.Move(pop[i],i);
        }
    }

    /**
     * 移动粒子
     * @param s
     * @param index
     * @return
     */
    public Particle Move(Particle s, int index){
        Learn(s,index);
        for(int d=0;d<dimension;d++){
            s.content[d] += s.velocity[d];
            /*if(s.content[d] < problem.lowers[d]){
                s.content[d] = problem.lowers[d];
                s.velocity[d] = 0;
            }else if(s.content[d] > problem.uppers[d]){
                s.content[d] = problem.uppers[d];
                s.velocity[d] = 0;
            }*/
            if(s.content[d] < problem.lowers[d] || s.content[d] > problem.uppers[d]){
                s.content[d] = (int) Math.round(problem.lowers[d] + Rand.nextDouble() * (problem.uppers[d] - problem.lowers[d]));
                s.velocity[d] = (int) Math.round(0.5 * (Rand.nextDouble() * (problem.uppers[d] - problem.lowers[d]) - s.content[d]));
            }
        }
        Evaluate(s);
        if(s.compareTo(s.pBest)>0){
            Solution pbest = new Solution();
            pbest.content = s.content.clone();
            pbest.setValue(s.getValue());
            s.pBest = pbest;
            if(s.compareTo(best) > 0){
                best = (Particle) s.clone();
            }
        }
        return s;
    }



    /**
     * 通过学习改变粒子速度
     * @param s
     * @param index
     */
    public void Learn(Particle s, int index)
    {
        for (int d = 0; d < dimension; d++)
            s.velocity[d] = (int) Math.round(wInertia * s.velocity[d] + Rand.nextDouble() * cRate1 * (s.pBest.content[d] - s.content[d]) + Rand.nextDouble() * cRate2 * (best.content[d] - s.content[d]));
    }

    @Override
    public void Calculate() {
        this.wInertia = wmax - (wmax - wmin) * ((double)iter/iters);
    }

    public static void main(String[] args){
        System.out.println("粒子群优化算法测试");
        MSiap problem = MSiap.generateProblem(3);
        PSOAlg psoAlg = new PSOAlg(3,problem);
        psoAlg.SolveF();
        psoAlg.printAll(psoAlg.pop);
        System.out.println("最优解：");
        psoAlg.print(psoAlg.best);
    }
}
