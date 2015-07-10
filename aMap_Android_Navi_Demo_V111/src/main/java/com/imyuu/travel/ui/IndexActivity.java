package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.amap.api.maps.MapView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.base.LocationService;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FrescoFactory;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.util.PreferencesUtils;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class IndexActivity extends Activity {
    private SimpleDraweeView vp;
    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.index);
        vp = (SimpleDraweeView) findViewById(R.id.iv_startapp);
        String adverImage = PreferencesUtils.getString(IndexActivity.this, "ADVERT_IMAGE");
        if(adverImage != null)
        {
            String url = Config.IMAGE_SERVER_ADDR+adverImage;
            try {
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(url))
                    .setProgressiveRenderingEnabled(true)
                    .build();
            vp.setDrawingCacheEnabled(true);
            PipelineDraweeController controller =  (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(vp.getController())
                    .build();
            Log.d("INDEX",url);
            vp.setController(controller);
            }
            catch (Exception e)
            {
                vp.setImageResource(R.drawable.index);
            }
         }
        else
            vp.setImageResource(R.drawable.index);
        queryStartAdvert();
        loadServerAddress();
        Intent startLocationServiceIntent = new Intent(this,
                LocationService.class);
        startService(startLocationServiceIntent);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        boolean push =  PreferencesUtils.getSettingBoolean(getBaseContext(), "switch_message");
        LogUtil.d("push","result:"+push);
        if(push)
            mPushAgent.enable();
        else
            mPushAgent.disable();
        PushAgent.getInstance(this).onAppStart();
        processThread();
    }

    private void queryStartAdvert()
    {
        ApiClient.getSysService().queryImageServer(new Callback<Map<String, String>>() {
            @Override
            public void success(Map<String, String> map, Response response) {
                if (map != null) {
                    String imageServer = map.get("imageServer");
                    String tilesServer = map.get("tilesServer");
                    String mapServer = map.get("mapServer");
                    String oldmapServer = map.get("oldMapServer");
                    String advertImage = map.get("advertImage");
                    Log.d("IndexActivity",imageServer);
                    PreferencesUtils.putString(IndexActivity.this, "IMAGE_SERVER_ADDR", imageServer);
                    PreferencesUtils.putString(IndexActivity.this,"TILE_URL_ROOT",tilesServer);
                    PreferencesUtils.putString(IndexActivity.this,"MAP_SERVER_ADDR",mapServer);
                    PreferencesUtils.putString(IndexActivity.this,"ADVERT_IMAGE",advertImage);
                    Config.setMAP_SERVER_ADDR(mapServer);
                    Config.setTILE_URL_ROOT(tilesServer);
                    Config.setIMAGE_SERVER_ADDR(imageServer);
                    Config.OLDMAP_SERVER_ADDR = oldmapServer;
                    Config.START_ADVERT = advertImage;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void loadServerAddress()
    {
        String imageServer = PreferencesUtils.getString(IndexActivity.this,"IMAGE_SERVER_ADDR");
        if(imageServer != null) {
            Config.setIMAGE_SERVER_ADDR(imageServer);
            Config.setTILE_URL_ROOT(PreferencesUtils.getString(IndexActivity.this, "TILE_URL_ROOT"));
            Config.setMAP_SERVER_ADDR(PreferencesUtils.getString(IndexActivity.this, "MAP_SERVER_ADDR"));
            Config.ADVERT_IMAGE = PreferencesUtils.getString(IndexActivity.this, "ADVERT_IMAGE");
        }
    }
    private void processThread() {
        //构建一个下载进度条

        new Thread() {
            public void run() {
                //在这里执行长耗时方法
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException ie) {

                }
                //第一次安装启动app后保存flag到手机中，每次启动都检查flag，存在的话就跳过引导页
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

}
