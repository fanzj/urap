package com.jary.eval.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Fantasy
 * @date 2018/2/24 15:49
 * @description
 */
public class DateUtils {

    public static String formatDate(Date date,String format){
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        return format1.format(date);
    }
}
