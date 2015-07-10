package com.imyuu.travel.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.SmsMessage;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginSMSActivity extends AppCompatActivity {

    @InjectView(R.id.et_sms_telno)
    EditText editTextTelno;

    @InjectView(R.id.et_sms_code)
    EditText editTextSmsCode;
    @InjectView(R.id.tx_sms_note)
    TextView tx_sms_note;
    private Activity mActivity;
    private String smsCode=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        mActivity = this;
        ButterKnife.inject(this);
        editTextSmsCode.setFocusable(false);
        editTextSmsCode.setFocusableInTouchMode(false);
    }

    @OnClick(R.id.bt_sms_request)
    public void requestSMSClick(View v) {

        if(editTextTelno.getText().length()<1)
        {
            ToastUtil.show(mActivity, "电话号码不能为空");
            return;
        }
        final String telno = editTextTelno.getText().toString();
        Log.d("sms",telno);
        SmsMessage  sms = new SmsMessage();
        sms.setTelno(telno);
        ApiClient.getUserService().requestVerifyCode(sms,new Callback<ServiceState>() {
            @Override
            public void success(ServiceState ss, Response response) {
                if(ss.getStateCode() ==0) {
                    smsCode = ss.getServiceMsg();
                    editTextSmsCode.setFocusable(true);
                    editTextSmsCode.setFocusableInTouchMode(true);
                    editTextSmsCode.requestFocus();
                    tx_sms_note.setText("验证码已通过短信形式发送到您的手机："+telno+",请注意查收。");
                    //ToastUtil.show(mActivity, "请求验证码成功，请注意查收");
                }
                else
                    ToastUtil.show(mActivity, "请求验证码失败，手机号码已注册");
            }

            @Override
            public void failure(RetrofitError error) {
                ToastUtil.show(mActivity, "请求验证码失败，系统错误");
                error.printStackTrace();
            }
        });
    }

    @OnClick(R.id.sms_pre_step)
    public void pre_stepClick(View v) {

        Intent intent = new Intent(getBaseContext(), MultiLoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.sms_next_step)
    public void pre_nextstepClick(View v) {

        Log.d("",smsCode +":"+ editTextTelno.getText().toString());
        if(smsCode == null)
        {
            ToastUtil.show(mActivity, "验证码不能为空");
            return;
        }
        if(smsCode != null && smsCode.equals(editTextSmsCode.getText().toString())) {
            Intent intent = new Intent(mActivity, UserRegisterActivity.class);
            intent.putExtra("register_telno",editTextTelno.getText().toString());
            startActivity(intent);
            return;
        }
        else
            ToastUtil.show(mActivity, "验证码不一致");
    }
}
