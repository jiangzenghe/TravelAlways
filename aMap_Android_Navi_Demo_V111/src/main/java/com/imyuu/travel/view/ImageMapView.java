package com.imyuu.travel.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.UrlTileProvider;

import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.base.AppApplication;
import com.imyuu.travel.bean.AiderByZipMap;
import com.imyuu.travel.model.RecommendLine;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.model.ScenicPointJson;
import com.imyuu.travel.model.SpotInfo;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.CustomTileProvider;
import com.imyuu.travel.util.GpsUtil;
import com.imyuu.travel.util.MyUrlTileProvider;
import com.imyuu.travel.util.SpotDataSorter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import com.imyuu.travel.R;

public class ImageMapView extends RelativeLayout implements LocationSource, AMapLocationListener,
        com.amap.api.maps.AMap.InfoWindowAdapter,
        android.view.View.OnClickListener,
        android.widget.AdapterView.OnItemClickListener,
        com.amap.api.maps.AMap.OnMapClickListener {
    public boolean isAuxiliary;
    public boolean isShowText;
    public boolean itemClick;
    //private AttractionPtd ptd;
    //ClusterOverlay clusterOverlay;
    android.widget.LinearLayout.LayoutParams lp2;
    android.widget.LinearLayout.LayoutParams lp3;
    PopupWindow popup_control;
    View root_control;
    ScenicAreaJson scenicArea;
    private AMap aMap;
    private TileOverlay tileOverlay;
    private String tileOverlayurl;
    private boolean IsOnLine;
    private String TAG;
    //  private MapGroupListAdapter adapter;
    private AiderByZipMap aiderByZipMap;
    private View aiderInfoWindow;
    private AppApplication app;
    // public ArcMenu arcMenu;
    private View attractionInfoWindow;
    private View attractionListInfoWindow;
    // private ChidMapListAdatper chidMapListAdatper;
    private ListView childs_attraction_listview;
    private Marker chooseMarker;
    private TextView close_group_view;
    private ImageButton close_window;
    private int clusterRadius;
    private Context context;
    private TextView distance_text;
    private ApplicationHelper helper;
    private ImageView huo_image;
    private String imageid;
    private boolean isAutoplay;
    private boolean isHandLocation;
    private RelativeLayout jd_layout;
    private String keywordvalue;
    private double lat;
    private double lng;
    private LoadView loadView;
    private LocationManagerProxy mAMapLocationManager;
    private com.amap.api.maps.LocationSource.OnLocationChangedListener mListener;
    private ExpandableListView map_listview;
    private List mclustlist;
    private MyExpBtn myExpBtn;
    private MyPlayBtn myPlayBtn;
    private LatLng point;
    private LinearLayout pop_content_layout;
    private TextView popupIntroduceText;
    private TextView popupText;
    private LinearLayout show_shareview;
    private List<ScenicPointJson> sortByDisData;
    private List<ScenicPointJson> spotList;
    private List<RecommendLine> lineList;
    private SharedPreferences sp;
    private TextView touch_misss;
    private TextView touch_misss_above;
    private TextView tourname_bottomview;

    private SlipSwitch swithch_map_shdt;
    private SlipSwitch swithch_map_xswz;
    private SlipSwitch swithch_map_zdjj;
    private List clusterItems;

    public ImageMapView(Context context1) {
        super(context1);
        TAG = "ImageMapView";
        itemClick = false;
        isAuxiliary = false;
        isShowText = false;
        clusterRadius = 20;
        tileOverlayurl = "";
        isHandLocation = false;
        isAutoplay = false;
        IsOnLine = true;
        root_control = null;
        popup_control = null;
        keywordvalue = null;
        init(context1);
    }

    public ImageMapView(Context context1, AttributeSet attributeset) {
        super(context1, attributeset);
        TAG = "ImageMapView";
        itemClick = false;
        isAuxiliary = false;
        isShowText = false;
        clusterRadius = 20;
        tileOverlayurl = "";
        isHandLocation = false;
        isAutoplay = false;
        IsOnLine = true;
        root_control = null;
        popup_control = null;
        keywordvalue = null;
        init(context1);
    }

    public ImageMapView(Context context1, AttributeSet attributeset, int i) {
        super(context1, attributeset, i);
        TAG = "ImageMapView";
        itemClick = false;
        isAuxiliary = false;
        isShowText = false;
        clusterRadius = 20;
        tileOverlayurl = "";
        isHandLocation = false;
        isAutoplay = false;
        IsOnLine = true;
        root_control = null;
        popup_control = null;
        keywordvalue = null;
        init(context1);
    }

    public static String getTourMapImageURL(int i) {
        return (new StringBuilder("http://www.imyuu.com:9100/tiles/")).append(i).append("/%d/%d_%d.png").toString();
    }

    public List<RecommendLine> getLineList() {
        return lineList;
    }

    public void downloadOnlineData(final String secnicId, String userId, String key) {
        //if 没有该景区的json数据，请求
        long start = System.currentTimeMillis();
        spotList = ScenicPointJson.loadAll(secnicId);
        long end = System.currentTimeMillis();
        if (spotList != null && spotList.size() > 0) {
            drawScenics(spotList, secnicId, false);
            Log.d("ApiClient", "222-" + spotList.size() + "--" + (end - start));
        } else
            ApiClient.getMapService().queryScenicSpotLists(secnicId, new Callback<List<ScenicPointJson>>() {
                @Override
                public void success(List<ScenicPointJson> spotList, Response response) {
                    try {
                        Log.d("ApiClient", "111-" + response.getStatus());
                        if (null != spotList)
                            Log.d("ApiClient", "---" + spotList);
                        drawScenics(spotList, secnicId, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                    //   consumeApiData();
                }
            });
    }

    public void downloadRecommendLine(final String secnicId) {
        //if 没有该景区的json数据，请求
        long start = System.currentTimeMillis();
        List<RecommendLine> linesList = RecommendLine.getAll(secnicId);
        long end = System.currentTimeMillis();
        if (linesList != null && linesList.size() > 0) {
            drawRecommendLines(linesList, secnicId, false);
            lineList = linesList;
            for (RecommendLine line : lineList) {
                line.getLineSpots();
            }
            Log.d("ApiClient", "222-" + linesList.size() + "--" + (end - start));

        } else
            ApiClient.getMapService().queryRecommendLines(secnicId, new Callback<List<RecommendLine>>() {
                @Override
                public void success(List<RecommendLine> rlineList, Response response) {
                    try {
                        Log.d("ApiClient", "111-" + response.getStatus());
                        if (null != rlineList)
                            Log.d("ApiClient", "---" + rlineList.size());
                        drawRecommendLines(rlineList, secnicId, true);
                        lineList = rlineList;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                    //   consumeApiData();
                }
            });
    }

    private void drawScenics(List<ScenicPointJson> spotList, String scenicId, boolean save) {

        for (ScenicPointJson spj : spotList) {
            try {
                RegionItem regionitem = new RegionItem(new LatLng(spj.getLat(), spj.getLng()), "景点", spj);
                //clusterOverlay.addClusterItem(regionitem);


                isHandLocation = true;
                MyLocationStyle mylocationstyle = new MyLocationStyle();
                //    mylocationstyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
                mylocationstyle.strokeColor(0xff000000);
                mylocationstyle.radiusFillColor(Color.argb(100, 0, 0, 180));
                mylocationstyle.strokeWidth(0.1F);
                aMap.setMyLocationStyle(mylocationstyle);
                aMap.setMyLocationRotateAngle(180F);
                aMap.setInfoWindowAdapter(this);
                aMap.setLocationSource(this);
                aMap.setOnMapClickListener(this);
                aMap.getUiSettings().setMyLocationButtonEnabled(false);
                aMap.getUiSettings().setZoomControlsEnabled(false);
                aMap.setMyLocationEnabled(true);
                aMap.setMyLocationType(1);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(spj.getLat(), spj.getLng()), 19 - 1.0F));

                loadView.setVisibility(View.GONE);

                spj.setScenicId(scenicId);
                if (save)
                    spj.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    private BitmapDescriptor CreateLayout(String s, int i)
//    {
//        View view = LayoutInflater.from(context).inflate(R.layout.map_jdmake_layut, null);
//        ImageView imageview = (ImageView)view.findViewById(R.id.jd_tubiao);
//        ((StrokeText)view.findViewById(R.id.jd_name)).setText(s);
//        imageview.setImageBitmap(readBitMap(context, i));
//        imageview.setPadding(0, 0, 0, 0);
//        return BitmapDescriptorFactory.fromView(view);
//    }

    private void drawRecommendLines(List<RecommendLine> lineList, String scenicId, boolean save) {

        for (RecommendLine line : lineList) {
            try {
                Log.d("drawRecommendLines", line.getLineName());
                if (save) {
                    Log.d("drawRecommendLines", line.getLineName());
                    line.save();
                    for (SpotInfo spotInfo : line.getLineSectionList()) {
                        spotInfo.save();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void init(Context context1) {
        context = context1;
        sp = context1.getSharedPreferences("Key_info", 0);
        app = (AppApplication) context1.getApplicationContext();
        helper = ApplicationHelper.getInstance();

        getPopop();
    }

    public void getPopop() {
      /*
        root_control = LayoutInflater.from(context).inflate(R.layout.map_control, null);
        popup_control = new PopupWindow(root_control, -1, -1);
        popup_control.setFocusable(true);
        popup_control.setTouchable(true);
        touch_misss = (TextView)root_control.findViewById(R.id.touch_misss);
        touch_misss_above = (TextView)root_control.findViewById(R.id.touch_misss_above);
        swithch_map_zdjj = (MySlipSwitch)root_control.findViewById(R.id.swithch_map_zdjj);
        swithch_map_shdt = (MySlipSwitch)root_control.findViewById(R.id.swithch_map_shdt);
        swithch_map_xswz = (MySlipSwitch)root_control.findViewById(R.id.swithch_map_xswz);
        close_window = (ImageButton)root_control.findViewById(R.id.close_window);
        swithch_map_zdjj.setImageResource(R.drawable.switch_open, R.drawable.switch_close, 0x7f02022c);
        swithch_map_shdt.setImageResource(R.drawable.switch_open, R.drawable.switch_close, 0x7f02022c);
        swithch_map_xswz.setImageResource(R.drawable.switch_open, R.drawable.switch_close, 0x7f02022c);
        swithch_map_shdt.updateSwitchState(true);
        swithch_map_zdjj.setOnSwitchListener(new MySlipSwitch.OnSwitchListener() {

            public void onSwitched(boolean flag)
            {
                    isAutoplay = flag;
            }

        });
        swithch_map_shdt.setOnSwitchListener(new MySlipSwitch.OnSwitchListener() {

            public void onSwitched(boolean flag)
            {
                if (flag)
                {
                    if (IsOnLine)
                    {
                        showTileOverlay();

                    } else
                    {
                        offonlinetileOverlay();

                    }
                } else {
                    tileOverlay.remove();
                }
            }

        });
        swithch_map_xswz.setOnSwitchListener(new MySlipSwitch.OnSwitchListener() {

            public void onSwitched(boolean flag)
            {
                if (flag)
                {
                    isShowText = true;
                } else
                {
                    isShowText = false;
                }
                if (clusterOverlay != null)
                {
                    clusterOverlay.setIsShowText(isShowText);
                    clusterOverlay.assignClusters();
                }
            }

        });
        touch_misss.setOnClickListener(this);
        touch_misss_above.setOnClickListener(this);
        close_window.setOnClickListener(this);
        */
    }

    private void playMusic(String audioMp3) {
        if (!IsOnLine) {
            if (!TextUtils.isEmpty(audioMp3)) {
                String s2 = (new StringBuilder(String.valueOf(Config.UU_FILEPATH))).append(scenicArea.getId()).append("/audio").append(audioMp3).toString();
                helper.getPlayer().playUrl(s2);
                helper.getPlayer().play();
            }
        } else if (!TextUtils.isEmpty(audioMp3)) {
            String s1 = (new StringBuilder(Config.IMAGE_SERVER_ADDR)).append(audioMp3).toString();
            helper.getPlayer().playUrl(s1);
            helper.getPlayer().play();
            return;
        }

    }

    public void activate(com.amap.api.maps.LocationSource.OnLocationChangedListener onlocationchangedlistener) {

        System.out.println("activate");
        mListener = onlocationchangedlistener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(getContext());
            if (!GpsUtil.isOpenMap(getContext())) {
                return;
            }
            mAMapLocationManager.requestLocationUpdates("gps", 2000L, 5F, this);
        }

    }

    private void load(boolean flag) {
        IsOnLine = flag;
//        clusterOverlay.setClusterRenderer(this);
//        clusterOverlay.setOnClusterClickListener(this);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17F));
    }

    public void deactivate() {
        System.out.println("deactivate");
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

    public void destroy() {

        if (tileOverlay != null) {
            tileOverlay.remove();
        }
        deactivate();
        return;

    }

    public boolean dispatchTouchEvent(MotionEvent motionevent) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(motionevent);
    }

    public int dp2px(Context context1, float f) {
        return (int) (0.5F + f * context1.getResources().getDisplayMetrics().density);
    }

    public void offline() {
        //clusterOverlay = new ClusterOverlay(aMap, dp2px(context, clusterRadius), context, 18, isShowText);
        offonlinetileOverlay();

    }

    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    public void onLocationChanged(AMapLocation amaplocation) {
        System.out.println("onLocationChanged");
        if (mListener != null && amaplocation != null && !isHandLocation) {
            mListener.onLocationChanged(amaplocation);
        }
        lat = amaplocation.getLatitude();
        lng = amaplocation.getLongitude();
        try {
            updateItelUIAndAudio();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    public void onMapClick(LatLng latlng) {
        if (show_shareview.getVisibility() == View.VISIBLE) {
            show_shareview.setVisibility(View.GONE);
        }
        int i = 0;
        do {
            if (i >= mclustlist.size()) {
                if (chooseMarker != null) {
                    chooseMarker.remove();
                }
                return;
            }
//            Marker marker = ((Cluster) mclustlist.get(i)).getMarker();
//            if (marker.isVisible()) {
//                marker.hideInfoWindow();
//            }
            i++;
        } while (true);
    }

    public void onProviderDisabled(String s) {
    }

    public void onProviderEnabled(String s) {
    }

    public Bitmap readBitMap(Context context1, int i) {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inPreferredConfig = android.graphics.Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(context1.getResources().openRawResource(i), null, options);
    }

    public void stopLocation() {
        clusterItems = null;
        mclustlist = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

    public void setUpMap(AMap amap, String url) {
        this.aMap = amap;
        this.tileOverlayurl = url;
    }

    public void createGroundOverlay() {
        if (tileOverlay != null) {
            tileOverlay.remove();
        }
        // CustomTileOnlineProvider customtileonlineprovider = new CustomTileOnlineProvider(5144, 4120, 0x33f0e, 0x18dcf, (new StringBuilder()).append(Environment.getExternalStorageDirectory()).append("/maptest/").toString());
        MyUrlTileProvider customtileonlineprovider = new MyUrlTileProvider(256, 256);
        if (customtileonlineprovider != null) {
            tileOverlay = aMap.addTileOverlay((new TileOverlayOptions()).tileProvider(customtileonlineprovider).diskCacheDir("/storage/amap/cache").diskCacheEnabled(true).diskCacheSize(1000));
            tileOverlay.setZIndex(0);
        }
    }

    //使用离线地图
    public void offonlinetileOverlay() {
        if (tileOverlay != null) {
            tileOverlay.remove();
        }
        CustomTileProvider customtileprovider = new CustomTileProvider((new StringBuilder(String.valueOf(Config.UU_FILEPATH))).append("虞山公园").append("/").toString());
        if (customtileprovider != null) {
            tileOverlay = aMap.addTileOverlay((new TileOverlayOptions()).tileProvider(customtileprovider).diskCacheDir("/storage/topview/cache").diskCacheEnabled(true).diskCacheSize(1000));
            tileOverlay.setZIndex(-10);
        }
    }

    private void showTileOverlay() {
        if (tileOverlay != null) {
            tileOverlay.remove();
        }
        UrlTileProvider urltileprovider = new UrlTileProvider(256, 256) {

            public URL getTileUrl(int i, int j, int k) {
                URL url;
                try {
                    String s = tileOverlayurl;
                    Object aobj[] = new Object[3];
                    aobj[0] = Integer.valueOf(k);
                    aobj[1] = Integer.valueOf(i);
                    aobj[2] = Integer.valueOf(j);
                    url = new URL(String.format(s, aobj));
                } catch (MalformedURLException malformedurlexception) {
                    malformedurlexception.printStackTrace();
                    return null;
                }
                return url;
            }
        };
        if (urltileprovider != null) {
            tileOverlay = aMap.addTileOverlay((new TileOverlayOptions()).tileProvider(urltileprovider).diskCacheDir("/storage/topview/cache").diskCacheEnabled(true).diskCacheSize(0x19000));
        }
    }


    public void onItemClick(AdapterView adapterview, View view, int i, long l) {
        String s = "";
        String s1 = "";
        String s2 = "";

        if (s.length() > 8) {
            String s3 = (new StringBuilder(String.valueOf(s.substring(0, 7)))).append("...").toString();
            popupText.setText(s3);
        } else {
            popupText.setText(s);
        }
        myPlayBtn.setMp3Url(s1);
        myPlayBtn.setTourName(s);
        if (TextUtils.isEmpty(s1)) {
            //        myPlayBtn.setImageResource(R.drawable.audio_no_img);
        } else {
            //      myPlayBtn.setImageResource(R.drawable.bofang_map);
        }
        if (TextUtils.isEmpty(s2)) {
            popupIntroduceText.setText(" ");

        } else {
            popupIntroduceText.setText(s2);

        }
    }

    private void updateItelUIAndAudio() throws IOException {
        sortByDisData = SpotDataSorter.sortByDistance(spotList, new LatLng(lat, lng));

        ScenicPointJson spj = sortByDisData.get(0);

        double d = AMapUtils.calculateLineDistance(new LatLng(lat, lng), new LatLng(spj.getLat(), spj.getLng()));

        if (isAutoplay && d <= 15D && !helper.getPlayer().mediaPlayer.isPlaying()) {
            String as[] = spj.getAudioUrl().split("/");
            String s = as[-1 + as.length];
            if (TextUtils.isEmpty(helper.getPlayer().getPath())) {
                playMusic(spj.getAudioUrl());
            } else {
                String as1[] = helper.getPlayer().getPath().split("/");
                if (!s.equalsIgnoreCase(as1[-1 + as1.length])) {
                    playMusic(spj.getAudioUrl());

                }
            }
        }
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
