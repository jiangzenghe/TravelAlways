package com.imyuu.travel.ui;

import org.nutz.json.Json;
import org.nutz.lang.Strings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.imyuu.travel.R;
import com.imyuu.travel.util.ConstantsOld;
import com.imyuu.travel.util.FileUtil;
import com.imyuu.travel.common.ICons;
import com.inttus.app.InttusActivity;

/**
 * 工程级<br>
 * 做一些工程级的封装
 * 
 * @author Llong
 * 
 */
public class TravelActvity extends InttusActivity implements ICons {
	protected String folderPath = Environment.getExternalStorageDirectory()
			+ "/" + ConstantsOld.SCENIC_ROUTER_FILE_PATH;
	/**
	 * 页面打开次数统计
	 */
	protected int pageCount;

	@Override
	public void showLong(String msg) {
		View toastRoot = infView(R.layout.wdg_toast);
		TextView message = (TextView) toastRoot.findViewById(R.id.message);
		message.setText(msg);
		Toast toastStart = new Toast(this);
		toastStart.setGravity(Gravity.TOP, 0, TOAST_OFFSET_Y);
		toastStart.setDuration(Toast.LENGTH_LONG);
		toastStart.setView(toastRoot);
		toastStart.show();
	}

	public void showLong(int str) {
		showLong(getResources().getString(str));
	}

	public void showShort(int str) {
		showShort(getResources().getString(str));
	}

	@Override
	public void showShort(String msg) {
		View toastRoot = infView(R.layout.wdg_toast);
		TextView message = (TextView) toastRoot.findViewById(R.id.message);
		message.setText(msg);
		Toast toastStart = new Toast(this);
		toastStart.setGravity(Gravity.TOP, 0, TOAST_OFFSET_Y);
		toastStart.setDuration(Toast.LENGTH_SHORT);
		toastStart.setView(toastRoot);
		toastStart.show();
	}

	protected void setPro(String key, Object object) {
		if (!Strings.isBlank(key)) {
			SharedPreferences sharedPreferences = getSharedPreferences(
					CONS_APP_KEY_SP, Context.MODE_PRIVATE);
			object = object == null ? "" : object;
			sharedPreferences.edit().putString(key, String.valueOf(object))
					.commit();
		}
	}

	protected String getPro(String key) {
		if (!Strings.isBlank(key)) {
			SharedPreferences sharedPreferences = getSharedPreferences(
					CONS_APP_KEY_SP, Context.MODE_PRIVATE);
			return sharedPreferences.getString(key, null);
		}
		return null;
	}

	protected <T> T getPro(String key, Class<T> clazz) {
		if (!Strings.isBlank(key)) {

			SharedPreferences sharedPreferences = getSharedPreferences(
					CONS_APP_KEY_SP, Context.MODE_PRIVATE);
			String v = sharedPreferences.getString(key, "");
			if (clazz != null && !Strings.isBlank(v)) {
				return Json.fromJson(clazz, v);
			}
		}
		return null;
	}

	protected String getPathFor(String scenicId, int type) {
		String path = Environment.getExternalStorageDirectory() + "/"
				+ ConstantsOld.SCENIC_ROUTER_FILE_PATH;
		switch (type) {
		/*
		 * 景区压缩包
		 */
		case 0:
			path = path + ConstantsOld.SCENIC + scenicId
					+ ConstantsOld.ALL_SCENIC_ZIP;
			break;
		/*
		 * 推荐景区压缩包
		 */
		case 1:
			path = path + ConstantsOld.SCENIC_RECOMMEND + scenicId
					+ ConstantsOld.ALL_SCENIC_ZIP;
			break;
		/*
		 * 景区广告资料压缩包
		 */
		case 2:
			path = path + ConstantsOld.SCENIC_ADVERT + scenicId
					+ ConstantsOld.ALL_SCENIC_ZIP;
			break;
		/*
		 * 景区资料目录
		 */
		case 3:
			path = path + ConstantsOld.SCENIC + scenicId;
			break;
		default:
			break;
		}
		return path;
	}

	protected void choiceMenu(final String[] items,
			final OnClickListener listener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.dialog)
				.setItems(items, listener)
				.setNegativeButton(R.string.global_cancel, null).create();
		Window window = alertDialog.getWindow();
		window.setGravity(Gravity.BOTTOM);
		alertDialog.show();
	}

	@Override
	protected void goNext(Class<? extends Activity> activity) {
		// TODO Auto-generated method stub
		super.goNext(activity);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_of_left);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	protected void setUserLogo(String filePath, String userId) {
		FileUtil.copyFile(Environment.getExternalStorageDirectory()
				+ ConstantsOld.IMAGE_FILE_PATH_TEMP + filePath,
				Environment.getExternalStorageDirectory()
						+ ConstantsOld.IMAGE_FILE_PATH, userId + "_"
						+ ConstantsOld.IMAGE_FILE_NAME);
	}
}
