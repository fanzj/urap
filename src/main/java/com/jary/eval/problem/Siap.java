package com.jary.eval.problem;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.jary.eval.constant.URAPConstant;
import com.jary.eval.utils.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * @author Fantasy
 * @date 2018/2/23 16:37
 * @description 机场安检分配问题
 */
public class SIAP {

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

    public SIAP(){}

    /**
     * 根据路径读取内容为字符串形式
     * @param path
     * @return
     */
    private String getContent(String path) throws IOException {
        URL url = Resources.getResource(path);
        String content = Resources.toString(url,Charsets.UTF_8);
        return content;
    }

    public boolean GenerateProblem(int problemNum) throws IOException {
        //读取实验数据
        String prefixPath = URAPConstant.PREFIX_PATH + String.format("%02d/",problemNum);

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

        //实验读取数据打印
        //printData();

        return false;
    }

    private void printData(){
        System.out.println("问题维度D："+D);
        System.out.println("时间上限："+T);
        System.out.println("危险品种类M："+M);
        System.out.println("行李件数N："+N);
        System.out.println("设备数目K："+K);
        System.out.println("安检员数："+Q);
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
        System.out.format("安检设备维度范围：[%02d, %02d]\n",lower1,upper1);
        System.out.format("安检人员维度范围：[%02d, %02d]\n",lower2,upper2);
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
       SIAP siap = new SIAP();
       siap.GenerateProblem(1);

    }
}
