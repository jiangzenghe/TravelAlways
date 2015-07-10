package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.imyuu.travel.R;

import com.imyuu.travel.adapters.VerticalPagerAdapter;
import com.imyuu.travel.view.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FunctionIntroActivity extends Activity implements android.view.View.OnClickListener{

    class GridAdapter extends BaseAdapter
    {

        int dpos;

        public int getCount()
        {
            return count;
        }

        public Object getItem(int i)
        {
            return null;
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        public View getView(int i, View view, ViewGroup viewgroup)
        {
            ImageView imageview = new ImageView(FunctionIntroActivity.this);
            if (dpos == i)
            {
                imageview.setImageResource(R.drawable.dian_default);
            } else
            {
                imageview.setImageResource(R.drawable.dian_select);
            }
            imageview.setOnClickListener( new android.view.View.OnClickListener() {

                public void onClick(View view)
                {

                    verticalViewPager.setCurrentItem(focus);
                }

            });
            return imageview;
        }

        public void noticeData(int i)
        {
            dpos = i;
            notifyDataSetChanged();
        }


        GridAdapter()
        {

            super();
            dpos = 0;
        }
    }

    class MyOnPageChangeListener   implements VerticalViewPager.OnPageChangeListener
    {


        public void onPageScrollStateChanged(int i)
        {
        }

        public void onPageScrolled(int i, float f, int j)
        {
        }

        public void onPageSelected(int i)
        {
            focus = i;
            gridAdapter.noticeData(focus);
        }

        public MyOnPageChangeListener()
        {

            super();
        }
    }


    private int count;
    private int focus;
    GridAdapter gridAdapter;
    private List listViews;
    private ImageButton load_down;
    private ImageButton load_up;
    private VerticalViewPager verticalViewPager;

    public FunctionIntroActivity()
    {
        count = 3;
        focus = 0;
    }

    public void onClick(View view)
    {
        view.getId();

        if (focus > 0)
        {
            focus = -1 + focus;
            verticalViewPager.setCurrentItem(focus);

        }

        if (focus < 3)
        {
            focus = 1 + focus;
            verticalViewPager.setCurrentItem(focus);

        }

    }
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.loadpage);
        ButterKnife.inject(this);
        load_up = (ImageButton)findViewById(R.id.load_up);
        load_down = (ImageButton)findViewById(R.id.load_down);
        verticalViewPager = (VerticalViewPager)findViewById(R.id.verticalViewPager);
        listViews = new ArrayList();
        LayoutInflater layoutinflater = getLayoutInflater();
        listViews.add(layoutinflater.inflate(R.layout.lay1, null));
        listViews.add(layoutinflater.inflate(R.layout.lay2, null));
        listViews.add(layoutinflater.inflate(R.layout.lay3, null));
        listViews.add(layoutinflater.inflate(R.layout.lay4, null));
        listViews.add(layoutinflater.inflate(R.layout.lay5, null));
        listViews.add(layoutinflater.inflate(R.layout.lay6, null));
        listViews.add(layoutinflater.inflate(R.layout.lay7, null));
        gridAdapter = new GridAdapter();
        GridView gridview = new GridView(this);
        gridview.setSelector(new ColorDrawable(0));
        gridview.setNumColumns(1);
        gridview.setVerticalSpacing(40);
        gridview.setGravity(17);
        gridview.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(15, -2));
        gridview.setAdapter(gridAdapter);
        ((LinearLayout)findViewById(R.id.category_layout)).addView(gridview);
        verticalViewPager.setAdapter(new VerticalPagerAdapter(listViews));

        verticalViewPager.setCurrentItem(focus);
        verticalViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        load_up.setOnClickListener(this);
        load_down.setOnClickListener(this);
    }

    protected void onPause()
    {
        super.onPause();
//        MobclickAgent.onPageEnd("SplashScreen");
//        MobclickAgent.onPause(this);
    }

    protected void onResume()
    {
        super.onResume();
//        MobclickAgent.onPageStart("SplashScreen");
//        MobclickAgent.onResume(this);
    }



    @OnClick(R.id.image_funtioncancel)
    public void cancelClick(View v) {
       this.finish();
    }
}
