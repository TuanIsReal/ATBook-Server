package com.tuanisreal.utils;

import java.util.Date;

public class DateFormat {
    public static final int JAVA_DATE_START_YEAR = 1900;

    public static Date parse(String yyyyMMddHHmmss) {
        if (yyyyMMddHHmmss == null || yyyyMMddHHmmss.isEmpty()) {
            return null;
        }
        try {
            int year = Integer.parseInt(yyyyMMddHHmmss.substring(0, 4));
            year -= JAVA_DATE_START_YEAR;
            int month = Integer.parseInt(yyyyMMddHHmmss.substring(4, 6));
            month--;
            int date = Integer.parseInt(yyyyMMddHHmmss.substring(6, 8));
            int hour = Integer.parseInt(yyyyMMddHHmmss.substring(8, 10));
            int min = Integer.parseInt(yyyyMMddHHmmss.substring(10, 12));
            int second = Integer.parseInt(yyyyMMddHHmmss.substring(12, 14));
            Date d = new Date(year, month, date, hour, min, second);
            return d;
        } catch (Exception ex) {
            return parseBackup(yyyyMMddHHmmss);
        }
    }

    public static Date parse_yyyyMMddHHmmssSSS(String yyyyMMddHHmmssSSS) {
        if (yyyyMMddHHmmssSSS == null || yyyyMMddHHmmssSSS.isEmpty()) {
            return null;
        }
        try {
            int year = Integer.parseInt(yyyyMMddHHmmssSSS.substring(0, 4));
            year -= JAVA_DATE_START_YEAR;
            int month = Integer.parseInt(yyyyMMddHHmmssSSS.substring(4, 6));
            month--;
            int date = Integer.parseInt(yyyyMMddHHmmssSSS.substring(6, 8));
            int hour = Integer.parseInt(yyyyMMddHHmmssSSS.substring(8, 10));
            int min = Integer.parseInt(yyyyMMddHHmmssSSS.substring(10, 12));
            int second = Integer.parseInt(yyyyMMddHHmmssSSS.substring(12, 14));
            int millisecond = Integer.parseInt(yyyyMMddHHmmssSSS.substring(14, 17));

            Date d = new Date(year, month, date, hour, min, second);
            d.setTime(d.getTime() + millisecond);

            return d;
        } catch (Exception ex) {
            return parseBackup(yyyyMMddHHmmssSSS);
        }
    }

    public static Date parse_yyyyMMdd(String yyyyMMdd) {
        if (yyyyMMdd == null || yyyyMMdd.isEmpty()) {
            return null;
        }
        try {
            int year = Integer.parseInt(yyyyMMdd.substring(0, 4));
            year -= JAVA_DATE_START_YEAR;
            int month = Integer.parseInt(yyyyMMdd.substring(4, 6));
            month--;
            int date = Integer.parseInt(yyyyMMdd.substring(6, 8));
            Date d = new Date(year, month, date);
            return d;
        } catch (Exception ex) {
            return DateTimeUtil.getGMTTime();
        }
    }

    private static Date parseBackup(String yyyyMMddHHmmss) {
        if (yyyyMMddHHmmss == null) {
            return null;
        }
        try {
            int year = Integer.parseInt(yyyyMMddHHmmss.substring(0, 4));
            year -= JAVA_DATE_START_YEAR;
            int month = Integer.parseInt(yyyyMMddHHmmss.substring(4, 6));
            month--;
            int date = Integer.parseInt(yyyyMMddHHmmss.substring(6, 8));
            int hour = Integer.parseInt(yyyyMMddHHmmss.substring(8, 10));
            int min = Integer.parseInt(yyyyMMddHHmmss.substring(10, 12));
            int second = Integer.parseInt(yyyyMMddHHmmss.substring(12, 14));

            Date d = new Date(year, month, date, hour, min, second);

            return d;
        } catch (Exception ex) {
            return DateTimeUtil.getGMTTime();
        }
    }

    public static Date parse_yyyy_MM_dd_HH_mm_ss(String yyyyMMddHHmmss) {
        if (yyyyMMddHHmmss == null || yyyyMMddHHmmss.isEmpty()) {
            return null;
        }
        try {
            int year = Integer.parseInt(yyyyMMddHHmmss.substring(0, 4));
            year -= JAVA_DATE_START_YEAR;
            int month = Integer.parseInt(yyyyMMddHHmmss.substring(4, 6));
            month--;
            int date = Integer.parseInt(yyyyMMddHHmmss.substring(6, 8));
            int hour = Integer.parseInt(yyyyMMddHHmmss.substring(8, 10));
            int min = Integer.parseInt(yyyyMMddHHmmss.substring(10, 12));
            int second = Integer.parseInt(yyyyMMddHHmmss.substring(12, 14));
            Date d = new Date(year, month, date, hour, min, second);
            return d;
        } catch (Exception ex) {
            return parseBackup(yyyyMMddHHmmss);
        }
    }
    private static final String ZERO = "0";

    public static String format_yyyyMMddHHmmssSSS(Date d) {
        if (d == null) {
            return null;
        }
        StringBuilder buidler = new StringBuilder()
                .append(d.getYear() + JAVA_DATE_START_YEAR)
                .append(format2DNumber(d.getMonth() + 1))
                .append(format2DNumber(d.getDate()))
                .append(format2DNumber(d.getHours()))
                .append(format2DNumber(d.getMinutes()))
                .append(format2DNumber(d.getSeconds()))
                .append(getMillisecond(d));
        return buidler.toString();
    }

    public static String format_yyyyMMddHHmmss(Date d) {
        if (d == null) {
            return null;
        }
        StringBuilder buidler = new StringBuilder()
                .append(d.getYear() + JAVA_DATE_START_YEAR)
                .append(format2DNumber(d.getMonth() + 1))
                .append(format2DNumber(d.getDate()))
                .append(format2DNumber(d.getHours()))
                .append(format2DNumber(d.getMinutes()))
                .append(format2DNumber(d.getSeconds()));
        return buidler.toString();
    }

    public static String format_yyyyMMddHHmmForDisplaying(String date) {
        if (date == null) {
            return null;
        }
        StringBuilder buidler = new StringBuilder()
                .append(date.substring(0, 4)).append("/")
                .append(date.substring(4, 6)).append("/")
                .append(date.substring(6, 8)).append(" ")
                .append(date.substring(8, 10)).append(":")
                .append(date.substring(10));
        return buidler.toString();
    }

    public static String format_yyyyMMddHHmmssSSS(long date) {
        return format_yyyyMMddHHmmssSSS(new Date(date));
    }

    public static String format_yyyyMMddHHmmss(long date) {
        return format_yyyyMMddHHmmss(new Date(date));
    }

    private static String getMillisecond(Date d) {
        int n = (int) (d.getTime() % 1000);
        n = n < 0 ? n + 1000 : n;
        return format3DNumber(n);
    }

    public static String format(Date d) {
        if (d == null) {
            return null;
        }
        StringBuilder buidler = new StringBuilder()
                .append(d.getYear() + JAVA_DATE_START_YEAR)
                .append(format2DNumber(d.getMonth() + 1))
                .append(format2DNumber(d.getDate()))
                .append(format2DNumber(d.getHours()))
                .append(format2DNumber(d.getMinutes()))
                .append(format2DNumber(d.getSeconds()));
        return buidler.toString();
    }

    public static String format(long date) {
        return format(new Date(date));
    }

    private static String format2DNumber(int n) {
        return n > 9 ? String.valueOf(n) : (ZERO + n);
    }

    private static String format3DNumber(int n) {
        return n < 10 ? String.valueOf(ZERO + ZERO + n) : (n < 100) ? String.valueOf(ZERO + n) : String.valueOf(n);
    }

    public static String format_yyyyMMdd(Date d) {
        if (d == null) {
            return null;
        }
        StringBuilder buidler = new StringBuilder()
                .append(d.getYear() + JAVA_DATE_START_YEAR)
                .append(format2DNumber(d.getMonth() + 1))
                .append(format2DNumber(d.getDate()));
        return buidler.toString();
    }

    public static Date parse_yyyyMMddHHmm(String yyyyMMddHHmmss) {
        if (yyyyMMddHHmmss == null || yyyyMMddHHmmss.isEmpty()) {
            return null;
        }
        try {
            int year = Integer.parseInt(yyyyMMddHHmmss.substring(0, 4));
            year -= JAVA_DATE_START_YEAR;
            int month = Integer.parseInt(yyyyMMddHHmmss.substring(4, 6));
            month--;
            int date = Integer.parseInt(yyyyMMddHHmmss.substring(6, 8));
            int hour = Integer.parseInt(yyyyMMddHHmmss.substring(8, 10));
            int min = Integer.parseInt(yyyyMMddHHmmss.substring(10, 12));
            Date d = new Date(year, month, date, hour, min);

            return d;
        } catch (Exception ex) {
            return parseBackup(yyyyMMddHHmmss);
        }
    }

    public static String format_yyyyMMddHHmm(Date date) {
        if (date == null) {
            return null;
        }
        StringBuilder buidler = new StringBuilder()
                .append(date.getYear() + JAVA_DATE_START_YEAR)
                .append(format2DNumber(date.getMonth() + 1))
                .append(format2DNumber(date.getDate()))
                .append(format2DNumber(date.getHours()))
                .append(format2DNumber(date.getMinutes()));
        return buidler.toString();
    }

    public static String format_yyyyMMddHHmm(Long date) {
        return format_yyyyMMddHHmm(new Date(date));
    }
}
