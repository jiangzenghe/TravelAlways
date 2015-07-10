package com.imyuu.travel.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.imyuu.travel.R;
import com.imyuu.travel.ui.fragment.DetailFragments;
import com.imyuu.travel.ui.fragment.ScenicFoodFragment;
import com.imyuu.travel.ui.fragment.ScenicHotelFragment;
import com.imyuu.travel.ui.fragment.ScenicSummaryFragment;
import com.imyuu.travel.ui.fragment.ScenicTipsFragment;
import com.imyuu.travel.ui.fragment.ScenicTransportFragment;
import com.imyuu.travel.util.LogUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by tule on 2015/4/28.
 */
public class ScenicAreaActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdaptor adaptor;
    private ArrayList<Fragment> views;
    private String scenicId = null;
    private String scenicName = null;
    @InjectView(R.id.txt_areaname)
    TextView txt_areaname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenicarea);
        ButterKnife.inject(this);
        initView();

        LogUtil.v("ok");
    }

    private void initView() {
        final Intent intent = getIntent();
        scenicId = intent.getStringExtra("scenicId");
        scenicName = intent.getStringExtra("scenicName");
        txt_areaname.setText(scenicName);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        views = new ArrayList<>();

        DetailFragments fragment = new DetailFragments();

        views.add(fragment);
        views.add(new ScenicSummaryFragment());
        views.add(new ScenicTransportFragment());
        views.add(new ScenicTipsFragment());
        views.add(new ScenicFoodFragment());
        views.add(new ScenicHotelFragment());
        adaptor = new MyViewPagerAdaptor(getSupportFragmentManager());
        viewPager.setAdapter(adaptor);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        tabs.setViewPager(viewPager);
    }

    /**
     * ViewPagerIndex adaptor
     */
    class MyViewPagerAdaptor extends FragmentPagerAdapter {
        private final String[] TITLES = {"详情", "简介", "交通", "小贴士", "美食",
                "酒店"};

        public MyViewPagerAdaptor(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            if (views != null)
                return views.get(position);
            return null;
        }

        @Override
        public int getCount() {
            if (views != null)
                return views.size();
            return 0;
        }
    }


    @OnClick(R.id.image_area_cancel)
    public void adviceFeedbackClick(View v) {
        Log.d("MainActivity", "about adviceFeedbackClick click");
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.image_area_sharing)
    public void areaSharingClick(View v) {
        Log.d("MainActivity", "about adviceFeedbackClick click");
        Intent intent = new Intent(getBaseContext(), SharingActivity.class);
        intent.putExtra("scenicId",scenicId);
        intent.putExtra("scenicName",scenicName);

        startActivity(intent);
    }
}
