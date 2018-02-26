package com.jary.eval.problem;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.jary.eval.constant.URAPConstant;
import com.jary.eval.entity.Solution;
import com.jary.eval.exception.AlgException;
import com.jary.eval.utils.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

/**
 * @author Fantasy
 * @date 2018/2/23 16:37
 * @description 机场安检分配问题
 */
public class Siap {

    public static final double landa = 5.0;//安检员检测能力系数
    public static final double C = 100.0;//罚函数系数
    public String res_subpath = URAPConstant.SIAP_PATH;//结果保存路径

    public int N;//行李数
    public int M;//危险品数
    public int K;//安检设备数
    public int Q;//安检人员数
    public int T;//时间上限
    public int D;//问题维度
    public double[] Wi;//危险品权重
    public double[] t0j;//行李最早到达时间
    public double[] Lj;//行李长度
    public double[] Vj;//行李体积
    public double[] Sk;//设备检测速度
    public double[][] aij;//行李携带危险品的概率
    public double[][] bik;//某种危险品被安检设备检测到的概率
    public double[][] yiq;//某种危险品被安检人员检测到的概率

    public int lower1;
    public int upper1;
    public int lower2;
    public int upper2;

    /******************* 中间变量 *************************/
    public double[][] tjk;//设备检测行李的时间
    public double[] tj;//安检员检测行李的时间

    public Siap() {
    }

    /**
     * 根据路径读取内容为字符串形式
     *
     * @param path
     * @return
     */
    private String getContent(String path) throws IOException {
        URL url = Resources.getResource(path);
        String content = Resources.toString(url, Charsets.UTF_8);
        return content;
    }

    public boolean GenerateProblem(int problemNum) throws IOException {
        //读取实验数据
        String prefixPath = URAPConstant.PREFIX_PATH + String.format("%02d/", problemNum);

        //时间上限
        this.T = Integer.parseInt(getContent(prefixPath + URAPConstant.TIME_UPPPER));

        //危险品权重
        this.Wi = FileUtils.StrTransfer1D(getContent(prefixPath + URAPConstant.wiStr));
        this.M = Wi.length;

        //行李长度
        this.Lj = FileUtils.StrTransfer1D(getContent(prefixPath + URAPConstant.ljStr));
        this.N = Lj.length;
        this.D = 2 * N;

        //行李最早达到时间
        this.t0j = FileUtils.StrTransfer1D(getContent(prefixPath + URAPConstant.t0jStr));

        //行李体积
        this.Vj = FileUtils.StrTransfer1D(getContent(prefixPath + URAPConstant.vjStr));

        //设备速度
        this.Sk = FileUtils.StrTransfer1D(getContent(prefixPath + URAPConstant.skStr));
        this.K = Sk.length;

        //行李携带危险品的概率
        this.aij = FileUtils.StrTransfer2D(getContent(prefixPath + URAPConstant.aijStr));

        //危险品被设备检测到的概率
        this.bik = FileUtils.StrTransfer2D(getContent(prefixPath + URAPConstant.bik));

        //危险品被安检员检测到的概率
        this.yiq = FileUtils.StrTransfer2D(getContent(prefixPath + URAPConstant.yiq));
        this.Q = yiq[0].length - 1;

        //上下限确定
        lower1 = 1;
        upper1 = K;
        lower2 = 0;
        upper2 = Q;

        //中间变量：检测时间的计算
        calDetectTime();

        //实验读取数据打印
        //printData();

        return false;
    }

    /**
     * 中间变量的计算
     * 包括：设备检测行李和安检员检测行李的时间
     */
    private void calDetectTime() {
        tj = new double[N];
        tjk = new double[N][K];
        for (int j = 0; j < N; j++) {
            tj[j] = landa * Math.sqrt(Vj[j]);
            for (int k = 0; k < K; k++) {
                tjk[j][k] = Lj[j] / Sk[k];
            }
        }
    }

    /**************************** 时间计算 *******************************/
    /**
     * 获取与行李j分配到同一个设备且比行李j早到达的行李集合
     *
     * @param j       第j件行李，对应设备位置为j*2
     * @param content 分配方案
     * @return
     */
    private List<Integer> getPreBaggageList(int j, int[] content) {
        List<Integer> preBaggageList = Lists.newArrayList();
        for (int d = 0; d < content.length; d += 2) {
            if (content[d] == content[j * 2] && d != (j * 2) //设备相同
                    && Double.compare(t0j[d / 2], t0j[j]) < 0) {//比行李j早到
                preBaggageList.add(d / 2);//行李编号
            }
        }
        return preBaggageList;
    }

    /**
     * 计算每件行李由设备检查完成的时间
     *
     * @param sol
     * @return
     */
    private double[] calT1(int[] content) {
        double[] t1 = new double[N];
        for (int j = 0; j < t1.length; j++) {//对每件行李
            List<Integer> preBaggageList = getPreBaggageList(j, content);
            int device = content[j * 2] - 1;//设备索引
            for (int i = 0; i < preBaggageList.size(); i++) {
                int baggage = preBaggageList.get(i);//行李索引
                t1[j] += tjk[baggage][device];
            }
        }
        return t1;
    }

    /**
     * 按t1时间升序排列的分配给安检员q的行李集合
     *
     * @param q       安检员编号
     * @param content 分配方案
     * @param t1
     * @return
     */
    private List<Integer> getBq(int q, int[] content, double[] t1) {
        List<Integer> bqList = Lists.newArrayList();
        for (int d = 1; d < D; d += 2) {
            int y = content[d];//决策变量，哪个安检员
            if (q != 0 && q == y) {
                bqList.add((d - 1) / 2);//行李编号
            }
        }
        bqList.sort((Integer i1, Integer i2) -> (Double.compare(t1[i1], t1[i2])));
        return bqList;
    }


    /**
     * 每件行李由安检员检测完成的时间
     * 返回行李的最大检测时间
     *
     * @param content
     * @return
     */
    private double calT2(int[] content) {
        double[] t1 = calT1(content);
        double[] t2 = new double[N];
        double t = 0;
        for (int q = 0; q <= Q; q++) {
            List<Integer> bqList = getBq(q, content, t1);
            for (int j = 0; j < bqList.size(); j++) {
                int baggage = bqList.get(j);//行李编号
                if (j == 0) {//第一个行李
                    t2[baggage] += t1[baggage] + tj[baggage];
                } else {
                    int pre = bqList.get(j - 1);//j的前一个行李
                    t2[baggage] = Math.max(t1[baggage], t2[pre]) + tj[baggage];
                }
            }
        }
        t = DoubleStream.of(t2).max().getAsDouble();
        return t;
    }

    /**
     * 对外暴露的适应度评估函数
     *
     * @param content
     * @return
     */
    public double Evaluate(Solution sol) {
        int[] content = sol.getContent();
        double maxt = calT2(content);
        double value = 0.0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                int x = content[j * 2];
                int y = content[j * 2 + 1];
                value += (Wi[i] * aij[i][j] * (1 - (1 - bik[i][x - 1]) * (1 - yiq[i][y])));
            }
        }
        double px = 0;
        if (Double.compare(maxt, T) > 0) {
            px = maxt - T;
        }
        value -= C * px;

        sol.setTime(maxt);
        sol.setValue(value);
        return value;
    }

    public static Siap generateProblem(int instanceNo){
        //问题初始化
        if(instanceNo<=0)
            throw new AlgException("请先设置问题实例规模！");

        Siap problem = new Siap();
        try {
            if(problem.GenerateProblem(instanceNo)){
                System.out.println("问题生成！准备执行。。。");
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
        System.out.println("设备数目K：" + K);
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
        System.out.println("危险品被设备检测到的概率：");
        FileUtils.print(bik);
        System.out.println("危险品被安检员检测到的概率");
        FileUtils.print(yiq);
        System.out.format("安检设备维度范围：[%02d, %02d]\n", lower1, upper1);
        System.out.format("安检人员维度范围：[%02d, %02d]\n", lower2, upper2);
    }

    @Override
    public String toString() {
        return "SIAP{" +
                "N=" + N +
                ", M=" + M +
                ", K=" + K +
                ", Q=" + Q +
                ", T=" + T +
                ", D=" + D +
                ", Wi=" + Arrays.toString(Wi) +
                ", t0j=" + Arrays.toString(t0j) +
                ", Lj=" + Arrays.toString(Lj) +
                ", Vj=" + Arrays.toString(Vj) +
                ", Sk=" + Sk +
                ", aij=" + Arrays.toString(aij) +
                ", bik=" + Arrays.toString(bik) +
                ", yiq=" + Arrays.toString(yiq) +
                ", lower1=" + lower1 +
                ", upper1=" + upper1 +
                ", lower2=" + lower2 +
                ", upper2=" + upper2 +
                '}';
    }

    public static void main(String[] args) throws IOException {
        Siap siap = new Siap();
        siap.GenerateProblem(1);

    }
}
