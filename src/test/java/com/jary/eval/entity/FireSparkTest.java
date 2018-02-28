package com.jary.eval.entity;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

/**
 * @author Fantasy
 * @date 2018/2/28 12:47
 * @description
 */
public class FireSparkTest {

    @Test
    public void test(){
        List<FireSpark> R = Lists.newArrayList();
        FireSpark s1 = new FireSpark();
        s1.setValue(2.34);
        FireSpark s2 = new FireSpark();
        s2.setValue(1.2);
        FireSpark s3 = new FireSpark();
        s3.setValue(4.3);
        R.add(s1);
        R.add(s2);
        R.add(s3);
        System.out.println("排序前：");
        System.out.println(R);
        System.out.println("排序后：");
        R.sort(Comparator.reverseOrder());
        System.out.println(R);
    }
}
