package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.imyuu.travel.R;

import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.api.StreamApiClient;
import com.imyuu.travel.model.Photo;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.BitmapUtils;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.FrescoFactory;
import com.imyuu.travel.util.ImageUtil;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.util.StringUtils;
import com.imyuu.travel.util.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import android.provider.MediaStore;
import android.content.ContentResolver;
import android.widget.Toast;

public class UserProfilerEditActivity extends UserActivity {
    @InjectView(R.id.et_username)
    EditText et_username;
    @InjectView(R.id.et_location)
    EditText location;

    @InjectView(R.id.bt_genderboy)
    Button bt_boy;
    @InjectView(R.id.bt_gendergirl)
    Button bt_girl;
    @InjectView(R.id.et_birthday)
    EditText et_birthday;
    @InjectView(R.id.image_headportrait)
    com.facebook.drawee.view.SimpleDraweeView headPortrait;
    private String telno;
    private UserInfoJson userInfoJson = null;
    private String gender="男";
    private Context mActivity;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profiler_edit);
        mActivity = this;
        ButterKnife.inject(this);
        userInfoJson = UserInfoJson.load();

        et_username.setText(userInfoJson.getLoginName());
        et_birthday.setText(userInfoJson.getBirthday());
        location.setText(userInfoJson.getAddress());
        telno = userInfoJson.getTel();
        if(!gender.equals(userInfoJson.getGender()))
        {
            bt_girl.setBackgroundColor(Color.GREEN);
            bt_boy.setBackgroundColor(Color.WHITE);
            gender="女";
        }

        this.loadHeadPortrait();
    }

    @OnClick(R.id.bt_genderboy)
    public void boyClick(View v)
    {
        gender="男";
        bt_boy.setBackgroundColor(Color.GREEN);
        bt_girl.setBackgroundColor(Color.WHITE);
    }

    @OnClick(R.id.bt_gendergirl)
    public void girlClick(View v)
    {
        gender="女";
        bt_girl.setBackgroundColor(Color.GREEN);
        bt_boy.setBackgroundColor(Color.WHITE);
    }

    @OnClick(R.id.image_captureportrait)
    public void takePhoto()
    {
       super.takePhoto();
    }
    @OnClick(R.id.image_headportrait)
    public void setlectPhoto()
    {
      super.setlectPhoto();
    }


    private void loadHeadPortrait() {
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
    }

    @OnClick(R.id.bt_register_finish)
    public void updateUserProfilerClick(View v) {

        if(StringUtils.isEmpty(et_username.getText().toString()))
        {
            ToastUtil.show(mActivity, "登录名不能为空");
            return;
        }
        if(StringUtils.isEmpty(location.getText().toString()))
        {
            ToastUtil.show(mActivity, "居住地不能为空");
            return;
        }
        if(StringUtils.isEmpty(et_birthday.getText().toString()))
        {
            ToastUtil.show(mActivity, "生日不能为空");
            return;
        }
        userInfoJson.setLoginName(et_username.getText().toString());
        userInfoJson.setBirthday(et_birthday.getText().toString());
        userInfoJson.setGender(gender);
        userInfoJson.setAddress(location.getText().toString());
         // userModel.setGender(bt_boy.);
        ApiClient.getUserService().updateUserProfiler(userInfoJson,new Callback<ServiceState>() {
            @Override
            public void success(ServiceState state, Response response) {
                if(state.getStateCode()==0) {
                    ToastUtil.show(mActivity, "更新信息成功");
                 //save userinfo here
                    userInfoJson.save();
                    ApplicationHelper.getInstance().refreshUserInfo(userInfoJson.getLoginName());
                    Intent intent = new Intent(getBaseContext(), UserHomeActivity.class);
                    startActivity(intent);
                }else
                    ToastUtil.show(mActivity, "更新信息失败，登录名可能已被使用");
            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();
                ToastUtil.show(mActivity, "更新信息失败，请检查网络！");
            }
        });

    }
    @Override
    public void loadPortrait(Bitmap bitmap)
    {
        headPortrait.setImageBitmap(bitmap);
        uploadPortrait(userInfoJson);
    }
    @OnClick(R.id.et_birthday)
    public void selectBirthClick(View v) {
        selectBirthClick();
    }
    @Override
    protected void setDate(String dateStr)
    {
        et_birthday.setText(dateStr);
    }


    @OnClick(R.id.to_smsdialog)
    public void cancelClick(View view)
    {
        this.finish();
    }

}
