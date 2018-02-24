package com.jary.eval.utils;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * @author Fantasy
 * @date 2018/2/24 20:45
 * @description
 */
public class JodaTimeUtils {

    @Test
    public void test(){
        DateTime dateTime = new DateTime();
        System.out.println(dateTime);
        long start = System.currentTimeMillis();
    }
}
