package com.imyuu.travel.util;

import android.media.MediaPlayer;
import android.util.Log;

public class MediaHelper {
	private static MediaPlayer single;
	public static String TAG = "IuuMedia";
	public static MediaPlayer s(){
		if (single == null) {
			single = new MediaPlayer();
		}
		return single;
	}
	
	public static synchronized MediaPlayer play(String url){
		
		try {
			if (s().isPlaying()) {
				s().stop();
			}
		} catch (Exception e) {
			Log.e(TAG, "", e);
		}
		try {
			single.setDataSource(url);
		} catch (Exception e) {
			Log.e(TAG, "", e);
		}
		
		try {
			single.prepare();
		} catch (Exception e) {
			Log.e(TAG, "", e);
		}
		single.start();
		return single;
	}
	
	public static synchronized MediaPlayer resume(){
		try {
			s().start();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return single;
	}
	
	public static synchronized MediaPlayer pause(){
		try {
			if (s().isPlaying()) {
				s().pause();
			}
		} catch (Exception e) {
			Log.e(TAG, "", e);
		}
		return single;
	}
	
	public static synchronized void release(){
		if (single == null) {
			return;
		}
		try {
			if (single.isPlaying()) {
				single.stop();
			}
		} catch (Exception e) {
			Log.e(TAG, "", e);
		} finally {
			try {
				single.release();
			} catch (Exception e2) {
				Log.e(TAG, "", e2);
			}
			single = null;
		}
	}
}
