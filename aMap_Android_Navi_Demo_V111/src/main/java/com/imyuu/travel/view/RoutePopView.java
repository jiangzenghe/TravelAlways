package com.imyuu.travel.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;
import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.bean.SpotModel;
import com.imyuu.travel.model.RecommendLine;
import com.imyuu.travel.ui.MapOnlineActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * 九宫格
 * 1.使用的单元Item必须实现CellItem接口
 * 2.必须调用init()
 * 3.如果使用默认的cell实体类，那么createGridView传入的资源布局中必须包含cell_title和cell_icon这两个资源名称
 * 
 */
public class RoutePopView extends PopupWindow {

	private String scenicId;
	private Context context;
	private ArrayList<RecommendLine> routeList=new ArrayList<RecommendLine>();
	private LayoutInflater inflater;

	private LinearLayout mPopView_layout;
	private View rootview;

	/**
	 * 构造器

	 */
	public RoutePopView(Context context, String scenicId, View view) {

		super(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
		this.scenicId = scenicId;
		this.context = context;
		this.rootview = view;
		onCreate();
	}

	public void onCreate() {
		MapOnlineActivity activity = (MapOnlineActivity)context;
//		mRoute_layout = (LinearLayout) activity.findViewById(R.id.layout_route);
		initRouteList(scenicId);
		inflater = LayoutInflater.from(context);
		// 引入窗口配置文件
		final View view = inflater.inflate(R.layout.main_top_right_dialog, null);
		mPopView_layout = (LinearLayout)view.findViewById(R.id.main_dialog_layout);
		// 创建PopupWindow对象
//		final PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
	}

	private void initRouteList(String scenicId) {
		routeList.clear();
		ApiClient.getIuuApiClient().queryRecommendLines(scenicId, new Callback<List<RecommendLine>>() {
			@Override
			public void success(List<RecommendLine> resultJson, Response response) {
				Toast.makeText(context, "加载成功", Toast.LENGTH_SHORT).show();
				if (resultJson == null) {
					Toast.makeText(context, "结果为空", Toast.LENGTH_SHORT).show();
				}
				for (RecommendLine each : resultJson) {
					if (each.getLineName().equals("经典路线")) {
						routeList.add(0, each);
					} else {
						routeList.add(1, each);
					}
				}
			}

			@Override
			public void failure(RetrofitError error) {
				Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void initItemForRoute(int count) {
		for(int i = 0; i< count; i++){
			// 动态添加景点布局
			LinearLayout linearLayoutMapLineItem = (LinearLayout)
					inflater.inflate(R.layout.map_line_item, mRoute_layout, false);
			linearLayoutMapLineItem.setTag(i);
			TextView textMapLineTitle = (TextView) linearLayoutMapLineItem
					.findViewById(R.id.text_map_line_title);
			textMapLineTitle.setText(spotList.get(i).getSpotName());
			ImageView imageMapLinePoint = (ImageView) linearLayoutMapLineItem
					.findViewById(R.id.image_map_line_point);
			// index等于0代表为线路上的第一个点
			if (i == 0) {
				imageMapLinePoint
						.setImageResource(R.drawable.img_map_point_choice);
				imageMapLinePoint
						.setBackgroundResource(R.drawable.img_map_line_r_half);
			} else if(i==count-1) {
				imageMapLinePoint.setBackgroundResource(R.drawable.img_map_line_l_half);
			} else {
				imageMapLinePoint
						.setImageResource(R.drawable.img_map_point);
			}
			mRoute_layout.addView(linearLayoutMapLineItem);

		}
	}
}
