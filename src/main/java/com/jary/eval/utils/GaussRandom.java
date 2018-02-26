package com.jary.eval.utils;

import java.util.Random;

/**
 * @author Fantasy
 * @date 2018/2/26 21:41
 * @description 高斯随机数生成器
 */
public class GaussRandom extends Random{

    private int phase = 0;
    private double x;

    public GaussRandom(){}

    public GaussRandom(int seed){
        super(seed);
    }

    /**
     * 生成标准正态（高斯）随机数
     * @return
     */
    @Override
    public double nextDouble() {
        //return super.nextDouble();
        double v1, v2, s;
        if(phase == 0){
            do{
                double u1 = super.nextDouble(), u2 = super.nextDouble();
                v1 = 2.0 * u1 - 1.0;
                v2 = 2.0 * u2 - 1.0;
                s = v1 * v1 + v2 * v2;
            }while (s >= 1.0 || s ==0.0);

            double tmp = Math.sqrt(-2.0 * Math.log(s) / s);
            phase = 1;
            x = v1 * tmp;
            return v2 * tmp;
        }else{
            phase = 0;
            return x;
        }
    }
}
