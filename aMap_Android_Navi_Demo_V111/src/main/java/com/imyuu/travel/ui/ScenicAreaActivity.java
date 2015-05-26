package com.imyuu.travel.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import com.astuetz.PagerSlidingTabStrip;
import com.facebook.drawee.view.SimpleDraweeView;
import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.Recommend;
import com.imyuu.travel.model.ScenicDetailJson;
import com.imyuu.travel.ui.fragment.DetailFragments;
import com.imyuu.travel.ui.fragment.ScenicAreaFragment;
import com.imyuu.travel.ui.fragment.ScenicFoodFragment;
import com.imyuu.travel.ui.fragment.ScenicSummaryFragment;
import com.imyuu.travel.ui.fragment.ScenicTipsFragment;
import com.imyuu.travel.ui.fragment.ScenicTransportFragment;
import com.imyuu.travel.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by tule on 2015/4/28.
 */
public class ScenicAreaActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private  ViewPager viewPager;
    private  MyViewPagerAdaptor adaptor;
    private  ArrayList<Fragment> views;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenicarea);

        initView();

        LogUtil.v("ok");
    }

    private void initView() {
        final Intent intent =getIntent();
        String scenicId=intent.getStringExtra("scenicId");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);

        mToolbar.setTitle(intent.getStringExtra("scenicName"));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.v("点击返回了");
                Intent intent1 = new Intent(ScenicAreaActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        views = new ArrayList<>();

        DetailFragments fragment=new DetailFragments();

        views.add(fragment);
        views.add(new ScenicSummaryFragment());
        views.add(new ScenicTransportFragment());
        views.add(new ScenicTipsFragment());
        views.add(new ScenicFoodFragment());
        views.add(new ScenicFoodFragment());
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
    class MyViewPagerAdaptor extends FragmentPagerAdapter{
        private final String[] TITLES = { "详情", "简介", "交通",  "小贴士", "美食",
                "酒店" };

        public MyViewPagerAdaptor(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            if(views!=null)
                return views.get(position);
            return null;
        }

        @Override
        public int getCount() {
            if(views!=null)
                return views.size();
            return 0;
        }
    }
}
