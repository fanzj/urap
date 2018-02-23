package com.jary.eval.utils;

import com.google.common.base.Splitter;
import com.jary.eval.exception.AlgException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Fantasy
 * @date 2018/2/23 18:28
 * @description
 */
public class FileUtils {

    /**
     * 将字符串以空格分割，转换为1维数组
     * @param str
     * @return
     */
    public static double[] StrTransfer1D(String str){
        if(str==null || "".equals(str))
            throw new AlgException("转换字符串为空！");

        double[] res =  Stream.of(str.split(" ")).mapToDouble(s -> Double.parseDouble(s)).toArray();
        return res;
    }

    /**
     * 将字符串以空格分割，转换为2维数组
     * @param str
     * @return
     */
    public static double[][] StrTransfer2D(String str){
        if(str==null || "".equals(str))
            throw new AlgException("转换字符串为空！");

        String[] array = str.split("\n");

        double[][] res = new double[array.length][];
        for(int i=0;i<array.length;i++){
            String line = array[i];
            res[i] = Stream.of(line.split(" ")).mapToDouble(s -> Double.parseDouble(s)).toArray();
        }
        return res;
    }

    /**
     * 将一维数组转换为指定行数和列数的二维数组
     * @param x
     * @param row
     * @param col
     * @return
     */
    public static double[][] OneDTransfer2D(double[] x,int row,int col){
        double[][] y = new double[row][col];
        int k = 0;
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                y[i][j] = x[k++];
            }
        }
        return y;
    }

    public static void print(double[] x){
        for(int i=0;i<x.length;i++){
            System.out.print(x[i]);
            if(i<x.length-1)
                System.out.print(" ");
            else
                System.out.println();
        }
    }

    public static void print(double[][] x){
        for(int i=0;i<x.length;i++){
            for(int j=0;j<x[0].length;j++){
                System.out.print(x[i][j]);
                if(j<x[0].length-1)
                    System.out.print(" ");
                else
                    System.out.println();
            }
        }
    }

    public static void main(String[] args){
     /*   String str = "2.007 2.006 1.627 0.567 0.177 2.775 0.510 1.192 0.270 2.091 1.887 0.103 2.806 2.608 2.134 1.175 1.935 2.295 1.212 2.939 0.836 2.033 1.104 1.783";

        double[] y =  Stream.of(str.split(" ")).mapToDouble(s -> Double.parseDouble(s)).toArray();
        System.out.println(y.length);
        for(int i=0;i<y.length;i++){
            System.out.print(y[i]+" ");
        }*/

        String str2 = "0.66856917 0.89448893 0.65612596 0.60344568 0.02144986 0.95020486 0.72275361 0.85894685 0.39634605 0.48695458 0.54924112 0.89241080 0.58908292 0.49417507 0.57272974 0.60042989 0.08545137 0.36658616 0.18995175 0.68010990 0.21399014 0.25570222 0.22353543 0.51888554\n" +
                "0.19205623 0.03549590 0.10326884 0.45045650 0.88259769 0.28873844 0.15053718 0.96745024 0.22845533 0.90901783 0.64418631 0.43552044 0.01168541 0.88316255 0.66735512 0.08894014 0.79730870 0.56977530 0.08735777 0.60435174 0.66405526 0.32180762 0.40448635 0.34718607\n" +
                "0.59908891 0.61458278 0.62064312 0.46436561 0.23935883 0.93540118 0.72500119 0.35725756 0.97914017 0.56683854 0.87143083 0.71044458 0.33148959 0.05307439 0.83121826 0.17473227 0.83075537 0.57706424 0.84464248 0.00691426 0.69394640 0.69609608 0.56626489 0.45416712\n" +
                "0.92067762 0.88424768 0.40132558 0.43754896 0.50457449 0.11248838 0.16442903 0.13692731 0.95343216 0.26064518 0.65674118 0.40876312 0.37109001 0.50017975 0.45420894 0.57852946 0.94089531 0.91269947 0.92685209 0.04114521 0.76879477 0.51635199 0.26796676 0.27565522\n" +
                "0.69350623 0.56072978 0.10211853 0.33449021 0.85188680 0.42063727 0.06209256 0.60299154 0.49952820 0.67005001 0.57750696 0.64148666 0.82977371 0.94982161 0.00158467 0.22005382 0.30876542 0.05617130 0.11761858 0.23521493 0.34979454 0.27171073 0.01295288 0.05814818";
        double[][] y = StrTransfer2D(str2);
        System.out.println(y.length);
        System.out.println(y[0].length);
        print(y);
/*
        double[] x = StrTransfer1D(str2);
        double[][] y = OneDTransfer2D(x,5,24);
       print(y);*/
    }
}
