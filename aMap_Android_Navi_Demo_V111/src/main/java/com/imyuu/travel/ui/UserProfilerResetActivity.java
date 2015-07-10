package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.StringUtils;
import com.imyuu.travel.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserProfilerResetActivity extends ActionBarActivity {

   @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.et_repassword)
    EditText et_repassword;

      private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profiler_reset);
        mActivity = this;
        ButterKnife.inject(this);
    }

    @OnClick(R.id.bt_register_finish)
    public void registerFinishClick(View v) {

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

       final UserInfoJson userModel =  UserInfoJson.load();
        String userId = userModel.getUserId();
        userModel.setPassword(password);
        // userModel.setGender(bt_boy.);
        ApiClient.getUserService().modifyPassword(userId,password,new Callback<ServiceState>() {
            @Override
            public void success(ServiceState state, Response response) {
                if(state.getStateCode()==0) {
                    ToastUtil.show(mActivity, "更新密码成功"+state.getServiceMsg());
                    //save userinfo here
                     userModel.save();
                    ApplicationHelper.getInstance().refreshUserInfo(userModel.getLoginName());

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }else
                    ToastUtil.show(mActivity, "更新密码失败");
            }

            @Override
            public void failure(RetrofitError error) {
                ToastUtil.show(mActivity, "注册失败，请检查网络！");
            }
        });

    }

    @OnClick(R.id.to_smsdialog)
    public void cancelClick(View view)
    {
        this.finish();
    }
}
