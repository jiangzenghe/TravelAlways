package com.imyuu.travel.bean;

import com.imyuu.travel.R;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class IuuTitleModel implements View.OnClickListener {
	protected Activity context;
	protected TextView titleText;
	protected View leftMenu;
	protected int leftMenuId;
	protected int titleTextId;
	protected ProgressBar progressBar;
	public IuuTitleModel(Activity context){
		this(context, R.id.image_search_back, R.id.text_app_title);
	}
	
	public IuuTitleModel(Activity context, int leftId, int titleId){
		this.context = context;
		this.leftMenuId = leftId;
		this.titleTextId = titleId;
		initIuuTitle();
	}
	
	public void backAble(boolean is){
		if (is) {
			leftMenu.setOnClickListener(this);
		} else {
			leftMenu.setOnClickListener(null);
		}
	}
	
	protected void initIuuTitle(){
		leftMenu = context.findViewById(leftMenuId);
		titleText = (TextView) context.findViewById(titleTextId);
		this.progressBar = (ProgressBar) context.findViewById(R.id.progressBar1);
		
		progress(false);
		backAble(true);
	}
	
	public Activity getContext() {
		return context;
	}
	public void setContext(Activity context) {
		this.context = context;
	}
	public TextView getTitleText() {
		return titleText;
	}
	public void setTitleText(TextView titleText) {
		this.titleText = titleText;
	}
	public View getLeftMenu() {
		return leftMenu;
	}
	public void setLeftMenu(View leftMenu) {
		this.leftMenu = leftMenu;
	}

	public void setTitle(String title){
		titleText.setText(title == null? context.getResources().getString(R.string.app_name): title);
	}
	
	@Override
	public void onClick(View v) {
		context.finish();
	}
	
	public void progress(boolean is){
		if (progressBar == null) {
			return;
		}
		if (is) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
	}
}
