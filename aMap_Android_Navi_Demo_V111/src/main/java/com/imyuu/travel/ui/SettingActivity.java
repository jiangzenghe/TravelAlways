package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.imyuu.travel.R;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.PreferencesUtils;
import com.imyuu.travel.util.ToastUtil;
import android.widget.ToggleButton;
import com.imyuu.travel.view.SlipSwitch;
import com.umeng.message.PushAgent;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingActivity extends Activity {

    @InjectView(R.id.slip_switch_message)
    ToggleButton  switch_message;
    @InjectView(R.id.slip_switch_voice)
    ToggleButton switch_voice;
    @InjectView(R.id.cachesize)
    TextView cachesize;
    private Context context;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        context = this;
        switch_voice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.putBoolean(getBaseContext(), "switch_voice", isChecked);

            }
        });
        long filesize = FileUtils.getDirSize(new File(Config.Map_FILEPATH));
        filesize += FileUtils.getDirSize(new File(Config.CACHE_DIR));
        cachesize.setText("当前缓存:"+FileUtils.formatFileSize(filesize));

        switch_message.setChecked(PreferencesUtils.getSettingBoolean(getBaseContext(),"switch_message"));
        switch_voice.setChecked(PreferencesUtils.getSettingBoolean(getBaseContext(),"switch_voice"));
        switch_message.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.putBoolean(getBaseContext(), "switch_message", isChecked);
                PushAgent mPushAgent = PushAgent.getInstance(context);
                if(isChecked)
                     mPushAgent.enable();
                else
                    mPushAgent.disable();
            }
        });

    }



    @OnClick(R.id.setting_cancel)
    public void cancelClick(View v)
    {
         this.finish();
    }

    @OnClick(R.id.cleancache)
    public void tv_cache_homeClick(View v)
    {
        FileUtils.deleteDir(Config.Map_FILEPATH);
        FileUtils.deleteDir(Config.CACHE_DIR);
        cachesize.setText("当前缓存:0MB");
        ToastUtil.show(getBaseContext(),"缓存清理成功");
    }

}
