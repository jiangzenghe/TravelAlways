package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.MessageJson;
import com.imyuu.travel.model.ServiceState;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AdviceFeedActivity extends Activity {

    @InjectView(R.id.tb_adviceconfirm)
    Button confirmBtn;
    @InjectView(R.id.edit_advice)
    EditText editAdvice;
    private Activity mActivity;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_feed);
        ButterKnife.inject(this);

        editAdvice.setFocusable(true);
        editAdvice.setFocusableInTouchMode(true);
        editAdvice.requestFocus();
        mActivity = this;
    }
    @OnClick(R.id.image_advicancel)
    public void cancelClick(View v) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.edit_advice)
    public void clearEditTextClick(View v) {
        editAdvice.clearComposingText();
    }

    @OnClick(R.id.tb_adviceconfirm)
    public void adviceFeedbackClick(View v) {
        String content = editAdvice.getText().toString();
        MessageJson  messageJson = new MessageJson();
        messageJson.setMessage(content);

        ApiClient.getSysService().sendSysMessage(messageJson,new Callback<ServiceState>() {
            @Override
            public void success(ServiceState ss, Response response) {

                Toast.makeText(mActivity, "反馈意见成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                retrofitError.printStackTrace();
                Toast.makeText(mActivity, "反馈意见失败", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }


}
