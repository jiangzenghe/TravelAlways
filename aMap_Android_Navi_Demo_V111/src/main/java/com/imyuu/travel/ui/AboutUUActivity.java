package com.imyuu.travel.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.imyuu.travel.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUUActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_uu);

        ButterKnife.inject(this);

        WebView mWebView = new WebView(this);


        mWebView.setVerticalScrollBarEnabled(false);


        ((LinearLayout)findViewById(R.id.txt_uuintro)).addView(mWebView);

        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setBackgroundColor(0);
        //mWebView.getBackground().setAlpha(0);
        String cc = getString(R.string.aboutiuu);
        mWebView.loadData(cc.trim(), "text/html; charset=UTF-8", null);

        mWebView.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.image_cancel)
    public void adviceFeedbackClick(View v) {
        Log.d("MainActivity", "about adviceFeedbackClick click");
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }


}
