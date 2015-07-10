package com.imyuu.travel.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.VisibleRegion;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.imyuu.travel.R;
import com.imyuu.travel.TTSController;
import com.imyuu.travel.util.Utils;

import java.lang.reflect.Field;

/**
 * 导航界面
 */
public class NavigateActivity extends Activity implements
        AMapNaviViewListener, OnCameraChangeListener,
        OnMapLoadedListener {
    //导航View
    private AMapNaviView mAmapAMapNaviView;
    //是否为模拟导航
    private boolean mIsEmulatorNavi = true;
    //记录有哪个页面跳转而来，处理返回键
    private int mCode = -1;
    private AMap mAMap;
    private AMapLocation aLocation;
    private LatLngBounds bounds;
    private LatLng center;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplenavi);
        Bundle bundle = getIntent().getExtras();
        processBundle(bundle);
        init(savedInstanceState);
        //CameraUpdateFactory.scrollBy(SCROLL_BY_PX, SCROLL_BY_PX)
        Log.i("MOVE", "onCreate");
    }

    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在View中
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(37.515658, 121.352212))
                .include(new LatLng(37.528217, 121.365323)).build();
        // 移动地图，所有marker自适应显示。LatLngBounds与地图边缘10像素的填充区域
        mAmapAMapNaviView.getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
    }

    private void removeLogo() {
        Class<?> c = null;
        try { //反射找到Resources类
            c = Class.forName("com.ls.widgets.map.utils.Resources");
            Object obj = c.newInstance();
            //找到Logo 属性，是一个数组
            Field field = c.getDeclaredField("LOGO");
            field.setAccessible(true);
            //将LOGO字段设置为null
            field.set(obj, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 对移动地图结束事件回调
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
//        mCameraTextView.setText("onCameraChangeFinish:"
//                + cameraPosition.toString());
        VisibleRegion visibleRegion = mAmapAMapNaviView.getMap().getProjection().getVisibleRegion();
        // 获取可视区域、

        LatLngBounds latLngBounds = visibleRegion.latLngBounds;

        Projection proj = mAmapAMapNaviView.getMap().getProjection();

        Point startPoint = proj.toScreenLocation(latLngBounds.southwest);
        Log.d("MOVE", startPoint.x + ":" + startPoint.y);
        // startPoint.offset(0, -100);
        if (!bounds.contains(mAmapAMapNaviView.getMap().getCameraPosition().target)) {
            // 获取可视区域的Bounds
            //boolean isContain = latLngBounds.contains(Constants.SHANGHAI);
            // 判断上海经纬度是否包括在当前地图可见区域
            LatLng move = mAmapAMapNaviView.getMap().getCameraPosition().target;
            mAmapAMapNaviView.getMap().moveCamera(CameraUpdateFactory.changeLatLng(center));
        }

    }

    private void processBundle(Bundle bundle) {
        if (bundle != null) {
            mIsEmulatorNavi = bundle.getBoolean(Utils.ISEMULATOR, true);
            mCode = bundle.getInt(Utils.ACTIVITYINDEX);
        }
    }

    /**
     * 往地图上添加一个groundoverlay覆盖物
     */
    private void addOverlayToMap(AMap mAMap) {
        //,
        center = new LatLng(37.520049, 121.360109);
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                center, 18));// 设置当前地图显示为北京市恭王府


        bounds = new LatLngBounds.Builder()
                .include(new LatLng(37.515658, 121.352212))
                .include(new LatLng(37.528217, 121.365323)).build();

        GroundOverlay groundoverlay = mAMap.addGroundOverlay(new GroundOverlayOptions()
                .anchor(0.5f, 0.5f)
                .transparency(0.5f)
                .image(BitmapDescriptorFactory
                        .fromResource(R.drawable.groundoverlay))
                .positionFromBounds(bounds));
        mAMap.getUiSettings().setCompassEnabled(true);
        mAMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);

    }

    /**
     * 初始化
     *
     * @param savedInstanceState
     */
    private void init(Bundle savedInstanceState) {


        mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.simplenavimap);
        mAmapAMapNaviView.onCreate(savedInstanceState);
        mAmapAMapNaviView.setAMapNaviViewListener(this);


        mAmapAMapNaviView.getMap().setOnCameraChangeListener(this);
        mAmapAMapNaviView.getMap().setOnMapLoadedListener(this);


        addOverlayToMap(mAmapAMapNaviView.getMap());
        TTSController.getInstance(this).startSpeaking();


        if (mIsEmulatorNavi) {
            // 设置模拟速度
            AMapNavi.getInstance(this).setEmulatorNaviSpeed(100);
            // 开启模拟导航
            AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);

        } else {
            // 开启实时导航
            AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode);
        }
    }

//-----------------------------导航界面回调事件------------------------

    /**
     * 导航界面返回按钮监听
     */
    @Override
    public void onNaviCancel() {
        Intent intent = new Intent(NavigateActivity.this,
                SimpleNaviRouteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviMapMode(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNaviTurnClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNextRoadClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onScanViewButtonClick() {
        // TODO Auto-generated method stub

    }

    /**
     * 返回键监听事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCode == Utils.SIMPLEROUTENAVI) {
                Intent intent = new Intent(NavigateActivity.this,
                        SimpleNaviRouteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();

            } else if (mCode == Utils.SIMPLEGPSNAVI) {
                Intent intent = new Intent(NavigateActivity.this,
                        GPSNaviActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // ------------------------------生命周期方法---------------------------
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAmapAMapNaviView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAmapAMapNaviView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mAmapAMapNaviView.onPause();
        AMapNavi.getInstance(this).stopNavi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAmapAMapNaviView.onDestroy();

        TTSController.getInstance(this).stopSpeaking();

    }

    @Override
    public void onLockMap(boolean arg0) {

        // TODO Auto-generated method stub

    }

    @Override
    public void onCameraChange(CameraPosition cp) {
        // TODO Auto-generated method stub

        onCameraChangeFinish(cp);
    }


}
