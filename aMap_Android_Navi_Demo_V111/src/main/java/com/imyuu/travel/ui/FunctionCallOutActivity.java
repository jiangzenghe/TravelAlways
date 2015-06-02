/******************************************************************************
 * Copyright (C) 2014-2020 Yantai HaiYi Software Co.,Ltd
 * All Rights Reserved.
 * 本软件为烟台海颐软件开发研制。未经本公司正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/
package com.imyuu.travel.ui;

import java.math.BigDecimal;
import java.sql.Timestamp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.imyuu.travel.R;
import com.imyuu.travel.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 *
 * 界面xml使用android:Theme.Holo.Dialog主题(否则弹出查询对话框spinner出现空白)
 * 
 * @author jiang
 *
 */
@SuppressLint("SimpleDateFormat")
public class FunctionCallOutActivity extends Activity implements OnClickListener {

	@InjectView(R.id.cancel_dialogCancel)
	ImageView dialogCancel;
	@InjectView(R.id.popActivity)
	LinearLayout popLayout;
	@InjectView(R.id.switch_gps)
	Switch gpsSwitch;
	@InjectView(R.id.switch_sound)
	Switch soundSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function_callout_dialog);
		ButterKnife.inject(this);

		initViewValue();
		dialogCancel.setOnClickListener(this);
		gpsSwitch.setOnClickListener(this);
		soundSwitch.setOnClickListener(this);

	}
	
	private void initViewValue(){
		Intent intent = getIntent();

		boolean gps = intent.getExtras().getBoolean("isGPSAuto");
		boolean sound = intent.getExtras().getBoolean("isSpeakingAuto");

		gpsSwitch.setChecked(gps);
		soundSwitch.setChecked(sound);

	}

	@Override
	public void onClick(View v) {
		// 查询
		if(v.getId() == R.id.cancel_dialogCancel){
			setResult(Activity.RESULT_CANCELED, null);
			finish();
			return;
		}

		if(v.getId() == R.id.switch_gps) {
			Intent intent = new Intent();

			intent.putExtra("autoGps", gpsSwitch.isChecked());
			intent.putExtra("autoSound", soundSwitch.isChecked());
			setResult(Activity.RESULT_OK, intent);
			finish();
		}

		if(v.getId() == R.id.switch_sound) {
			Intent intent = new Intent();

			if(!gpsSwitch.isChecked()) {
				gpsSwitch.setChecked(true);
				ToastUtil.show(FunctionCallOutActivity.this, "开启自动讲解需要先开启定位");
			}
			intent.putExtra("autoGps", gpsSwitch.isChecked());
			intent.putExtra("autoSound", soundSwitch.isChecked());
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
//		if(v.getId() == R.id.ok_btn) {
//			Intent intent = new Intent();
//
//			if(!gpsSwitch.isChecked() && soundSwitch.isChecked()) {
//				gpsSwitch.setChecked(true);
//				ToastUtil.show(FunctionCallOutActivity.this, "开启自动讲解需要先开启定位");
//			}
//			intent.putExtra("autoGps", gpsSwitch.isChecked());
//			intent.putExtra("autoSound", soundSwitch.isChecked());
//			setResult(Activity.RESULT_OK, intent);
//			finish();
//		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float popwidth = popLayout.getWidth();
		float popheight = popLayout.getHeight();
		float x = event.getX();
		float y = event.getY();
		
		if((0<x && x<popwidth) &&(0<y && y<popheight)){
			return false;
		}
		 
		setResult(Activity.RESULT_CANCELED, null);
		finish();
		return true;
	}
	
}
