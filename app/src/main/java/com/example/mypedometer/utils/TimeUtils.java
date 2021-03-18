package com.example.mypedometer.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    /**
     * 从时间戳中获取年月日信息
     * @param timestamp 时间戳
     * @return yyyy:MM:dd
     */
    public static String TimeToDate(long timestamp){
        Date date = new Date(timestamp);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        return dateFormat.format(date);
    }

    /**
     * 从时间戳中获取时分秒信息
     * @param timeDelt 时间戳
     * @return hh:mm:ss
     */
    public static String TimeToHMS(long timeDelt){
        int hours = (int) (timeDelt / (1000 * 60 * 60));
        int minutes = (int) (timeDelt % (1000 * 60 * 60)) / (1000 * 60);
        long temp = (timeDelt % (1000 * 60 * 60)) % (1000 * 60);
        int seconds = (int) (temp / 1000);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
