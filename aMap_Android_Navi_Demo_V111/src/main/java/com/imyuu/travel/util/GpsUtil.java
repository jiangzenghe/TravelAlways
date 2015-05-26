package com.imyuu.travel.util;
import android.content.DialogInterface;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;

public class GpsUtil
{

    public GpsUtil()
    {
    }

    public static boolean isOPen(Context context)
    {
        return ((LocationManager)context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps");
    }

    public static boolean isOpenMap(Context context)
    {
        return !NetUtil.isNetConnected(context) || isOPen(context);
    }

    public void alertGPS(final Context context)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        if (!isOPen(context))
        {
            builder.setMessage("�ǣ�����������Ҫ�ľ����ҵ��Լ���λ�á�\n" +
                    "�������������д�GPS��");
            builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

               public void onClick(DialogInterface dialoginterface, int i)
                {
                }
           });
            builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialoginterface, int i)
                {
                    Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                    context.startActivity(intent);
                }

            
         
            });
            builder.create();
            builder.show();
        }
    }
}

