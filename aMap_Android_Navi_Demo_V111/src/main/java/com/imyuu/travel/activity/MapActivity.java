package com.imyuu.travel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.navi.AMapNavi;
import com.imyuu.travel.R;
import com.imyuu.travel.model.RecommendLine;
import com.imyuu.travel.model.SpotInfo;
import com.imyuu.travel.view.ImageMapView;

public class MapActivity extends Activity {
    LatLngBounds bounds;
    LinearLayout serviceContainer;
    private MapView mMapView;
    private ImageMapView amp_view;
    private AMap mAMap;
    private AMapNavi mAMapNavi;
    private GroundOverlay groundoverlay;
    private TextView textMapTitle;
    private Button buttonMapMenuChoice;
    private Button buttonMapMenuService;
    private Button btn_warning;
    private boolean isSelectLine = false;
    private Bitmap bitmapPoint;
    private Bitmap bitmapScenic;
    private Bitmap bitmapVoice;
    private String scenicId;
    private RelativeLayout relativelayoutMapAdvert;
    private ImageView imageMapAdvertClose;
    //private SlideShowView slideshowviewAdvert;
    private LinearLayout linearlayoutMapLine;
    private LinearLayout layoutWarningTag;
    private HorizontalScrollView horizontalscrollviewMapLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_test);
        scenicId = "334";
        initView();
        initAMap(savedInstanceState);

    }

    private void initView() {

        mMapView = (MapView) findViewById(R.id.routemap);
        amp_view = new ImageMapView(this);//ImageMapView)findViewById(R.id.amp_view);
        textMapTitle = (TextView) findViewById(R.id.text_app_title);
        buttonMapMenuChoice = (Button) findViewById(R.id.button_map_menu_choice);
        buttonMapMenuService = (Button) findViewById(R.id.button_map_menu_service);
        linearlayoutMapLine = (LinearLayout) findViewById(R.id.linearlayout_map_line);
        btn_warning = (Button) findViewById(R.id.warning_tag);
        horizontalscrollviewMapLine = (HorizontalScrollView) findViewById(R.id.horizontalscrollview_map_line);
        serviceContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        layoutWarningTag = (LinearLayout) findViewById(R.id.layout_warning_tag);
        horizontalscrollviewMapLine.setVisibility(View.GONE);
        textMapTitle.setText("1111111111111111111");

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private void initAMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        amp_view.setUpMap(mAMap, "");


        amp_view.downloadRecommendLine(scenicId);

        btn_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btn = new Button(getBaseContext());
                btn.setText("警报0/0");
                //  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                //  lp.addRule(LinearLayout.ALIGN_PARENT_BOTTOM);
                //   lp.addRule(LinearLayout.RIGHT_OF,R.id.warning_tag);
                //  layoutWarningTag.addView(btn,lp);
            }
        });
        buttonMapMenuService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalscrollviewMapLine.setVisibility(View.GONE);
                Log.d("service state:", "-" + serviceContainer.getVisibility());
                if (serviceContainer.getVisibility() != View.VISIBLE)
                    serviceContainer.setVisibility(View.VISIBLE);
                else
                    serviceContainer.setVisibility(View.GONE);
            }
        });
        buttonMapMenuChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 加载路线名称
                serviceContainer.setVisibility(View.GONE);
                if (amp_view.getLineList() == null)
                    return;
                final String[] items = new String[amp_view.getLineList().size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = amp_view.getLineList().get(i).getLineName();
                }
                AlertDialog alertDialog = new AlertDialog.Builder(
                        MapActivity.this, R.style.dialog)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                isSelectLine = true;
                                loadMap(amp_view.getLineList().get(which).getLineId(), isSelectLine);
                            }
                        }).setNegativeButton(R.string.global_cancel, null)
                        .create();
                Window window = alertDialog.getWindow();
                window.setGravity(Gravity.BOTTOM); // window.setGravity(Gravity.TOP);

                alertDialog.show();
                // 设置对话框位置
                WindowManager.LayoutParams lp = window.getAttributes();
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                lp.width = dm.widthPixels / 2;
                window.setAttributes(lp);
            }
        });
    }

    // 加载地图方法
    private void loadMap(String lineId, boolean isSelectLine) {

        if (!TextUtils.isEmpty(lineId)) {

            if (this.amp_view.getLineList().size() > 0) {
                int index = 0;

                linearlayoutMapLine.removeAllViews();
                RecommendLine currentLine = null;
                for (RecommendLine recommendLine : this.amp_view.getLineList()) {
                    if (recommendLine.getLineId().equals(lineId)) {
                        currentLine = recommendLine;
                        break;
                    }
                }
                Log.d("currentLine:", currentLine.toString());
                LayoutInflater inflater = LayoutInflater.from(this);
                for (SpotInfo spotInfo : currentLine.getLineSectionList()) {
                    // 动态添加景点布局
                    LinearLayout linearLayoutMapLineItem = (LinearLayout)
                            inflater.inflate(R.layout.map_line_item, linearlayoutMapLine, false);
                    linearLayoutMapLineItem.setTag(spotInfo.getId());
                    TextView textMapLineTitle = (TextView) linearLayoutMapLineItem
                            .findViewById(R.id.text_map_line_title);
                    textMapLineTitle.setText(spotInfo.getSpotName());
                    ImageView imageMapLinePoint = (ImageView) linearLayoutMapLineItem
                            .findViewById(R.id.image_map_line_point);
                    Log.d("spotInfo:", spotInfo.toString());
                    // index等于0代表为线路上的第一个点
                    if (index == 0) {
                        imageMapLinePoint
                                .setImageResource(R.drawable.img_map_point_checked);
                        imageMapLinePoint
                                .setBackgroundResource(R.drawable.img_map_line_r_half);
//                        pointCur = changeXY(
//                                scenicMapModelA.getRelativeLongitude(),
//                                scenicMapModelA.getRelativeLatitude(),
//                                scenicModel.getScenicmapMaxy());
                    } else {
                        imageMapLinePoint
                                .setImageResource(R.drawable.img_map_point);
                    }
                    linearLayoutMapLineItem
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    for (int i = 0; i < linearlayoutMapLine
//                                            .getChildCount(); i++) {
//                                        View view = linearlayoutMapLine
//                                                .getChildAt(i);
//                                        if (view.getTag().toString()
//                                                .equals(v.getTag().toString())) {
//                                            ScenicMapModel scenicMapModel = scenicMapHashMap
//                                                    .get(v.getTag().toString());
//                                            if (scenicMapModel != null) {
//                                                ImageView imageMapLinePoint = (ImageView) v
//                                                        .findViewById(R.id.image_map_line_point);
//                                                imageMapLinePoint
//                                                        .setImageResource(R.drawable.img_map_point_checked);
//                                                Point pointFrom = pointCur;
//                                                pointCur = changeXY(
//                                                        scenicMapModel
//                                                                .getRelativeLongitude(),
//                                                        scenicMapModel
//                                                                .getRelativeLatitude(),
//                                                        scenicModel
//                                                                .getScenicmapMaxy());
//                                                imageMapMap.move(pointFrom,
//                                                        pointCur);
//
//                                                // imageMapMap.setLockAutoMoving(true);
//                                                if (pointCur != null) {
//                                                    imageMapMap
//                                                            .setPoint(pointCur);
//                                                    imageMapMap
//                                                            .moveCenter(pointCur);
//                                                }
//                                                imageMapMap.invalidate();
//                                            }
//                                        } else {
//                                            ImageView imageMapLinePoint = (ImageView) view
//                                                    .findViewById(R.id.image_map_line_point);
//                                            imageMapLinePoint
//                                                    .setImageResource(R.drawable.img_map_point);
//                                        }
//                                    }
                                }
                            });
//                    LinearLayout.LayoutParams lp;
//                    DisplayMetrics dm = new DisplayMetrics();
//                    getWindowManager().getDefaultDisplay().getMetrics(dm);
//                    // 判断手机分辨率，设定景点图标之间的距离
//                    if (dm.widthPixels <= 480 && dm.heightPixels <= 800) {
//                        lp = new LinearLayout.LayoutParams(130,
//                                LinearLayout.LayoutParams.MATCH_PARENT);
//                    } else {
//                        lp = new LinearLayout.LayoutParams(230,
//                                LinearLayout.LayoutParams.MATCH_PARENT);
//                    }
//                    lp.weight = 1;
//                    linearLayoutMapLineItem.setLayoutParams(lp);
                    linearlayoutMapLine.addView(linearLayoutMapLineItem);
                    index++;
                    // 当index等于路线list大小的时候，表示游标到了最后一个点

                    // 将路线中的所有坐标点按照顺序添加到list中


                }
            }

        }
        // 判断是否选过路线
        if (isSelectLine) {
            horizontalscrollviewMapLine.setVisibility(View.VISIBLE);
            Log.d("is select:", "horizontalscrollviewMapLine should show now");
        } else {
            // 第一次选择路线并加载地图
            horizontalscrollviewMapLine.setVisibility(View.GONE);
            bitmapPoint = BitmapFactory.decodeResource(getResources(),
                    R.drawable.icon_map_point);
            bitmapScenic = BitmapFactory.decodeResource(getResources(),
                    R.drawable.img_map_voice);
            bitmapVoice = BitmapFactory.decodeResource(getResources(),
                    R.drawable.img_map_voice_playing);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);


        }
    }

}
