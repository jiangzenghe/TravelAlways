package com.imyuu.travel.util;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.imyuu.travel.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 图乐 on 2015/4/29.
 */
public class AndroidUtils {
    /**
     * 将dp转化为px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static  String getShortDate()
    {
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }
    public static  String getLongDate()
    {
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }

    public static String format(String time)
    {

       return time.substring(5,7) + "月"+time.substring(8,10) + "日" +
                time.substring(10,16);
    }

    public static int getAge(String birth)
    {
        long time=System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        Date d1=new Date(time);
        String t1=format.format(d1);

        return Integer.parseInt(t1.substring(0,4))- Integer.parseInt(birth.substring(0,4))+1;
    }



    public static String loadPhoneNumber(Context context)
    {
        TelephonyManager phoneMgr=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        //txtPhoneModel.setText(Build.MODEL); //手机型号
       return phoneMgr.getLine1Number();//本机电话号码
      //  txtSdkVersion.setText(Build.VERSION.SDK);//SDK版本号
       // txtOsVersion.setText(Build.VERSION.RELEASE);//Firmware/OS 版本号
    }
}
