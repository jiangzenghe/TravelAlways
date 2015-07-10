package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.imyuu.travel.R;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.ui.UserProfilerResetActivity;
import com.imyuu.travel.util.AndroidUtils;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.BitmapUtils;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.FrescoFactory;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.util.ToastUtil;
import com.imyuu.travel.view.RoundImageView;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserProfilerActivity extends Activity {

    @InjectView(R.id.profiler_username)
    TextView userName;
    @InjectView(R.id.profiler_location)
    TextView profiler_location;
    @InjectView(R.id.profiler_age)
    TextView profiler_age;
    @InjectView(R.id.image_personal_home)
    com.facebook.drawee.view.SimpleDraweeView headPortrait;
    @InjectView(R.id.profiler_gender)
    ImageView profiler_gender;
    @InjectView(R.id.profiler_level_5)
    ImageView profiler_level_5;
    @InjectView(R.id.profiler_level_4)
    ImageView profiler_level_4;
    @InjectView(R.id.profiler_level_3)
    ImageView profiler_level_3;
    @InjectView(R.id.profiler_level_2)
    ImageView profiler_level_2;
    @InjectView(R.id.profiler_level_1)
    ImageView profiler_level_1;

    private UserInfoJson userInfoJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profiler);
        ButterKnife.inject(this);
        String url= null;
        if(ApplicationHelper.getInstance().getLoginUser() != null) {
            UserInfoJson userInfoJson = ApplicationHelper.getInstance().getLoginUser();
            LogUtil.d("loadHeadPortrait", userInfoJson.toString());
            if (!"0".equals(userInfoJson.getSsoSource())) {
                url = userInfoJson.getImageUrl();
                FrescoFactory.setDraweewithController(headPortrait, url);
            } else {
                if (FileUtils.isExist(Config.FACE_FILE_PATH + Config.FACE_FILE_NAME))
                    url = Config.FACE_FILE_PATH + Config.FACE_FILE_NAME;
                headPortrait.setImageURI(Uri.fromFile(new File(url)));
            }
        }
        userInfoJson = UserInfoJson.load();

        userName.setText(userInfoJson.getNickName());
        String birth = userInfoJson.getBirthday();
        int age = 0;
        if(birth != null && birth.length()>4)
            age= AndroidUtils.getAge(birth);
        profiler_age.setText(age+"岁");
        if("男".equals(userInfoJson.getGender()))
            profiler_gender.setImageResource(R.drawable.gender_man);
        else
        profiler_gender.setImageResource(R.drawable.gender_girl);
        profiler_location.setText(userInfoJson.getAddress());
        userInfoJson.setLevel("2");
        int level = Integer.parseInt(userInfoJson.getLevel());
        final ImageView[] imageview ={profiler_level_1,profiler_level_2,profiler_level_3,profiler_level_4,profiler_level_5};
        for(int i=0;i<level;i++)
        {
            imageview[i].setImageResource(R.drawable.userlevel_light);
        }
        for(int i=level;i<5;i++)
        {
            imageview[i].setImageResource(R.drawable.userlevel_dark);
        }
    }

    @OnClick(R.id.bt_setting)
    public void bt_settingClick(View v)
    {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.back_image)
    public void bt_cancelClick(View v)
    {
        this.finish();
    }



    @OnClick(R.id.profiler_update)
    public void profiler_updateClick(View v)
    {
        Intent intent = new Intent(this, UserProfilerEditActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.profiler_resetpassword)
    public void resetpasswordClick(View v)
    {
        Intent intent = new Intent(this, UserProfilerResetActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.profiler_logout)
    public void logoutClick(View v)
    {
        ApplicationHelper.getInstance().logOut();
        headPortrait.setImageBitmap(BitmapUtils.loadHeadPortrait(this));
        ToastUtil.show(getBaseContext(),"已退出登录");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
