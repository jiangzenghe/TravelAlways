package com.imyuu.travel.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.imyuu.travel.R;
import com.imyuu.travel.bean.City;
import com.imyuu.travel.ui.fragment.ScenicAreaFragment;
import com.imyuu.travel.ui.fragment.ScenicAreaSmallFragment;
import com.imyuu.travel.util.AndroidUtils;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.view.DrawableCenterButton;
import com.umeng.message.PushAgent;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements AMapLocationListener {
    ScenicAreaSmallFragment scenicAreaSmallFragment;
     private DrawerLayout mDrawerLayout;
    private DrawableCenterButton citytextView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;
    private boolean isSmall = false;
    private ScenicAreaFragment scenicAreaFragment;
    private LocationManagerProxy aMapLocManager;
//    private double lat;
//    private double lng;
    private String cityName;
    @InjectView(R.id.layout_aboutIUU)
    LinearLayout abountIUU;
    @InjectView(R.id.layout_suggest)
    LinearLayout layout_suggest;

    @InjectView(R.id.layout_protocol)
    LinearLayout layout_protocol;
    @InjectView(R.id.layout_servicetel)
    LinearLayout layout_servicetel;
    @InjectView(R.id.layout_problem)
    LinearLayout layout_problem;
    @InjectView(R.id.layout_function)
    LinearLayout  layout_function;
    @InjectView(R.id.et_phonenumber)
    TextView et_phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        PushAgent.getInstance(this).onAppStart();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.xiaotu:
                        LogUtil.v("切换视图");
                        replaceFragment(R.id.frame_container, new ScenicAreaSmallFragment());
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
        setSupportActionBar(mToolbar);


        initView();

        aMapLocManager = LocationManagerProxy.getInstance(this);
        aMapLocManager.requestLocationData("lbs", 2000L, 10F, this);
    }


    private void initView() {

        View actionbarLayout = LayoutInflater.from(this).inflate(
                R.layout.layout_actionbar, null);
        citytextView = (DrawableCenterButton)actionbarLayout.findViewById(R.id.text_choose_city);
        citytextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QueryCityActivity.class);
                intent.putExtra("curCity", cityName);
                startActivityForResult(intent,0);
            }
        });

        scenicAreaSmallFragment = new ScenicAreaSmallFragment();
        scenicAreaFragment = new ScenicAreaFragment();
        ActionBar mActionBar = getSupportActionBar();

        mActionBar.setDisplayHomeAsUpEnabled(true);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mActionBar.setCustomView(actionbarLayout, params);
        mActionBar.setDisplayShowCustomEnabled(true);
        //mActionBar.setBackgroundDrawable(R.drawable.actionbar_background);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name,
                R.string.hello_world);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        replaceFragment(R.id.frame_container, scenicAreaFragment);
        replaceFragment(R.id.drawer_view, new Fragment());
    }

    private void replaceFragment(int container, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {

        }
        switch (item.getItemId()) {
            case R.id.xiaotu:
                if (isSmall) {
                    replaceFragment(R.id.frame_container, scenicAreaFragment);
                    isSmall = false;
                } else {
                    replaceFragment(R.id.frame_container, scenicAreaSmallFragment);
                    isSmall = true;
                }
                break;
            case R.id.action_postion:
                Intent intent = new Intent(MainActivity.this, ClusterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true; // 记得返回true，否则无效
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
//            lat = aMapLocation.getLatitude();
//            lng = aMapLocation.getLongitude();
            cityName = aMapLocation.getCity();
            citytextView.setText(cityName);
            scenicAreaFragment.searchScenicInfo(cityName);
            stopLocation();
        }
    }

    private void stopLocation() {
        if (aMapLocManager != null) {
            aMapLocManager.removeUpdates(this);
            aMapLocManager.destroy();
            aMapLocManager = null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    //tv_aboutIUU
    @OnClick(R.id.layout_aboutIUU)
    public void aboutUUClick(View v) {
        Log.d("MainActivity","about IUU click");
        Intent intent = new Intent(MainActivity.this, AboutUUActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_protocol)
    public void aboutLicenseClick(View v) {
        Intent intent = new Intent(MainActivity.this, ServiceProtocolActivity.class);
        startActivity(intent);
        Log.d("MainActivity","about license click");

    }

    @OnClick(R.id.layout_servicetel)
    public void serviceTelClick(View v) {
        Log.d("MainActivity","about serviceTel click");
        showServiceTelDialog();
//        Intent intent = new Intent(MainActivity.this, ServiceTelActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.layout_function)
    public void functionIntroClick(View v) {
        Log.d("MainActivity","about functionIntro click");
        Intent intent = new Intent(MainActivity.this, FunctionIntroActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.layout_problem)
    public void aboutFaqClick(View v) {
        Log.d("MainActivity","about faq click");
        Intent intent = new Intent(MainActivity.this, FAQActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_suggest)
    public void adviceFeedbackClick(View v) {
        Log.d("MainActivity","about adviceFeedbackClick click");
        Intent intent = new Intent(MainActivity.this, AdviceFeedActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.drawer_view)
    public void containerClick(View v) {
       //consume event from blank area
    }

    public void dialTel()
    {
        String number = et_phonenumber.getText().toString();
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void showServiceTelDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.activity_service_tel);
        // 为确认按钮添加事件,执行退出应用操作
        Button bt_dialtel = (Button) window.findViewById(R.id.bt_dialtel);

        bt_dialtel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {

                String number = et_phonenumber.getText().toString();
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                startActivity(intent);

                dlg.cancel();
            }
        });
        Button bt_service_tel_cancel = (Button) window.findViewById(R.id.bt_service_tel_cancel);

        bt_service_tel_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return ;
        }
        //接受结束的字符串
        City city = (City)data.getExtras().getParcelable("cityResult");

        if(city!=null) {
            scenicAreaFragment.searchScenicInfo(city.getCityName());
            citytextView.setText(city.getCityName());
        }

    }
}
