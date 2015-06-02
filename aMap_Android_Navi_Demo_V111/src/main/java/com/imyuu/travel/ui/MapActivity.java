package com.imyuu.travel.ui;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.VisibleRegion;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.imyuu.travel.R;
import com.imyuu.travel.adapters.GridAdapter;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.RecommendLine;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.model.ScenicPointJson;
import com.imyuu.travel.model.SpotInfo;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.CommonUtils;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.MarkerUtils;
import com.imyuu.travel.util.MarkerUtilsFor2D;
import com.imyuu.travel.util.Player;
import com.imyuu.travel.util.ToastUtil;
import com.imyuu.travel.view.ColumnHorizontalScrollView;
import com.imyuu.travel.view.GridView;

/**
 * AMapV1地图demo总汇
 */
public final class MapActivity extends Activity implements OnMarkerClickListener, OnInfoWindowClickListener, 
	InfoWindowAdapter, OnCameraChangeListener, OnMapLoadedListener, LocationSource, 
	AMapLocationListener, AMapNaviListener {

	private AMap mMap;
	private MapView mapView;
	private TextView cilckText;
	private TextView routeText;
	private GridView layoutShow;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private GroundOverlay groundoverlay;
	private AMapNavi mAMapNavi;
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	private LinearLayout mRoute_layout;
	private RelativeLayout rl_column;
	private TextView redAlert;
	private LinearLayout layout_redalert;
	
	private float zoom;
	private NaviLatLng mNaviEnd;
	private NaviLatLng mNaviStart;
	private Marker mMarkerRouteStart;
	private Marker mMarkerRouteEnd;

	// 规划线路
//	private RouteOverLay mRouteOverLay;
	private Polyline lineDraw;
    private String scenicId;
    private ScenicAreaJson scenic;

	/** 分类列表*/
	private ArrayList<RecommendLine> routeList=new ArrayList<RecommendLine>();
	private ArrayList<SpotInfo> spotList=new ArrayList<SpotInfo>();
	private ArrayList<ScenicPointJson> markerList=new ArrayList<ScenicPointJson>();
	private MarkerUtils markerUtils;

    @InjectView(R.id.image_cancel_back)
    ImageView vm_cancel;
    @InjectView(R.id.text_label)
    TextView text_label;

    @OnClick(R.id.image_cancel_back)
    public void cacelBackClick() {
        Intent intent = new Intent(MapActivity.this,ClusterActivity.class);
        startActivity(intent);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
        ButterKnife.inject(this);

        scenic   = (ScenicAreaJson)getIntent().getExtras().getSerializable("scenicInfo");
        if(scenic != null) {
            scenicId = scenic.getScenicId();
        }

//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(true);//显示返回箭头
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle("                        "+"崂山风景区");
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		
		init();
		
		initRouteList("394");
		
		initView();
	}

	private void removeRoute() {
		if(lineDraw != null) {
			lineDraw.remove();
		}
		if(mMarkerRouteStart!=null) {			
			mMarkerRouteStart.remove();
		}
		if(mMarkerRouteEnd!=null) {
//			mRouteOverLay.removeFromMap();
			mMarkerRouteEnd.remove();
		}
	}
	
	private void initRouteList(String scenicId) {
		routeList.clear();
		ApiClient.getIuuApiClient().queryRecommendLines(scenicId, new Callback<List<RecommendLine>>() {
	        @Override
	        public void success(List<RecommendLine> resultJson, Response response) {
	        	Toast.makeText(MapActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
	        	if(resultJson == null) {
	        		Toast.makeText(MapActivity.this, "结果为空", Toast.LENGTH_SHORT).show();
	        	}
	        	for(RecommendLine each:resultJson) {
	        		if(each.getLineName().equals("经典路线")) {
	        			routeList.add(0, each);
	        		} else {
	        			routeList.add(1, each);
	        		}
	        	}
	        }

	        @Override
	        public void failure(RetrofitError error) {
	            Toast.makeText(MapActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	
	private void initView() {

		mAMapNavi = AMapNavi.getInstance(this);
		mAMapNavi.setAMapNaviListener(this);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		redAlert = (TextView) findViewById(R.id.text_redalert);
		layout_redalert = (LinearLayout) findViewById(R.id.layout_redalert);
		mRoute_layout = (LinearLayout) findViewById(R.id.layout_route);
		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
		cilckText = (TextView) findViewById(R.id.help);
		layoutShow = (GridView) findViewById(R.id.layout_show);
		routeText = (TextView) findViewById(R.id.route);
		layout_redalert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				redAlert.setVisibility(View.VISIBLE);
			}
			
		});
		
		LayoutInflater inflater = LayoutInflater.from(this); 
        // 引入窗口配置文件 
        final View view = inflater.inflate(R.layout.main_top_right_dialog, null); 
        // 创建PopupWindow对象 
        final PopupWindow pop = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false); 
        // 需要设置一下此参数，点击外边可消失 
        pop.setBackgroundDrawable(new BitmapDrawable()); 
        //设置点击窗口外边窗口消失 
        pop.setOutsideTouchable(true); 
        // 设置此参数获得焦点，否则无法点击 
        pop.setFocusable(true); 
        routeText.setOnClickListener(new OnClickListener() { 
               
            @Override 
            public void onClick(View v) { 
                if(pop.isShowing()) { 
                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏 
                    pop.dismiss(); 
                } else { 
                	if(rl_column.getVisibility() == View.VISIBLE) {
                		Drawable nav_up=getResources().getDrawable(R.drawable.uparrow);
                		nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
						routeText.setCompoundDrawables(nav_up,null,null,null);
                		rl_column.setVisibility(View.GONE);
                		return;
                	}
                    // 显示窗口 
                	int[] location = new int[2];  
                    v.getLocationOnScreen(location);  
                    int mWindowHeight = CommonUtils.getWindowsHeight(MapActivity.this);
                    pop.showAtLocation(v, Gravity.LEFT | Gravity.BOTTOM, 0, mWindowHeight-location[1]+pop.getHeight()); 
//                    final LinearLayout classic_route = (LinearLayout)view.findViewById(R.id.classic_route);
//                    final LinearLayout classic_good = (LinearLayout)view.findViewById(R.id.good_route);
					final LinearLayout classic_route = null;
					final LinearLayout classic_good = null;
                    
                    classic_route.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if(pop.isShowing()) pop.dismiss();
							
							removeRoute();
							initColumn("1");
							Drawable nav_down=getResources().getDrawable(R.drawable.downarrow);
							nav_down.setBounds(0, 0, nav_down.getMinimumWidth(), nav_down.getMinimumHeight());
							routeText.setCompoundDrawables(nav_down,null,null,null);
							rl_column.setVisibility(View.GONE);
	                    	Animation mShowAction = AnimationUtils.loadAnimation(MapActivity.this, R.anim.right_in);
	    					rl_column.setAnimation(mShowAction);
	    					rl_column.setVisibility(View.VISIBLE);
	    					
	    					ArrayList<LatLng> arg1 = new ArrayList<LatLng>();
	    					for(SpotInfo each:spotList) {
	    						arg1.add(new LatLng(each.getLat(),each.getLng()));
//	    						mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(each.getLatitude(),each.getLongitude())));
	    					}
	    					if(arg1.size() != 0) {	    						
	    						lineDraw = mMap.addPolyline(new PolylineOptions().zIndex(10)
	    								.addAll(arg1).color(Color.RED).visible(true));
	    						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
	    								arg1.get(0), 18));  //37.5206,121.358
	    					} else {
	    						rl_column.setVisibility(View.GONE);
	    					}
						}
                    	
                    });
                    classic_good.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if(pop.isShowing()) pop.dismiss();
							removeRoute();
							initColumn("2");
							Drawable nav_down=getResources().getDrawable(R.drawable.downarrow);
							nav_down.setBounds(0, 0, nav_down.getMinimumWidth(), nav_down.getMinimumHeight());
							routeText.setCompoundDrawables(nav_down,null,null,null);
							rl_column.setVisibility(View.GONE);
	                    	Animation mShowAction = AnimationUtils.loadAnimation(MapActivity.this, R.anim.right_in);
	    					rl_column.setAnimation(mShowAction);
	    					rl_column.setVisibility(View.VISIBLE);
	    					
	    					ArrayList<LatLng> arg1 = new ArrayList<LatLng>();
	    					for(SpotInfo each:spotList) {
	    						arg1.add(new LatLng(each.getLat(),each.getLng()));
//	    						mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(each.getLatitude(),each.getLongitude())));
	    					}
	    					if(arg1.size() != 0) {	    						
	    						lineDraw = mMap.addPolyline(new PolylineOptions().zIndex(10)
	    								.addAll(arg1).color(Color.RED).visible(true));
	    						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
	    								arg1.get(0), 18));  //37.5206,121.358
	    					} else {
	    						rl_column.setVisibility(View.GONE);
	    					}
						}
                    	
                    });
                } 
                   
            } 
        }); 
		cilckText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(layoutShow.getVisibility() == View.VISIBLE) {
					TranslateAnimation mHideAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,     
							Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,     
							0.0f, Animation.RELATIVE_TO_SELF, 1.0f);  
					mHideAction.setDuration(50); 
					layoutShow.setAnimation(mHideAction);
					layoutShow.setVisibility(View.GONE);
				} else {
					TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,     
							Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,     
							1.0f, Animation.RELATIVE_TO_SELF, 0.0f);  
					mShowAction.setDuration(50); 
					layoutShow.setAnimation(mShowAction);
					layoutShow.setVisibility(View.VISIBLE);
				}
			}
		});
		
		initGridView();
	}
	
	/** 
	 *  初始化Column栏目项
	 * */
	private void initColumn(String type) { //"1"经典  "2"畅游
		mRoute_layout.removeAllViews();
		spotList.clear();
		RecommendLine line;
		if(type.equals("1")&&routeList!=null) {
			line = routeList.get(0);
		} else {
			if(routeList.size()>1) {				
				line = routeList.get(1);
			} else {
				return;
			}
		}
		int count = 0;
		if(line!=null&&line.getLineSectionList()!=null) {
			for(SpotInfo each : line.getLineSectionList()) {
				spotList.add(each);
			}
			count =  line.getLineSectionList().size();
		}
		LayoutInflater  inflater=LayoutInflater.from(this);
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
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        	for (int i = 0; i < mRoute_layout.getChildCount(); i++) {                        		
                        		View view = mRoute_layout.getChildAt(i);
                        		if (view.getTag().toString()
                        				.equals(v.getTag().toString())) {
                        			ImageView imageMapLinePoint = (ImageView) v.findViewById(R.id.image_map_line_point);
                                    imageMapLinePoint
                                          .setImageResource(R.drawable.img_map_point_choice);
                                    LatLng center = new LatLng(spotList.get(i).getLat(), spotList.get(i).getLng());
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    		center, 18));  //37.5206,121.358
                        		}
                        	}
                        }
                    });
            mRoute_layout.addView(linearLayoutMapLineItem);
			
		}
	}
	
	private void initGridView() {
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<Drawable> images = new ArrayList<Drawable>();
		
		Drawable object0 = 
				this.getResources().getDrawable(R.drawable.item_toilet);
		images.add(object0);
		titles.add("洗手间");
		keys.add("2");
		Drawable object1 = 
				this.getResources().getDrawable(R.drawable.item_rope);
		images.add(object1);
		titles.add("索道");
		keys.add("3");
		Drawable object2 = 
				this.getResources().getDrawable(R.drawable.item_dock);
		images.add(object2);
		titles.add("码头");
		keys.add("4");
		Drawable object3 = 
				this.getResources().getDrawable(R.drawable.item_service);
		images.add(object3);
		titles.add("服务中心");
		keys.add("5");
		Drawable object4 = 
				this.getResources().getDrawable(R.drawable.item_park);
		images.add(object4);
		titles.add("停车场");
		keys.add("6");
		Drawable object5 = 
				this.getResources().getDrawable(R.drawable.item_exchange);
		images.add(object5);
		titles.add("换乘中心");
		keys.add("7");
		Drawable object6 = 
				this.getResources().getDrawable(R.drawable.item_ticket);
		images.add(object6);
		titles.add("售票处");
		keys.add("8");
		Drawable object7 = 
				this.getResources().getDrawable(R.drawable.item_inexit);
		images.add(object7);
		keys.add("9");
		titles.add("出入口");
		final GridAdapter adapter = new GridAdapter(MapActivity.this, titles, keys, images);
		layoutShow.setAdapter(adapter);
		layoutShow.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String item = adapter.getItem(arg2);
				if(markerUtils!=null) {
					markerUtils.removeAllAddition();
					markerUtils.addMarkerGrphic(item);
				}
				
			}
			
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		 case android.R.id.home:
		        finish(); 
		        return true; 
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
	
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (mMap == null) {
			mMap = mapView.getMap();
			mMap.setOnMapLoadedListener(this);
			mMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
			mMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
			mMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
			mMap.setOnCameraChangeListener(this);
			mMap.getUiSettings().setCompassEnabled(false);
			mMap.getUiSettings().setZoomControlsEnabled(false);
			
			setUpMap();
		}
		
	}
	
	private void setUpMap() {
        
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		mMap.setMyLocationStyle(myLocationStyle);// 将自定义的 myLocationStyle 对象添加到地图上
		mMap.setLocationSource(this);// 设置定位监听 //设置定位资源。如果不设置此定位资源则定位按钮不可点击。
		mMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		mMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
 
	}
	
	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			mNaviStart = new NaviLatLng(aLocation.getLatitude(), aLocation.getLongitude());
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}
	
	@Override
	public void onMapLoaded() {
		zoom = mMap.getCameraPosition().zoom;
		// 设置所有maker显示在View中
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(36.1427,120.6837), 18));  //37.5206,121.358
		addOverlayToMap();
		getScenicSpotsNet("394");
	}
	

	private void getScenicSpotsNet(String scenicId) {
		ApiClient.getIuuApiClient().queryScenicSpotLists(scenicId, new Callback<List<ScenicPointJson>>() {
	        @Override
	        public void success(List<ScenicPointJson> resultJson, Response response) {
	        	Toast.makeText(MapActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
	        	if(resultJson == null) {
	        		Toast.makeText(MapActivity.this, "结果为空", Toast.LENGTH_SHORT).show();
	        	}
	        	markerList = (ArrayList)resultJson;
	        	markerUtils = new MarkerUtils(MapActivity.this, mMap, markerList);
	        	addMarkerFunc("1");
	        }

	        @Override
	        public void failure(RetrofitError error) {
	            Toast.makeText(MapActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	
	/**
	 * 往地图上添加一个groundoverlay覆盖物
	 */
	private void addOverlayToMap() {
//		mRouteOverLay = new RouteOverLay(mMap, null);
        if(scenic != null) {
//            LatLng northeast = scenic.get;
//            LatLng southwest =;
            double lat_left = 36.1379;
            double lng_left = 120.6739;
            double lat_right = 36.14150;
            double lng_right = 120.6773;

            LatLngBounds bounds = new LatLngBounds.Builder()
            .include(new LatLng(lat_left,lng_left))
            .include(new LatLng(lat_right,lng_right)).build();
            Log.d("MapActivity", Config.Map_FILEPATH);
            groundoverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                    .anchor(0.5f, 0.5f)
                    .transparency(0f)
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.laoshan))
//                            .image(BitmapDescriptorFactory
//                                    .fromPath(Environment.getExternalStorageDirectory().getAbsolutePath() +
//                                            scenic.getImageUrl()))
                    .positionFromBounds(bounds).zIndex(1));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng((lat_left+lat_right)/2,(lng_left+lng_right)/2), 18));// 设置当前地图显示
        }


    }
	
	private void addMarkerFunc(String spotype) {
		markerUtils.addMarkerGrphic(spotype);
	}
	
	@Override
	public void onCameraChange(CameraPosition cp) {
		// TODO Auto-generated method stub
		onCameraChangeFinish(cp);
	}
	
    @Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
    	zoom = cameraPosition.zoom;
    	
    	if(markerUtils!=null && zoom < 17 ) {
    		markerUtils.setAllUnVisible();
    	} else if(markerUtils!=null) {
    		markerUtils.setAllVisible();
    	}
    	
	    VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion(); 
	    LatLngBounds bounds = new LatLngBounds.Builder()
        .include(new LatLng(36.1377,120.6737))   //36.1377,120.6737 laoshan  37.515658,121.352212
		.include(new LatLng(36.1415,120.6758)).build();  //36.1415,120.6758  37.528217,121.365323
	    // 获取可视区域
	    LatLngBounds curBounds = visibleRegion.latLngBounds;
	    if(!bounds.contains(curBounds))
	    {
//	    	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//					new LatLng(37.522,121.356), 18));
	    }
	}
	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		mapView.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		if(marker.getObject() != null) {//1marker			
			View infoWindow = getLayoutInflater().inflate(
					R.layout.custom_info_window, null);
			
			render(marker, infoWindow);
			return infoWindow;
		} else {			
			return null;
		}
		
	}

	/**
	 * 监听点击infowindow窗口事件回调
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		if(ApplicationHelper.getInstance().getPlayer().getStatus() == 0) {
            ApplicationHelper.getInstance().getPlayer().stop();
		}
	}
	
	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		if(arg0.getObject() != null) {//1marker	
			
		}
		return false;
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {
		final TextView voiceView = (TextView) view.findViewById(R.id.voice);
		final TextView naviView = (TextView) view.findViewById(R.id.navi);
		
		final ScenicPointJson point = (ScenicPointJson)marker.getObject();
//		String point = (String)marker.getObject();
		if(point.getSpotType().equals("1")) {//point.getSpotType()
			voiceView.setTag(point.getScenicId());
			voiceView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					player.playUrl(Environment.getExternalStorageDirectory().getAbsolutePath()
//							+"/Music/anyone of us.mp3");
                    Player player = ApplicationHelper.getInstance().getPlayer();
					if(player.getStatus() != 0 && scenic != null) {
                        String url = point.getAudioUrl();
						player.playUrl(Config.IMAGE_SERVER_ADDR + url);

					} else if(player.getStatus() == 0) {
						player.stop();
					}
//					player.playUrl(Environment.getExternalStorageDirectory().getAbsolutePath()
//							+"/Samsung/Music/Over_the_horizon.mp3");
				}
			
			});
		} else {
			voiceView.setVisibility(View.GONE);
		}
		naviView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mNaviStart.setLatitude(36.138143);//120.674922,36.138143
				mNaviStart.setLongitude(120.674922);
        //        mNaviStart.setLatitude(AppApplication.getInstance().getMyLocation().latitude);
        //        mNaviStart.setLongitude(AppApplication.getInstance().getMyLocation().longitude);
				mNaviEnd = new NaviLatLng(point.getLat(), point.getLng());
				calculateFootRoute();
                naviView.setVisibility(View.GONE);
                voiceView.setVisibility(View.GONE);
			}
			
		});
		
	}
	
	//计算步行路线
	private void calculateFootRoute() {
		if(mNaviStart != null && mNaviEnd !=null) {
			boolean isSuccess = mAMapNavi.calculateWalkRoute(mNaviStart ,mNaviEnd);
			if (!isSuccess) {
				ToastUtil.show(this, "路线计算失败,检查参数情况");
			}
		} else {
			ToastUtil.show(this, "失败,起点或终点未设定");
		}
	}
	
	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		// TODO Auto-generated method stub
		ToastUtil.show(this, "路径规划出错" + arg0);
	}

	@Override
	public void onCalculateRouteSuccess() {
		AMapNaviPath naviPath = mAMapNavi.getNaviPath();
		if (naviPath == null) {
			return;
		}
		removeRoute();
		List<NaviLatLng> arg0 = naviPath.getCoordList();
		ArrayList<LatLng> arg1 = new ArrayList<LatLng>();
		// 获取路径规划线路，显示到地图上
//		mRouteOverLay.setRouteInfo(naviPath);
//		mRouteOverLay.addToMap();
//		mRouteOverLay.drawArrow(arg0);
		mMarkerRouteStart = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.start))
				.position(new LatLng(mNaviStart.getLatitude(),mNaviStart.getLongitude())));
		mMarkerRouteEnd = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.end))
				.position(new LatLng(mNaviEnd.getLatitude(),mNaviEnd.getLongitude())));
		for(NaviLatLng each:arg0) {
			arg1.add(new LatLng(each.getLatitude(),each.getLongitude()));
//			mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(each.getLatitude(),each.getLongitude())));
		}
		lineDraw = mMap.addPolyline(new PolylineOptions().addAll(arg1).color(Color.RED).visible(true).zIndex(10));
		
	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNaviInfoUpdate(NaviInfo arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub
		
	}
}
