package com.imyuu.travel.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.bean.SpotModel;
import com.imyuu.travel.model.RecommendLine;
import com.imyuu.travel.model.SpotInfo;
import com.imyuu.travel.ui.MapOnlineActivity;
import com.imyuu.travel.util.CommonUtils;

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
	private MapOnlineActivity context;
	private ArrayList<RecommendLine> routeList=new ArrayList<RecommendLine>();
	private LayoutInflater inflater;

	private LinearLayout mPopView_layout;
	private View rootview;

	/**
	 * 构造器

	 */
	public RoutePopView(MapOnlineActivity context, String scenicId, View view) {

		super(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
		this.scenicId = scenicId;
		this.context = context;
		this.rootview = view;
		onCreate();
	}

	public void onCreate() {
		// 需要设置一下此参数，点击外边可消失
		this.setBackgroundDrawable(new BitmapDrawable());
		//设置点击窗口外边窗口消失
		this.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		this.setFocusable(true);

//		mRoute_layout = (LinearLayout) activity.findViewById(R.id.layout_route);
		initRouteList(scenicId);

		mPopView_layout = (LinearLayout)rootview.findViewById(R.id.main_dialog_layout);

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

	public void aaa() {
		final LinearLayout classic_route = (LinearLayout) view.findViewById(R.id.classic_route);
		final LinearLayout classic_good = (LinearLayout) view.findViewById(R.id.good_route);

		classic_route.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popView.isShowing()) popView.dismiss();

				removeRoute();
				initColumn("1");
				indicateAnimation(rl_column, routeText, 0);

				ArrayList<LatLng> arg1 = new ArrayList<LatLng>();
				for (SpotInfo each : spotList) {
					arg1.add(new LatLng(each.getLat(), each.getLng()));
//	    						mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(each.getLatitude(),each.getLongitude())));
				}
				if (arg1.size() != 0) {
					lineDraw = mMap.addPolyline(new PolylineOptions().zIndex(10)
							.addAll(arg1).color(Color.RED).visible(true));
//								if(mCurrentVirtualPoint == null) {
					mCurrentVirtualPoint = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_point)));
//								}
					mCurrentVirtualPoint.setPosition(arg1.get(0));
					mCurrentVirtualPoint.setObject(0);
//								mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//										arg1.get(0), 19));  //37.5206,121.358
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							arg1.get(0), 19), 2000, null);  //37.5206,121.358
				} else {
					rl_column.setVisibility(View.GONE);
				}
			}

		});

		classic_good.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popView.isShowing()) popView.dismiss();
				removeRoute();
				initColumn("2");
				//设置rl_column的显示，设置routeText的图片。
				indicateAnimation(rl_column, routeText, 0);

				ArrayList<LatLng> arg1 = new ArrayList<LatLng>();
				for (SpotInfo each : spotList) {
					arg1.add(new LatLng(each.getLat(), each.getLng()));
				}
				if (arg1.size() != 0) {
					lineDraw = mMap.addPolyline(new PolylineOptions().zIndex(10)
							.addAll(arg1).color(Color.RED).visible(true));
					if (mCurrentVirtualPoint == null) {
						mCurrentVirtualPoint = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_point)));
					}
					mCurrentVirtualPoint.setPosition(arg1.get(0));
					mCurrentVirtualPoint.setObject(0);
//								mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//										arg1.get(0), 19));  //37.5206,121.358
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							arg1.get(0), 19), 2000, null);  //37.5206,121.358
				} else {
					rl_column.setVisibility(View.GONE);
				}
			}

		});
	}
}
