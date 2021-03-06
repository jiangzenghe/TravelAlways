package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
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

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.GroundOverlay;
import com.amap.api.maps2d.model.GroundOverlayOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.maps2d.model.VisibleRegion;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.imyuu.travel.R;
import com.imyuu.travel.TTSController;
import com.imyuu.travel.adapters.GridAdapter;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.base.AppApplication;
import com.imyuu.travel.bean.ScenicAdvertOldModel;
import com.imyuu.travel.bean.SpotModel;
import com.imyuu.travel.model.Recommend;
import com.imyuu.travel.model.RecommendLine;
import com.imyuu.travel.model.ScenicAdvertJson;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.model.ScenicDetailJson;
import com.imyuu.travel.model.ScenicPointJson;
import com.imyuu.travel.model.SpotInfo;
import com.imyuu.travel.network.CacheThread;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.CommonUtils;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.util.MarkerUtilsFor2D;
import com.imyuu.travel.util.Player;
import com.imyuu.travel.util.PreferencesUtils;
import com.imyuu.travel.util.ToastUtil;
import com.imyuu.travel.view.ColumnHorizontalScrollView;
import com.imyuu.travel.view.GridView;
import com.imyuu.travel.view.RoutePopView;
import com.imyuu.travel.view.SlideShowView;

import org.apache.commons.httpclient.util.URIUtil;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * AMapV1地图demo总汇
 */
public final class MapOnlineActivity extends Activity implements AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener,
		AMap.InfoWindowAdapter, AMap.OnCameraChangeListener, AMap.OnMapLoadedListener, LocationSource,
	AMapLocationListener, AMapNaviListener,AMap.OnMapTouchListener {

	private AMap mMap;
	private MapView mapView;
	private TextView cilckText;
	private GridView layoutShow;
	private TextView routeText;
	private RelativeLayout rl_column;
	private ImageView imageMapAdvertClose;
	private SlideShowView slideshowviewAdvert;
	private RelativeLayout relativelayoutMapAdvert;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private AMapNavi mAMapNavi;
	private RoutePopView popView;

	public AMap getmMap() {
		return mMap;
	}

	public void setmMap(AMap mMap) {
		this.mMap = mMap;
	}

	private TextView redAlert;
	private LinearLayout layout_redalert;
	
	private float zoom;
	private View curDisplayView;
	private boolean isGPSAuto = false;
	private boolean isSpeakingAuto = false;
	private String mCurPalyingURL = "";
	private Player player;
	private NaviLatLng mNaviEnd;
	private NaviLatLng mNaviStart;
	private Marker mMarkerRouteStart;
	private Marker mMarkerRouteEnd;

	// 规划线路
	private Polyline lineDraw;
	private Marker mCurrentVirtualPoint;
    private ScenicAreaJson scenic;
	private String scenicId = "";
	//广告
	private ImageView[] imageViews;
	/** 分类列表*/
	private ArrayList<ScenicPointJson> markerList=new ArrayList<ScenicPointJson>();
	private MarkerUtilsFor2D markerUtilsFor2D;

	@InjectView(R.id.layout_function)
	LinearLayout layout_function;
    @InjectView(R.id.image_cancel_back)
    ImageView vm_cancel;
    @InjectView(R.id.tx_scenicName)
    TextView tx_scenicName;

    @OnClick(R.id.image_cancel_back)
    public void cacelBackClick() {
        Intent intent = new Intent(MapOnlineActivity.this, MainActivity.class);
        startActivity(intent);
    }

	@OnClick(R.id.layout_function)
	public void callFuncAlert() {
		Intent intent = new Intent(MapOnlineActivity.this, FunctionCallOutActivity.class);
		intent.putExtra("isGPSAuto", isGPSAuto);
		intent.putExtra("isSpeakingAuto", isSpeakingAuto);
		startActivityForResult(intent, 1000);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_online_activity);
        ButterKnife.inject(this);

        scenic   = (ScenicAreaJson)getIntent().getExtras().getSerializable("scenicInfo");

		if(scenic != null) {
			String tvl_url = Config.TILE_URL_ROOT +scenic.getScenicId()+"/%d/%d/%d.png";
			MapsInitializer.replaceURL(tvl_url, "OnLine");
			scenicId = scenic.getScenicId();
            tx_scenicName.setText(scenic.getScenicName());
		}

		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		player = ApplicationHelper.getInstance().getPlayer();
		
		initMap();
		initView();
	}

	public void removeRoute() {
		if (rl_column.getVisibility() == View.VISIBLE) {
			Drawable nav_up = getResources().getDrawable(R.drawable.uparrow);
			nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
			routeText.setCompoundDrawables(nav_up, null, null, null);
			rl_column.setVisibility(View.GONE);
		}
		popView.removeRoute();
		if(lineDraw != null) {
			lineDraw.remove();
		}
		if(mMarkerRouteStart!=null) {
			mMarkerRouteStart.remove();
		}

		if(mMarkerRouteEnd!=null) {
			mMarkerRouteEnd.remove();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1000 && resultCode == RESULT_OK) {
			//modify by kevin at 7-7
            isGPSAuto = PreferencesUtils.getSettingBoolean(getBaseContext(), "switch_GPSAUTO");

            isSpeakingAuto=PreferencesUtils.getSettingBoolean(getBaseContext(), "switch_SpeackingAUTO");

            if(!isSpeakingAuto) {
				if(player.getStatus() == 0) player.stop();
			}
			if(isGPSAuto) {
				mMap.setMyLocationEnabled(true);
				mMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
			} else if(mAMapLocationManager!=null){
				mAMapLocationManager.removeUpdates(this);
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void initView() {
        // modify by kevin at 7-7
        isGPSAuto = PreferencesUtils.getSettingBoolean(getBaseContext(), "switch_GPSAUTO");
        isSpeakingAuto=PreferencesUtils.getSettingBoolean(getBaseContext(), "switch_SpeackingAUTO");

		mAMapNavi = AMapNavi.getInstance(this);
		TTSController ttsManager = TTSController.getInstance(this);// 初始化语音模块
		ttsManager.init();
		if(mAMapNavi != null) {
			AMapNavi.getInstance(this).setAMapNaviListener(ttsManager);// 设置语音模块播报
			mAMapNavi.setAMapNaviListener(this);
		}

		imageMapAdvertClose = (ImageView) findViewById(R.id.image_map_advert_close);
		relativelayoutMapAdvert = (RelativeLayout) findViewById(R.id.relativelayout_map_advert);
		slideshowviewAdvert = (SlideShowView) findViewById(R.id.slideshowview_advert);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		routeText = (TextView) findViewById(R.id.route);
		cilckText = (TextView) findViewById(R.id.help);
		layoutShow = (GridView) findViewById(R.id.layout_show);
		redAlert = (TextView) findViewById(R.id.text_redalert);
		layout_redalert = (LinearLayout) findViewById(R.id.layout_redalert);
		layout_redalert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				redAlert.setVisibility(View.VISIBLE);
			}

		});
		imageMapAdvertClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				relativelayoutMapAdvert.setVisibility(View.GONE);
			}
		});

		LayoutInflater inflater = LayoutInflater.from(this); 
        // 引入窗口配置文件 
        final View view = inflater.inflate(R.layout.main_top_right_dialog, null);
        // 创建PopupWindow对象 
		popView = new RoutePopView(MapOnlineActivity.this, scenicId, view);

        routeText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popView.isShowing()) {
					// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
					popView.dismiss();
				} else {
					if (rl_column.getVisibility() == View.VISIBLE) {
						Drawable nav_up = getResources().getDrawable(R.drawable.uparrow);
						nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
						routeText.setCompoundDrawables(nav_up, null, null, null);
						rl_column.setVisibility(View.GONE);
						return;
					}
					// 显示窗口
					int[] location = new int[2];
					v.getLocationOnScreen(location);
					int mWindowHeight = CommonUtils.getWindowsHeight(MapOnlineActivity.this);
					popView.showAtLocation(v, Gravity.LEFT | Gravity.BOTTOM, 0, mWindowHeight - location[1] + popView.getHeight());

				}

			}
		});

		//点击助手
		cilckText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				indicateAnimation(layoutShow, null, 1);
			}
		});
		
		initGridView();
	}

	//隐藏与显示的动画效果
	public void indicateAnimation(View view, TextView flag,int type) {

		if(type == 0) {//horizontal
			Drawable nav_down = getResources().getDrawable(R.drawable.downarrow);
			nav_down.setBounds(0, 0, nav_down.getMinimumWidth(), nav_down.getMinimumHeight());
			flag.setCompoundDrawables(nav_down, null, null, null);
			view.setVisibility(View.GONE);
			Animation mShowAction = AnimationUtils.loadAnimation(MapOnlineActivity.this, R.anim.right_in);
			view.setAnimation(mShowAction);
			view.setVisibility(View.VISIBLE);

		} else { //vertical
			TranslateAnimation mHideAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
			TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
					1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			if (view.getVisibility() == View.VISIBLE) {
				mHideAction.setDuration(50);
				view.setAnimation(mHideAction);
				view.setVisibility(View.GONE);
			} else {
				mShowAction.setDuration(50);
				view.setAnimation(mShowAction);
				view.setVisibility(View.VISIBLE);
			}
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
		final GridAdapter adapter = new GridAdapter(MapOnlineActivity.this, titles, keys, images);
		layoutShow.setAdapter(adapter);
		layoutShow.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String item = adapter.getItem(arg2);
				if(markerUtilsFor2D !=null) {
					markerUtilsFor2D.removeAllAddition();
					markerUtilsFor2D.addMarkerGrphic(item);
					if(mNaviStart!=null) {
						LatLng in = new LatLng(mNaviStart.getLatitude(), mNaviStart.getLongitude());
						LatLng posi = markerUtilsFor2D.moveToNearestPosition(in, item);

						if(mCurrentVirtualPoint == null) {
							mCurrentVirtualPoint = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.0f)
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_point)));
						}
						mCurrentVirtualPoint.setPosition(posi);
					}
				}
				indicateAnimation(layoutShow, null, 1);

			}
			
		});
	}

	@Override
	public void onTouch(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		int[] location = new int[2];
		if(layoutShow.getVisibility() == View.VISIBLE) {
			layoutShow.getLocationOnScreen(location);
			int x_layout = location[0];
			int y_layout = location[1];
			if((x>x_layout&&x<x_layout+layoutShow.getWidth())
					&&(y>y_layout && y<y_layout+layoutShow.getHeight())){

			} else {
				indicateAnimation(layoutShow, null, 1);
			}
			return;
		}
//		if(curDisplayView != null) {
//			curDisplayView.getLocationOnScreen(location);
//			int x_layout = location[0];
//			int y_layout = location[1];
//			if((x>x_layout&&x<x_layout+curDisplayView.getWidth())
//					&&(y>y_layout && y<y_layout+curDisplayView.getHeight())){
//
//			} else {
//				curDisplayView.setVisibility(View.GONE);
//			}
//		}

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
	private void initMap() {
		if (mMap == null) {
			mMap = mapView.getMap();
			mMap.setOnMapLoadedListener(this);
			mMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
			mMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
			mMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
			mMap.setOnCameraChangeListener(this);
			mMap.setOnMapTouchListener(this);
			mMap.getUiSettings().setCompassEnabled(true);
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
		mMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		mMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
 
	}
	
	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {

		if(scenic != null && mListener != null && aLocation != null && scenic.getRight_lat() !=null &&
			scenic.getRight_lng()!=null &&scenic.getLat()!=null &&scenic.getLng()!=null) {

			double lat_left = scenic.getLat();
			double lng_left = scenic.getLng();
			double lat_right = scenic.getRight_lat();
			double lng_right = scenic.getRight_lng();

			LatLngBounds bounds = new LatLngBounds.Builder()
					.include(new LatLng(lat_left,lng_left))
					.include(new LatLng(lat_right,lng_right)).build();
			LatLng curPosition = new LatLng(aLocation.getLatitude(), aLocation.getLongitude());

			if(!bounds.contains(curPosition)) {
				ToastUtil.show(this, "定位位置离本景点太远，请到达景点再定位使用此功能");
				return;
			}

			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			mNaviStart = new NaviLatLng(aLocation.getLatitude(), aLocation.getLongitude());
			if(isSpeakingAuto) {
				ScenicPointJson spot = markerUtilsFor2D.getNearestSpot(curPosition);
				if(spot != null && spot.getAudioUrl() != null) {
					if(!mCurPalyingURL.equals(spot.getAudioUrl())) {
						//play
						if(player.getStatus() == 0)  player.stop();
						String url = spot.getAudioUrl();
						playAudioWithCache(url);
						mCurPalyingURL = spot.getAudioUrl();
					}
				};
			}

		}

	}
    //modify by kevin @ 2015-5-2
    private void playAudioWithCache(String url) {
        if(url==null || url.length()<1)
            return;
        String filename = url.substring(url.lastIndexOf("/"));
        String localFile = Config.CACHE_DIR +"/"+scenicId + filename;
        if(FileUtils.isExist(localFile)) {
            LogUtil.d("playAudioWithCache",localFile);
            player.playUrl(localFile);
        }
        else
           player.playUrl(Config.IMAGE_SERVER_ADDR + url);
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
			if(isGPSAuto) {
				mAMapLocationManager.requestLocationData(
						LocationProviderProxy.AMapNetwork, 5 * 2000, 10, this);
			} else {
				mAMapLocationManager.requestLocationData(
						LocationProviderProxy.AMapNetwork, -1, 10, this);
			}
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
		if(scenic != null && scenic.getRight_lat() !=null &&
			scenic.getRight_lng() !=null &&scenic.getLat()!=null &&scenic.getLng()!=null) {
			double centerLat = (scenic.getLat()+scenic.getRight_lat())/2;
			double centerLng = (scenic.getLng()+scenic.getRight_lng())/2;
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(centerLat, centerLng), 19));  //37.5206,121.358
//			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//					new LatLng(36.139143, 120.674922), 19));  //37.5206,121.358
			mNaviStart = new NaviLatLng(centerLat, centerLng);

//			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//					new LatLng(scenic.getLat(), scenic.getLng()), 19));  //37.5206,121.358
			getScenicSpotsNet(scenic.getScenicId());
		}
	}

	private void getScenicSpotsNet(final String scenicId) {
		ApiClient.getMapService().queryScenicSpotLists(scenicId, new Callback<List<ScenicPointJson>>() {
			@Override
			public void success(List<ScenicPointJson> resultJson, Response response) {
				Toast.makeText(MapOnlineActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
				if (resultJson == null) {
					Toast.makeText(MapOnlineActivity.this, "结果为空", Toast.LENGTH_SHORT).show();
					return;
				}
				List<String> audioUrls = new ArrayList<String>();
				for (ScenicPointJson spj : resultJson) {
					audioUrls.add(spj.getAudioUrl());
				}
				new CacheThread(audioUrls, scenicId).start();
				markerList = (ArrayList) resultJson;
				markerUtilsFor2D = new MarkerUtilsFor2D(MapOnlineActivity.this, mMap, markerList);
				addMarkerFunc("1");
				getScenicAdvertNet(scenic.getScenicId());
			}

			@Override
			public void failure(RetrofitError error) {
				Toast.makeText(MapOnlineActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getScenicAdvertNet(String scenicId) {
		ApiClient.getMapService().queryScenicAdverts(scenicId, new Callback<List<ScenicAdvertJson>>() {
			@Override
			public void success(List<ScenicAdvertJson> resultJson, Response response) {
				Toast.makeText(MapOnlineActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
				if (resultJson == null) {
					Toast.makeText(MapOnlineActivity.this, "结果为空", Toast.LENGTH_SHORT).show();
					return;
				}
				ArrayList<ScenicAdvertJson> markerAdvertList = (ArrayList) resultJson;
				loadAdvertList(markerAdvertList);
				markerUtilsFor2D.addMarkerGrphic(markerAdvertList);
			}

			@Override
			public void failure(RetrofitError error) {
				Toast.makeText(MapOnlineActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void loadAdvertList(ArrayList<ScenicAdvertJson> scenicAdvertModelList) {
		// 广告加载
		if (scenicAdvertModelList.size() > 0) {
			WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			int width = wm.getDefaultDisplay().getWidth();
			int height = 40;
			Bitmap bitmap = null;
            List<ImageView> images = new ArrayList<ImageView>();
			for (int i = 0; i < scenicAdvertModelList.size(); i++) {
				ScenicAdvertJson scenicAdvertModel = scenicAdvertModelList
						.get(i);
                if("2".equals(scenicAdvertModel.getAdvertType())) {
                    // 加载广告图片
                    ImageView    imageViews = new ImageView(MapOnlineActivity.this);
                    images.add(imageViews);
//				bitmap = BitmapFactory
//						.decodeFile(Config.UU_FILEPATH
//								+ scenicId + "/"
//								+ scenicAdvertModel.getAdvertPic());
//				imageViews[i].setImageBitmap(bitmap);
                    LogUtil.d("loadAdvertList",Config.IMAGE_SERVER_ADDR + scenicAdvertModel.getAdvertPic());
                    imageViews.setImageURI(Uri.parse(Config.IMAGE_SERVER_ADDR + scenicAdvertModel.getAdvertPic()));
                    //height = width * bitmap.getHeight() / bitmap.getWidth();
                    imageViews.setTag(scenicAdvertModel
                            .getAdvertScenicId());
                    imageViews
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
									getScenicSpotsInfo(v.getTag().toString());
                                }
                            });
                }
			}
			LayoutParams params = slideshowviewAdvert.getLayoutParams();
			params.width = width;
			params.height = height;
            imageViews = new ImageView[images.size()];
            int i=0;
            for (ImageView v:images)
                imageViews[i++] = v;
			slideshowviewAdvert.setImageViews(imageViews);
			slideshowviewAdvert.invalidate();
			slideshowviewAdvert.setLayoutParams(params);
            slideshowviewAdvert.setVisibility(View.VISIBLE);
            relativelayoutMapAdvert.setVisibility(View.VISIBLE);
		} else {
			relativelayoutMapAdvert.setVisibility(View.GONE);
		}
	}

	private void getScenicSpotsInfo(final String sId) {
		 ApiClient.getIuuApiClient().queryScenicDetail(scenicId, new Callback<ScenicDetailJson>() {
			 @Override
			 public void success(ScenicDetailJson scenicDetailJson, Response response) {
				 if (scenicDetailJson == null)
					 return;
				 ScenicAreaJson scenicAreaJson = new ScenicAreaJson();
				 scenicAreaJson.setScenicId(sId);
				 scenicAreaJson.setScenicName(scenicDetailJson.getScenicName());
				 scenicAreaJson.setImageUrl(scenicDetailJson.getImageUrl());
				 scenicAreaJson.setLat(scenicDetailJson.getLat());
				 scenicAreaJson.setLng(scenicDetailJson.getLng());
				 scenicAreaJson.setRight_lat(scenicDetailJson.getRight_lat());
				 scenicAreaJson.setRight_lng(scenicDetailJson.getRight_lng());
				 scenicAreaJson.setCanNavi(scenicDetailJson.getCanNavi());
				 scenicAreaJson.setMapSize(scenicDetailJson.getMapSize());
				 Intent intent = new Intent();
				 intent.setClass(MapOnlineActivity.this,
						 MapOnlineActivity.class);
				 intent.putExtra("scenicInfo", scenicAreaJson);
				 startActivity(intent);
				 finish();
			 }

			 @Override
			 public void failure(RetrofitError error) {
				 Toast.makeText(MapOnlineActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
			 }
		 });

	}

	private void addMarkerFunc(String spotype) {
		markerUtilsFor2D.addMarkerGrphic(spotype);
	}
	
	@Override
	public void onCameraChange(CameraPosition cp) {
		// TODO Auto-generated method stub
		onCameraChangeFinish(cp);
	}
	
    @Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
    	zoom = cameraPosition.zoom;
		if(mMap == null) return;

    	if(markerUtilsFor2D !=null && zoom < 18 ) {
    		markerUtilsFor2D.setAllUnVisible();
    	} else if(markerUtilsFor2D !=null) {
    		markerUtilsFor2D.setAllVisible();
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
		AMapNavi.getInstance(this).stopNavi();
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
		TTSController.getInstance(this).stopSpeaking();
		mapView.onDestroy();
		AMapNavi.getInstance(this).destroy();
		ApplicationHelper.getInstance().getPlayer().ondestroy();
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
					R.layout.marker_info_item, null);
			
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
//		if(ApplicationHelper.getInstance().getPlayer().getStatus() == 0) {
//            ApplicationHelper.getInstance().getPlayer().stop();
//		}
	}
	
	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub

		return false;
	}

	public void renderView(Marker marker, final View view) {
		final TextView voiceView = (TextView) view.findViewById(R.id.voice);
		final TextView spotNameView = (TextView) view.findViewById(R.id.spot_name);
		final TextView naviView = (TextView) view.findViewById(R.id.navi);

		final ScenicPointJson point = (ScenicPointJson)marker.getObject();
		spotNameView.setText(point.getScenicPointName());

	}
	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker,final View view) {
//		curDisplayView = view;
		final TextView voiceView = (TextView) view.findViewById(R.id.voice);
		final TextView spotNameView = (TextView) view.findViewById(R.id.spot_name);
		final TextView naviView = (TextView) view.findViewById(R.id.navi);
		final ImageView cancelView = (ImageView) view.findViewById(R.id.pop_cancel_btn);
		cancelView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				view.setVisibility(View.GONE);
			}
		});

		final ScenicPointJson point = (ScenicPointJson)marker.getObject();
		spotNameView.setText(point.getScenicPointName());
		if(point.getSpotType().equals("1")) {//point.getSpotType()
			voiceView.setTag(point.getAudioUrl());
			voiceView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(player.getStatus() == 0 && !point.getAudioUrl().equals(mCurPalyingURL)) {
						player.stop();
					}
					if(scenic == null) return;
					if(!point.getAudioUrl().equals(mCurPalyingURL) || player.getStatus() == 3
							|| player.getStatus() == 1) {
						String url = point.getAudioUrl();
						playAudioWithCache(url);
						mCurPalyingURL = point.getAudioUrl();
					} else {
						player.pause();
					}
				}
			
			});
		} else {
			voiceView.setVisibility(View.GONE);
		}
		naviView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// test data
				if(mNaviStart==null) {
					mNaviStart = new NaviLatLng(36.138143, 120.674922);
				}
				mNaviStart.setLatitude(36.138143);//120.674922,36.138143
				mNaviStart.setLongitude(120.674922);

//                mNaviStart.setLatitude(AppApplication.getInstance().getMyLocation().latitude);
//                mNaviStart.setLongitude(AppApplication.getInstance().getMyLocation().longitude);
				Log.e("endLoc", point.getLat() +"," + point.getLng());
				mNaviEnd = new NaviLatLng(point.getLat(), point.getLng());
				if(player.getStatus() == 0) {player.stop();}
				calculateFootRoute();
                view.setVisibility(View.GONE);
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
		mMarkerRouteStart = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.start))
				.position(new LatLng(mNaviStart.getLatitude(),mNaviStart.getLongitude())));
		mMarkerRouteEnd = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.end))
				.position(new LatLng(mNaviEnd.getLatitude(),mNaviEnd.getLongitude())));
		for(NaviLatLng each:arg0) {
			arg1.add(new LatLng(each.getLatitude(),each.getLongitude()));
		}
		lineDraw = mMap.addPolyline(new PolylineOptions().addAll(arg1).color(Color.RED).visible(true).zIndex(10));

		TTSController.getInstance(this).startSpeaking();
		// 设置模拟速度
		AMapNavi.getInstance(this).setEmulatorNaviSpeed(100);
		// 开启模拟导航
		AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);
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
