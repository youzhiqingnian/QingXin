package com.qingxin.medical.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2016/7/12.
 */
public class TimeUtils {

    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;
    private static final long ONE_MOUTH = ONE_DAY * 30;
    private static final long ONE_YEAR = ONE_DAY * 365;

    @SuppressLint("SimpleDateFormat")
    public static String getdetailTime(long timeStamp) {
        Date date1 = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date1);
        return dateString;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getdetailTime2(long timeStamp) {
        Date date1 = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(date1);
        return dateString;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getdetailTime3(long timeStamp) {
        Date date1 = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = format.format(date1);
        return dateString;
    }


    public static String getDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getYestoday(String dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null;
        try {
            date2 = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getdetailTime(date2.getTime() - 1000 * 60 * 60 * 24);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getNextDay(String dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null;
        try {
            date2 = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getdetailTime(date2.getTime() + 1000 * 60 * 60 * 24);
    }
    public static String getEarly4Hours(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        Date date2 = null;
        try {
            date2 = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(date2.getTime() - 1000 * 60 * 60 * 3);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTomorrow() {
        Date date1 = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date1);
        return dateString;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getToday() {
        Date date1 = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date1);
        return dateString;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getOneWeekLater() {
        Date date1 = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date1);
        return dateString;
    }

    @SuppressLint("SimpleDateFormat")
    public static int getDD(long timeStamp) {
        Date date1 = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("dd");
        String dateString = format.format(date1);
        return Integer.valueOf(dateString);
    }

    @SuppressLint("SimpleDateFormat")
    public static long getyyyyMMdd(long timeStamp) {
        Date date1 = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dateString = format.format(date1);
        return Long.valueOf(dateString);
    }

    @SuppressLint("SimpleDateFormat")
    public static int getDayOfYear(long timeStamp) {
        Date date1 = new Date(timeStamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取时间戳
     * @param date
     * @return
     */
    public static long getTimeStamp(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long timeStamp = 0;
        try{
            timeStamp = simpleDateFormat.parse(date).getTime();
        }catch(Exception e){
            e.printStackTrace();
        }

        return timeStamp;
    }


    public static long getTimeStamp2(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        long timeStamp = 0;
        try{
            timeStamp = simpleDateFormat.parse(date).getTime();
        }catch(Exception e){
            e.printStackTrace();
        }

        return timeStamp;
    }

    public static long getTimeStamp3(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeStamp = 0;
        try{
            timeStamp = simpleDateFormat.parse(date).getTime();
        }catch(Exception e){
            e.printStackTrace();
        }

        return timeStamp;
    }

    public static long getTimeStamp4(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long timeStamp = 0;
        try{
            timeStamp = simpleDateFormat.parse(date).getTime();
        }catch(Exception e){
            e.printStackTrace();
        }

        return timeStamp;
    }

    public static long getTimeStamp5(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/dd HH:mm");
        long timeStamp = 0;
        try{
            timeStamp = simpleDateFormat.parse(date).getTime();
        }catch(Exception e){
            e.printStackTrace();
        }

        return timeStamp;
    }

    public static long getTrainaBeginStamp(String date,String timeFormate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormate);
        long timeStamp = 0;
        try{
            timeStamp = simpleDateFormat.parse(date).getTime();
        }catch(Exception e){
            e.printStackTrace();
        }

        return timeStamp;
    }


    @SuppressLint("SimpleDateFormat")
    public static int getYear(long timeStamp) {
        Date date1 = new Date(timeStamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        return cal.get(Calendar.YEAR);
    }


    /**
     * 获取目标时间和当前时间之间的差距
     *
     * @param date
     * @return
     */
    public static String getTimestampString(Date date) {
        Date curDate = new Date();
        long splitTime = curDate.getTime() - date.getTime();
        if (splitTime < ONE_YEAR) {
            if (splitTime < ONE_MINUTE) {
                return "刚刚";
            }
            if (splitTime < ONE_HOUR) {
                return String.format("%d分钟前", splitTime / ONE_MINUTE);
            }

            if (splitTime < ONE_DAY) {
                return String.format("%d小时前", splitTime / ONE_HOUR);
            }
//            if (splitTime < ONE_MOUTH) {
            return String.format("%d天前", splitTime / ONE_DAY);
//            }
//            return String.format("%d月前", splitTime / ONE_MOUTH);
        }
        return String.format("%d年前", splitTime / ONE_YEAR);
    }

    /**
     * String 转换 Date
     *
     * @param str
     * @param format
     * @return
     */
    public static Date string2Date(String str, String format) {
        try {
            return new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 将接口返回的日期格式转换成需要的格式
     *
     * @param time
     * @return
     */
    public static String getyyyy_MM_dd(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date2 = null;
        try {
            date2 = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date2Format(date2, "yyyy-MM-dd");
    }

    public static String getMMdd(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null;
        try {
            date2 = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date2Format(date2, "MM-dd");
    }

    @SuppressLint("SimpleDateFormat")
    public static long getyyyyMMddHHmmss(long timeStamp) {
        Date date1 = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = format.format(date1);
        return Long.valueOf(dateString);
    }

    public static String getMMdd2(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date2 = null;
        try {
            date2 = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date2Format(date2, "MM-dd");
    }


    public static String date2Format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getJavaTime(String timeString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
//        if(TextUtils.isEmpty( timeString)){
//            timeString = "1900-00-00 00:00" ;
//        }
        Date date = new Date();
        try {
            date = format.parse(timeString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getSecondTime(date.getTime());
    }

    public static String getSecondTime(long timeStamp) {
        Date date1 = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = format.format(date1);
        return dateString;
    }

    public static String getStandardDate(long timeStamp) {
        Date date1 = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date1);
        return dateString;
    }

    public static String getTomorrowWeekDay(String date) {
        long dateLong = Long.valueOf(date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10));
        if (date.equals(getdetailTime(System.currentTimeMillis()))) {
            return "今天";
        } else if (dateLong - getyyyyMMdd(System.currentTimeMillis()) == 1) {
            return "明天";
        } else if (dateLong - getyyyyMMdd(System.currentTimeMillis()) == 2) {
            return "后天";
        } else {
            return getWeekDay(date);
        }
    }

    public static String getWeekDay(String date) {
        String Week = "周";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//也可将此值当参数传进来

        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                Week += "日";
                break;
            case 2:
                Week += "一";
                break;
            case 3:
                Week += "二";
                break;
            case 4:
                Week += "三";
                break;
            case 5:
                Week += "四";
                break;
            case 6:
                Week += "五";
                break;
            case 7:
                Week += "六";
                break;
            default:
                break;
        }
        return Week;
    }

    public static String getWeekDay2(String date) {
        String Week = "周";
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");//也可将此值当参数传进来

        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                Week += "日";
                break;
            case 2:
                Week += "一";
                break;
            case 3:
                Week += "二";
                break;
            case 4:
                Week += "三";
                break;
            case 5:
                Week += "四";
                break;
            case 6:
                Week += "五";
                break;
            case 7:
                Week += "六";
                break;
            default:
                break;
        }
        return Week;
    }

    /**
     * @param date "yyyy-MM-dd"格式
     * @return mm月dd日的形式
     */
    public static String getHYDate(String  date) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sb = new StringBuilder();
        try {
            c.setTime(format.parse(date));
            int mm = c.get(Calendar.MONTH) + 1;
            String mmStr = mm < 10 ? "" + 0 + mm : mm + "";
            sb.append(mmStr);
            sb.append("月");
            int dd = c.get(Calendar.DAY_OF_MONTH);
            String ddStr = dd < 10 ? "" + 0 + dd : dd + "";
            sb.append(ddStr);
            sb.append("日");
            return sb.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期比较，返回两个日期差的天数
     *
     * @param date1 较早的 格式 yyyy-MM-dd
     * @param date2 较晚的
     * @return date2 - date1所差的天数
     */
    public static int compareDay(String date1, String date2) {
        int delta = 0 ;
        int yyyy1 = Integer.parseInt(date1.substring(0, 4));
        int yyyy2 = Integer.parseInt(date2.substring(0, 4));
        Log.d("TAG", "compareDay: " + date1);
        int mm1 = Integer.parseInt(date1.substring(5, 7));
        int mm2 = Integer.parseInt(date2.substring(5, 7));
        int dd1 = Integer.parseInt(date1.substring(8, 10));
        int dd2 = Integer.parseInt(date2.substring(8, 10));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (yyyy1 == yyyy2) {
            try {
                int dayOfYear1 = getDayOfYear(format.parse(date1).getTime());
                int dayOfYear2 = getDayOfYear(format.parse(date2).getTime());
                delta = dayOfYear2 - dayOfYear1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            if (yyyy1 % 4 == 0 && yyyy1 % 100 != 0 || (yyyy1 % 400 == 0)) {
                try {
                    delta =  getDelta(366, date1, date2, format);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    delta =  getDelta(365, date1, date2, format);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return delta;
    }

    private static int getDelta(int i, String date1, String date2, SimpleDateFormat format) throws ParseException {
        return i - getDayOfYear(format.parse(date1).getTime()) + getDayOfYear(format.parse(date2).getTime());
    }

    /**
     * 计算飞行时间
     * @param duration
     * @return
     */
    public static String getFlyDuration(int duration){
        if(duration >= 60){
            if(duration % 60 == 0){
                return (duration / 60) + "时";
            }else{
                return (duration / 60) + "时" + (duration % 60) + "分";
            }
        }else{
            return duration + "分";
        }


    }

    /**
     * 计算飞行时间
     * @param duration
     * @return
     */
    public static String getInternationalFlyDuration(String duration){
        if(duration.contains("h") && duration.contains("m")){
            duration = duration.replace("h","时");
            duration = duration.replace("m","分");
            return duration;
        }else if(duration.contains("h") && !duration.contains("m")){
            duration = duration.replace("h","时");
            return duration;
        }else if(!duration.contains("h") && duration.contains("m")){
            duration = duration.replace("m","分");
            return duration;
        }else{
            return duration;
        }

    }

}
