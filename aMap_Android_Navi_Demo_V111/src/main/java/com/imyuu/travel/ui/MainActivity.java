package com.imyuu.travel.ui;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.imyuu.travel.R;
import com.imyuu.travel.ui.fragment.ScenicAreaFragment;
import com.imyuu.travel.ui.fragment.ScenicAreaSmallFragment;
import com.imyuu.travel.util.AndroidUtils;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.LogUtil;


public class MainActivity extends AppCompatActivity   implements AMapLocationListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;
    private boolean isSmall =false;
    ScenicAreaSmallFragment  scenicAreaSmallFragment;
    private ScenicAreaFragment scenicAreaFragment;
    TextView textView;

    private LocationManagerProxy aMapLocManager;
    private double lat;
    private double lng;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
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
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams
                (getWindow().getWindowManager().getDefaultDisplay().getWidth() * 1 / 2,
                LinearLayout.LayoutParams.MATCH_PARENT));
        textView = new TextView(this);

        textView.setTextSize(18);
        textView.setWidth(AndroidUtils.dp2px(getApplicationContext(), 130));
        textView.setGravity(Gravity.RIGHT);
        linearLayout.setBackgroundColor(Color.RED);

        scenicAreaSmallFragment= new ScenicAreaSmallFragment();
        scenicAreaFragment=new ScenicAreaFragment();
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setCustomView(textView);
        mActionBar.setDisplayShowCustomEnabled(true); //enable it to display a
        mActionBar.setDisplayHomeAsUpEnabled(true);


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
        switch (item.getItemId()){
            case R.id.xiaotu:
                if(isSmall){
                    replaceFragment(R.id.frame_container, scenicAreaFragment);
                    isSmall  =false;
                }
                else {
                    replaceFragment(R.id.frame_container, scenicAreaSmallFragment);
                    isSmall  =true;
                }
                break;
            case R.id.action_postion:
                Intent intent = new Intent(MainActivity.this,ClusterActivity.class);
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
        if (aMapLocation != null)
        {
            lat = aMapLocation.getLatitude();
            lng = aMapLocation.getLongitude();
            cityName = aMapLocation.getCity();
            textView.setText(cityName);
            LogUtil.v("定位信息"+cityName);
            stopLocation();
        }
    }

    private void stopLocation()
    {
        if (aMapLocManager != null)
        {
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
}
