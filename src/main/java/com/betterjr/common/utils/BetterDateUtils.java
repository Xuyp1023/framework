package com.betterjr.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author zhoucy
 */
public class BetterDateUtils extends org.apache.commons.lang3.time.DateUtils {
    private final static long ND = 1000 * 24 * 60 * 60;
    private final static long NH = 1000 * 60 * 60;
    private final static long NM = 1000 * 60;
    private final static long NS = 1000;

    private static String[] parsePatterns = { "yyyyMMdd", "yyyyMMdd HHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd HH:mm:ss", "yyyy-MM", "yyyy/MM/dd", "yyyy-MM-dd",
            "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm", "yyyy.MM", "yyyyMM" };

    private static String[] patterDislay = { "yyyy年MM月dd日", "yyyy年MM月dd日 HH时mm分", "yyyy年MM月dd日 HH时mm分ss秒", "HH时mm分ss秒",
            "yyyy年MM月" };

    public static String DATE_DEFFMT = "yyyy-MM-dd";

    public static String DATETIME_DEFFMT = "yyyy-MM-dd HH:mm:ss";

    public static String TIMESTAMP_DEFFMT = "HH:mm:ss";

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    public static String formatDispay(String anDate) {
        if (StringUtils.isNotBlank(anDate)) {
            String pp = null;
            String opp = null;
            Date dd = null;
            try {
                if (anDate.length() == 8) {
                    pp = "yyyyMMdd";
                    opp = patterDislay[0];
                } else if (anDate.length() == 6) {
                    pp = "HHmmss";
                    opp = patterDislay[3];
                } else if (anDate.length() == 13) {
                    pp = "yyyyMMdd HHmm";
                    opp = patterDislay[1];
                } else if (anDate.length() > 13) {
                    pp = "yyyyMMdd HHmmss";
                    opp = patterDislay[2];
                } else {
                    dd = parseDate(anDate, parsePatterns);
                    opp = patterDislay[0];
                }
                dd = parseDate(anDate, pp);
                return formatDate(dd, opp);
            }
            catch (ParseException e) {
                return anDate;
            }
        } else {
            return anDate;
        }
    }

    public static String formatMonthDispay(String anDate) {
        if (StringUtils.isNotBlank(anDate)) {
            String pp = null;
            String opp = null;
            Date dd = null;
            try {
                pp = "yyyyMM";
                opp = patterDislay[4];
                dd = parseDate(anDate, pp);
                return formatDate(dd, opp);
            }
            catch (ParseException e) {
                return anDate;
            }
        } else {
            return anDate;
        }
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    public static String getCHDate() {

        return DateFormatUtils.format(new Date(), "yyyy年MM月dd日");
    }

    public static String getCHDateTime() {

        return DateFormatUtils.format(new Date(), "yyyy年MM月dd日  HH时mm分");
    }

    public static java.util.Date getNow() {
        return new Date(System.currentTimeMillis());
    }

    public static java.sql.Timestamp getTimeStamp() {

        return new java.sql.Timestamp(System.currentTimeMillis());
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        if (date == null) {
            return null;
        }
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    // 格式化输出日期格式
    public static String formatDispDate(String anDate) {
        if (StringUtils.isNotBlank(anDate)) {
            StringBuilder sb = new StringBuilder();
            int[] arrInt = new int[] { 0, 4, 6, 8 };
            for (int i = 0; i < 3; i++) {
                sb.append(anDate.substring(arrInt[i], arrInt[i + 1])).append("-");
            }
            sb.setLength(sb.length() - 1);
            return sb.toString();
        }
        return anDate;
    }

    /**
     * 格式化输出日期格式  202013 HH:mm:ss
     * 
     * 
     */
    public static String formatDispTime(String anTime) {
        if (StringUtils.isNotBlank(anTime)) {
            StringBuilder sb = new StringBuilder();
            int[] arrInt = new int[] { 0, 2, 4, 6 };
            for (int i = 0; i < 3; i++) {
                sb.append(anTime.substring(arrInt[i], arrInt[i + 1])).append(":");
            }
            sb.setLength(sb.length() - 1);
            return sb.toString();
        }
        return anTime;
    }

    public static String formatNumberDate(Date anDate) {
        return formatDate(anDate, "yyyyMMdd");
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getNumTime() {
        return formatDate(new Date(), "HHmmss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getNumDate() {
        return formatDate(new Date(), "yyyyMMdd");
    }

    public static String getNumDateTime() {
        return formatDate(new Date(), "yyyyMMdd HHmmss");
    }

    public static String getNumMonth() {
        return formatDate(new Date(), "yyyyMM");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeekByDate(String date) {
        try {
            return formatDate(parseDate(date, parsePatterns), "E");
        }
        catch (ParseException e) {
            return null;
        }
    }

    /**
     * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e) {
            return null;
        }
    }

    /**
     * 
     * 过去多少天的日期
     * 
     * @param 当前时间过去的天数
     * @return 相对现在过去天数
     * @throws 异常情况
     */
    public static Date pastDate(int anDay) {

        return addDays(getNow(), anDay);
    }

    public static String pastDateStr(int anDay) {

        return formatNumberDate(addDays(getNow(), anDay));
    }

    /**
     * 获取过去的天数
     * 
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     * 
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     * 
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     * 
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取两个日期之间的天数
     * 
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 计算日期之间的天数，如果起始日期或终止日期为空或0；则返回0
     * 
     * @param anBefore 起始日期
     * @param anAfter  终止日期
     * @return
     */
    public static int getDistanceOfTwoDay(String anBefore, String anAfter) {

        Date actualDate = parseDate(anBefore);
        Date endDate = parseDate(anAfter);
        if (null == actualDate || null == endDate) {

            return 0;
        }

        return (int) Math.round(BetterDateUtils.getDistanceOfTwoDate(actualDate, endDate));
    }

    /**
     * 获得当前的小时
     * @return
     */
    public static int getHourTime() {
        Calendar cd = Calendar.getInstance();
        cd.setTime(getNow());
        return cd.get(Calendar.HOUR_OF_DAY);
    }

    public static String dateDiff(Date anStartTime, Date anEndTime, String anFormat) {
        long diff = anEndTime.getTime() - anStartTime.getTime();
        long day = diff / ND;
        long hour = diff % ND / NH;
        long min = diff % ND % NH / NM;
        long sec = diff % ND % NH % NM / NS;
        Calendar cd = Calendar.getInstance();
        cd.set(1970, 01, (int) day, (int) hour, (int) min, (int) sec);

        return BetterDateUtils.formatDate(cd.getTime(), anFormat);
    }

    /**
     * 获取日期之间的天数
     * 
     * @param d1
     * @param d2
     * @return
     */
    public static int getDaysBetween(java.util.Calendar d1, java.util.Calendar d2) {
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);
        int y2 = d2.get(java.util.Calendar.YEAR);
        if (d1.get(java.util.Calendar.YEAR) != y2) {
            d1 = (java.util.Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
                d1.add(java.util.Calendar.YEAR, 1);
            }
            while (d1.get(java.util.Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 获取工作天数
     * 
     * @param d1
     * @param d2
     * @return
     */
    public static int getWorkingDay(java.util.Calendar d1, java.util.Calendar d2) {
        int result = -1;
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        // int betweendays = getDaysBetween(d1, d2);
        // int charge_date = 0;
        int charge_start_date = 0;// 开始日期的日期偏移量
        int charge_end_date = 0;// 结束日期的日期偏移量
        // 日期不在同一个日期内
        int stmp;
        int etmp;
        stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
        etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
        if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
            charge_start_date = stmp - 1;
        }
        if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
            charge_end_date = etmp - 1;
        }
        // }
        result = (getDaysBetween(getNextMonday(d1), getNextMonday(d2)) / 7) * 5 + charge_start_date - charge_end_date;
        // System.out.println("charge_start_date>" + charge_start_date);
        // System.out.println("charge_end_date>" + charge_end_date);
        // System.out.println("between day is-->" + betweendays);
        return result;
    }

    /**
     * 获取中文日期
     * 
     * @param date
     * @return
     */
    public static String getChineseWeek(Calendar date) {
        final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        // System.out.println(dayNames[dayOfWeek - 1]);
        return dayNames[dayOfWeek - 1];
    }

    /**
     * 获得日期的下一个星期一的日期
     * 
     * @param date
     * @return
     */
    public static Calendar getNextMonday(Calendar date) {
        Calendar result = null;
        result = date;
        do {
            result = (Calendar) result.clone();
            result.add(Calendar.DATE, 1);
        }
        while (result.get(Calendar.DAY_OF_WEEK) != 2);
        return result;
    }

    /**
     * 获取休息日
     * 
     * @param d1
     * @param d2
     * @return
     */
    public static int getHolidays(Calendar d1, Calendar d2) {
        return getDaysBetween(d1, d2) - getWorkingDay(d1, d2);
    }

    public static String addStrDays(String anDate, final int amount) {

        return addStrDays(anDate, amount, null);
    }

    public static String addStrDays(String anDate, final int amount, String anPatten) {
        Date dd = null;
        try {
            if (StringUtils.isNotBlank(anPatten)) {
                dd = parseDate(anDate, anPatten);
                dd = addDays(dd, amount);
                return formatDate(dd, anPatten);
            } else {
                dd = parseDate(anDate, parsePatterns);
                dd = addDays(dd, amount);
                return formatNumberDate(dd);
            }

        }
        catch (ParseException e) {
            return anDate;
        }
    }

    public static String addStrMonths(String anDate, final int amount) {

        return addStrMonths(anDate, amount, null);
    }

    public static String addStrMonths(String anDate, final int amount, String anPatten) {
        Date dd = null;
        try {
            if (StringUtils.isNotBlank(anPatten)) {
                dd = parseDate(anDate, anPatten);
                dd = addMonths(dd, amount);
                return formatDate(dd, anPatten);
            } else {
                dd = parseDate(anDate, parsePatterns);
                dd = addMonths(dd, amount);
                return formatNumberDate(dd);
            }

        }
        catch (ParseException e) {
            return anDate;
        }
    }

    public static String formatRemotePath(String anWorkPath, String anWorkDate) {
        if (StringUtils.isNotBlank(anWorkDate)) {
            Date workD = BetterDateUtils.parseDate(anWorkDate);
            if (workD != null) {
                anWorkPath = anWorkPath.replaceAll("@Y", BetterDateUtils.formatDate(workD, "yyyy"));
                anWorkPath = anWorkPath.replaceAll("@M", BetterDateUtils.formatDate(workD, "MM"));
                anWorkPath = anWorkPath.replaceAll("@D", BetterDateUtils.formatDate(workD, "dd"));
            }
        }
        return anWorkPath;
    }

    private static Date getDay(Date date, int anLength) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, anLength);
        date = calendar.getTime();
        return date;
    }

    // 得到后一天
    public static Date getNextDay(Date date) {

        return getDay(date, 1);

    }

    // 得到前一天
    public static Date getForwardDay(Date date) {

        return getDay(date, -1);
    }

    public static String getForwardDay() {

        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date date = format.parse(getNumDate());
            return format.format(getForwardDay(date));

        }
        catch (Exception e) {

            return getNumDate();
        }

    }

    public static String getNextDay() {

        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            Date date = format.parse(getNumDate());
            return format.format(getNextDay(date));

        }
        catch (Exception e) {

            return getNumDate();
        }

    }

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        // System.out.println(pastDateStr(-60));
        // System.out.println(formatDispTime("102123"));
        // System.out.println(formatDate(parseDate("2010/3/6")));
        // System.out.println(getDate("yyyy年MM月dd日 E"));
        // long time = new Date().getTime()-parseDate("2017-05-01").getTime();
        // System.out.println(time/(24*60*60*1000));
        // System.out.println(formatMonthDispay("201705"));
        // System.out.println(formatDispTime("111212"));
        // System.out.println(getNumMonth());

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = format.parse(getNumDate());
        System.out.println(getNumDate());
        System.out.println(getForwardDay());
        System.out.println(getNextDay());
        System.out.println("forward........." + format.format(getForwardDay(date)));

        // Date date2=new Date(2017, 6, 30);
        System.out.println("nexData........." + format.format(getNextDay(format.parse(getNumDate()))));

        // long time = BetterDateUtils.parseDate("201607").getTime()-BetterDateUtils.parseDate("201705").getTime();
        // System.out.println(time);
    }
}
