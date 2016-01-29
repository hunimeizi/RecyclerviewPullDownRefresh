package com.recyclerviewpulldownrefresh.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 本类最主要的是获取时间以及对时间格式的转换
 * 1. TimeSet.getDate()               获取日期时间
 * 2. TimeSet.getCurrentTimeMills()   获取当前时间毫秒值
 * 3. TimeSet.getCurrentTimeSeconds() 获取当前时间秒值
 * 4. TimeSet.getDate()               获取指定格式日期,eg:2014-11-07 09:41
 * 5. TimeSet.getDate2()              获取指定格式日期,eg:2014年11月07日
 * 6. TimeSet.getDate3()              获取指定格式日期,eg:20141107
 * 7. TimeSet.GetDateResult()         得到最终显示值。eg:凌晨，中午，昨天，11月7日，2014年8月23日
 * 8. TimeSet.getDaojiTime()          得到倒计时的秒值
 * 9. TimeSet.getOpen()               判断是否可以开奖
 * 10.TimeSet.getDifference(long,long)比较两个时间点的差值 getDifference(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
 * 11.TimeSet.getDifference(long)     毫秒值转换成 年月日时分秒 getDifference(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
 * 12.TimeSet.getGeShiDate(long)      通过秒数换算出 分 秒
 * 13.TimeSet.getStandardDate(long)   时间转换成多少分钟前
 */
public class TimeSet {
    private static SimpleDateFormat formatBuilder;

    /**
     * 获取日期时间
     *
     * @param format String
     * @return String
     */
    public static String getDate(String format) {
        formatBuilder = new SimpleDateFormat(format);
        return formatBuilder.format(new Date());
    }

    /**
     * 获取当前时间毫秒值
     *
     * @return long
     */
    public static long getCurrentTimeMills() {
        return new Date().getTime();
    }

    /**
     * 获取当前时间秒值
     *
     * @return long
     */
    public static long getCurrentTimeSeconds() {
        return new Date().getTime() / 1000;
    }

    /**
     * 获取指定格式日期,eg:2014-11-07 09:41
     *
     * @return String
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd HH:mm");
    }

    /**
     * 获取指定格式日期,eg:2014年11月07日
     *
     * @return String
     */
    public static String getDate2() {
        return getDate("yyyy年MM月dd日");
    }

    /**
     * 获取指定格式日期,eg:20141107094253
     *
     * @return String
     */
    public static String getDate3() {
        return getDate("yyyyMMddHHMMSS");
    }
    public static String getDate4() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到最终显示值。eg:凌晨，中午，昨天，11月7日，2014年8月23日
     *
     * @param time String
     * @return str
     */
    public static String GetDateResult(String time) {
        if (time != null && !time.equals("")) {
            // 2011 11 21 12 12 12yyyyMMddhhmmss //2013 03 12 12 50 01
            int year = Integer.valueOf((String) time.subSequence(0, 4));
            int month = Integer.valueOf((String) time.subSequence(4, 6));
            int date = Integer.valueOf((String) time.subSequence(6, 8));
            int hour = Integer.valueOf((String) time.subSequence(8, 10));
            int minute = Integer.valueOf((String) time.subSequence(10, 12));
            // int second=Integer.valueOf((String) time.subSequence(12, 14));
            Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
            int year1 = c.get(Calendar.YEAR);
            // int month1 = c.get(Calendar.MONTH)+1;
            // int date1 = c.get(Calendar.DATE);
            int data11 = c.get(Calendar.DAY_OF_YEAR);
            // int hour1 = c.get(Calendar.HOUR_OF_DAY);
            // int minute1 = c.get(Calendar.MINUTE);
            // int second1 = c.get(Calendar.SECOND);
            String dayofyear = year + "/" + month + "/" + date;
            String str = null;
            int dataM = Math.abs(data11 - GetDayOfYear(dayofyear));
            int yearM = Math.abs(year1 - year);
            if (dataM == 0) {// 今天
                if (hour >= 0 && hour < 5) {// 凌晨
                    str = "凌晨";
                } else if (hour >= 5 && hour < 9) {// 早晨
                    str = "早晨";
                } else if (hour >= 9 && hour < 13) {// 上午
                    str = "上午";
                } else if (hour >= 13 && hour < 19) {// 下午
                    str = "下午";
                } else if (hour >= 18 && hour < 21) {// 晚上
                    str = "晚上";
                } else if (hour >= 21 && hour <= 24) {// 深夜
                    str = "深夜";
                }
            } else if (dataM == 1) { // 昨天
                str = "昨天";
            } else if (dataM == 2) {// 周几
                str = Week(time);
            } else {// 年月日
                if (yearM == 0) { // 月 日
                    str = month + "月" + date + "日";
                } else {// 年月日
                    str = year + "年" + month + "月" + date + "日";
                }
            }
            return str + "  " + NumDouble(hour) + ":" + NumDouble(minute);
        } else
            return "";
    }

    /**
     * 将时间转换成 24时格式
     *
     * @param timea  十二小时格式
     * @return  返回值
     */
    private static String NumDouble(int timea) {
        DecimalFormat df = new DecimalFormat("00");
        return df.format(timea);
    }


    public static String Week(String time) {
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = sdfInput.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;
        return dayNames[dayOfWeek];
    }

    private static int GetDayOfYear(String time) {
        @SuppressWarnings("deprecation")
        Date date = new Date(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 得到当前值
     *
     * @return str 和服务端发送数据一致
     */
    public static String GetNow() {
        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH) + 1;
        int date1 = c.get(Calendar.DATE);
        int hour1 = c.get(Calendar.HOUR_OF_DAY);
        int minute1 = c.get(Calendar.MINUTE);
        int second1 = c.get(Calendar.SECOND);
        return year1 + "" + NumDouble(month1) + "" + NumDouble(date1) + "" + NumDouble(hour1) + "" + NumDouble(minute1) + "" + NumDouble(second1);
    }

    /**
     * 得到当前时间 yy-mm-dd HH:mm:ss
     *
     * @return str 和服务端发送数据一致
     */
    public static String Getnowtime() {
        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH) + 1;
        int date1 = c.get(Calendar.DATE);
        int hour1 = c.get(Calendar.HOUR_OF_DAY);
        int minute1 = c.get(Calendar.MINUTE);
        int second1 = c.get(Calendar.SECOND);
        return year1 + "-" + NumDouble(month1) + "-" + NumDouble(date1) + " " + NumDouble(hour1) + ":" + NumDouble(minute1) + ":" + NumDouble(second1);
    }

    public static String strToDate(String strDate) throws ParseException {
        int a;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date strtodate = formatter.parse(strDate);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(strtodate);
        a = cal1.get(Calendar.DAY_OF_WEEK);
        return a + "";
    }

    /**
     * 通过参数传入1-7 返回周几
     *
     * @param sdate String
     * @return String
     */
    public static String getWeekStr(String sdate) {
        String str;
        str = sdate;
        if ("1".equals(str)) {
            str = "周日";
        } else if ("2".equals(str)) {
            str = "周一";
        } else if ("3".equals(str)) {
            str = "周二";
        } else if ("4".equals(str)) {
            str = "周三";
        } else if ("5".equals(str)) {
            str = "周四";
        } else if ("6".equals(str)) {
            str = "周五";
        } else if ("7".equals(str)) {
            str = "周六";
        }
        return str;
    }

    /**
     * 得到倒计时的秒值
     *
     * @param strDate String
     * @return long
     */
    public static long getDaojiTime(String strDate) {
        long i = 0;
        Calendar r = Calendar.getInstance();
        Calendar r1 = Calendar.getInstance();// 创建Calendar对象初始化为系统当前值
        try {
            // String str = "2013-05-23 15:36:12";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date birthday = sdf.parse(strDate);
            r.setTime(birthday);// 用获得的Date设置r
            Date t = r.getTime();
            i = (r1.getTimeInMillis() - r.getTimeInMillis()) / 1000;// 进行比较,两者具体相差多少毫秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 判断是否可以开始刮奖
     *
     * @param strDate  时间格式
     * @return false不可以，true可以
     */
    public static boolean getOpen(String strDate) {
        long i;
        Calendar r = Calendar.getInstance();
        Calendar r1 = Calendar.getInstance();// 创建Calendar对象初始化为系统当前值
        try {
            // String str = "2013-05-23 15:36:12";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date birthday = sdf.parse(strDate);
            System.out.print(birthday);
            r.setTime(birthday);// 用获得的Date设置r
            Date t = r.getTime();
            i = r1.compareTo(r);// 比较两个时间的先后r1是当前时间 r是你指定的时间
            if (i > 0) {
                return true;
            } else if (i < 0) {
                return false;
            } else {
                return false;
            }
        } catch (ParseException e) {
            System.out.print("失败");
            return false;
        }
    }

    /**
     * 得到闹钟的时间
     *
     * @param sd 时间 毫秒
     * @return String 日期
     */
    public static String getDateToString(long sd) {
        // long sd=1345185923140L;
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(gc.getTime());
    }

    /**
     * 本地开奖时间
     *
     * @param sd long
     * @return String
     */
    public static String getOpenDateToString(long sd) {
        // long sd=1345185923140L;
        sd += Calendar.getInstance().getTimeInMillis();// 本地开奖时间
        Date dat = new Date(sd);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(gc.getTime());
    }

    /**
     * 本地开奖时间
     *
     * @param sd 时间
     * @return date
     */
    public static Date getOpenDateToDate(long sd) {
        // long sd=1345185923140L;
        sd += Calendar.getInstance().getTimeInMillis();// 本地开奖时间
        return new Date(sd);
    }

    /**
     * 本地开奖时间
     *
     * @param sd 时间
     * @return long
     */
    public static long getOpenDateToLong(long sd) {
        // long sd=1345185923140L;
        sd += Calendar.getInstance().getTimeInMillis();// 本地开奖时间
        return sd;
    }

    // 查分毫秒值用的常量
    private final static long yearLevelValue = 365 * 24 * 60 * 60 * 1000l;
    private final static long monthLevelValue = 30 * 24 * 60 * 60 * 1000l;
    private final static long dayLevelValue = 24 * 60 * 60 * 1000l;
    private final static long hourLevelValue = 60 * 60 * 1000l;
    private final static long minuteLevelValue = 60 * 1000l;
    private final static long secondLevelValue = 1000l;

    /**
     * 比较两个时间点的差值 getDifference(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
     *
     * @param nowTime    long
     * @param targetTime long
     * @return String
     */
    public static String getDifference(long nowTime, long targetTime) {// 目标时间与当前时间差
        long period = targetTime - nowTime;
        return getDifference(period);
    }

    /**
     * 毫秒值转换成 年月日时分秒 getDifference(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
     *
     * @param period long
     * @return String
     */
    private static String getDifference(long period) {
        String result;

        /******* 计算出时间差中的年、月、日、天、时、分、秒 *******/
        int year = getYear(period);
        int month = getMonth(period - year * yearLevelValue);
        int day = getDay(period - year * yearLevelValue - month * monthLevelValue);
        int hour = getHour(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue);
        int minute = getMinute(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue - hour * hourLevelValue);
        int second = getSecond(period - year * yearLevelValue - month * monthLevelValue - day * dayLevelValue - hour * hourLevelValue - minute * minuteLevelValue);

        result = year + "年" + month + "月" + day + "天" + hour + "时" + minute + "分" + second + "秒";
        return result;
    }

    /**
     * 通过秒数换算出 分 秒
     *
     * @param miao long
     * @return String
     */
    public static String getGeShiDate(long miao) {
        long minute = miao / 60;
        long scond = miao - minute * 60;
        return minute + "分" + scond + "秒";
    }

    public static int getYear(long period) {
        return (int) (period / yearLevelValue);
    }

    public static int getMonth(long period) {
        return (int) (period / monthLevelValue);
    }

    public static int getDay(long period) {
        return (int) (period / dayLevelValue);
    }

    public static int getHour(long period) {
        return (int) (period / hourLevelValue);
    }

    public static int getMinute(long period) {
        return (int) (period / minuteLevelValue);
    }

    public static int getSecond(long period) {
        return (int) (period / secondLevelValue);
    }

    /**
     * 获取当毫秒数
     *
     * @param date  需要转换的时间（格式yyyy/MM/dd HH:mm:ss）
     * @return  返回long
     */
    public static long getTimeMillis(String date) {
        long timeMillis = 0;
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date));
            timeMillis = calendar.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMillis;
    }

    public static String gettime() {
        return null;

    }

    /**
     * @param date 时间
     * @param format 需要格式化的格式模板
     */
    public static long getTimeMillis(String date, String format) {
        long timeMillis = 0;
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(new SimpleDateFormat(format).parse(date));
            timeMillis = calendar.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMillis;
    }


    public static String getDateStringByTimeMillis(long timeMillis) {
        Date dat = new Date(timeMillis);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(gc.getTime());
    }

    public static String getDateString4TimeMillis(long timeMillis) {
        Date dat = new Date(timeMillis);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(gc.getTime());
    }

    /**
     *时间转换成多少分钟之前
     *  @param timeStr 时间
     * @return String
     */
    public static String getStandardDate(String timeStr) {
        StringBuilder sb = new StringBuilder();
        long t = Long.parseLong(timeStr);
        // long time = System.currentTimeMillis() - (t * 1000);
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time / 1000);// 秒前
        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前
        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时
        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前
        if (day - 1 > 0) {
            sb.append(day).append("天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour).append("小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute).append("分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill).append("秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    public static long timeDateFormat(String time, String chooseTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long diff = 0;
        try {
            Date d1 = df.parse(time);
            Date d2 = df.parse(chooseTime);
            diff = d1.getTime() - d2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diff;
    }
    /**
     * 根据传入的时间格式 返回当前日期时间
     *
     * @param format String
     * @return String
     */
    public static String getCurrentDate(String format) {
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;
    }
}
