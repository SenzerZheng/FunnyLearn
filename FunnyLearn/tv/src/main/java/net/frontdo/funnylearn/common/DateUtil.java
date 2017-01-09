package net.frontdo.funnylearn.common;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ProjectName: DateUtil
 * Description: 日期格式计算工具
 * <p>
 * author: JeyZheng
 * version: 2.0
 * created at: 11/20/2016 17:07
 */
public class DateUtil {
    //    public static final int SECOND = 1000;
//    public static final int MINUTE = 1000;
//    public static final int HOUR = 1000;
//    public static final int DAY = 1000;
//    public static final int MONTY = 1000;
    public static final String SDF_01 = "yyyy-MM-dd";
    public static final String SDF_02 = "yyyy年MM月dd日";
    public static final String SDF_03 = "yyyy-MM";


    /**
     * 2岁5个月
     *
     * @param strDate "2013-02-19 09:29:58";
     */
    public static String getYearMonth(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat(SDF_01);
        Calendar srcCal = Calendar.getInstance();
        Calendar curCal = Calendar.getInstance();

        String dateStr = "";
        try {
            Date srcDate = format.parse(strDate);

            srcCal.setTimeInMillis(srcDate.getTime());
            curCal.setTimeInMillis(System.currentTimeMillis());

            int day = curCal.get(Calendar.DAY_OF_MONTH) - srcCal.get(Calendar.DAY_OF_MONTH);
            int month = curCal.get(Calendar.MONTH) - srcCal.get(Calendar.MONTH);
            int year = curCal.get(Calendar.YEAR) - srcCal.get(Calendar.YEAR);           // year

            if (day < 0) {              // get day
                month -= 1;
                curCal.add(Calendar.MONTH, -1);
//                day = day + curCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            if (month < 0) {            // get month
                month = (month + 12) % 12;
                year--;
            }

            dateStr = year + "岁" + month + "个月";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateStr;
    }

    /**
     * 2（2岁）
     *
     * @param strDate "2013-02-19 09:29:58";
     */
    public static int getAge(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat(SDF_01);
        Calendar srcCal = Calendar.getInstance();
        Calendar curCal = Calendar.getInstance();

        int age = 0;
        try {
            Date srcDate = format.parse(strDate);

            srcCal.setTimeInMillis(srcDate.getTime());
            curCal.setTimeInMillis(System.currentTimeMillis());

            age = curCal.get(Calendar.YEAR) - srcCal.get(Calendar.YEAR);           // year
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return age;
    }

    /**
     * 获取yyyy-MM
     *
     * @param strDate
     * @return
     */
    public static String formatYearMonth(String strDate) {
        if (TextUtils.isEmpty(strDate)) {
            return "";
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(SDF_01);
            Date date = dateFormat.parse(strDate);
            dateFormat = new SimpleDateFormat(SDF_03);

            return dateFormat.format(date);
        } catch (ParseException e) {

            e.printStackTrace();
            return "";
        }
    }

    /**
     * yyyy-MM-dd to yyyy年MM月dd日
     *
     * @param strDate
     * @return
     */
    public static String getCNDate(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat(SDF_01);
        SimpleDateFormat format2 = new SimpleDateFormat(SDF_02);

        Date date = null;
        String cnStrDate = "";
        try {
            date = format.parse(strDate);
            cnStrDate = format2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cnStrDate;
    }

    /**
     * get yyyy-MM-dd
     *
     * @return
     */
    public static String getENDate() {
        SimpleDateFormat format = new SimpleDateFormat(SDF_01);
        return format.format(new Date());
    }

    public static String getSelectedDate(int year, int month, int dayOfMonth) {
        final String FILL_CHAR = "0";
        final int TWO_NUM = 10;

        int realMonth = month + 1;
        String strMonth = realMonth < TWO_NUM ? FILL_CHAR + realMonth : String.valueOf(realMonth);
        String strDay = dayOfMonth < TWO_NUM ? FILL_CHAR + dayOfMonth : String.valueOf(dayOfMonth);
        return year + "-" + strMonth + "-" + strDay;
    }
}
