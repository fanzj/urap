package com.jary.eval.entity;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Fantasy on 2018/2/21.
 */
public class SolutionTest {

    Solution a;
    Solution b;

    @Before
    public void initSolution(){
        System.out.println("初始化两个解");
        a = new Solution();
        a.setDimension(3);
        a.setContent(new double[]{3.4, 5.6, 2.1});
        a.setValue(20.345);

        b = new Solution(new double[]{0.31,1.9,4.7});
        b.setDimension(3);
        b.setValue(17.88);
        System.out.println(a);
        System.out.println(b);
    }

    @Test
    public void testCmp(){
        System.out.println("解适应度值的比较：");
        int cmp = b.compareTo(a);
        System.out.println(cmp);
    }

    @Test
    public void testClone(){
        Solution c = a.clone();
        System.out.println(c);
        System.out.println(a);
        System.out.println("对clone后的结果修改");
        c.setDimension(5);
        double[] content = c.getContent();
        content[1] = 1.2345;
        System.out.println(c);
        System.out.println(a);
    }
}
