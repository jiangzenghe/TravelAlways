package com.imyuu.travel.common;

public interface ICons {
	/**
	 * TOAST提醒距顶部距离，可参照screen定制
	 */
	public static int TOAST_OFFSET_Y = 420;
	
	public static final int CONS_APP_IMG_OFFSET = 40;
	
	public static String CONS_APP_KEY_SP = "com.imyuu";
	
	public static String CONS_APP_REM_PW = "com.imyuu.REMEMBER_PW";
	public static String CONS_APP_USER_ID = "com.imyuu.USER_ID";
	public static String CONS_APP_USER_NAME = "com.imyuu.USER_NAME";
	public static String CONS_APP_PASSWORD = "com.imyuu.PASSWORD";
	public static String CONS_APP_SCENICS_ID = "com.imyuu.SCENICS_ID";
	public static String CONS_APP_SIGN_PHONE = "com.imyuu.SIGN_PHONE";
	public static String CONS_APP_SIGN_PASSWORD = "com.imyuu.SIGN_PASSWORD";

	public static String CONS_APP_SCENICS_TYPE = "com.imyuu.SCENICS_TYPE";
	
	public static final int CONS_APP_SCENICS = 0;
	public static final int CONS_APP_SCENICS_RECOMMEND = 1;
	
	public static final int CONS_APP_IMG_ROUND_CORNER = 10;
	
	public static final int CONS_APP_SCENIC_IMG_ROUND_CORNER = 20;
	
	public static final int CONS_APP_MEDIA_READY = 0;
	public static final int CONS_APP_MEDIA_PLAYING = 1;
	public static final int CONS_APP_MEDIA_RESUME = 2;
	public static final int CONS_APP_MEDIA_RESTART = 3;
	
	public static final int CONS_APP_IUU_LAYER_ID = 10000;
	
	public static final int CONS_APP_IUU_MOVE_SPEED = 100;
	
	public static final int CONS_APP_IUU_MAP_MIN_LEVEL = 10;
	
	public static final int CONS_APP_IUU_MAP_MAX_LEVEL = 14;
	
	public static interface IuuDownloadCons {
		public static final int IUU_DOWN_STATE_NET_ERROR = -100;
		
		public static final int IUU_DOWN_STATE_START = 1;
		public static final int IUU_DOWN_STATE_DOWNLOADING = 2;
		public static final int IUU_DOWN_STATE_FINISH = 3;
	}
	
}
