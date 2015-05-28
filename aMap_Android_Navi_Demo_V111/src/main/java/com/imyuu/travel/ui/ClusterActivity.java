package com.imyuu.travel.ui;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.imyuu.travel.R;
import  com.imyuu.travel.bean.*;
import  com.imyuu.travel.api.ApiClient;
import  com.imyuu.travel.model.*;
import com.imyuu.travel.util.ClusterUtils;
/**
 * AMapV1地图demo总汇
 */
public final class ClusterActivity extends Activity implements OnCameraChangeListener, OnMapLoadedListener,
	LocationSource, AMapLocationListener, OnMarkerClickListener, InfoWindowAdapter {

	private AMap mMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	
	private ArrayList<ScenicAreaJson> scenicLists;
	private ArrayList<PointsClusterEntity> mClusterDatas;
	private ClusterUtils utils;
    @InjectView(R.id.image_cancel_back)
    ImageView vm_cancel;
    @InjectView(R.id.text_choose_city)
    Button tx_chooseCity;
	private float zoom; //记载缩放级别

    @OnClick(R.id.image_cancel_back)
    public void cacelBackClick() {
        Intent intent = new Intent(ClusterActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.text_choose_city)
    public void chooseCityClick() {
        Intent intent = new Intent(ClusterActivity.this,QueryCityActivity.class);
        startActivityForResult(intent,0);

    }


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.cluster_activity);
        ButterKnife.inject(this);
//		ActionBar actionBar = getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(false);//显示返回箭头
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle("                        "+"");
		mapView = (MapView) findViewById(R.id.map_cluster);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
        vm_cancel.setImageResource(R.drawable.m_44);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_cluster, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {  
	    case R.id.action_query:  
	       // 查询
	    	Intent intent2 =new Intent(ClusterActivity.this,QueryCityActivity.class);
	    	this.startActivityForResult(intent2, 0);
	        return true;  
	    default:  
	        return super.onOptionsItemSelected(item);  
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return ;
		}
		//接受结束的字符串
		City city = (City)data.getExtras().getParcelable("cityResult");
		
		if(city!=null) {
			LatLng position = city.getCityPosition();
			if(position != null) {
				mMap.moveCamera(CameraUpdateFactory.changeLatLng(position));
                mMap.setMyLocationEnabled(false);
                tx_chooseCity.setText(city.getCityName());
			}
		}
			
	}
	
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		scenicLists = new ArrayList<ScenicAreaJson>();
		mClusterDatas = new ArrayList<PointsClusterEntity>();
		
		if (mMap == null) {
			mMap = mapView.getMap();
			mMap.setOnMapLoadedListener(this);
			mMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
			mMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		}
        getScenics();
	}
	
	private void getScenics() {
//		scenicLists = CityScenicUtils.createCityScenics();
//		clusterShow();
//		setUpMap();
//		mMap.setOnCameraChangeListener(ClusterActivity.this);
		ApiClient.getIuuApiClient().getScenicListbyPage(510, 0, new Callback<List<ScenicAreaJson>>() {
	        @Override
	        public void success(List<ScenicAreaJson> result, Response response) {
	        	Toast.makeText(ClusterActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
	        	if(result == null) {
	        		Toast.makeText(ClusterActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
	        	}
              //  Log.d("-------","datasize"+result.size());
	        	scenicLists.clear();
	        	for(ScenicAreaJson each:result) {
                  //  Log.d("-------","datasize"+each.getLat()+"---"+each.getLng());
                  //  Log.d("-------",each.getScenicName());
//                    ScenicModel object = new ScenicModel();
                    if(each.getLat() != null && each.getLat()>0
                            && each.getLng() != null && each.getLng()>0) {
//                        object.setLatLng(new LatLng(each.getLat(), each.getLng()));
//						object.setScenicId(each.getScenicId());
//
//                        object.setScenicName(each.getScenicName());
                        scenicLists.add(each);
                    }
	        	}
	        	clusterShow();
	    		setUpMap();
	    		mMap.setOnCameraChangeListener(ClusterActivity.this);
	        }

	        @Override
	        public void failure(RetrofitError error) {
	            Toast.makeText(ClusterActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	
	public void clusterShow() {
		if(scenicLists.size() == 0) {
			return;
		}
		
		mClusterDatas.clear();

		utils = new ClusterUtils(ClusterActivity.this, mMap, scenicLists);
		mClusterDatas = utils.resetMarks();
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
            tx_chooseCity.setText(aLocation.getCity());
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
	}
	
	@Override
	public void onCameraChange(CameraPosition cp) {
		// TODO Auto-generated method stub
		onCameraChangeFinish(cp);
	}
	
    @Override
	public void onCameraChangeFinish(CameraPosition cameraPosition) {
    	if(cameraPosition.zoom == zoom) {
    		
    	} else {
    		zoom = cameraPosition.zoom;
    		clusterShow();
    	}
	}
	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		init();//必须写在这里
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
		System.exit(0);
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
	public boolean onMarkerClick(Marker arg0) {
		if(arg0.getObject() != null) {//1marker
			PointsClusterEntity result = (PointsClusterEntity)arg0.getObject();
			if(result.getClusterCount() == 1) {
				Intent intent = new Intent(ClusterActivity.this, MapOnlineActivity.class);
                intent.putExtra("scenicInfo", result.getSubScenicEntity().get(0));
//				Intent intent = new Intent(ClusterActivity.this, MarkerActivity.class);
				startActivity(intent);
			} 
//			else {
//				Intent intent = new Intent(ClusterActivity.this, CopyOfMapActivity.class);
//				startActivity(intent);
//			}
		}
		
//		arg0.showInfoWindow();
		return false;
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
