package com.imyuu.travel.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserSharingModel;
import com.imyuu.travel.util.AndroidUtils;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.view.CustomShareBoard;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

import com.umeng.socialize.media.GooglePlusShareContent;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;

import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.TwitterShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;

import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import com.imyuu.travel.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SharingActivity extends AppCompatActivity {


    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(Config.DESCRIPTOR);
    private Activity mActivity;
    private String scenicId;
    private String scenciName;
    private String sharingImage = "http://www.imyuu.com:9100/images/social_uu.png";
    private  String shareContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        scenciName = (String)getIntent().getExtras().get("scenicName");
        scenicId = (String)getIntent().getExtras().get("scenicId");
        ButterKnife.inject(this);

        mActivity = this;
        shareContent = "IUU旅行让世界变小。我今天来"+scenciName+"玩了，景色非常不做，你还等什么呢。http://www.imyuu.com";
        mController.setShareContent(shareContent);
        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(mActivity,sharingImage     ));
        initUmeng();
    }

    public void initUmeng()
    {
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT,
                SHARE_MEDIA.EMAIL,
                SHARE_MEDIA.RENREN);
      //  mController.openShare(mActivity, false);
        // 配置需要分享的相关平台
        //configPlatforms();
        // 设置分享的内容
        //setShareContent();
        mController.registerListener(new SnsPostListener() {

            @Override
            public void onStart() {
                Toast.makeText(getActivity(), "share start...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public Activity getActivity()
    {
        return this;
    }

    @OnClick(R.id.image_sharingclose)
    public void sharingclose(View v) {
      this.finish();;
    }

    @OnClick(R.id.sharing_weixin)
    public void sharing_weixinClick(View v) {
       this.addWXPlatform();

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent
                .setShareContent(shareContent);
        weixinContent.setTitle("IUU旅行");
        weixinContent.setTargetUrl(Config.sharingTargetURL);
        UMImage urlImage = new UMImage(getActivity(),sharingImage          );
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

       this.performShare(SHARE_MEDIA.WEIXIN);
      //  initUmeng();
    }

    @OnClick(R.id.sharing_weixin_circle)
    public void sharing_weixin_cirlceClick(View v) {
        this.addWXPlatform();
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia
                .setShareContent(shareContent);
        circleMedia.setTitle("IUU旅行-朋友圈");
        //  circleMedia.setShareMedia(urlImage);
        // circleMedia.setShareMedia(uMusic);
        // circleMedia.setShareMedia(video);
        circleMedia.setTargetUrl(Config.sharingTargetURL);
        mController.setShareMedia(circleMedia);
        this.performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    @OnClick(R.id.sharing_tencentweibo)
    public void sharing_tencentweiboClick(View v) {
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        this.performShare(SHARE_MEDIA.TENCENT);
    }

    @OnClick(R.id.sharing_weibo)
    public void sharing_weiboClick(View v) {
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        //this.performShare(SHARE_MEDIA.SINA);
        directShare(SHARE_MEDIA.SINA);
    }

    @OnClick(R.id.sharing_email)
    public void sharing_emailClick(View v) {
        addEmail();
        UMImage localImage = new UMImage(getActivity(), R.drawable.icon_map_point);
        MailShareContent mail = new MailShareContent(localImage);
        mail.setTitle("IUU旅行");
        mail.setShareContent(shareContent);
        // 设置tencent分享内容
        mController.setShareMedia(mail);
        this.performShare(SHARE_MEDIA.EMAIL);
    }

    @OnClick(R.id.sharing_renren)
    public void sharing_renrenoClick(View v) {
        // 添加人人网SSO授权
        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(getActivity(),
                "476682", "fdc9262487ab48e3a0f86f3e392ffd0b",
                "e2634bf6c9ee4697a0d0f060cbb9c3af");
        mController.getConfig().setSsoHandler(renrenSsoHandler);
        // 设置renren分享内容
        RenrenShareContent renrenShareContent = new RenrenShareContent();
        renrenShareContent.setShareContent(shareContent);
        UMImage image = new UMImage(getActivity(),
                BitmapFactory.decodeResource(getResources(), R.drawable.img_main_logo));
        image.setTitle("IUU旅行");
        image.setThumb("http://www.imyuu.com:9100/images/social_uu.png");
        renrenShareContent.setShareImage(image);
        renrenShareContent.setAppWebSite("http://www.imyuu.com");
        mController.setShareMedia(renrenShareContent);
        this.performShare(SHARE_MEDIA.RENREN);
    }

    @OnClick(R.id.sharing_qq)
    public void sharing_qqClick(View v) {
        addQQPlatform();
        mController.setAppWebSite(SHARE_MEDIA.QQ, "http://www.imyuu.com");
        this.performShare(SHARE_MEDIA.QQ);
    }

    @OnClick(R.id.sharing_qqzone)
    public void sharing_qqzoneClick(View v) {
        addQQQZonePlatform();
        this.performShare(SHARE_MEDIA.QZONE);
    }

    /**
     * 调用postShare分享。跳转至分享编辑页，然后再分享。</br> [注意]<li>
     * 对于新浪，豆瓣，人人，腾讯微博跳转到分享编辑页，其他平台直接跳转到对应的客户端
     */
    private void postShare() {
        CustomShareBoard shareBoard = new CustomShareBoard(getActivity());
        shareBoard.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出；
     * 其它平台分别启动客户端分享</br>
     */
    private void directShare(SHARE_MEDIA platform) {

        mController.directShare(getActivity(), platform, new SnsPostListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = "分享成功";
                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享失败 [" + eCode + "]";
                }
                saveUserSharing(platform);

                Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserSharing(SHARE_MEDIA platform) {
        final UserSharingModel sharingModel = new UserSharingModel();
        sharingModel.setScenicId(scenicId);
        try {
            sharingModel.setUserId(ApplicationHelper.getInstance().getLoginUser().getUserId());
        }catch (Exception e)
        {
            LogUtil.d("usershare","user is not logged in yet");
        }
        sharingModel.setSharingPlatform(platform.name());
        sharingModel.setRemark(scenciName);

        ApiClient.getSocialService().sendSharing(sharingModel,new Callback<ServiceState>() {
            @Override
            public void success(ServiceState state, Response response) {
                Log.d("sendSharing", state.toString());
                sharingModel.setSharingTime(AndroidUtils.getLongDate());
                sharingModel.save();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    /**
     * 一键分享到多个已授权平台。</br>
     */
    private void shareMult() {
        SHARE_MEDIA[] platforms = new SHARE_MEDIA[] {
                SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.RENREN
        };
        mController.postShareMulti(getActivity(), new MulStatusListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(MultiStatus multiStatus, int st, SocializeEntity entity) {
                String showText = "分享结果：" + multiStatus.toString();
                Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
            }
        }, platforms);
    }


    /**
     * 添加Email平台</br>
     */
    private void addEmail() {
        // 添加email
        EmailHandler emailHandler = new EmailHandler();
        emailHandler.addToSocialSDK();
    }

    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mActivity, Config.weixin_appId, Config.weixin_appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mActivity, Config.weixin_appId, Config.weixin_appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
   }

    private void performShare(SHARE_MEDIA platform) {
        mController.postShare(mActivity, platform, new SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = platform.toString();
                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                    showText += "平台分享成功";
                } else {
                    showText += "平台分享失败";
                }
                saveUserSharing(platform);
                Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
                //dismiss();
            }
        });
    }
    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQQZonePlatform() {
        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), Config.qqSharingappId, Config.qqSharingKey);
        qZoneSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(shareContent);
        UMImage image = new UMImage(getActivity(),
                BitmapFactory.decodeResource(getResources(), R.drawable.img_main_logo));
        image.setTitle("IUU旅行");
        image.setThumb("http://www.imyuu.com:9100/images/social_uu.png");
        qqShareContent.setShareImage(image);
        qqShareContent.setAppWebSite("http://www.imyuu.com");
        qqShareContent.setTargetUrl(Config.sharingTargetURL);
        mController.setShareMedia(qqShareContent);
        mController.setAppWebSite(SHARE_MEDIA.QZONE, "http://www.imyuu.com");
    }

    private void addQQPlatform() {
       // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(),
                Config.qqSharingappId, Config.qqSharingKey);
        qqSsoHandler.setTargetUrl("http://www.imyuu.com");
        qqSsoHandler.setTitle("IUU旅行");

        qqSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(shareContent);
        UMImage image = new UMImage(getActivity(),
                BitmapFactory.decodeResource(getResources(), R.drawable.img_main_logo));
        image.setTitle("IUU旅行");
        image.setThumb("http://www.imyuu.com:9100/images/social_uu.png");
        qqShareContent.setShareImage(image);
        qqShareContent.setAppWebSite("http://www.imyuu.com");
        qqShareContent.setTargetUrl(Config.sharingTargetURL);
        mController.setShareMedia(qqShareContent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
