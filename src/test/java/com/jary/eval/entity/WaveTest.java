package com.jary.eval.entity;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Fantasy
 * @date 2018/2/26 20:35
 * @description
 */
public class WaveTest {

    Wave a;
    Wave b;

    @Before
    public void initSolution(){
        System.out.println("初始化两个水波：");
        a = new Wave();
        a.setDimension(3);
        a.setContent(new int[]{3, 5, 2});
        a.setValue(20.345);

        b = new Wave(new int[]{0,9,7});
        b.setDimension(3);
        b.setValue(17.88);
        System.out.println(a);
        System.out.println(b);

    }

    @Test
    public void testClone(){
        Wave c = (Wave) a.clone();
        System.out.println(c);
        System.out.println(a);
        System.out.println("对clone后的结果修改");
        c.setDimension(5);
        int[] content = c.getContent();
        content[1] = 10;
        c.amplitude = 1.2;
        System.out.println(c);
        System.out.println(a);
    }

    @Test
    public void testCmp(){
        System.out.println("解适应度值的比较：");
        int cmp = b.compareTo(a);
        System.out.println(cmp);
    }
}
