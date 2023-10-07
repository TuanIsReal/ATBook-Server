package com.tuanisreal.utils;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public static long currentTime() {
        return System.currentTimeMillis();
    }

    public static int getCurrentMillisecond(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MILLISECOND);
    }

    public static Date getGMTTime() {
        Date d = new Date(currentTime());
        return d;
    }

    public static Date getDateTime(long currentTime) {
        Date date = new Date(currentTime);
        return date;
    }

    public static String getTime() {
        Date d = new Date(currentTime());
        return DateFormat.format(d);
    }
}
