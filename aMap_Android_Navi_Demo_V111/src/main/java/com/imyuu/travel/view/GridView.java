package com.imyuu.travel.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 九宫格
 * 1.使用的单元Item必须实现CellItem接口
 * 2.必须调用init()
 * 3.如果使用默认的cell实体类，那么createGridView传入的资源布局中必须包含cell_title和cell_icon这两个资源名称
 * 
 */
public class GridView extends android.widget.GridView {

	
	private Context context;
	
	/**
	 * 构造器
	 * @param context上下文
	 * @param attrs自定义资源
	 */
	public GridView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.context = context;
	}
	/**
	 * 构造器
	 * @param context上下文
	 */
	public GridView(Context context){
		this(context, null);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(     
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
}
