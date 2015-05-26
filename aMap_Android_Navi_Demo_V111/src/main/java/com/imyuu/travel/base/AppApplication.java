package com.imyuu.travel.base;

import android.app.Activity;
import android.app.Application;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.maps.model.LatLng;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.util.ArrayList;

/**
 * Created by tule on 2015/4/27.
 */
public class AppApplication  extends Application {

    private ArrayList<Activity> activities = new ArrayList<Activity>();
    private static AppApplication instance;

    public void setMyLocation(LatLng myLocation) {
        this.myLocation = myLocation;
    }

    public LatLng getMyLocation() {
        return myLocation;
    }

    private LatLng myLocation = null;
    public AppApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        Fresco.initialize(this.getBaseContext());
        Log.d("IuuApplication", "init");
        instance = this;

        myLocation = new LatLng(36.487512,120.989576);

    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        Fresco.shutDown();
        ActiveAndroid.dispose();
    }
    //单例模式中获取唯一的MyApplication实例
    public static AppApplication getInstance() {
        if (null == instance) {
            instance = new AppApplication();
        }
        return instance;
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

}
