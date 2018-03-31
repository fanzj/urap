package com.jary.eval.problem;

import com.google.common.collect.Lists;
import com.jary.eval.constant.URAPConstant;
import com.jary.eval.entity.TwoTuple;
import com.jary.eval.exception.AlgException;
import com.jary.eval.utils.DateUtils;
import com.jary.eval.utils.FileUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.DoubleStream;

public class MSiap extends Siap {

    /***************** 输入变量 ********************/
    public int R;//第二层安检设备数
    protected double[][] bir;//某种危险品被二层安检设备检测到的概率
    public double[] Sr;//二层设备检测速度

    /***************** 中间变量 ********************/
    protected double[][] tjr;//二层设备检测行李的时间

    public MSiap(){
        super();
    }

    @Override
    public boolean GenerateProblem(int problemNum) throws IOException {
        //读取实验数据
        String prefixPath = URAPConstant.PREFIX_PATH + String.format("%02d/", problemNum) + "msiap/";

        //时间上限
        this.T = Integer.parseInt(getContent(prefixPath + URAPConstant.TIME_UPPPER));

        //危险品权重
        this.Wi = FileUtils.StrTransfer1D(getContent(prefixPath + URAPConstant.wiStr));
        this.M = Wi.length;

        //行李长度
        this.Lj = FileUtils.StrTransfer1D(getContent(prefixPath + URAPConstant.ljStr));
        this.N = Lj.length;
        this.D = 3 * N;

        //行李最早达到时间
        this.t0j = FileUtils.StrTransfer1D(getContent(prefixPath + URAPConstant.t0jStr));

        //行李体积
        this.Vj = FileUtils.StrTransfer1D(getContent(prefixPath + URAPConstant.vjStr));

        //设备速度
        this.Sk = FileUtils.StrTransfer1D(getContent(prefixPath + URAPConstant.skStr));
        this.K = Sk.length;
        this.Sr = FileUtils.StrTransfer1D(getContent(prefixPath + URAPConstant.srStr));
        this.R = Sr.length;

        //行李携带危险品的概率
        this.aij = FileUtils.StrTransfer2D(getContent(prefixPath + URAPConstant.aijStr));

        //危险品被设备检测到的概率
        this.bik = FileUtils.StrTransfer2D(getContent(prefixPath + URAPConstant.bik));
        this.bir = FileUtils.StrTransfer2D(getContent(prefixPath + URAPConstant.bir));

        //危险品被安检员检测到的概率
        this.yiq = FileUtils.StrTransfer2D(getContent(prefixPath + URAPConstant.yiq));
        this.Q = yiq[0].length - 1;


        this.initParams();

        return true;
    }

    @Override
    protected void initParams() {
        lowers = new int[D];
        uppers = new int[D];
        for(int d=0;d<D;d++){
            if(d%3==0){
                lowers[d] = 1;
                uppers[d] = K;
            }else if(d%3==1){
                lowers[d] = 0;
                uppers[d] = R;
            }
            else if(d%3==2){
                lowers[d] = 0;
                uppers[d] = Q;
            }
        }

        //中间变量：检测时间的计算
        calDetectTime();
    }

    /**
     * 中间变量的计算
     * 包括：设备检测行李和安检员检测行李的时间
     */
    @Override
    protected void calDetectTime() {
        tj = new double[N];
        tjk = new double[N][K];
        tjr = new double[N][R];
        for (int j = 0; j < N; j++) {
            tj[j] = landa * Math.sqrt(Vj[j]);
            for (int k = 0; k < K; k++) {
                tjk[j][k] = Lj[j] / Sk[k];
            }
            for(int r = 0;r<R;r++){
                tjr[j][r] = Lj[j] / Sr[r];
            }
        }
    }

    /**************************** 时间计算 *******************************/
    /**
     * 获取与行李j分配到同一个设备且比行李j早到达的行李集合
     *
     * @param j       第j件行李，对应L1设备位置为j*3
     * @param content 分配方案
     * @return
     */
    private List<Integer> getPreBaggageList(int j, int[] content) {
        List<Integer> preBaggageList = Lists.newArrayList();
        for (int d = 0; d < content.length; d += 3) {
            if (content[d] == content[j * 3] && d != (j * 3) //设备相同
                    && Double.compare(t0j[d / 3], t0j[j]) < 0) {//比行李j早到
                preBaggageList.add(d / 3);//行李编号
            }
        }
        return preBaggageList;
    }

    /**
     * 计算每件行李由设备检查完成的时间
     *
     * @param content
     * @return
     */
    private double[] calT1(int[] content) {
        double[] t1 = new double[N];
        for (int j = 0; j < t1.length; j++) {//对每件行李
            List<Integer> preBaggageList = getPreBaggageList(j, content);
            int device = content[j * 3] - 1;//L1设备索引
            for (int i = 0; i < preBaggageList.size(); i++) {
                int baggage = preBaggageList.get(i);//行李索引
                t1[j] += tjk[baggage][device];
            }
        }
        return t1;
    }

    /**
     * 按t1时间升序排列的分配给L2设备的行李集合
     *
     * @param r       L2设备编号
     * @param content 分配方案
     * @param t1
     * @return
     */
    private List<Integer> getBr(int r, int[] content, double[] t1) {
        List<Integer> bqList = Lists.newArrayList();
        for (int d = 1; d < D; d += 3) {
            int y = content[d];//决策变量，哪个L2设备
            if (r != 0 && r == y) {
                bqList.add((d - 1) / 3);//行李编号
            }
        }
        bqList.sort((Integer i1, Integer i2) -> (Double.compare(t1[i1], t1[i2])));
        return bqList;
    }


    /**
     * 每件行李由L2设备检测完成的时间
     * 返回行李的最大检测时间
     *
     * @param content
     * @return
     */
    private double[] calT2(int[] content) {
        double[] t1 = calT1(content);
        double[] t2 = new double[N];
        double t = 0;
        for (int r = 1; r <= R; r++) {
            List<Integer> bqList = getBr(r, content, t1);
            for (int j = 0; j < bqList.size(); j++) {
                int baggage = bqList.get(j);//行李编号
                if (j == 0) {//第一个行李
                    t2[baggage] += t1[baggage] + tjr[baggage][r-1];
                } else {
                    int pre = bqList.get(j - 1);//j的前一个行李
                    t2[baggage] = Math.max(t1[baggage], t2[pre]) + tjr[baggage][r-1];
                }
            }
        }
        return t2;
    }

    /**
     * 按t1时间升序排列的分配给安检员的行李集合
     *
     * @param q       安检员编号
     * @param content 分配方案
     * @param t2
     * @return
     */
    private List<Integer> getCq(int q, int[] content, double[] t2) {
        List<Integer> bqList = Lists.newArrayList();
        for (int d = 2; d < D; d += 3) {
            int y = content[d];//决策变量，哪个L2设备
            if (q != 0 && q == y) {
                bqList.add((d - 2) / 3);//行李编号
            }
        }
        bqList.sort((Integer i1, Integer i2) -> (Double.compare(t2[i1], t2[i2])));
        return bqList;
    }


    /**
     * 每件行李由安检员检测完成的时间
     * 返回行李的最大检测时间
     *
     * @param content
     * @return
     */
    private double calT3(int[] content) {
        double[] t2 = calT2(content);
        double[] t3 = new double[N];
        double t = 0;
        for (int q = 0; q <= Q; q++) {
            List<Integer> bqList = getCq(q, content, t2);
            for (int j = 0; j < bqList.size(); j++) {
                int baggage = bqList.get(j);//行李编号
                if (j == 0) {//第一个行李
                    t3[baggage] += t2[baggage] + tj[baggage];
                } else {
                    int pre = bqList.get(j - 1);//j的前一个行李
                    t3[baggage] = Math.max(t2[baggage], t3[pre]) + tj[baggage];
                }
            }
        }
        t = DoubleStream.of(t3).max().getAsDouble();
        return t;
    }

    /**
     * 对外暴露的适应度评估函数
     *
     * @param content
     * @return (适应度，计算时间)
     */
    public TwoTuple<Double, Double> Evaluate(int[] content) {
        double maxt = calT3(content);
        double value = 0.0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                int x = content[j * 3];
                int y = content[j * 3 + 1];
                int z = content[j * 3 + 2];
                value += (Wi[i] * aij[i][j] * (1 - (1 - bik[i][x - 1]) * (1 - bir[i][y]) * (1 - yiq[i][z])));
            }
        }
        double px = 0;
        if (Double.compare(maxt, T) > 0) {
            px = maxt - T;
        }
        value -= C * px;

        return new TwoTuple<>(value,maxt);//返回适应度值和计算时间
    }

    public static MSiap generateProblem(int instanceNo){
        //问题初始化
        if(instanceNo<=0)
            throw new AlgException("请先设置问题实例规模！");

        MSiap problem = new MSiap();
        try {
            if(problem.GenerateProblem(instanceNo)){
                System.out.println("问题生成！准备执行。。。"+"当前时间："+ new Date());
            }
        } catch (IOException e) {
            throw new AlgException("问题生成失败！");
        }
        return problem;
    }

    /***************************** 测试 **********************************/

    private void printData() {
        System.out.println("问题维度D：" + D);
        System.out.println("时间上限：" + T);
        System.out.println("危险品种类M：" + M);
        System.out.println("行李件数N：" + N);
        System.out.println("L1设备数目K：" + K);
        System.out.println("L2设备数目R：" + R);
        System.out.println("安检员数：" + Q);
        System.out.println("危险品权重Wi：");
        FileUtils.print(Wi);
        System.out.println("行李最早达到时间：");
        FileUtils.print(t0j);
        System.out.println("行李长度Lj：");
        FileUtils.print(Lj);
        System.out.println("行李体积Vj：");
        FileUtils.print(Vj);
        System.out.println("设备速度Sk：");
        FileUtils.print(Sk);
        System.out.println("行李携带危险品的概率aij：");
        FileUtils.print(aij);
        System.out.println("危险品被L1设备检测到的概率：");
        FileUtils.print(bik);
        System.out.println("危险品被L2设备检测到的概率：");
        FileUtils.print(bir);
        System.out.println("危险品被安检员检测到的概率");
        FileUtils.print(yiq);
        System.out.format("L1安检设备维度范围：[%02d, %02d]\n", lowers[0], uppers[0]);
        System.out.format("L2安检设备维度范围：[%02d, %02d]\n", lowers[1], uppers[1]);
        System.out.format("安检人员维度范围：[%02d, %02d]\n", lowers[2], uppers[2]);
    }

    public static void main(String[] args) throws IOException {
        MSiap mSiap = new MSiap();
        mSiap.GenerateProblem(1);
        mSiap.printData();
    }
}
