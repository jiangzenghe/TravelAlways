package com.imyuu.travel.util;

import android.os.Environment;

/**
 * $Author: Frank $
 * $Date: 2014/12/23 20:45 $
 *
 * @author Frank
 * @since 1.0
 */
public class ConstantsOld {
    public static final String SHAREDPREFERENCES_NAME = "Travel";
    public static final String WELCOME_FLAG = "WELCOME_FLAG";
    public static final String INDEX_FLAG = "INDEX_FLAG";
    public static final String API_MESSAGE_KEY = "API_MESSAGE_KEY";
    public static final String SEARCH_CONTENT_KEY = "SEARCH_CONTENT_KEY";
    public static final String SCIENCE_ID_KEY = "SCIENCE_ID_KEY";
    public static final String SCIENCE_LINE_ID_KEY = "SCIENCE_LINE_ID_KEY";
    public static final String DEFAULT_LOGO_SELECTED_KEY = "DEFAULT_LOGO_SELECTED_KEY";
    public static final int WELCOME_FLAG_READED = 1;
    public final static String DATABASE_NAME = "imyuu";
    public final static int DATABASE_VERSION = 1;
    //全部景区缩略图保存路径
    public static final String SCENIC_IMAGE_FILE_PATH = Environment.getExternalStorageDirectory() + "/imyuu/scenic/";
    /*
     * 老版本路径地址   2015年2月5日09:09:30
     */
    public static final String SCENIC_RECOMMEND_IMAGE_FILE_PATH_OLD = Environment.getExternalStorageDirectory() + "/imyuu/scenicRecommend/";

    public static final String SCENIC_RECOMMEND_IMAGE_FILE_PATH = Environment.getExternalStorageDirectory() + "/imyuu/scenic/";
    //单个景区保存路径
    public static final String SCENIC_SINGLE_FILE_PATH = Environment.getExternalStorageDirectory() + "/imyuu/scenic";
    public static final String SCENIC_ADVERT_FILE_PATH = Environment.getExternalStorageDirectory() + "/imyuu/scenicAdvert";
    public static final String SCENIC_RECOMMEND_FILE_PATH = Environment.getExternalStorageDirectory() + "/imyuu/scenicRecommend";

    public static final String SCENIC_ROUTER_FILE_PATH = "imyuu/";

    public static final String API_ALL_SCENIC_DOWNLOAD = "http://www.imyuu.com/trip/allScenicScenicAreaAction.action";
    public static final String API_SINGLE_SCENIC_DOWNLOAD = "http://www.imyuu.com/trip/oneScenicScenicAreaAction.action?scenicId=";
    public static final String API_SCENIC_ADVERT_DOWNLOAD = "http://www.imyuu.com/trip/oneScenicAdvertScenicAreaAction.action?scenicId=";
    public static final String API_SCENIC_RECOMMEND_DOWNLOAD = "http://www.imyuu.com/trip/allscenicRecommendScenicAreaAction.action";
    public static final String API_USER_SIGN = "http://www.imyuu.com/trip/insertFromAPPRegRegisterAccountAction.action";
    public static final String API_USER_UPDATE = "http://www.imyuu.com/trip/updateFromAPPRegRegisterAccountAction.action";
    public static final String API_USER_LOGIN = "http://www.imyuu.com/trip/loginInfoFromAPPRegRegisterAccountAction.action";

    public static final String SCENIC = "scenic";
    public static final String SCENIC_RECOMMEND = "scenicRecommend";
    public static final String SCENIC_ADVERT = "scenicAdvert";
    public static final String ALL_SCENIC_ZIP = ".zip";
    public static final String ALL_SCENIC_JSON = ".json";
    public static final String LIST_KEY_SCIENINFO_LINE_NAME = "line_name";

    /* 请求码*/
    public static final int IMAGE_REQUEST_CODE = 0;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int DEFAULT_REQUEST_CODE = 2;

    /*头像名称*/
    public static final String IMAGE_FILE_NAME = "faceImage.jpg";
    //头像保存路径
    public static final String IMAGE_FILE_PATH = "/imyuu/userlogo/";
    //头像临时保存路径
    public static final String IMAGE_FILE_PATH_TEMP = "/imyuu/userlogo/temp/";
}
