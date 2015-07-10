package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.imyuu.travel.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceProtocolActivity extends Activity {

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceprotocol);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitle("2323232");
//        setSupportActionBar(mToolbar);
        ButterKnife.inject(this);

        WebView mWebView = new WebView(this);


        mWebView.setVerticalScrollBarEnabled(false);


        ((LinearLayout)findViewById(R.id.txt_uuprotocol)).addView(mWebView);

        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setBackgroundColor(0);
        //mWebView.getBackground().setAlpha(0);
        String cc = getString(R.string.serviceprotcol);
        mWebView.loadData(cc.trim(), "text/html; charset=UTF-8", null);

        mWebView.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.image_protocl_cancel)
    public void adviceFeedbackClick(View v) {
        Log.d("MainActivity", "about adviceFeedbackClick click");
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }


}
