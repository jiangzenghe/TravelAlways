package com.imyuu.travel.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;

import com.imyuu.travel.R;
import com.imyuu.travel.bean.RecommendLinesectionModel;
import com.imyuu.travel.bean.RecommendLinesectionguideModel;
import com.imyuu.travel.bean.ScenicAdvertOldModel;
import com.imyuu.travel.bean.ScenicMapOldModel;
import com.imyuu.travel.bean.ScenicOldModel;
import com.imyuu.travel.bean.ScenicRecommendLineModel;
import com.imyuu.travel.database.RecommendLinesectionDataHelper;
import com.imyuu.travel.database.RecommendLinesectionguideDataHelper;
import com.imyuu.travel.database.ScenicAdvertDataHelper;
import com.imyuu.travel.database.ScenicDataHelper;
import com.imyuu.travel.database.ScenicMapDataHelper;
import com.imyuu.travel.database.ScenicRecommendLineDataHelper;
import com.imyuu.travel.polites.GestureImageView;
import com.imyuu.travel.util.ConstantsOld;
import com.imyuu.travel.view.SlideShowView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * $Author: Frank $
 * $Date: 2014/12/28 17:24 $
 *
 * @author Frank
 * @since 1.0
 */
public class MapOldActivity extends Activity {
    private ImageView imageMapBack;
    private TextView textMapTitle;
    private GestureImageView imageMapMap;
    private ScenicOldModel scenicModel;
    private Button buttonMapMenuChoice;
    private MediaPlayer mediaPlayer;
    private boolean isSelectLine = false;
    private List<Point> points;
    private List<Point> pointsScenic;
    private List<Point> pointsVoice;
    private Point pointCur;
    private Bitmap bitmapPoint;
    private Bitmap bitmapScenic;
    private Bitmap bitmapVoice;
    private String scenicId;
    private ImageView[] imageViews;
    private RelativeLayout relativelayoutMapAdvert;
    private ImageView imageMapAdvertClose;
    private SlideShowView slideshowviewAdvert;
    private LinearLayout linearlayoutMapLine;
    private HashMap<String, ScenicMapOldModel> scenicMapHashMap;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private List<ScenicMapOldModel> scenicMapModelList;
    private List<ScenicMapOldModel> scenicMapModelHasVoiceList;
    private HorizontalScrollView horizontalscrollviewMapLine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        initView();
        Intent intent = getIntent();
        if (intent != null) {
//            scenicId = intent.getStringExtra(ConstantsOld.SCIENCE_ID_KEY);
            scenicId="221";
        }
        if (TextUtils.isEmpty(scenicId)) {
            Toast.makeText(MapOldActivity.this, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            ScenicDataHelper scenicDataHelper = new ScenicDataHelper(MapOldActivity.this);
            scenicModel = scenicDataHelper.getModelByScenicId(scenicId);
            scenicDataHelper.close();
            if (scenicModel == null) {
                Toast.makeText(MapOldActivity.this, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                textMapTitle.setText(scenicModel.getScenicName());
                ScenicAdvertDataHelper scenicAdvertDataHelper = new ScenicAdvertDataHelper(MapOldActivity.this);
                List<ScenicAdvertOldModel> scenicAdvertModelList = scenicAdvertDataHelper.getListByScenicId(scenicId);
                scenicAdvertDataHelper.close();
                //广告加载
                if (scenicAdvertModelList.size() > 0) {
                    relativelayoutMapAdvert.setVisibility(View.VISIBLE);
                    imageViews = new ImageView[scenicAdvertModelList.size()];
                    for (int i = 0; i < scenicAdvertModelList.size(); i++) {
                        ScenicAdvertOldModel scenicAdvertModel = scenicAdvertModelList.get(i);
                        //加载广告图片
                        imageViews[i] = new ImageView(MapOldActivity.this);
                        imageViews[i].setImageBitmap(BitmapFactory.decodeFile(ConstantsOld.SCENIC_ADVERT_FILE_PATH + scenicId + "/" + scenicAdvertModel.getAdvertPic()));
                        imageViews[i].setTag(scenicAdvertModel.getAdvertscenicId());
                        imageViews[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent intent = new Intent();
//                                intent.setClass(MapOldActivity.this, ScenicInfoActivity.class);
//                                intent.putExtra(ConstantsOld.SCIENCE_ID_KEY, v.getTag().toString());
//                                startActivity(intent);
//                                finish();
                            }
                        });
                    }
                    slideshowviewAdvert.setImageViews(imageViews);
                    slideshowviewAdvert.invalidate();
                } else {
                    relativelayoutMapAdvert.setVisibility(View.GONE);
                }
                loadMap(null, isSelectLine);
            }
        }
        imageMapBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();//停止播放
                    mediaPlayer.release();//释放资源
                    mediaPlayer = null;
                }
                finish();
            }
        });
        imageMapAdvertClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativelayoutMapAdvert.setVisibility(View.GONE);
            }
        });
        buttonMapMenuChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScenicRecommendLineDataHelper scenicRecommendLineDataHelper = new ScenicRecommendLineDataHelper(MapOldActivity.this);
                final ArrayList<ScenicRecommendLineModel> scenicRecommendLineModelArrayList = scenicRecommendLineDataHelper.getListByScenicId(scenicModel.getScenicId());
                scenicRecommendLineDataHelper.close();
                //加载路线名称
                final String[] items = new String[scenicRecommendLineModelArrayList.size()];
                for (int i = 0; i < scenicRecommendLineModelArrayList.size(); i++) {
                    items[i] = scenicRecommendLineModelArrayList.get(i).getRecommendRoutename();
                }
                AlertDialog alertDialog = new AlertDialog.Builder(MapOldActivity.this, R.style.dialog)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                isSelectLine = true;
                                ScenicRecommendLineModel scenicRecommendLineModel = scenicRecommendLineModelArrayList.get(which);
                                loadMap(scenicRecommendLineModel.getScenicRecommendLineId(), isSelectLine);
                            }
                        })
                        .setNegativeButton(R.string.global_cancel, null).create();
                Window window = alertDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);   //window.setGravity(Gravity.TOP);
                alertDialog.show();
                //设置对话框位置
                WindowManager.LayoutParams lp = window.getAttributes();
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                lp.width = dm.widthPixels;
                window.setAttributes(lp);
            }
        });

    }

    private void initView() {
        imageMapBack = (ImageView) findViewById(R.id.image_map_back);
        imageMapAdvertClose = (ImageView) findViewById(R.id.image_map_advert_close);
        imageMapMap = (GestureImageView) findViewById(R.id.image_map_map);
        textMapTitle = (TextView) findViewById(R.id.text_map_title);
        buttonMapMenuChoice = (Button) findViewById(R.id.button_map_menu_choice);
        relativelayoutMapAdvert = (RelativeLayout) findViewById(R.id.relativelayout_map_advert);
        linearlayoutMapLine = (LinearLayout) findViewById(R.id.linearlayout_map_line);
        slideshowviewAdvert = (SlideShowView) findViewById(R.id.slideshowview_advert);
        horizontalscrollviewMapLine = (HorizontalScrollView) findViewById(R.id.horizontalscrollview_map_line);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放地图图片资源
        if (imageMapMap != null) imageMapMap.setRecycle(true);
        //释放广告图片资源
        if (slideshowviewAdvert != null) slideshowviewAdvert.close();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();//停止播放
            mediaPlayer.release();//释放资源
            mediaPlayer = null;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String uri, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(uri, options);
    }

    /**
     * 创建本地MP3
     *
     * @return
     */
    public MediaPlayer createLocalMp3(String url) {
        /**
         * 创建音频文件的方法：
         * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
         * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
         *   mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
         */
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);//前提是sdcard卡要先导入音频文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    //加载地图方法
    private void loadMap(String lineId, boolean isSelectLine) {
        if (!TextUtils.isEmpty(lineId)) {
            ScenicMapDataHelper scenicMapDataHelper = new ScenicMapDataHelper(MapOldActivity.this);
            RecommendLinesectionDataHelper recommendLinesectionDataHelper = new RecommendLinesectionDataHelper(MapOldActivity.this);
            RecommendLinesectionguideDataHelper recommendLinesectionguideDataHelper = new RecommendLinesectionguideDataHelper(MapOldActivity.this);
            scenicMapModelList = scenicMapDataHelper.getListByScenicId(scenicId);
            pointsScenic = new ArrayList<Point>();
            scenicMapModelHasVoiceList = new ArrayList<ScenicMapOldModel>();
            //获取所有带有解说音频的景点
            for (ScenicMapOldModel scenicMapModel : scenicMapModelList) {
                if (!TextUtils.isEmpty(scenicMapModel.getScenicspotVoice())) {
                    pointsScenic.add(changeXY(scenicMapModel.getRelativeLongitude(), scenicMapModel.getRelativeLatitude(), scenicModel.getScenicmapMaxy()));
                    scenicMapModelHasVoiceList.add(scenicMapModel);
                }
            }
            List<RecommendLinesectionModel> recommendLinesectionModels = recommendLinesectionDataHelper.getListByRouteId(lineId);
            points = new ArrayList<Point>();
            if (recommendLinesectionModels.size() > 0) {
                int index = 0;
                scenicMapHashMap = new HashMap<String, ScenicMapOldModel>();
                linearlayoutMapLine.removeAllViews();
                for (RecommendLinesectionModel recommendLinesectionModel : recommendLinesectionModels) {
                    ScenicMapOldModel scenicMapModelA = scenicMapDataHelper.getModelById(recommendLinesectionModel.getAspotId());
                    ScenicMapOldModel scenicMapModelB = scenicMapDataHelper.getModelById(recommendLinesectionModel.getBspotId());
                    scenicMapHashMap.put(scenicMapModelA.getScenicMapId(), scenicMapModelA);
                    scenicMapHashMap.put(scenicMapModelB.getScenicMapId(), scenicMapModelB);
                    //动态添加景点布局
                    LinearLayout linearLayoutMapLineItem = (LinearLayout) this.getLayoutInflater().inflate(R.layout.map_line_item, null);
                    linearLayoutMapLineItem.setTag(scenicMapModelA.getScenicMapId());
                    TextView textMapLineTitle = (TextView) linearLayoutMapLineItem.findViewById(R.id.text_map_line_title);
                    textMapLineTitle.setText(scenicMapModelA.getScenicspotName());
                    ImageView imageMapLinePoint = (ImageView) linearLayoutMapLineItem.findViewById(R.id.image_map_line_point);
                    //index等于0代表为线路上的第一个点
                    if (index == 0) {
                        imageMapLinePoint.setImageResource(R.drawable.img_map_point_checked);
                        imageMapLinePoint.setBackgroundResource(R.drawable.img_map_line_r_half);
                        pointCur = changeXY(scenicMapModelA.getRelativeLongitude(), scenicMapModelA.getRelativeLatitude(), scenicModel.getScenicmapMaxy());
                    } else {
                        imageMapLinePoint.setImageResource(R.drawable.img_map_point);
                    }
                    linearLayoutMapLineItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < linearlayoutMapLine.getChildCount(); i++) {
                                View view = linearlayoutMapLine.getChildAt(i);
                                if (view.getTag().toString().equals(v.getTag().toString())) {
                                    ScenicMapOldModel scenicMapModel = scenicMapHashMap.get(v.getTag().toString());
                                    if (scenicMapModel != null) {
                                        ImageView imageMapLinePoint = (ImageView) v.findViewById(R.id.image_map_line_point);
                                        imageMapLinePoint.setImageResource(R.drawable.img_map_point_checked);
                                        Point pointFrom = pointCur;
                                        pointCur = changeXY(scenicMapModel.getRelativeLongitude(), scenicMapModel.getRelativeLatitude(), scenicModel.getScenicmapMaxy());
                                        imageMapMap.move(pointFrom, pointCur);
                                        if (pointCur != null) imageMapMap.setPoint(pointCur);
                                        imageMapMap.invalidate();
                                    }
                                } else {
                                    ImageView imageMapLinePoint = (ImageView) view.findViewById(R.id.image_map_line_point);
                                    imageMapLinePoint.setImageResource(R.drawable.img_map_point);
                                }
                            }
                        }
                    });
                    LinearLayout.LayoutParams lp;
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    //判断手机分辨率，设定景点图标之间的距离
                    if (dm.widthPixels <= 480 && dm.heightPixels <= 800) {
                        lp = new LinearLayout.LayoutParams(130, LinearLayout.LayoutParams.MATCH_PARENT);
                    } else {
                        lp = new LinearLayout.LayoutParams(230, LinearLayout.LayoutParams.MATCH_PARENT);
                    }
                    lp.weight = 1;
                    linearLayoutMapLineItem.setLayoutParams(lp);
                    linearlayoutMapLine.addView(linearLayoutMapLineItem);
                    index++;
                    //当index等于路线list大小的时候，表示游标到了最后一个点
                    if (index == recommendLinesectionModels.size()) {
                        linearLayoutMapLineItem = (LinearLayout) this.getLayoutInflater().inflate(R.layout.map_line_item, null);
                        linearLayoutMapLineItem.setTag(scenicMapModelB.getScenicMapId());
                        textMapLineTitle = (TextView) linearLayoutMapLineItem.findViewById(R.id.text_map_line_title);
                        textMapLineTitle.setText(scenicMapModelB.getScenicspotName());
                        imageMapLinePoint = (ImageView) linearLayoutMapLineItem.findViewById(R.id.image_map_line_point);
                        imageMapLinePoint.setImageResource(R.drawable.img_map_point);
                        imageMapLinePoint.setBackgroundResource(R.drawable.img_map_line_l_half);
                        linearLayoutMapLineItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0; i < linearlayoutMapLine.getChildCount(); i++) {
                                    View view = linearlayoutMapLine.getChildAt(i);
                                    if (view.getTag().toString().equals(v.getTag().toString())) {
                                        ScenicMapOldModel scenicMapModel = scenicMapHashMap.get(v.getTag().toString());
                                        if (scenicMapModel != null) {
                                            ImageView imageMapLinePoint = (ImageView) v.findViewById(R.id.image_map_line_point);
                                            imageMapLinePoint.setImageResource(R.drawable.img_map_point_checked);
                                            Point pointFrom = pointCur;
                                            pointCur = changeXY(scenicMapModel.getRelativeLongitude(), scenicMapModel.getRelativeLatitude(), scenicModel.getScenicmapMaxy());
                                            imageMapMap.move(pointFrom, pointCur);
                                            if (pointCur != null) imageMapMap.setPoint(pointCur);
                                            imageMapMap.invalidate();
                                        }
                                    } else {
                                        ImageView imageMapLinePoint = (ImageView) view.findViewById(R.id.image_map_line_point);
                                        imageMapLinePoint.setImageResource(R.drawable.img_map_point);
                                    }
                                }
                            }
                        });
                        if (dm.widthPixels <= 480 && dm.heightPixels <= 800) {
                            lp = new LinearLayout.LayoutParams(130, LinearLayout.LayoutParams.MATCH_PARENT);
                        } else {
                            lp = new LinearLayout.LayoutParams(230, LinearLayout.LayoutParams.MATCH_PARENT);
                        }
                        lp.weight = 1;
                        linearLayoutMapLineItem.setLayoutParams(lp);
                        linearlayoutMapLine.addView(linearLayoutMapLineItem);
                    }
                    //将路线中的所有坐标点按照顺序添加到list中
                    List<RecommendLinesectionguideModel> recommendLinesectionguideModelList = recommendLinesectionguideDataHelper.getListByDetailId(recommendLinesectionModel.getRecommendLinesectionId());
                    points.add(changeXY(scenicMapModelA.getRelativeLongitude(), scenicMapModelA.getRelativeLatitude(), scenicModel.getScenicmapMaxy()));
                    for (RecommendLinesectionguideModel recommendLinesectionguideModel : recommendLinesectionguideModelList) {
                        points.add(changeXY(recommendLinesectionguideModel.getRelativeLongitude(), recommendLinesectionguideModel.getRelativeLatitude(), scenicModel.getScenicmapMaxy()));
                    }
                    points.add(changeXY(scenicMapModelB.getRelativeLongitude(), scenicMapModelB.getRelativeLatitude(), scenicModel.getScenicmapMaxy()));

                }
            }
            scenicMapDataHelper.close();
            recommendLinesectionDataHelper.close();
            recommendLinesectionguideDataHelper.close();
        }
        //判断是否选过路线
        if (isSelectLine) {
            horizontalscrollviewMapLine.setVisibility(View.VISIBLE);
            if (points != null && points.size() > 0)
                imageMapMap.setPoints(points);
            if (pointsScenic != null && pointsScenic.size() > 0)
                imageMapMap.setPointsScenic(pointsScenic);
            if (pointCur != null) imageMapMap.setPoint(pointCur);
            if (pointsScenic != null && pointsScenic.size() > 0) {
                pointsVoice = new ArrayList<Point>();
                for (int i = 0; i < pointsScenic.size(); i++) {
                    //为景点语音解说设定触屏范围坐标
                    Point point = pointsScenic.get(i);
                    float x1 = imageMapMap.getImageX() - bitmapScenic.getWidth() / 2
                            - imageMapMap.getImageWidth() * imageMapMap.getScale() / 2 + point.x * imageMapMap.getScale()
                            + bitmapScenic.getWidth();
                    float x2 = imageMapMap.getImageX() - bitmapScenic.getWidth() / 2
                            - imageMapMap.getImageWidth() * imageMapMap.getScale() / 2 + point.x * imageMapMap.getScale();
                    float y1 = imageMapMap.getImageY() - bitmapScenic.getHeight()
                            - imageMapMap.getImageHeight() * imageMapMap.getScale() / 2 + point.y * imageMapMap.getScale()
                            + bitmapScenic.getHeight() + 65;
                    float y2 = imageMapMap.getImageY() - bitmapScenic.getHeight()
                            - imageMapMap.getImageHeight() * imageMapMap.getScale() / 2 + point.y * imageMapMap.getScale() + 60;
                    pointsVoice.add(new Point((int) x2, (int) y2));
                    pointsVoice.add(new Point((int) x2, (int) y1));
                    pointsVoice.add(new Point((int) x1, (int) y1));
                    pointsVoice.add(new Point((int) x1, (int) y2));
                    break;
                }
            }
            if (pointsVoice != null) imageMapMap.setPointsVoice(pointsVoice);
            imageMapMap.invalidate();
        } else {
            //第一次选择路线并加载地图
            horizontalscrollviewMapLine.setVisibility(View.GONE);
            bitmapPoint = BitmapFactory.decodeResource(getResources(), R.drawable.icon_map_point);
            bitmapScenic = BitmapFactory.decodeResource(getResources(), R.drawable.img_map_voice);
            bitmapVoice = BitmapFactory.decodeResource(getResources(), R.drawable.img_map_voice_playing);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            if (points != null && points.size() > 0)
                imageMapMap.setPoints(points);
            if (pointsScenic != null && pointsScenic.size() > 0)
                imageMapMap.setPointsScenic(pointsScenic);
            if (bitmapPoint != null) imageMapMap.setBitmap(bitmapPoint);
            if (bitmapScenic != null) imageMapMap.setBitmapScenic(bitmapScenic);
            if (bitmapVoice != null) imageMapMap.setBitmapVoice(bitmapVoice);
            if (pointCur != null) imageMapMap.setPoint(pointCur);
            //根据手机分辨率调整地图画线和坐标的偏移量
            if (dm.widthPixels <= 768 && dm.heightPixels <= 1280) {
                imageMapMap.setOff(21);
            }
            try {
                //地图资源优化处理
                Bitmap bitmap = decodeSampledBitmapFromResource(ConstantsOld.SCENIC_SINGLE_FILE_PATH + scenicId + "/" + scenicModel.getScenicMapurl(), scenicModel.getScenicmapMaxx(), scenicModel.getScenicmapMaxy());
                if (bitmap != null)
                    imageMapMap.setImageBitmap(bitmap);
                else {
                    Toast.makeText(MapOldActivity.this, R.string.map_loading_fail, Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (Exception e) {
                Toast.makeText(MapOldActivity.this, R.string.map_loading_fail, Toast.LENGTH_SHORT).show();
                finish();
            }
            imageMapMap.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    GestureImageView view = ((GestureImageView) v);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            float startX = event.getX();
                            float startY = event.getY();
                            if (pointsScenic != null)
                                for (int i = 0; i < pointsScenic.size(); i++) {
                                    Point point = pointsScenic.get(i);
                                    float x1 = view.getImageX() - bitmapScenic.getWidth() / 2
                                            - view.getImageWidth() * view.getScale() / 2 + point.x * view.getScale()
                                            + bitmapScenic.getWidth();
                                    float x2 = view.getImageX() - bitmapScenic.getWidth() / 2
                                            - view.getImageWidth() * view.getScale() / 2 + point.x * view.getScale();
                                    float y1 = view.getImageY() - bitmapScenic.getHeight()
                                            - view.getImageHeight() * view.getScale() / 2 + point.y * view.getScale()
                                            + bitmapScenic.getHeight() + 65;
                                    float y2 = view.getImageY() - bitmapScenic.getHeight()
                                            - view.getImageHeight() * view.getScale() / 2 + point.y * view.getScale() + 60;
                                    //判断点击的区域是否在某个景点的感触区域内
                                    if (startX <= x1 && startX >= x2 && startY <= y1 && startY >= y2) {
                                        boolean createState = false;
                                        //判断是否有音频正在播放，若没有则创建新的，若有则停止播放
                                        if (mediaPlayer == null) {
                                            mediaPlayer = createLocalMp3(ConstantsOld.SCENIC_SINGLE_FILE_PATH + scenicModel.getScenicId() + "/" + scenicMapModelHasVoiceList.get(i).getScenicspotVoice());
                                            createState = true;
                                        } else {
                                            try {
                                                if (mediaPlayer.isPlaying()) {
                                                    mediaPlayer.stop();//停止播放
                                                    mediaPlayer.release();//释放资源
                                                    mediaPlayer = null;
                                                    if (point != null) imageMapMap.setPointVoice(null);
                                                    imageMapMap.setPoint(point);
                                                    imageMapMap.invalidate();
                                                }
                                            } catch (Exception e) {
                                                mediaPlayer.release();//释放资源
                                                mediaPlayer = null;
                                                if (point != null) imageMapMap.setPointVoice(null);
                                                imageMapMap.setPoint(point);
                                                imageMapMap.invalidate();
                                            }
                                        }
                                        if (mediaPlayer != null) {
                                            //当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，
                                            //以便其他应用程序可以使用该资源:
                                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                @Override
                                                public void onCompletion(MediaPlayer mp) {
                                                    mp.release();//释放音频资源
                                                }
                                            });
                                            try {
                                                //在播放音频资源之前，必须调用Prepare方法完成些准备工作
                                                if (createState) mediaPlayer.prepare();
                                                //开始播放音频
                                                mediaPlayer.start();
                                                if (point != null) imageMapMap.setPointVoice(point);
                                                imageMapMap.setPoint(point);
                                                imageMapMap.invalidate();
                                            } catch (IllegalStateException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        break;
                                    }
                                }
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:

                            break;
                    }
                    return true;
                }
            });
        }
    }

    //坐标系转化方法
    private Point changeXY(double x, double y, int maxy) {
        int rx = (int) x;
        int ry = (int) (maxy - y);
        return new Point(rx, ry);
    }
}
