package com.imyuu.travel.util;

import android.os.Environment;

public class Config {


    public static final String UU_FILEPATH = (new StringBuilder()).append(Environment.getExternalStorageDirectory()).append("/imyuu/").toString();
    public static final String Map_FILEPATH = (new StringBuilder()).append(Environment.getExternalStorageDirectory()).append("/imyuu/map/").toString();
    public static final String CACHE_DIR = UU_FILEPATH+"cache";
    public static final int PHOTOALBUM_PROPORTION_DENOMINATOR = 640;
    public static final int PHOTOALBUM_PROPORTION_MOLECULE = 450;
    public static final String SERVER_ADDR = "http://imyuu.com:8900/api/";


    public static  String OLDMAP_SERVER_ADDR = "http://imyuu.com:9100/download/";
    public static  String MAP_SERVER_ADDR = "http://imyuu.com:9100/map/";
    public static  String IMAGE_SERVER_ADDR = "http://imyuu.com:9100/images/";
    public static  String TILE_URL_ROOT = "http://imyuu.com:9100/tiles/";
    public static  String ADVERT_IMAGE="http://imyuu.com:9100/images/advert/";
    public static String START_ADVERT = "start_advert.jpg";
    public static final String jsonFile = ".json";
    public static final int THREAD_NUM = 3;
    public static final int PAGE_SIZE = 10;
    public static final int LIMIT = 10;
    public static final String SHAREDPREFERENCES_NAME = "imyuu";
    public static final String WELCOME_FLAG = "welcome";
    public static final int WELCOME_FLAG_READED = 1;
    public static  final String DESCRIPTOR = "com.umeng.share";


    public static void setMAP_SERVER_ADDR(String MAP_SERVER_ADDR) {
        Config.MAP_SERVER_ADDR = MAP_SERVER_ADDR;
    }

    public static void setIMAGE_SERVER_ADDR(String IMAGE_SERVER_ADDR) {
        Config.IMAGE_SERVER_ADDR = IMAGE_SERVER_ADDR;
    }

    public static void setTILE_URL_ROOT(String TILE_URL_ROOT) {
        Config.TILE_URL_ROOT = TILE_URL_ROOT;
    }
    /* 请求码*/
    public static final int IMAGE_REQUEST_CODE = 0;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int DEFAULT_REQUEST_CODE = 2;

    /*头像名称*/
    public static final String FACE_FILE_NAME = "faceImage.jpg";
    //头像保存路径
    public static final String FACE_FILE_PATH = UU_FILEPATH+"userlogo/";
    //头像临时保存路径
    public static final String IMAGE_FILE_PATH_TEMP = "temp/";

    public static final  String weixin_appId = "wx7039733184c8596f";
    public static final String weixin_appSecret = "e966541c3fe2f59864918573e6c87a2d";

    public static final  String qq_appId = "100424468";
    public static final String qq_appSecret = "c7394704798a158208a74ab60104f0ba";
    public static final String qqSharingappId =  "1104261190";
    public static final String qqSharingKey = "zR27KSvTwEjQp6jZ";
    public static final String sharingTargetURL = "http://www.imyuu.com";
    public static String getMapImageURL(String scenicId) {
        return (new StringBuilder("http://imyuu.com:8900/tiles/")).append(scenicId).append("/%d/%d_%d.png").toString();
    }

}
