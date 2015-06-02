package com.imyuu.travel.util;

import android.os.Environment;

public class Config
{

    //public static final String NEW_FILENAME = "imyuu";
    public static final String NEW_FILEPATH = (new StringBuilder()).append(Environment.getExternalStorageDirectory()).append("/imyuu/").toString();
    public static final String Map_FILEPATH = (new StringBuilder()).append(Environment.getExternalStorageDirectory()).append("/imyuu/map/").toString();
    public static final int PHOTOALBUM_PROPORTION_DENOMINATOR = 640;
    public static final int PHOTOALBUM_PROPORTION_MOLECULE = 450;
    public static final String SERVER_ADDR = "http://imyuu.com:8900/api/";
    public static final String TVL_URL_ROOT = "http://imyuu.com:9100/tiles/";
    public static final String MAP_SERVER_ADDR = "http://imyuu.com:9100/map/";
    public static final String IMAGE_SERVER_ADDR = "http://imyuu.com:9100/images/";
    public static final String  jsonFile= ".json";
    public static final int THREAD_NUM = 3;
    public static final String SHAREDPREFERENCES_NAME="imyuu";
    public static final String WELCOME_FLAG="welcome";
    public static final int WELCOME_FLAG_READED=1;

    public Config()
    {
    }

    public static String getTourMapImageURL(int i)
    {
        return (new StringBuilder("http://imyuu.com:8900/tiles/")).append(i).append("/%d/%d_%d.png").toString();
    }

}
