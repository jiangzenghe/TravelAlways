package com.imyuu.travel.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil
{

    public NetUtil()
    {
    }

    public static boolean isNetConnected(Context context)
    {
        NetworkInfo networkinfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isAvailable();
    }
}
