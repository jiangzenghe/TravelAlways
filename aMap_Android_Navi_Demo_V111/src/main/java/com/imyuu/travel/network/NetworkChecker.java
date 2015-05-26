package com.imyuu.travel.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Build;
import android.provider.Settings;

public class NetworkChecker {

	/**
	 * ����������״̬�����ж�
	 * 
	 * @return true, ���ã� false�� ������
	 */
	public boolean isOpenNetwork(Context context) {

		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
	}

	public int CheckNetworkState(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// ���3G��wifi��2G������״̬�����ӵģ����˳���������ʾ��ʾ��Ϣ�����������ý���
		if (mobile == State.CONNECTED || mobile == State.CONNECTING)
			return 3;
		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			return 1;
		// û����������
		return 0;
	}

	public void showTips(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("网络状态");
		builder.setMessage("设置网络");
		
		builder.setPositiveButton("确定",
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
						// ���û���������ӣ�������������ý���
				context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
				}
			});
		builder.setNegativeButton("取消",
			new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
				});
		builder.create();
		builder.show();
	}

	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connect = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connect == null) {
			return false;
		} else// get all network info
		{
			NetworkInfo[] info = connect.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
