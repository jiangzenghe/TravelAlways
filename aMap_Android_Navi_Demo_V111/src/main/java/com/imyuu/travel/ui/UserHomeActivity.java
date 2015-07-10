package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.imyuu.travel.R;
import com.imyuu.travel.model.CommentsInfoJson;
import com.imyuu.travel.model.MapInfoModel;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.model.UserSharingModel;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.BitmapUtils;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.FrescoFactory;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.view.RoundImageView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.io.File;
import java.util.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserHomeActivity extends AppCompatActivity {


    @InjectView(R.id.tx_myhome_mapnum)
    TextView mapNum;
    @InjectView(R.id.tx_myhome_commentnum)
    TextView commentsNum;
    @InjectView(R.id.tx_myhome_tripnum)
    TextView  tripNum;
    @InjectView(R.id.tx_myhome_sharinggnum)
    TextView  sharingNum;
    @InjectView(R.id.image_personal_home)
    com.facebook.drawee.view.SimpleDraweeView headPortrait;
    @InjectView(R.id.bt_home)
    Button home;
    @InjectView(R.id.bt_message)
    Button message;
    @InjectView(R.id.bt_my)
    Button my;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        ButterKnife.inject(this);
       initView();
        int defaultImage =  R.drawable.img_sign_photo;
        String url= null;
        if(ApplicationHelper.getInstance().getLoginUser() != null) {
            UserInfoJson userInfoJson = ApplicationHelper.getInstance().getLoginUser();
            LogUtil.d("loadHeadPortrait", userInfoJson.toString());
            if (!"0".equals(userInfoJson.getSsoSource())) {
                url = userInfoJson.getImageUrl();
                FrescoFactory.setDraweewithController(headPortrait,url);
            } else {
                if (FileUtils.isExist(Config.FACE_FILE_PATH + Config.FACE_FILE_NAME))
                    url = Config.FACE_FILE_PATH + Config.FACE_FILE_NAME;
                headPortrait.setImageURI(Uri.fromFile(new File(url)));
            }
        }
//       else
//            headPortrait.setImageResource(defaultImage);
        home.setBackgroundResource(R.drawable.tab_home_drawable);
        my.setBackgroundResource(R.drawable.ic_tab_my_press);
        message.setBackgroundResource(R.drawable.ic_tab_message);
    }

    private void initView()
    {
        mapNum.setText(""+MapInfoModel.count());
        commentsNum.setText(""+CommentsInfoJson.count());
        sharingNum.setText(""+ UserSharingModel.count());
    }

    @OnClick(R.id.image_personal_home)
    public void image_personal_homeClick(View v)
    {
        if(UserInfoJson.load() != null)
        {
            Intent intent = new Intent(this, UserProfilerActivity.class);
            startActivity(intent);
            return;
        }

        Intent intent = new Intent(this, MultiLoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.bt_setting)
    public void setting_homeClick(View v)
    {
         Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.bt_home)
    public void main_homeClick(View v)
    {
        home.setBackgroundResource(R.drawable.tab_home_press);
        my.setBackgroundResource(R.drawable.ic_tab_message);
        message.setBackgroundResource(R.drawable.ic_tab_message);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.userhome_map)
    public void mapManagementClick(View v)
    {
        Intent intent = new Intent(this, MapManagementActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.userhome_comments)
    public void userCommentsClick(View v)
    {
        Intent intent = new Intent(this, UserCommentsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.userhome_sharing)
    public void userSharingClick(View v)
    {
        Intent intent = new Intent(this, UserSharingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.bt_message)
    public void messageClick(View v) {
        home.setBackgroundResource(R.drawable.ic_tab_home);
        my.setBackgroundResource(R.drawable.ic_tab_my);
        message.setBackgroundResource(R.drawable.ic_tab_message_press);
    }

    @OnClick(R.id.bt_my)
    public void myClick(View v) {
        home.setBackgroundResource(R.drawable.ic_tab_home);
        my.setBackgroundResource(R.drawable.ic_tab_my_press);
        message.setBackgroundResource(R.drawable.ic_tab_message);

    }
}
