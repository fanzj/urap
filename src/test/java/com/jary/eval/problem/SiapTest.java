package com.jary.eval.problem;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.stream.DoubleStream;

/**
 * @author Fantasy
 * @date 2018/2/24 14:26
 * @description
 */
public class SiapTest {

    @Test
    public void test(){
        List<Integer> bqList = Lists.newArrayList();
        double[] t1 = new double[]{3.2,1.8,9,4.3};
        bqList.add(3);
        bqList.add(1);
        bqList.add(2);
        bqList.add(0);

        bqList.sort((Integer i1, Integer i2) -> (Double.compare(t1[i1],t1[i2])));
        System.out.println(bqList);
    }

    @Test
    public void test2(){
        double[] t = new double[]{3.4,1.3,5.6,2.4,9.5,5.4};
        System.out.println(DoubleStream.of(t).max().getAsDouble());
    }
}
