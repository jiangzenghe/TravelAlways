package com.imyuu.travel.bean;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.imyuu.travel.R;
import com.imyuu.travel.common.ICons;
import com.imyuu.travel.util.MediaHelper;
import com.ls.widgets.map.model.MapObject;

public class MapVoiceObject extends MapObject implements ICons {
	/**
	 * 0 初始化状态
	 * 1 播放视频状态
	 * 2 暂停播放状态
	 */
	private int state;
	private String mediaUrl;
	private Activity context;
	public MapVoiceObject(Object id, Drawable drawable, int x, int y,
			boolean isTouchable, boolean isScalable) {
		super(id, drawable, x, y, isTouchable, isScalable);
		// TODO Auto-generated constructor stub
	}

	public MapVoiceObject(Object id, Drawable drawable, int x, int y,
			boolean isTouchable) {
		super(id, drawable, x, y, isTouchable);
		// TODO Auto-generated constructor stub
	}

	public MapVoiceObject(Object id, Drawable drawable, int x, int y,
			int pivotX, int pivotY, boolean isTouchable, boolean isScalable) {
		super(id, drawable, x, y, pivotX, pivotY, isTouchable, isScalable);
		// TODO Auto-generated constructor stub
	}

	public MapVoiceObject(Object id, Drawable drawable, int x, int y,
			int pivotX, int pivotY) {
		super(id, drawable, x, y, pivotX, pivotY);
		// TODO Auto-generated constructor stub
	}

	public MapVoiceObject(Object id, Drawable drawable, int x, int y) {
		super(id, drawable, x, y);
		// TODO Auto-generated constructor stub
	}

	public MapVoiceObject(Object id, Drawable drawable, Point position,
			boolean isTouchable, boolean isScalable) {
		super(id, drawable, position, isTouchable, isScalable);
		// TODO Auto-generated constructor stub
	}

	public MapVoiceObject(Object id, Drawable drawable, Point position,
			boolean isTouchable) {
		super(id, drawable, position, isTouchable);
		// TODO Auto-generated constructor stub
	}

	public MapVoiceObject(Object id, Drawable drawable, Point position,
			Point pivotPoint, boolean isTouchable, boolean isScalable) {
		super(id, drawable, position, pivotPoint, isTouchable, isScalable);
		// TODO Auto-generated constructor stub
	}

	public MapVoiceObject(Object id, Drawable drawable, Point position,
			Point pivotPoint, boolean isTouchable) {
		super(id, drawable, position, pivotPoint, isTouchable);
		// TODO Auto-generated constructor stub
	}

	public MapVoiceObject(Object id, Drawable drawable, Point position,
			Point pivotPoint) {
		super(id, drawable, position, pivotPoint);
		// TODO Auto-generated constructor stub
	}

	public MapVoiceObject(Object id, Drawable drawable, Point position) {
		super(id, drawable, position);
		// TODO Auto-generated constructor stub
	}

	public MapVoiceObject(Object id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		switch (state) {
		case CONS_APP_MEDIA_READY:
			setDrawable(context.getResources().getDrawable(R.drawable.img_map_voice));
			MediaHelper.release();
			break;
		case CONS_APP_MEDIA_PLAYING:
			setDrawable(context.getResources().getDrawable(R.drawable.img_map_voice_playing));
			MediaHelper.play(mediaUrl);
			break;
		case CONS_APP_MEDIA_RESUME:
			setDrawable(context.getResources().getDrawable(R.drawable.img_map_voice));
			MediaHelper.pause();
			break;
		case CONS_APP_MEDIA_RESTART:
			setDrawable(context.getResources().getDrawable(R.drawable.img_map_voice_playing));
			MediaHelper.resume();
			break;
		default:
			break;
		}
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public Activity getContext() {
		return context;
	}

	public void setContext(Activity context) {
		this.context = context;
	}

}
