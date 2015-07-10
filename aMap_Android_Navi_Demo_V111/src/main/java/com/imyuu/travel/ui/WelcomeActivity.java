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

import com.imyuu.travel.R;
import com.imyuu.travel.adapters.ViewPagerAdapter;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * $Author: Frank $
 * $Date: 2014/12/22 21:01 $
 *
 * @author Frank
 * @since 1.0
 */
public class WelcomeActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    //引导图片资源
    private static final int[] pics = {R.drawable.img_welcome_01,
            R.drawable.img_welcome_02, R.drawable.img_welcome_03,
            R.drawable.img_welcome_04};
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor editor;
    private Button buttonWelcomeStart;
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    //底部小店图片
    private ImageView[] dots;

    //记录当前选中位置
    private int currentIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome);
        //第一次安装启动app后保存flag到手机中，每次启动都检查flag，存在的话就跳过引导页
        mySharedPreferences = getSharedPreferences(Config.SHAREDPREFERENCES_NAME, 0);
        int flag = mySharedPreferences.getInt(Config.WELCOME_FLAG, 0);
        if (flag == Config.WELCOME_FLAG_READED) {
            Intent intent = new Intent();
            intent.setClass(this, IndexActivity.class);
            startActivity(intent);
            finish();
        } else {
            //因为只有安装后第一次启动app才会进入这个方法，所以在app卸载后SDCard中的旧的地图资源在这时候进行清除
            FileUtils.deleteDir(Environment.getExternalStorageDirectory() + "/" + Config.Map_FILEPATH);
            views = new ArrayList<View>();
            LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            //初始化引导图片列表
            for (int pic : pics) {
                ImageView iv = new ImageView(this);
                iv.setLayoutParams(mParams);
                iv.setBackgroundResource(pic);
                views.add(iv);
            }
            vp = (ViewPager) findViewById(R.id.viewpager);
            //初始化Adapter
            vpAdapter = new ViewPagerAdapter(views);
            vp.setAdapter(vpAdapter);
            //绑定回调
            vp.setOnPageChangeListener(this);
            buttonWelcomeStart = (Button) findViewById(R.id.button_welcome_start);
            buttonWelcomeStart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    editor = mySharedPreferences.edit();
                    editor.putInt(Config.WELCOME_FLAG, Config.WELCOME_FLAG_READED);
                    editor.apply();
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            //初始化底部小点
            initDots();

        }
    }

    private void initDots() {
        LinearLayout linearlayoutWelcomeDot = (LinearLayout) findViewById(R.id.linearlayout_welcome_dot);
//
        dots = new ImageView[pics.length];
//
//        //循环取得小点图片
//        for (int i = 0; i < pics.length; i++) {
//            dots[i] = (ImageView) linearlayoutWelcomeDot.getChildAt(i);
//            dots[i].setImageResource(R.drawable.img_dark_dot);//都设为灰色
//            dots[i].setOnClickListener(this);
//            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
//        }

        currentIndex = 0;
        // dots[currentIndex].setImageResource(R.drawable.img_white_dot);//设置为白色，即选中状态
        buttonWelcomeStart.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }

    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

//        dots[positon].setImageResource(R.drawable.img_white_dot);
//        dots[currentIndex].setImageResource(R.drawable.img_dark_dot);

        currentIndex = positon;
        if (currentIndex == dots.length - 1)
            buttonWelcomeStart.setVisibility(View.VISIBLE);
        else buttonWelcomeStart.setVisibility(View.INVISIBLE);
    }

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状态
        setCurDot(arg0);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }
}
