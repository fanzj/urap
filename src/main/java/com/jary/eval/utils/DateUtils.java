package com.jary.eval.utils;

import com.jary.eval.exception.AlgException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Fantasy
 * @date 2018/2/24 15:49
 * @description
 */
public class DateUtils {

    private static final Long MILLSECOND = 1L;

    private static final Long SECOND = 1000 * MILLSECOND;

    private static final Long MINUTE = 60 * SECOND;

    private static final Long HOUR = 60 * MINUTE;

    private static final Long DAY = 24 * HOUR;

    public static String formatDate(Date date,String format){
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        return format1.format(date);
    }

    public static String AsSecStr(long time){
        return String.valueOf(time * 1.0 / SECOND) + "秒";
    }

    public static String AsMinStr(long time){
        String res = "";
        long min = time / MINUTE;
        res = min + "分";
        long min_yu = time % MINUTE;
        if(min_yu!=0){
            res += AsSecStr(min_yu);
        }
        return res;
    }

    public static String AsHourStr(long time){
        String res = "";
        long hour = time / HOUR;
        res = hour + "时";
        long hour_yu = time % HOUR;
        if(hour_yu!=0){
            res += AsMinStr(hour_yu);
        }
        return res;
    }

    public static String AsDayStr(long time){
        String res = "";
        long day = time / DAY;
        res = day + "天";
        long day_yu = time % DAY;
        if(day_yu!=0){
            res += AsHourStr(day_yu);
        }
        return res;
    }

    /**
     * 时间转化
     * @param time
     * @return
     */
    public static String AsTimeStr(Long time){
        if(time < 0)
            throw new AlgException("传入时间参数不能为负！");
        else if(time < MINUTE)
            return AsSecStr(time);
        else if(time < HOUR)
            return AsMinStr(time);
        else if(time < DAY)
            return AsHourStr(time);
        else
            return AsDayStr(time);
    }

    public static void main(String[] args){
        System.out.println(AsTimeStr(123L));
        System.out.println(AsTimeStr(4321L));
        System.out.println(AsTimeStr(5000L));
        System.out.println(MINUTE);
        System.out.println(AsTimeStr(123450L));
        System.out.println(HOUR);
        System.out.println(AsHourStr(3610000L));
        System.out.println(DAY);
        System.out.println(AsDayStr(86400012L));
    }
}
