package com.imyuu.travel.view;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
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
	private ArrayList<SpotInfo> spotList=new ArrayList<SpotInfo>();
	private LayoutInflater inflater;

	private LinearLayout mPopView_layout;
	private LinearLayout mRoute_layout;
	private TextView routeText;
	private RelativeLayout rl_column;
	private View rootview;  //R.layout.main_top_right_dialog
	private AMap mMap;

	private Polyline lineDraw;
	private Marker mCurrentVirtualPoint;

	/**
	 * 构造器

	 */
	public RoutePopView(MapOnlineActivity context, String scenicId, View view) {

		super(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
		this.scenicId = scenicId;
		this.context = context;
		this.rootview = view;
		this.mMap = context.getmMap();
		onCreate();
	}

	public void onCreate() {
		// 需要设置一下此参数，点击外边可消失
		this.setBackgroundDrawable(new BitmapDrawable());
		//设置点击窗口外边窗口消失
		this.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		this.setFocusable(true);

		inflater = LayoutInflater.from(context);
		mPopView_layout = (LinearLayout)rootview.findViewById(R.id.main_dialog_layout);
		mRoute_layout = (LinearLayout) context.findViewById(R.id.layout_route);
		rl_column = (RelativeLayout) context.findViewById(R.id.rl_column);
		routeText = (TextView) context.findViewById(R.id.route);
		initRouteList(scenicId);

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
					routeList.add(each);
				}
				if (routeList.size() > 0) {
					initItemForRoute(routeList.size());
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
			final LinearLayout linearLayoutMapPopItem = (LinearLayout)
					inflater.inflate(R.layout.map_pop_item, mPopView_layout, false);
			if(i==count -1) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(0,0,0,15);
				linearLayoutMapPopItem.setLayoutParams(lp);
			}
			linearLayoutMapPopItem.setTag(i);
			final TextView textMapLineTitle = (TextView) linearLayoutMapPopItem
					.findViewById(R.id.text_map_pop_title);
			textMapLineTitle.setText(routeList.get(i).getLineName());
			setOnClickListener(linearLayoutMapPopItem);
			mPopView_layout.addView(linearLayoutMapPopItem);
		}
	}

	public void setOnClickListener(final LinearLayout in) {

		in.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (RoutePopView.this.isShowing()) RoutePopView.this.dismiss();

				removeRoute();
				initColumn((int)in.getTag());
				context.indicateAnimation(rl_column, routeText, 0);

				ArrayList<LatLng> arg1 = new ArrayList<LatLng>();
				for (SpotInfo each : spotList) {
					arg1.add(new LatLng(each.getLat(), each.getLng()));
				}
				if (arg1.size() != 0) {
					lineDraw = mMap.addPolyline(new PolylineOptions().zIndex(10)
							.addAll(arg1).color(Color.RED).visible(true));
					if(mCurrentVirtualPoint == null) {
						mCurrentVirtualPoint = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
								.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_point)));
					}
					mCurrentVirtualPoint.setPosition(arg1.get(0));
					mCurrentVirtualPoint.setObject(0);
//					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//						arg1.get(0), 19));  //37.5206,121.358
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							arg1.get(0), 19), 2000, null);  //37.5206,121.358
				} else {
					rl_column.setVisibility(View.GONE);
				}
			}

		});

	}

	/**
	 *  初始化Column栏目项
	 * */
	private void initColumn(int type) { //"1"经典  "2"畅游...
		mRoute_layout.removeAllViews();
		spotList.clear();
		RecommendLine line;
		if(routeList == null || routeList.size() == 0) return;

		line = routeList.get(type);

		int count = 0;
		if(line!=null && line.getLineSectionList()!=null) {
			for(SpotInfo each : line.getLineSectionList()) {
				spotList.add(each);
			}
			count =  line.getLineSectionList().size();
		}

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
			linearLayoutMapLineItem
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							for (int i = 0; i < mRoute_layout.getChildCount(); i++) {
								View view = mRoute_layout.getChildAt(i);
								ImageView eachMapLinePoint = (ImageView) view.findViewById(R.id.image_map_line_point);
								eachMapLinePoint.setImageResource(R.drawable.img_map_point);
								if (view.getTag().toString()
										.equals(v.getTag().toString())) {
									ImageView imageMapLinePoint = (ImageView) v.findViewById(R.id.image_map_line_point);
									imageMapLinePoint
											.setImageResource(R.drawable.img_map_point_choice);
									LatLng center = new LatLng(spotList.get(i).getLat(), spotList.get(i).getLng());
//                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//											center, 19));  //37.5206,121.358

									ArrayList<SpotModel> temp = new ArrayList<SpotModel>();
									int flag = (int)mCurrentVirtualPoint.getObject();
									if(flag<i) {
										for(int key = flag;key<=i;key++) {
											LatLng latlng = new LatLng(spotList.get(key).getLat(), spotList.get(key).getLng());
											SpotModel model = new SpotModel(key, latlng);
											temp.add(model);
										}
									} else {
										for(int key = flag;key>=i;key--) {
											LatLng latlng = new LatLng(spotList.get(key).getLat(), spotList.get(key).getLng());
											SpotModel model = new SpotModel(key, latlng);
											temp.add(model);
										}
									}

									if(temp.size() > 0) {
										movePoint(mCurrentVirtualPoint, temp);
									}

								}
							}
						}
					});
			mRoute_layout.addView(linearLayoutMapLineItem);

		}
	}

	public void movePoint(final Marker marker, final ArrayList<SpotModel> temp) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final LatLng startLatLng = marker.getPosition();
		final long duration = 500;

		final ArrayList<SpotModel> addtional = new ArrayList<SpotModel>();
		if(temp.size() > 0) {
			SpotModel last = new SpotModel(temp.get(0).getRouteIndex(),temp.get(0).getLatLng());
			addtional.add(last);
			for(int i = 0;i<temp.size();i++) {

				if(i>0) {
					Point nowPoint = mMap.getProjection().toScreenLocation(temp.get(i).getLatLng());
					Point lastPoint = mMap.getProjection().toScreenLocation(last.getLatLng());
					int count = Math.abs(nowPoint.x-lastPoint.x) + Math.abs(nowPoint.y-lastPoint.y);
					count=(count-count%50)/50 + 1;
					double add_lat = (temp.get(i).getLatLng().latitude - last.getLatLng().latitude)/count;
					double add_lon = (temp.get(i).getLatLng().longitude - last.getLatLng().longitude)/count;
					for(int j=1;j<count;j++) {
						LatLng tempLatlng = new LatLng(last.getLatLng().latitude + j*add_lat, last.getLatLng().longitude + j*add_lon);
						SpotModel each = new SpotModel(temp.get(i).getRouteIndex(), tempLatlng);
						addtional.add(each);
					}
					addtional.add(temp.get(i));
				}
				last = new SpotModel(temp.get(i).getRouteIndex(),temp.get(i).getLatLng());
			}
		}

		handler.post(new Runnable() {
			int each = 0;

			@Override
			public void run() {

				Log.e("each", each + "");
				marker.setPosition(addtional.get(each).getLatLng());
				marker.setObject(addtional.get(each).getRouteIndex());
				mMap.invalidate();// 刷新地图
//				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//						addtional.get(each).getLatLng(), 19));  //37.5206,121.358
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						addtional.get(each).getLatLng(), 19), 50, null);  //37.5206,121.358

				if(each != addtional.size()-1) {
					each+=1;
					handler.postDelayed(this, duration);
				} else {
					handler.removeCallbacks(this);
				}
			}
		});

	}

	public void removeRoute() {
		if(lineDraw != null) {
			lineDraw.remove();
		}
//		if(mCurrentVirtualPoint!=null) {
//			mCurrentVirtualPoint.remove();
//		}
	}
}
