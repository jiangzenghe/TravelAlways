package com.imyuu.travel.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.imyuu.travel.R;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.PreferencesUtils;
import com.imyuu.travel.util.ToastUtil;
import com.imyuu.travel.view.SlipSwitch;


import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingTestActivity extends Activity {

     @InjectView(R.id.slip_switch_voice)
     SlipSwitch switch_voice;
     @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.inject(this);

//        switch_voice.setImageResource(R.drawable.m_316,R.drawable.m_316,R.drawable.switch_btn_slip);
//         switch_voice.setSwitchState(PreferencesUtils.getBoolean(getBaseContext(),"switch_voice"));
//
//
//        switch_voice.setOnSwitchListener(new SlipSwitch.OnSwitchListener() {
//            public void onSwitched(boolean flag)
//            {
//                PreferencesUtils.putBoolean(getBaseContext(),"switch_voice",flag);
//            }
//        });
    }




}
