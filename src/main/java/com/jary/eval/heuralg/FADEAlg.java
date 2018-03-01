package com.jary.eval.heuralg;

import com.google.common.collect.Lists;
import com.jary.eval.entity.FireSpark;
import com.jary.eval.entity.FourTuple;
import com.jary.eval.entity.Solution;
import com.jary.eval.entity.ThreeTuple;
import com.jary.eval.exception.AlgException;
import com.jary.eval.problem.Siap;
import com.jary.eval.utils.MathUtils;

import java.util.Arrays;
import java.util.Comparator;
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
    private double sumS = 0.0, sumA = 0.0;//用于计算Si和Ai

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
        for(int i=0;i<pop.length;i++){//遍历种群中每个烟火
            sumS += (cBest.getValue() - pop[i].getValue());//用于计算Si
            sumA += (pop[i].getValue() - cWorst.getValue());//用于计算Ai
        }
    }

    @Override
    public void Evolve() {
        for(int i=0;i<pop.length;i++){//遍历种群中的每个烟火
            BlastMethod1(i); //对烟花i进行爆破1
        }
        BlastMethod2();//从S中随机选择Mx个烟花作为集合P进行爆破2
        getR();//R = R∪S(S为种群)

        //轮盘赌选择，从R中随机选择size个并把他们加入种群，每个x∈R具有一个选择概率
        RouletteSelect(R);

        //遍历种群中的每个解，应用DE的变异、交叉、选择操作
        FireSpark spark;
        for(int i=0;i<pop.length;i++){
            spark = DiffEvolve(pop[i],i);
            if(spark.compareTo(pop[i])>0){
                pop[i] = spark;
            }
        }

        this.UpdateBest();
        this.Calculate();
    }

    public FireSpark DiffEvolve(FireSpark s, int index){
        ThreeTuple<Integer,Integer,Integer> ind = RandomSelectThreeIndices(pop.length, index);
        int r1 = ind.first, r2 = ind.second, r3 = ind.third;
        FireSpark u = s.clone();
        int j = Rand.nextInt(dimension);
        for(int d=0;d<dimension;d++){
            if(Double.compare(Rand.nextDouble(),Cr)<0 || j==d){
                double x = pop[r1].content[d] + F * (pop[r2].content[d] - pop[r3].content[d]);
                u.content[d] = (int) Math.round(x);
                CheckBound(u,d);
            }
        }
        Evaluate(u);
        return u;
    }

    /**
     * 轮盘赌选择
     */
    private void RouletteSelect(List<FireSpark> list){
        calRate(list);//计算累积概率
        int k =0;
        for(int i=0;i<pop.length;i++){
            double r = (Rand.nextInt(65535) % 1000) / 1000.0;
            for(int j=0;j<list.size();j++){
                if(list.get(j).pi >= r){
                    pop[k++] = list.get(j).clone();
                    break;
                }
            }
        }
    }

    /**
     * 计算种群各个个体的累积概率，前提是已经计算出各个个体的适应度， 作为轮盘赌选择策略的一部分
     * @param list
     */
    private void calRate(List<FireSpark> list){
        double sum = 0.0;
        for(int i=0;i<list.size();i++){
            sum += list.get(i).getValue();
        }

        FireSpark spark = list.get(0);
        spark.pi = spark.getValue() / sum;
        for(int i=1;i<list.size();i++){
            FireSpark spark1 = list.get(i);
            FireSpark spark2 = list.get(i-1);
            spark1.pi = spark1.getValue() / sum + spark2.pi;
        }
    }

    /**
     * 求集合R
     */
    private void getR() {
        for(int i =0;i<pop.length;i++){
            R.add(pop[i].clone());
        }
        //对集合R按适应度从大到小排序
        R.sort(Comparator.reverseOrder());
        best = R.get(0).clone();
        while (R.size() > 2*size){
            R.remove(R.size()-1);
        }
    }

    /**
     * 爆破方式1
     * @param index 种群中的烟花索引位置
     * @param index
     */
    private void BlastMethod1(int index){
        //计算Si和Ai
        double sval = Mm * ((cWorst.getValue() - pop[index].getValue() - epsilon) / (sumS - epsilon));
        Si[index] = (int) Math.round(sval);
        double aval = Ax * ((pop[index].getValue() - cBest.getValue() - epsilon) / (sumA - epsilon));
        Ai[index] = aval;
        if(Si[index] <Smin){
            Si[index] = Smin;
        }else if(Si[index] > Smax){
            Si[index] = Smax;
        }

        for(int j=0;j<Si[index];j++){
            FireSpark spark = pop[index].clone();//生成一个火星
            int z = (int) Math.round(dimension * Rand.nextDouble());//生成z个方向进行散开
            for(int k=0;k<z;k++){//每个方向相当于问题维度的某几维
                int pos = Rand.nextInt(dimension);//随机生成一维
                spark.content[pos] = (int) (pop[index].content[pos] + Math.round(Ai[index] * MathUtils.doubleAToB(-1,1)));
                CheckBound(spark,pos);
            }
            Evaluate(spark);
            R.add(spark);
        }
    }

    /**
     * 爆破方式2
     */
    private void BlastMethod2(){
        //从种群中随机选择Mx个烟花作为集合P
        for(int i=0;i<Mx;i++){
            int index = Rand.nextInt(pop.length);
            while (isSelected[index]){
                index = Rand.nextInt(pop.length);
            }
            P.add(pop[index].clone());
            isSelected[index] = true;
        }

        //遍历集合P的每一个烟花进行爆破2
        for(int i=0;i<P.size();i++){
            FireSpark s1 = P.get(i);
            FireSpark s2 = s1.clone();
            int z = (int) Math.round(dimension * Rand.nextDouble());
            for(int k=0;k<z;k++){//每个方向相当于问题维度的某几维
                int pos = Rand.nextInt(dimension);
                s2.content[pos] = (int) Math.round(1 + Rand.nextGaussian() * s1.content[pos]);
                CheckBound(s2,pos);
            }
            Evaluate(s2);
            R.add(s2);
        }
    }

    @Override
    public void Calculate() {
        R.clear();
        P.clear();
        for(int i=0;i<pop.length;i++){//遍历种群中每个烟火
            sumS += (cBest.getValue() - pop[i].getValue());//用于计算Si
            sumA += (pop[i].getValue() - cWorst.getValue());//用于计算Ai
        }
        Arrays.fill(isSelected,false);
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

    public static void main(String[] args){
        System.out.println("FADE算法测试");
        Siap problem = Siap.generateProblem(1);
        FADEAlg fadeAlg = new FADEAlg(1,problem);
        fadeAlg.SolveF();
        fadeAlg.printAll(fadeAlg.pop);
        System.out.println("最优解：");
        fadeAlg.print(fadeAlg.best);
    }
}
