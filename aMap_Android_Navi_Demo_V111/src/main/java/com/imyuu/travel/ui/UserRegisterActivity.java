package com.imyuu.travel.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.api.StreamApiClient;
import com.imyuu.travel.model.Photo;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.ImageUtil;
import com.imyuu.travel.util.StringUtils;
import com.imyuu.travel.util.ToastUtil;

import com.imyuu.travel.view.RoundImageView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class UserRegisterActivity extends UserActivity {

    @InjectView(R.id.et_username)
    EditText et_username;
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.et_repassword)
    EditText et_repassword;
    @InjectView(R.id.bt_genderboy)
    Button bt_boy;
    @InjectView(R.id.bt_gendergirl)
    Button bt_girl;
    @InjectView(R.id.et_birthday)
    EditText et_birthday;
    @InjectView(R.id.image_headportrait)
    RoundImageView headPortrait;
    private Activity mActivity;
    private String telno;
    private String gender="男";
    UserInfoJson userModel = new UserInfoJson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        telno = getIntent().getStringExtra("register_telno");
         mActivity = this;
        ButterKnife.inject(this);
    }

    @Override
    public void loadPortrait(Bitmap bitmap)
    {
        headPortrait.setImageBitmap(bitmap);
    }

    @OnClick(R.id.bt_register_finish)
    public void registerFinishClick(View v) {


        if(StringUtils.isEmpty(et_username.getText().toString()))
        {
            ToastUtil.show(mActivity,"登录名不能为空");
            return;
        }
        if(StringUtils.isEmpty(et_password.getText().toString()))
        {
            ToastUtil.show(mActivity,"密码不能为空");
            return;
        }
        String password = et_password.getText().toString();
        String repws = et_repassword.getText().toString();
        if(!password.equals(repws))
        {
            ToastUtil.show(mActivity, "两次输入的密码必须相同");
            return;
        }

        userModel.setLoginName(et_username.getText().toString());
        userModel.setBirthday(et_birthday.getText().toString());
        userModel.setPassword(et_password.getText().toString());
        userModel.setTel(telno);
        userModel.setSsoSource("0");
        userModel.setPlatform(0);
        userModel.setGender(gender);
        userModel.setNickName(userModel.getLoginName());
        saveUserRegisterInfo();

    }

    private void saveUserRegisterInfo( ) {


        // userModel.setGender(bt_boy.);
        ApiClient.getUserService().userRegister(userModel,new Callback<ServiceState>() {
            @Override
            public void success(ServiceState state, Response response) {
                if(state.getStateCode()==0) {
                    ToastUtil.show(mActivity, "注册成功");
                    userModel.setUserId(state.getServiceMsg());
                    ApplicationHelper.getInstance().refreshUserInfo(userModel.getLoginName());
                    //save userinfo here
                    UserInfoJson.clear();
                    userModel.save();
                    Log.d("save user",userModel.toString());
                    if(uploadRequired)
                         uploadPortrait(userModel);

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }else
                    ToastUtil.show(mActivity, "注册失败，登录名或电话已被使用");
            }

            @Override
            public void failure(RetrofitError error) {
                ToastUtil.show(mActivity, "注册失败，请检查网络！");
            }
        });
    }


    @OnClick(R.id.to_smsdialog)
    public void cancelToSMS(View v)
    {
        this.finish();
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

    @OnClick(R.id.et_birthday)
    public void selectBirthClick(View v) {
        selectBirthClick();
    }
    @Override
    protected void setDate(String dateStr)
    {
        et_birthday.setText(dateStr);
    }
    @OnClick(R.id.image_captureportrait)
    public void takePhoto()
    {
        super.takePhoto();;
    }
    @OnClick(R.id.image_headportrait)
    public void setlectPhoto()
    {
       super.setlectPhoto();
    }
}
