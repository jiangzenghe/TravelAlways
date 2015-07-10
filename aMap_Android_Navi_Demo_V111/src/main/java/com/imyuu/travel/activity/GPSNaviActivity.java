package com.imyuu.travel.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.imyuu.travel.R;
import com.imyuu.travel.TTSController;
import com.imyuu.travel.util.Utils;

import java.util.ArrayList;

/**
 * 实时导航界面
 */
public class GPSNaviActivity extends Activity implements OnClickListener,
        AMapNaviListener, AMapNaviViewListener {
    //起点终点坐标显示
    private TextView mStartPointTextView;
    private TextView mEndPointTextView;
    //实时导航按钮
    private Button mStartNaviButton;

    // 地图和导航资源
    private MapView mMapView;
    private AMap mAMap;
    private AMapNavi mAMapNavi;


    //起点终点
    private NaviLatLng mNaviStart = new NaviLatLng(39.989614, 116.481763);
    private NaviLatLng mNaviEnd = new NaviLatLng(39.983456, 116.3154950);
    //起点终点列表
    private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
    private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();

    private ProgressDialog mRouteCalculatorProgressDialog;// 路径规划过程显示状态

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplegps_navi);
        //语音播报开始
        TTSController.getInstance(this).startSpeaking();
        initView();

    }


    private void initView() {
        mStartPoints.add(mNaviStart);
        mEndPoints.add(mNaviEnd);
        mStartPointTextView = (TextView) findViewById(R.id.gps_start_position_textview);
        mEndPointTextView = (TextView) findViewById(R.id.gps_end_position_textview);

        mStartNaviButton = (Button) findViewById(R.id.gps_start_navi_button);
        mStartNaviButton.setOnClickListener(this);

        mStartPointTextView.setText(mNaviStart.getLatitude() + ","
                + mNaviStart.getLongitude());
        mEndPointTextView.setText(mNaviEnd.getLatitude() + ","
                + mNaviEnd.getLongitude());

        mRouteCalculatorProgressDialog = new ProgressDialog(this);
        mRouteCalculatorProgressDialog.setCancelable(true);

        AMapNavi.getInstance(this).setAMapNaviListener(this);
    }

    //--------------------------------点击事件------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gps_start_navi_button:
                AMapNavi.getInstance(this).calculateDriveRoute(mStartPoints,
                        mEndPoints, null, AMapNavi.DrivingDefault);
                mRouteCalculatorProgressDialog.show();
                break;
        }

    }

    //---------------------导航回调--------------------
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
        mRouteCalculatorProgressDialog.dismiss();

    }

    @Override
    public void onCalculateRouteSuccess() {
        mRouteCalculatorProgressDialog.dismiss();
        Intent intent = new Intent(GPSNaviActivity.this,
                NavigateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Bundle bundle = new Bundle();
        bundle.putInt(Utils.ACTIVITYINDEX, Utils.SIMPLEGPSNAVI);
        bundle.putBoolean(Utils.ISEMULATOR, false);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

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
    public void onNaviInfoUpdated(AMapNaviInfo arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReCalculateRouteForYaw() {
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

    //---------------------导航View事件回调-----------------------------
    @Override
    public void onNaviCancel() {


    }

    @Override
    public void onNaviMapMode(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNaviSetting() {
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

    //返回键处理事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(GPSNaviActivity.this,
                    SimpleNaviRouteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 删除导航监听

        AMapNavi.getInstance(this).removeAMapNaviListener(this);
    }

    @Override
    public void onLockMap(boolean arg0) {

        // TODO Auto-generated method stub

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo arg0) {

        // TODO Auto-generated method stub

    }

}
