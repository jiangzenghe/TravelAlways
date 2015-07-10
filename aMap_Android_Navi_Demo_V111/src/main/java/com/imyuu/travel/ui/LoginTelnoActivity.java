package com.imyuu.travel.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginTelnoActivity extends AppCompatActivity {

    @InjectView(R.id.et_login_telno)
    EditText login_name;
    @InjectView(R.id.et_login_password)
    EditText password;
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActivity = this;
        ButterKnife.inject(this);
    }

    @OnClick(R.id.chahao)
    public void bt_close()
    {
        this.finish();
    }
    @OnClick(R.id.bt_login_login)
    public void bt_loginClick(View v) {
        if(login_name.getText().length()<1)
        {
            ToastUtil.show(mActivity, "登录名不能为空");
            return;
        }
        if(password.getText().length()<1)
        {
            ToastUtil.show(mActivity, "密码不能为空");
            return;
        }
        final UserInfoJson userModel = new UserInfoJson();
        userModel.setLoginName(login_name.getText().toString().trim());
        userModel.setPassword(password.getText().toString().trim());
        userModel.setSsoSource("0");
        userModel.setLat(ApplicationHelper.getInstance().getLat());
        userModel.setLng(ApplicationHelper.getInstance().getLng());
        ApiClient.getUserService().userLogin(userModel,new Callback<UserInfoJson>() {
            @Override
            public void success(UserInfoJson user, Response response) {
                if(!"0".equals(user.getUserId())) {
                    ToastUtil.show(mActivity, "登录成功");
                    UserInfoJson.clear();
                    user.save();
                    ApplicationHelper.getInstance().refreshUserInfo(userModel.getLoginName());
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }else
                    ToastUtil.show(mActivity, "登录失败，用户名或密码错误");
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                ToastUtil.show(mActivity, "登录失败，系统错误");
            }
        });
    }

    @OnClick(R.id.tx_login_zhuce)
    public void registerClick(View v) {
        Intent intent = new Intent(getBaseContext(), LoginSMSActivity.class);
        startActivity(intent);
    }


}
