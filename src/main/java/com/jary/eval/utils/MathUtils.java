package com.jary.eval.utils;

import com.jary.eval.exception.AlgException;

import java.util.Random;

/**
 * @author Fantasy
 * @date 2018/2/28 10:11
 * @description
 */
public class MathUtils {

    private static Random Rand = new Random();

    public static double doubleAToB(double lower, double upper){
        if(lower > upper)
            throw new AlgException("下界必须不大于上界！");
        return lower + Rand.nextDouble() * (upper - lower);
    }
}
