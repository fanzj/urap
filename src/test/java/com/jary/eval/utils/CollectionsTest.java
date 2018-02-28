package com.jary.eval.utils;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author Fantasy
 * @date 2018/2/28 10:39
 * @description
 */
public class CollectionsTest {

    @Test
    public void testList(){
        List<Integer> list = Lists.newArrayList();
        list.add(1);
        list.add(3);
        System.out.println(list.size());
        list.clear();
        System.out.println(list.size());
        list.add(5);
        System.out.println(list.size());
        list.add(6);
        list.add(9);
        System.out.println(list.size());
    }
}
