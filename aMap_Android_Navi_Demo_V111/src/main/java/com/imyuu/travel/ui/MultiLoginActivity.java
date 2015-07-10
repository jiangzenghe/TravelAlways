package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.Food;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.util.AndroidUtils;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.HttpUtil;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.util.PreferencesUtils;
import com.imyuu.travel.util.ToastUtil;
import com.umeng.message.UmengRegistrar;
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

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MultiLoginActivity extends AppCompatActivity {
    private Activity mContext;
    private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_login);
        ButterKnife.inject(this);
        mContext = this;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister
        EventBus.getDefault().unregister(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multi_login2, menu);
        return true;
    }

    @OnClick(R.id.home_qq)
    public void qqClick(View v) {

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mContext, Config.qq_appId,
                 Config.qq_appSecret);
        qqSsoHandler.addToSocialSDK();

        doAuth( SHARE_MEDIA.QQ);

    }

    @OnClick(R.id.home_weixin)
    public void weixinkClick(View v) {
        UMWXHandler wxHandler = new UMWXHandler(mContext, Config.weixin_appId, Config.weixin_appSecret);
        wxHandler.addToSocialSDK();
        //check if the user had register,if not,jump to the regsiter page
        doAuth(SHARE_MEDIA.WEIXIN);
    }

    @OnClick(R.id.home_weibo)
    public void weiboClick(View v) {
        SinaSsoHandler  sinaSsoHandler = new SinaSsoHandler();
        sinaSsoHandler.setTargetUrl("http://open.weibo.com/apps/1294438134/info/advanced");
        mController.getConfig().setSsoHandler(sinaSsoHandler);
        //check if the user had register,if not,jump to the regsiter page
        doAuth(SHARE_MEDIA.SINA);
    }

    @OnClick(R.id.home_telno)
    public void telnoClick(View v) {
        Intent intent = new Intent(this, LoginTelnoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_chahao)
    public void sharingclose(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void doAuth(SHARE_MEDIA platform)
    {
        mController.doOauthVerify(mContext,platform,new SocializeListeners.UMAuthListener() {
            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
               e.printStackTrace();
            }
            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {

//                    final UserInfoJson userModel = new UserInfoJson();
//                    userModel.setPassword("000000");
//                    userModel.setTel(AndroidUtils.loadPhoneNumber(mContext));
//                    userModel.setSsoSource("3");
//                    userModel.setPlatform(0);
//                    if(SHARE_MEDIA.WEIXIN.equals(platform)) {
//                        final String access_token = value.getString("access_token");
//                        final String openid = value.getString("openid");
//                        userModel.setSsoSource("1");
//                        userModel.setSsoAccount(value.getString("uid"));
//                        userModel.setLoginName(userModel.getSsoAccount());
//                        // https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
//                        LogUtil.d("access_token",access_token+"   "+openid);
//                       //final String access_token2 = "0Qot3mlmxUSSOsWCk27hiK9MnDba6SCmZ8HH8Eus-Rn09n8z0IpAIn2uzxXuvTznAbZDAFQScFY2QJKf4YH-Y9X7eJrwAz9gJZKF27XUioA";
//                        final String uri = "https://api.weixin.qq.com/cgi-bin/user/info?";
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                UserInfoJson userInfoJson = HttpUtil.requestWeiXinUserInfo(uri,access_token,openid,userModel);
//                                EventBus.getDefault().post(userInfoJson);
//                            }
//                        }).start();
//
//                        return;
//                    }
//                    else
                    LogUtil.d("doAuth",platform.toString());
                         getPlatformInfo(value.getString("uid"),platform);
                   // Toast.makeText(mContext, "授权成功.",  Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(mContext, "授权失败",    Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancel(SHARE_MEDIA platform) {}
            @Override
            public void onStart(SHARE_MEDIA platform) {}
        });
    }

    public void onEventMainThread(UserInfoJson userModel) {
        LogUtil.d("onEventMainThread",userModel.toString());
       saveUserRegisterInfo(userModel);

    }
    private void getPlatformInfo(final String uid,final SHARE_MEDIA platform)
    {
        mController.getPlatformInfo(mContext, platform, new SocializeListeners.UMDataListener() {
            @Override
            public void onStart() {
                Toast.makeText(mContext, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onComplete(int status, Map<String, Object> info) {
                if(status == 200 && info != null){
                    StringBuilder sb = new StringBuilder();
                    Set<String> keys = info.keySet();
                    for(String key : keys){
                        sb.append(key+"="+info.get(key).toString()+"\r\n");
                    }
                    Log.d("GETPLatform",sb.toString());
                    final UserInfoJson userModel = new UserInfoJson();
                    userModel.setPassword("000000");
                    userModel.setTel(AndroidUtils.loadPhoneNumber(mContext));
                    userModel.setPlatform(0);

                    if(SHARE_MEDIA.WEIXIN.equals(platform)) {
                        userModel.setSsoSource("1");
                        userModel.setSsoAccount(info.get("openid").toString());
                        userModel.setLoginName(info.get("openid").toString());
                        if("1".equals(info.get("sex").toString()))
                            userModel.setGender("男");
                        else
                            userModel.setGender("女");
                        userModel.setImageUrl(info.get("headimgurl").toString());
                        userModel.setAddress(info.get("province").toString()+info.get("city").toString());
                        userModel.setNickName(info.get("nickname").toString());
                    }
                    else
                    if(SHARE_MEDIA.SINA.equals(platform)){
                        userModel.setSsoSource("2");
                        userModel.setNickName(info.get("screen_name").toString());
                        userModel.setAddress(info.get("location").toString());
                        String sex = info.get("gender").toString();
                        if("1".equals(sex))
                            userModel.setGender("男");
                        else
                            userModel.setGender("女");
                        userModel.setSsoAccount(info.get("uid").toString());
                        userModel.setLoginName(userModel.getSsoAccount());
                        userModel.setImageUrl(info.get("profile_image_url").toString());
//                        UMPlatformData platform = new UMPlatformData(UMedia.SINA_WEIBO, "user_id");
//                        platform.setWeiboId(userModel.getSsoAccount());  //optional
//                        MobclickAgent.onSocialEvent(this, platform);
                    }
                    else   if(SHARE_MEDIA.QQ.equals(platform))
                    {
                        userModel.setLoginName(uid);
                        userModel.setGender(info.get("gender").toString());
                        userModel.setNickName(info.get("screen_name").toString());
                        userModel.setAddress(info.get("province").toString()+info.get("city").toString());
                        userModel.setSsoSource("3");
                        userModel.setImageUrl(info.get("profile_image_url").toString());

                    }
                    saveUserRegisterInfo(userModel);
                }else{
                    Log.d("TestData","发生错误："+status);
                }
            }
        });
    }

    private void logout(SHARE_MEDIA platform)
    {
        mController.deleteOauth(mContext, platform,
                new SocializeListeners.SocializeClientListener() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onComplete(int status, SocializeEntity entity) {
                        if (status == 200) {
                            Toast.makeText(mContext, "删除成功.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "删除失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void saveUserRegisterInfo( final UserInfoJson userModel) {

        Log.d("MultiLogin",userModel.toString());
        String device_token = UmengRegistrar.getRegistrationId(this);
        if(userModel.getTel() == null)
            userModel.setTel("");
        userModel.setDesc(device_token);
        userModel.setLat(ApplicationHelper.getInstance().getLat());
        userModel.setLng(ApplicationHelper.getInstance().getLng());
        ApiClient.getUserService().userRegister(userModel,new Callback<ServiceState>() {
            @Override
            public void success(ServiceState state, Response response) {
                if(state.getStateCode()==0) {
                    ToastUtil.show(mContext, "登录成功" + state.getServiceMsg());
                    userModel.setUserId(state.getServiceMsg());
                  //save userinfo here
                    UserInfoJson.clear();
                    userModel.save();
                    ApplicationHelper.getInstance().refreshUserInfo(userModel.getLoginName());
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }else if(userModel.getSsoSource().equals("0"))
                    ToastUtil.show(mContext, "注册失败，登录名或电话已被使用");
            }

            @Override
            public void failure(RetrofitError error) {
                ToastUtil.show(mContext, "注册失败，请检查网络！");
            }
        });
    }
}
