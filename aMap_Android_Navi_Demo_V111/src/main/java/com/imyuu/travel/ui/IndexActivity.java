package com.imyuu.travel.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.maps.MapView;
import com.imyuu.travel.R;
import com.imyuu.travel.base.AMapHelper;
import com.imyuu.travel.base.LocationService;


/**
 * $Author: Frank $
 * $Date: 2014/12/22 21:01 $
 *
 * @author Frank
 * @since 1.0
 */
public class IndexActivity extends Activity  {
      private ImageView vp;
     private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.index);
        vp = (ImageView)findViewById(R.id.iv_startapp);
        vp.setImageResource(R.drawable.index);

        Intent startLocationServiceIntent = new Intent(this,
                LocationService.class);
        startService(startLocationServiceIntent);
        processThread();


    }
    private void processThread(){
        //构建一个下载进度条

        new Thread(){
            public void run(){
                //在这里执行长耗时方法
                try {
                    Thread.sleep(3 * 1000);
                }catch(InterruptedException ie)
                {

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
