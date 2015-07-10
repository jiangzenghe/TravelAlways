package com.imyuu.travel.view;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.imyuu.travel.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;


/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 * 既支持自动轮播页面也支持手势滑动切换页面
 *
 * @author caizhiming
 */

public class SlideShowView extends FrameLayout {
    private Context context;
    //轮播图图片数量
    private final static int IMAGE_COUNT = 5;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 3000;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;
    //自定义轮播图的资源ID
    private int[] imagesResIds;
    //自定义轮播图View
    private ImageView[] imageViews;
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    //Handler
//    private Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            viewPager.setCurrentItem(currentItem);
//        }
//
//    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1) {
                        viewPager.setCurrentItem(0, true);
                    } else
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                    if (isAutoPlay) {
                        this.sendEmptyMessageDelayed(0, TIME_INTERVAL);
                    }
                    break;

                case 1:
                    if (isAutoPlay) {
                        this.sendEmptyMessageDelayed(0, TIME_INTERVAL);
                    }
                    break;
            }
        }
    };

    public SlideShowView(Context context) {
        this(context, null);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initData();
        initUI(context);
        if (isAutoPlay) {
            startPlay();
        }

    }

    @Override
    public void invalidate() {
        super.invalidate();
        initData();
        initUI(context);
        if (isAutoPlay) {
            startPlay();
        }
    }

    /**
     * 开始轮播图切换
     */
    private void startPlay() {
//        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        scheduledExecutorService.scheduleWithFixedDelay(new SlideShowTask(), 1, TIME_INTERVAL, TimeUnit.SECONDS);
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessage(1);
    }

    /**
     * 停止轮播图切换
     */
    private void stopPlay() {
//        scheduledExecutorService.shutdown();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 初始化相关Data
     */
    private void initData() {
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();
    }

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);
        if (imageViews != null) {
            for (ImageView imageView : imageViews) {
                imageView.setScaleType(ScaleType.FIT_XY);
                imageViewsList.add(imageView);
            }
        }
//        dotViewsList.add(findViewById(R.id.v_dot1));
//        dotViewsList.add(findViewById(R.id.v_dot2));
//        dotViewsList.add(findViewById(R.id.v_dot3));
//        dotViewsList.add(findViewById(R.id.v_dot4));
//        dotViewsList.add(findViewById(R.id.v_dot5));

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 填充ViewPager的页面适配器
     *
     * @author caizhiming
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View container, int position, Object object) {
            //((ViewPag.er)container).removeView((View)object);
            ((ViewPager) container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(View container, final int position) {
//            imageViews[position].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    System.out.println("========>>> 点击了viewpager的第 " + position
//                            + " 项");
//                }
//            });
            ((ViewPager) container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }

    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     *
     * @author caizhiming
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
//                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int pos) {
            currentItem = pos;
            for(int i=0;i < dotViewsList.size();i++){
                if(i == pos){
                    ((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.dian_default);
                }else {
                    ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.dian_select);
                }
            }
        }

    }

    /**
     * 执行轮播图切换任务
     *
     * @author caizhiming
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    /**
     * 销毁ImageView资源，回收内存
     *
     * @author caizhiming
     */
    private void destoryBitmaps() {
        if (imageViews != null)
            for (int i = 0; i < imageViews.length; i++) {
                ImageView imageView = imageViewsList.get(i);
                Drawable drawable = imageView.getDrawable();
                if (drawable != null) {
                    //解除drawable对view的引用
                    drawable.setCallback(null);
                }
            }
    }

    public void close() {
        stopPlay();
        destoryBitmaps();
    }

    public ImageView[] getImageViews() {
        return imageViews;
    }

    public void setImageViews(ImageView[] imageViews) {
        this.imageViews = imageViews;
    }
}
