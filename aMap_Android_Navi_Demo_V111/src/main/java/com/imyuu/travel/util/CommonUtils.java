package com.imyuu.travel.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 工具类
 *
 * @author <p>Modification History:</p>
 *         <p>Date             Author      Description</p>
 *         <p>--------------------------------------------------------------</p>
 *         <p>          new</p>
 *         <p>  </p>
 */
public class CommonUtils {
    /**
     * 里程桩格式前缀
     */
    public static final String MILEAGE_PREFIX = "K";
    public static DecimalFormat mileageFormatter = new DecimalFormat(MILEAGE_PREFIX + "####.000");
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private static long lastClickTime; //用于记录控件的点击时间

    public static Bundle getActivityMetaDataBundle(
            PackageManager packageManager, ComponentName component) {
        Bundle bundle = null;
        try {
            ActivityInfo ai = packageManager.getActivityInfo(component,
                    PackageManager.GET_META_DATA);
            bundle = ai.metaData;
        } catch (NameNotFoundException e) {
            Log.e("getMetaDataBundle", e.getMessage(), e);
        }

        return bundle;
    }

    /**
     * 将一个日期类型格式化为一个年-月-日 小时:分钟 如 2014-09-12 17:24
     *
     * @param date
     * @return
     */
    public static String toString(Date date) {
        return dateFormat.format(date);
    }

    /**
     * 将一个形如2014-09-12 17:24的日期格式字符串转换为日期类型
     *
     * @param text
     * @return
     * @throws java.text.ParseException
     */
    public static Date toDate(String text) throws ParseException {
        return dateFormat.parse(text);
    }

    /**
     * 用指定的字符做为连字符拼接一个字符串数组到一个字符串。concat(",","a","b","c")=>"a,b,c"
     *
     * @param sp
     * @param items
     * @return
     */
    public static String concat(String sp, String[] items) {
        if (items == null || items.length == 0) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < items.length; i++) {
            if (i != 0) {
                buffer.append(sp);
            }
            buffer.append(items[i]);
        }
        return buffer.toString();
    }

    /**
     * 写字节数组到指定的文件中。
     *
     * @param data
     * @param file
     * @throws java.io.IOException
     */
    public static void writeBytesToFile(byte[] data, File file) throws IOException {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            fout.write(data, 0, data.length);
        } catch (IOException e) {
            throw e;
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 读取指定的文件到一个字节数组中.
     *
     * @param file
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static byte[] readFileAllContentToByteArray(File file) throws FileNotFoundException, IOException {
        FileInputStream fin = null;
        ByteArrayOutputStream bout = null;

        try {
            fin = new FileInputStream(file);
            bout = new ByteArrayOutputStream();
            byte[] bytes = new byte[16 * 1024];
            int len = 0;
            while ((len = fin.read(bytes)) >= 0) {
                bout.write(bytes, 0, len);
            }

            return bout.toByteArray();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                    fin = null;
                } catch (IOException e) {
                }
            }

            if (bout != null) {
                try {
                    bout.close();
                    bout = null;
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 天气预报 返回当前星期
     */
    public static String parseWeek(int i) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return weekDays[i];
    }

    /**
     * 把数值字符串格式化为里程桩格式。如1=> k1+000
     */
    public static String mileageForamt(String value) {
        String text = mileageFormatter.format(Float.parseFloat(value));
        return text.replace('.', '+');
    }

    /**
     * 把里程桩格式字符串转化为一个数值格式。如：k1+000=> 1.000
     *
     * @param text
     * @return
     * @throws java.text.ParseException
     */
    public static String valueOfMileage(String text) throws ParseException {
        if (TextUtils.isEmpty(text) || !text.startsWith(MILEAGE_PREFIX)) {
            throw new ParseException("无效的里程桩字符串值:" + text, 0);
        }

        return text.substring(1).replace('+', '.');
    }

    public static boolean isNum(String value) {
        String temp = "^[0-9]+(.[0-9]{1,3})?$";
        Pattern p = Pattern.compile(temp);
        Matcher m = p.matcher(value);
        return m.matches();
    }

    public static String formatValue(String value) {
        return mileageForamt(value);
    }

    /**
     * 读取流中的数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        //如果字节流中的数据不等于-1，就说明一直有，然后循环读出
        while ((len = inStream.read(buffer)) != -1) {
            //将读出的数据放入内存中
            outputStream.write(buffer);

        }
        inStream.close();
        return outputStream.toByteArray();
    }

    /**
     * 把数据按照一定的格式做转化,返回年份
     */
    public static String formatDateToYear(String value) {
        String s = "";
        String[] a = value.split("-");
        s = a[0];
        return s;
    }

    /**
     * 把数据按照一定的格式做转化,返回天
     */
    public static String formatDateToDay(String value) {
        String s = "";
        String[] a = value.split("-");
        s = a[1] + "." + a[2];
        return s;
    }

    /**
     * 用于判断控件点击是否过于频繁
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取屏幕的宽度
     */
    public final static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的度
     */
    public final static int getWindowsHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 字符串非空判断
     *
     * @param str
     * @return
     */
    public static boolean strIsNotNull(String str) {
        if (str == null || str.trim().length() <= 0)
            return false;
        return true;
    }

    public static boolean FileCopy(File come, File to) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        int i = 0;
        try {
            fis = new FileInputStream(come); // 文件输入流
            bis = new BufferedInputStream(fis); // 连接带缓冲的输入流
            fos = new FileOutputStream(to); // 文件输出流
            byte[] b = new byte[1024];
            while ((i = bis.read(b)) != -1) {
                fos.write(b, 0, i); // 写数据
            }
        } catch (IOException e) {
            throw e;
        } finally {
            fis.close();
            fos.close();
            bis.close();
        }
        return true;
    }

    /**
     * 系统当前时间和传入时间大小的比较(如果传入的时间大于当前时间则返回true) 参数为String类型
     */
    public static boolean compareWithCurrentTime(String timestamp) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        if (currentTimestamp.toString().compareTo(timestamp) < 0) {
            return true;
        }
        return false;
    }
}
