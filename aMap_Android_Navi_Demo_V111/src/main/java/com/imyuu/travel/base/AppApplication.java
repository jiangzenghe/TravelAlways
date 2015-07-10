package com.imyuu.travel.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.amap.api.maps.model.LatLng;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by tule on 2015/4/27.
 */
public class AppApplication extends Application {

    private static AppApplication instance;
    private ArrayList<Activity> activities = new ArrayList<Activity>();
    private LatLng myLocation = null;

    public AppApplication() {
        instance = this;
    }

    //单例模式中获取唯一的MyApplication实例
    public static AppApplication getInstance() {
        if (null == instance) {
            instance = new AppApplication();
        }
        return instance;
    }

    public LatLng getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(LatLng myLocation) {
        this.myLocation = myLocation;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createDir();
        ActiveAndroid.initialize(this);

        Context context = getApplicationContext();
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder()//
                .setBaseDirectoryPath(new File(Config.UU_FILEPATH))
                .setBaseDirectoryName("cache")
                .setMaxCacheSize(1024*1024*100)
                .build();
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)//
                .setMainDiskCacheConfig(diskCacheConfig)
                .build();

//        ImagePipelineConfig config = ImagePipelineConfig.newBuilder()
//                .setBitmapMemoryCacheParamsSupplier(bitmapCacheParamsSupplier)
//                .setCacheKeyFactory(cacheKeyFactory)
//                .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)
//                .setExecutorSupplier(executorSupplier)
//                .setImageCacheStatsTracker(imageCacheStatsTracker)
//                .setMainDiskCacheConfig(mainDiskCacheConfig)
//                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
//                .setNetworkFetchProducer(networkFetchProducer)
//                .setPoolFactory(poolFactory)
//                .setProgressiveJpegConfig(progressiveJpegConfig)
//                .setRequestListeners(requestListeners)
//                .setSmallImageDiskCacheConfig(smallImageDiskCacheConfig)
//                .build();
//        Fresco.initialize(context, config);
        Fresco.initialize(context,imagePipelineConfig);
      //  Fresco.initialize(this.getBaseContext());

        instance = this;

        myLocation = new LatLng(36.487512, 120.989576);


        //saveUserInfo();
        Log.d("IuuApplication", "init");
    }

    private void createDir()
    {
        if(!FileUtils.isExist(Config.UU_FILEPATH))
            FileUtils.createDirectory(Config.UU_FILEPATH);
        if(!FileUtils.isExist(Config.Map_FILEPATH))
            FileUtils.createDirectory(Config.Map_FILEPATH);
        FileUtils.createNoMedia();
        com.umeng.socialize.utils.Log.LOG = true;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        Fresco.shutDown();
        ActiveAndroid.dispose();
    }

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void deleteActivity(Activity activity) {
        activities.remove(activity);
    }

    //finish
    public void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
        activities.clear();

    }

    private void saveUserInfo()
    {
        UserInfoJson         loginUser = new UserInfoJson();
        loginUser.setLoginName("jack jos");
        loginUser.setNickName("jack");
        loginUser.setGender("man");
        loginUser.setUserId("11111");
        loginUser.setPassword("394");
        loginUser.setAge(20);
        loginUser.setPlatform(0);
        loginUser.setUserId("4002");
        loginUser.setTel("15969658180");
        loginUser.save();
        Log.d("app",UserInfoJson.load().toString());
        UserInfoJson.load("15969658180");
    }
}
