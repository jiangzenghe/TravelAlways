package com.imyuu.travel.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
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
import com.imyuu.travel.model.CityInfoJson;
import com.imyuu.travel.network.DownloadProgressListener;
import com.imyuu.travel.network.FileDownloader;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.ConstantsOld;
import com.imyuu.travel.util.FileUtil;
import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.HttpOldUtil;
import com.imyuu.travel.util.HttpOldUtil;
import com.imyuu.travel.util.JSonUtil;
import com.imyuu.travel.util.ZipUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DownloadOldActivity extends Activity {
    private TextView resultView;
    private ProgressDialog progressDialog = null;
    private Activity activity;
    private String scenicId;
    private List<ScenicRecommendLineModel> scenicRecommendLineModelList;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        resultView = (TextView) this.findViewById(R.id.resultView);
        Button button = (Button) this.findViewById(R.id.button);
    }
    /**
     * 主线程(UI线程)
     * 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
     * 如果想让更新后的显示界面反映到屏幕上，需要用Handler设置。
     * @param
     * @param
     */
    public void download(final Activity activity, final String scenicId) {
        this.activity = activity;
        this.scenicId = scenicId;
//        this.scenicId = "221";

        ScenicRecommendLineDataHelper scenicRecommendLineDataHelper = new ScenicRecommendLineDataHelper(activity);
        scenicRecommendLineModelList = scenicRecommendLineDataHelper.getListByScenicId(scenicId);
        scenicRecommendLineDataHelper.close();

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("正在读取数据");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //下载景区详细资料
        new Thread() {
            @Override
            public void run() {
                HttpOldUtil httpUtil = new HttpOldUtil();
                int result = httpUtil.downFile(ConstantsOld.API_ALL_SCENIC_DOWNLOAD, ConstantsOld.SCENIC_ROUTER_FILE_PATH, ConstantsOld.SCENIC + ConstantsOld.ALL_SCENIC_ZIP);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt(ConstantsOld.API_MESSAGE_KEY, result);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            //获取api访问结果，-1为获取失败 0为下载成功；1为本地已经存在
            int result = bundle.getInt(ConstantsOld.API_MESSAGE_KEY);
            switch (result) {
                case -1:
                    Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    break;
                case 0:
                    try {
                        //根据路径解压缩下载zip文件
                        ZipUtil.upZipFile(new File(Environment.getExternalStorageDirectory() + "/" + ConstantsOld.SCENIC_ROUTER_FILE_PATH + ConstantsOld.SCENIC + ConstantsOld.ALL_SCENIC_ZIP), Environment.getExternalStorageDirectory() + "/" + ConstantsOld.SCENIC_ROUTER_FILE_PATH);
                        //从解压出来的目录中读取json文件的内容
                        String json = FileUtil.readFile(ConstantsOld.SCENIC_IMAGE_FILE_PATH + ConstantsOld.SCENIC + ConstantsOld.ALL_SCENIC_JSON);
                        if (TextUtils.isEmpty(json)) {
                            Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                        } else {
                            //json解析并保存的手机的SQLite 数据库
                            ScenicDataHelper dataHelper = new ScenicDataHelper(activity);
                            try {
                                JSONTokener jsonParser = new JSONTokener(json);
                                JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
                                JSONArray jsonArray = jsonObject.getJSONArray("scenics");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String scenicId = jsonArray.getJSONObject(i).getString("id");
                                    if (!TextUtils.isEmpty(scenicId)) {
                                        if (dataHelper.getModelByScenicId(scenicId) == null) {
                                            ScenicOldModel scenicModel = new ScenicOldModel();
                                            scenicModel.setScenicId(scenicId);
                                            scenicModel.setScenicName(jsonArray.getJSONObject(i).getString("scenicName"));
                                            scenicModel.setScenicLocation(jsonArray.getJSONObject(i).getString("scenicLocation"));
                                            scenicModel.setCenterabsoluteLongitude(jsonArray.getJSONObject(i).getDouble("centerabsoluteLongitude"));
                                            scenicModel.setCenterabsoluteLatitude(jsonArray.getJSONObject(i).getDouble("centerabsoluteLatitude"));
                                            scenicModel.setCenterrelativeLongitude(jsonArray.getJSONObject(i).getDouble("centerrelativeLongitude"));
                                            scenicModel.setCenterrelativeLatitude(jsonArray.getJSONObject(i).getDouble("centerrelativeLatitude"));
                                            scenicModel.setAbsoluteLongitude(jsonArray.getJSONObject(i).getDouble("absoluteLongitude"));
                                            scenicModel.setAbsoluteLatitude(jsonArray.getJSONObject(i).getDouble("absoluteLatitude"));
                                            scenicModel.setScenicNote(jsonArray.getJSONObject(i).getString("scenicNote"));
                                            scenicModel.setScenicMapurl(jsonArray.getJSONObject(i).getString("scenicMapurl"));
                                            scenicModel.setScenicSmallpic(jsonArray.getJSONObject(i).getString("scenicSmallpic"));
                                            scenicModel.setScenicmapMaxx(jsonArray.getJSONObject(i).getInt("scenicmapMaxx"));
                                            scenicModel.setScenicmapMaxy(jsonArray.getJSONObject(i).getInt("scenicmapMaxy"));
                                            scenicModel.setLineColor(jsonArray.getJSONObject(i).getString("lineColor"));
                                            dataHelper.saveModel(scenicModel);
                                        }
                                    }
                                }
                                //更新完景区基本资料以后继续更新ditu
                                new Thread() {
                                    @Override
                                    public void run() {
                                        HttpOldUtil HttpOldUtil = new HttpOldUtil();
                                        int result = HttpOldUtil.downFile(ConstantsOld.API_SINGLE_SCENIC_DOWNLOAD + "m" + scenicId,
                                                ConstantsOld.SCENIC_ROUTER_FILE_PATH, ConstantsOld.SCENIC + scenicId + ConstantsOld.ALL_SCENIC_ZIP);
                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(ConstantsOld.API_MESSAGE_KEY, result);
                                        message.setData(bundle);
                                        mapHandler.sendMessage(message);
                                    }
                                }.start();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } finally {
                                dataHelper.close();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    break;
                default:
                    String json = FileUtil.readFile(ConstantsOld.SCENIC_IMAGE_FILE_PATH + ConstantsOld.SCENIC + ConstantsOld.ALL_SCENIC_JSON);
                    if (TextUtils.isEmpty(json)) {
                        Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                    } else {
                        ScenicDataHelper dataHelper = new ScenicDataHelper(activity);
                        try {
                            JSONTokener jsonParser = new JSONTokener(json);
                            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
                            JSONArray jsonArray = jsonObject.getJSONArray("scenics");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String scenicId = jsonArray.getJSONObject(i).getString("id");
                                if (!TextUtils.isEmpty(scenicId)) {
                                    if (dataHelper.getModelByScenicId(scenicId) == null) {
                                        ScenicOldModel scenicModel = new ScenicOldModel();
                                        scenicModel.setScenicId(scenicId);
                                        scenicModel.setScenicName(jsonArray.getJSONObject(i).getString("scenicName"));
                                        scenicModel.setScenicLocation(jsonArray.getJSONObject(i).getString("scenicLocation"));
                                        scenicModel.setCenterabsoluteLongitude(jsonArray.getJSONObject(i).getDouble("centerabsoluteLongitude"));
                                        scenicModel.setCenterabsoluteLatitude(jsonArray.getJSONObject(i).getDouble("centerabsoluteLatitude"));
                                        scenicModel.setCenterrelativeLongitude(jsonArray.getJSONObject(i).getDouble("centerrelativeLongitude"));
                                        scenicModel.setCenterrelativeLatitude(jsonArray.getJSONObject(i).getDouble("centerrelativeLatitude"));
                                        scenicModel.setAbsoluteLongitude(jsonArray.getJSONObject(i).getDouble("absoluteLongitude"));
                                        scenicModel.setAbsoluteLatitude(jsonArray.getJSONObject(i).getDouble("absoluteLatitude"));
                                        scenicModel.setScenicNote(jsonArray.getJSONObject(i).getString("scenicNote"));
                                        scenicModel.setScenicMapurl(jsonArray.getJSONObject(i).getString("scenicMapurl"));
                                        scenicModel.setScenicSmallpic(jsonArray.getJSONObject(i).getString("scenicSmallpic"));
                                        scenicModel.setScenicmapMaxx(jsonArray.getJSONObject(i).getInt("scenicmapMaxx"));
                                        scenicModel.setScenicmapMaxy(jsonArray.getJSONObject(i).getInt("scenicmapMaxy"));
                                        scenicModel.setLineColor(jsonArray.getJSONObject(i).getString("lineColor"));
                                        dataHelper.saveModel(scenicModel);
                                    }
                                }
                            }
                            new Thread() {
                                @Override
                                public void run() {
                                    HttpOldUtil HttpOldUtil = new HttpOldUtil();
                                    int result = HttpOldUtil.downFile(ConstantsOld.API_SINGLE_SCENIC_DOWNLOAD + "m" + scenicId,
                                            ConstantsOld.SCENIC_ROUTER_FILE_PATH, ConstantsOld.SCENIC + scenicId + ConstantsOld.ALL_SCENIC_ZIP);
                                    Message message = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(ConstantsOld.API_MESSAGE_KEY, result);
                                    message.setData(bundle);
                                    mapHandler.sendMessage(message);
                                }
                            }.start();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } finally {
                            dataHelper.close();
                        }
                    }
            }
        }
    };

    private Handler mapHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int result = bundle.getInt(ConstantsOld.API_MESSAGE_KEY);
            switch (result) {
                case -1:
                    Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    try {
                        ZipUtil.upZipFile(new File(Environment.getExternalStorageDirectory() + "/" + ConstantsOld.SCENIC_ROUTER_FILE_PATH + ConstantsOld.SCENIC + scenicId + ConstantsOld.ALL_SCENIC_ZIP), Environment.getExternalStorageDirectory() + "/" + ConstantsOld.SCENIC_ROUTER_FILE_PATH);
                        String json = FileUtil.readFile(ConstantsOld.SCENIC_SINGLE_FILE_PATH + scenicId + "/" + ConstantsOld.SCENIC + scenicId + ConstantsOld.ALL_SCENIC_JSON);
                        if (TextUtils.isEmpty(json)) {
                            Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                        } else {
                            ScenicMapDataHelper scenicMapDataHelper = new ScenicMapDataHelper(activity);
                            ScenicRecommendLineDataHelper scenicRecommendLineDataHelper = new ScenicRecommendLineDataHelper(activity);
                            RecommendLinesectionDataHelper recommendLinesectionDataHelper = new RecommendLinesectionDataHelper(activity);
                            RecommendLinesectionguideDataHelper recommendLinesectionguideDataHelper = new RecommendLinesectionguideDataHelper(activity);
                            try {
                                JSONTokener jsonParser = new JSONTokener(json);
                                JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
                                JSONArray jsonArray = jsonObject.getJSONArray("scenicMap");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String scenicMapId = jsonArray.getJSONObject(i).getString("id");
                                    if (!TextUtils.isEmpty(scenicMapId)) {
                                        if (scenicMapDataHelper.getModelById(scenicMapId) == null) {
                                            ScenicMapOldModel ScenicMapOldModel = new ScenicMapOldModel();
                                            ScenicMapOldModel.setScenicMapId(scenicMapId);
                                            ScenicMapOldModel.setScenicId(jsonArray.getJSONObject(i).getString("scenicId"));
                                            ScenicMapOldModel.setScenicName(jsonArray.getJSONObject(i).getString("scenicName"));
                                            ScenicMapOldModel.setScenicspotName(jsonArray.getJSONObject(i).getString("scenicspotName"));
                                            ScenicMapOldModel.setRelativeLongitude(jsonArray.getJSONObject(i).getDouble("relativeLongitude"));
                                            ScenicMapOldModel.setRelativeLatitude(jsonArray.getJSONObject(i).getDouble("relativeLatitude"));
                                            ScenicMapOldModel.setScenicspotNote(jsonArray.getJSONObject(i).getString("scenicspotNote"));
                                            ScenicMapOldModel.setScenicspotVoice(jsonArray.getJSONObject(i).getString("scenicspotVoice"));
                                            ScenicMapOldModel.setScenicspotMarkertype(jsonArray.getJSONObject(i).getString("scenicspotMarkertype"));
                                            ScenicMapOldModel.setScenicspotSmallpic(jsonArray.getJSONObject(i).getString("scenicspotSmallpic"));
                                            ScenicMapOldModel.setAbsoluteLongitude(jsonArray.getJSONObject(i).getDouble("absoluteLongitude"));
                                            ScenicMapOldModel.setAbsoluteLatitude(jsonArray.getJSONObject(i).getDouble("absoluteLatitude"));
                                            ScenicMapOldModel.setRelativeHeight(jsonArray.getJSONObject(i).getDouble("relativeHeight"));
                                            ScenicMapOldModel.setRelativeWidth(jsonArray.getJSONObject(i).getDouble("relativeWidth"));
                                            scenicMapDataHelper.saveModel(ScenicMapOldModel);
                                        }
                                    }
                                }
                                jsonArray = jsonObject.getJSONArray("scenicRecommendLine");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONArray jsonArrayL = jsonArray.getJSONArray(i);
                                    for (int j = 0; j < jsonArrayL.length(); j++) {
                                        if (j == 0) {
                                            String scenicRecommendLineId = jsonArrayL.getJSONObject(j).getString("id");
                                            if (!TextUtils.isEmpty(scenicRecommendLineId)) {
                                                if (scenicRecommendLineDataHelper.getModelById(scenicRecommendLineId) == null) {
                                                    ScenicRecommendLineModel scenicRecommendLineModel = new ScenicRecommendLineModel();
                                                    scenicRecommendLineModel.setScenicId(jsonArrayL.getJSONObject(j).getString("scenicId"));
                                                    scenicRecommendLineModel.setScenicRecommendLineId(scenicRecommendLineId);
                                                    scenicRecommendLineModel.setScenicName(jsonArrayL.getJSONObject(j).getString("scenicName"));
                                                    scenicRecommendLineModel.setRecommendRoutename(jsonArrayL.getJSONObject(j).getString("recommendRoutename"));
                                                    scenicRecommendLineModel.setRouteTotaltime(jsonArrayL.getJSONObject(j).getString("routeTotaltime"));
                                                    scenicRecommendLineModel.setRecommendRoutenote(jsonArrayL.getJSONObject(j).getString("recommendRoutenote"));
                                                    scenicRecommendLineDataHelper.saveModel(scenicRecommendLineModel);
                                                }
                                            }
                                        } else {
                                            String recommendLinesectionId = jsonArrayL.getJSONObject(j).getString("id");
                                            if (!TextUtils.isEmpty(recommendLinesectionId)) {
                                                if (recommendLinesectionDataHelper.getModelById(recommendLinesectionId) == null) {
                                                    RecommendLinesectionModel recommendLinesectionModel = new RecommendLinesectionModel();
                                                    recommendLinesectionModel.setRecommendLinesectionId(recommendLinesectionId);
                                                    recommendLinesectionModel.setScenicId(jsonArrayL.getJSONObject(j).getString("scenicId"));
                                                    recommendLinesectionModel.setRecommendrouteId(jsonArrayL.getJSONObject(j).getString("recommendrouteId"));
                                                    recommendLinesectionModel.setRouteOrder(jsonArrayL.getJSONObject(j).getInt("routeOrder"));
                                                    recommendLinesectionModel.setAspotId(jsonArrayL.getJSONObject(j).getString("aspotId"));
                                                    recommendLinesectionModel.setBspotId(jsonArrayL.getJSONObject(j).getString("bspotId"));
                                                    recommendLinesectionModel.setAbTotaltime(jsonArrayL.getJSONObject(j).getString("abTotaltime"));
                                                    recommendLinesectionModel.setAscenicspotName(jsonArrayL.getJSONObject(j).getString("ascenicspotName"));
                                                    recommendLinesectionModel.setBscenicspotName(jsonArrayL.getJSONObject(j).getString("bscenicspotName"));
                                                    recommendLinesectionModel.setRecommendRoutename(jsonArrayL.getJSONObject(j).getString("recommendRoutename"));
                                                    recommendLinesectionModel.setScenicName(jsonArrayL.getJSONObject(j).getString("scenicName"));
                                                    recommendLinesectionDataHelper.saveModel(recommendLinesectionModel);
                                                    if (jsonArrayL.getJSONObject(j).has("recommendLinesectionguide")) {
                                                        JSONArray jsonArrayLG = jsonArrayL.getJSONObject(j).getJSONArray("recommendLinesectionguide");
                                                        for (int k = 0; k < jsonArrayLG.length(); k++) {
                                                            String recommendLinesectionguideId = jsonArrayLG.getJSONObject(k).getString("id");
                                                            if (!TextUtils.isEmpty(recommendLinesectionguideId)) {
                                                                if (recommendLinesectionguideDataHelper.getModelById(recommendLinesectionguideId) == null) {
                                                                    RecommendLinesectionguideModel recommendLinesectionguideModel = new RecommendLinesectionguideModel();
                                                                    recommendLinesectionguideModel.setRecommendLinesectionguideId(recommendLinesectionguideId);
                                                                    recommendLinesectionguideModel.setRecommendrouteId(jsonArrayLG.getJSONObject(k).getString("recommendrouteId"));
                                                                    recommendLinesectionguideModel.setRouteOrder(jsonArrayLG.getJSONObject(k).getInt("routeOrder"));
                                                                    recommendLinesectionguideModel.setRelativeLongitude(jsonArrayLG.getJSONObject(k).getDouble("relativeLongitude"));
                                                                    recommendLinesectionguideModel.setRelativeLatitude(jsonArrayLG.getJSONObject(k).getDouble("relativeLatitude"));
                                                                    recommendLinesectionguideModel.setRecommendrouteDetailid(jsonArrayLG.getJSONObject(k).getString("recommendrouteDetailid"));
                                                                    recommendLinesectionguideModel.setRecommendrouteDetailname(jsonArrayLG.getJSONObject(k).getString("recommendrouteDetailname"));
                                                                    recommendLinesectionguideModel.setRecommendrouteName(jsonArrayLG.getJSONObject(k).getString("recommendrouteName"));
                                                                    recommendLinesectionguideModel.setAbsoluteLongitude(jsonArrayLG.getJSONObject(k).getDouble("absoluteLongitude"));
                                                                    recommendLinesectionguideModel.setAbsoluteLatitude(jsonArrayLG.getJSONObject(k).getDouble("absoluteLatitude"));
                                                                    recommendLinesectionguideDataHelper.saveModel(recommendLinesectionguideModel);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                new Thread() {
                                    @Override
                                    public void run() {
                                        HttpOldUtil HttpOldUtil = new HttpOldUtil();
                                        int result = HttpOldUtil.downFile(ConstantsOld.API_SCENIC_ADVERT_DOWNLOAD + scenicId, ConstantsOld.SCENIC_ROUTER_FILE_PATH, ConstantsOld.SCENIC_ADVERT + scenicId + ConstantsOld.ALL_SCENIC_ZIP);
                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(ConstantsOld.API_MESSAGE_KEY, result);
                                        message.setData(bundle);
                                        handlerAdvert.sendMessage(message);
                                    }
                                }.start();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                            } finally {
                                scenicMapDataHelper.close();
                                scenicRecommendLineDataHelper.close();
                                recommendLinesectionDataHelper.close();
                                recommendLinesectionguideDataHelper.close();
                                progressDialog.dismiss();
                            }
                        }
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        Log.e("error-here",e.getMessage().toString());
                        Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    break;
                default:
                    String json = FileUtil.readFile(ConstantsOld.SCENIC_SINGLE_FILE_PATH + scenicId + "/" + ConstantsOld.SCENIC + scenicId + ConstantsOld.ALL_SCENIC_JSON);
                    if (TextUtils.isEmpty(json)) {
                        Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                    } else {
                        ScenicMapDataHelper scenicMapDataHelper = new ScenicMapDataHelper(activity);
                        ScenicRecommendLineDataHelper scenicRecommendLineDataHelper = new ScenicRecommendLineDataHelper(activity);
                        RecommendLinesectionDataHelper recommendLinesectionDataHelper = new RecommendLinesectionDataHelper(activity);
                        RecommendLinesectionguideDataHelper recommendLinesectionguideDataHelper = new RecommendLinesectionguideDataHelper(activity);
                        try {
                            JSONTokener jsonParser = new JSONTokener(json);
                            JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
                            JSONArray jsonArray = jsonObject.getJSONArray("scenicMap");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String scenicMapId = jsonArray.getJSONObject(i).getString("id");
                                if (!TextUtils.isEmpty(scenicMapId)) {
                                    if (scenicMapDataHelper.getModelById(scenicMapId) == null) {
                                        ScenicMapOldModel ScenicMapOldModel = new ScenicMapOldModel();
                                        ScenicMapOldModel.setScenicMapId(scenicMapId);
                                        ScenicMapOldModel.setScenicId(jsonArray.getJSONObject(i).getString("scenicId"));
                                        ScenicMapOldModel.setScenicName(jsonArray.getJSONObject(i).getString("scenicName"));
                                        ScenicMapOldModel.setScenicspotName(jsonArray.getJSONObject(i).getString("scenicspotName"));
                                        ScenicMapOldModel.setRelativeLongitude(jsonArray.getJSONObject(i).getDouble("relativeLongitude"));
                                        ScenicMapOldModel.setRelativeLatitude(jsonArray.getJSONObject(i).getDouble("relativeLatitude"));
                                        ScenicMapOldModel.setScenicspotNote(jsonArray.getJSONObject(i).getString("scenicspotNote"));
                                        ScenicMapOldModel.setScenicspotVoice(jsonArray.getJSONObject(i).getString("scenicspotVoice"));
                                        ScenicMapOldModel.setScenicspotMarkertype(jsonArray.getJSONObject(i).getString("scenicspotMarkertype"));
                                        ScenicMapOldModel.setScenicspotSmallpic(jsonArray.getJSONObject(i).getString("scenicspotSmallpic"));
                                        ScenicMapOldModel.setAbsoluteLongitude(jsonArray.getJSONObject(i).getDouble("absoluteLongitude"));
                                        ScenicMapOldModel.setAbsoluteLatitude(jsonArray.getJSONObject(i).getDouble("absoluteLatitude"));
                                        ScenicMapOldModel.setRelativeHeight(jsonArray.getJSONObject(i).getDouble("relativeHeight"));
                                        ScenicMapOldModel.setRelativeWidth(jsonArray.getJSONObject(i).getDouble("relativeWidth"));
                                        scenicMapDataHelper.saveModel(ScenicMapOldModel);
                                    }
                                }
                            }
                            jsonArray = jsonObject.getJSONArray("scenicRecommendLine");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray jsonArrayL = jsonArray.getJSONArray(i);
                                for (int j = 0; j < jsonArrayL.length(); j++) {
                                    if (j == 0) {
                                        String scenicRecommendLineId = jsonArrayL.getJSONObject(j).getString("id");
                                        if (!TextUtils.isEmpty(scenicRecommendLineId)) {
                                            if (scenicRecommendLineDataHelper.getModelById(scenicRecommendLineId) == null) {
                                                ScenicRecommendLineModel scenicRecommendLineModel = new ScenicRecommendLineModel();
                                                scenicRecommendLineModel.setScenicId(jsonArrayL.getJSONObject(j).getString("scenicId"));
                                                scenicRecommendLineModel.setScenicRecommendLineId(scenicRecommendLineId);
                                                scenicRecommendLineModel.setScenicName(jsonArrayL.getJSONObject(j).getString("scenicName"));
                                                scenicRecommendLineModel.setRecommendRoutename(jsonArrayL.getJSONObject(j).getString("recommendRoutename"));
                                                scenicRecommendLineModel.setRouteTotaltime(jsonArrayL.getJSONObject(j).getString("routeTotaltime"));
                                                scenicRecommendLineModel.setRecommendRoutenote(jsonArrayL.getJSONObject(j).getString("recommendRoutenote"));
                                                scenicRecommendLineDataHelper.saveModel(scenicRecommendLineModel);
                                            }
                                        }
                                    } else {
                                        String recommendLinesectionId = jsonArrayL.getJSONObject(j).getString("id");
                                        if (!TextUtils.isEmpty(recommendLinesectionId)) {
                                            if (recommendLinesectionDataHelper.getModelById(recommendLinesectionId) == null) {
                                                RecommendLinesectionModel recommendLinesectionModel = new RecommendLinesectionModel();
                                                recommendLinesectionModel.setRecommendLinesectionId(recommendLinesectionId);
                                                recommendLinesectionModel.setScenicId(jsonArrayL.getJSONObject(j).getString("scenicId"));
                                                recommendLinesectionModel.setRecommendrouteId(jsonArrayL.getJSONObject(j).getString("recommendrouteId"));
                                                recommendLinesectionModel.setRouteOrder(jsonArrayL.getJSONObject(j).getInt("routeOrder"));
                                                recommendLinesectionModel.setAspotId(jsonArrayL.getJSONObject(j).getString("aspotId"));
                                                recommendLinesectionModel.setBspotId(jsonArrayL.getJSONObject(j).getString("bspotId"));
                                                recommendLinesectionModel.setAbTotaltime(jsonArrayL.getJSONObject(j).getString("abTotaltime"));
                                                recommendLinesectionModel.setAscenicspotName(jsonArrayL.getJSONObject(j).getString("ascenicspotName"));
                                                recommendLinesectionModel.setBscenicspotName(jsonArrayL.getJSONObject(j).getString("bscenicspotName"));
                                                recommendLinesectionModel.setRecommendRoutename(jsonArrayL.getJSONObject(j).getString("recommendRoutename"));
                                                recommendLinesectionModel.setScenicName(jsonArrayL.getJSONObject(j).getString("scenicName"));
                                                recommendLinesectionDataHelper.saveModel(recommendLinesectionModel);
                                                if (jsonArrayL.getJSONObject(j).has("recommendLinesectionguide")) {
                                                    JSONArray jsonArrayLG = jsonArrayL.getJSONObject(j).getJSONArray("recommendLinesectionguide");
                                                    for (int k = 0; k < jsonArrayLG.length(); k++) {
                                                        String recommendLinesectionguideId = jsonArrayLG.getJSONObject(k).getString("id");
                                                        if (!TextUtils.isEmpty(recommendLinesectionguideId)) {
                                                            if (recommendLinesectionguideDataHelper.getModelById(recommendLinesectionguideId) == null) {
                                                                RecommendLinesectionguideModel recommendLinesectionguideModel = new RecommendLinesectionguideModel();
                                                                recommendLinesectionguideModel.setRecommendLinesectionguideId(recommendLinesectionguideId);
                                                                recommendLinesectionguideModel.setRecommendrouteId(jsonArrayLG.getJSONObject(k).getString("recommendrouteId"));
                                                                recommendLinesectionguideModel.setRouteOrder(jsonArrayLG.getJSONObject(k).getInt("routeOrder"));
                                                                recommendLinesectionguideModel.setRelativeLongitude(jsonArrayLG.getJSONObject(k).getDouble("relativeLongitude"));
                                                                recommendLinesectionguideModel.setRelativeLatitude(jsonArrayLG.getJSONObject(k).getDouble("relativeLatitude"));
                                                                recommendLinesectionguideModel.setRecommendrouteDetailid(jsonArrayLG.getJSONObject(k).getString("recommendrouteDetailid"));
                                                                recommendLinesectionguideModel.setRecommendrouteDetailname(jsonArrayLG.getJSONObject(k).getString("recommendrouteDetailname"));
                                                                recommendLinesectionguideModel.setRecommendrouteName(jsonArrayLG.getJSONObject(k).getString("recommendrouteName"));
                                                                recommendLinesectionguideModel.setAbsoluteLongitude(jsonArrayLG.getJSONObject(k).getDouble("absoluteLongitude"));
                                                                recommendLinesectionguideModel.setAbsoluteLatitude(jsonArrayLG.getJSONObject(k).getDouble("absoluteLatitude"));
                                                                recommendLinesectionguideDataHelper.saveModel(recommendLinesectionguideModel);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            new Thread() {
                                @Override
                                public void run() {
                                    HttpOldUtil HttpOldUtil = new HttpOldUtil();
                                    int result = HttpOldUtil.downFile(ConstantsOld.API_SCENIC_ADVERT_DOWNLOAD + scenicId, ConstantsOld.SCENIC_ROUTER_FILE_PATH, ConstantsOld.SCENIC_ADVERT + scenicId + ConstantsOld.ALL_SCENIC_ZIP);
                                    Message message = new Message();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(ConstantsOld.API_MESSAGE_KEY, result);
                                    message.setData(bundle);
                                    handlerAdvert.sendMessage(message);
                                }
                            }.start();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                        } finally {
                            scenicMapDataHelper.close();
                            scenicRecommendLineDataHelper.close();
                            recommendLinesectionDataHelper.close();
                            recommendLinesectionguideDataHelper.close();
                            progressDialog.dismiss();
                        }
                    }
                    progressDialog.dismiss();
            }
        }
    };
    private Handler handlerAdvert = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int result = bundle.getInt(ConstantsOld.API_MESSAGE_KEY);
            switch (result) {
                case -1:
                    Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    try {
                        ZipUtil.upZipFile(new File(Environment.getExternalStorageDirectory() + "/" + ConstantsOld.SCENIC_ROUTER_FILE_PATH + ConstantsOld.SCENIC_ADVERT + scenicId + ConstantsOld.ALL_SCENIC_ZIP), Environment.getExternalStorageDirectory() + "/" + ConstantsOld.SCENIC_ROUTER_FILE_PATH);
                        String json = FileUtil.readFile(ConstantsOld.SCENIC_ADVERT_FILE_PATH + scenicId + "/" + ConstantsOld.SCENIC_ADVERT + scenicId + ConstantsOld.ALL_SCENIC_JSON);
                        if (TextUtils.isEmpty(json)) {
                            Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                        } else {
                            ScenicAdvertDataHelper scenicAdvertDataHelper = new ScenicAdvertDataHelper(activity);
                            try {
                                JSONTokener jsonParser = new JSONTokener(json);
                                JSONArray jsonArray = (JSONArray) jsonParser.nextValue();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String scenicAdvertId = jsonArray.getJSONObject(i).getString("id");
                                    if (!TextUtils.isEmpty(scenicAdvertId)) {
                                        if (scenicAdvertDataHelper.getModelById(scenicAdvertId) == null) {
                                            ScenicAdvertOldModel ScenicAdvertOldModel = new ScenicAdvertOldModel();
                                            ScenicAdvertOldModel.setScenicAdvertId(scenicAdvertId);
                                            ScenicAdvertOldModel.setScenicId(jsonArray.getJSONObject(i).getString("scenicId"));
                                            ScenicAdvertOldModel.setScenicName(jsonArray.getJSONObject(i).getString("scenicName"));
                                            ScenicAdvertOldModel.setPubTime(jsonArray.getJSONObject(i).getString("pubTime"));
                                            ScenicAdvertOldModel.setAdvertAdmin(jsonArray.getJSONObject(i).getString("advertAdmin"));
                                            ScenicAdvertOldModel.setAdvertPic(jsonArray.getJSONObject(i).getString("advertPic"));
                                            ScenicAdvertOldModel.setAdvertLink(jsonArray.getJSONObject(i).getString("advertLink"));
                                            ScenicAdvertOldModel.setAdvertRemark(jsonArray.getJSONObject(i).getString("advertRemark"));
                                            ScenicAdvertOldModel.setAdvertscenicId(jsonArray.getJSONObject(i).getString("advertscenicId"));
                                            ScenicAdvertOldModel.setAdvertscenicName(jsonArray.getJSONObject(i).getString("advertscenicName"));
                                            scenicAdvertDataHelper.saveModel(ScenicAdvertOldModel);
                                        }
                                    }
                                }
                                ScenicRecommendLineDataHelper scenicRecommendLineDataHelper = new ScenicRecommendLineDataHelper(activity);
                                scenicRecommendLineModelList = scenicRecommendLineDataHelper.getListByScenicId(scenicId);
                                scenicRecommendLineDataHelper.close();
                                if (scenicRecommendLineModelList.size() > 0) {
//                                    linearlayoutScieninfoDownload.setVisibility(View.GONE);
//                                    linearlayoutScieninfoGoto.setVisibility(View.VISIBLE);

//                                        listScieninfoLine.setVisibility(View.VISIBLE);
//                                        SimpleAdapter arrayAdapter = new SimpleAdapter(activity, getData(), R.layout.scenicinfo_line_item, new String[]{ConstantsOld.LIST_KEY_SCIENINFO_LINE_NAME}, new int[]{R.id.text_scenicinfo_line_name});
//                                        listScieninfoLine.setAdapter(arrayAdapter);
                                } else {
//                                    linearlayoutScieninfoDownload.setVisibility(View.VISIBLE);
//                                    linearlayoutScieninfoGoto.setVisibility(View.GONE);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                            } finally {
                                scenicAdvertDataHelper.close();
                                progressDialog.dismiss();
                            }
                        }
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    break;
                default:
                    String json = FileUtil.readFile(ConstantsOld.SCENIC_ADVERT_FILE_PATH + scenicId + "/" + ConstantsOld.SCENIC_ADVERT + scenicId + ConstantsOld.ALL_SCENIC_JSON);
                    if (TextUtils.isEmpty(json)) {
                        Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                    } else {
                        ScenicAdvertDataHelper scenicAdvertDataHelper = new ScenicAdvertDataHelper(activity);
                        try {
                            JSONTokener jsonParser = new JSONTokener(json);
                            JSONArray jsonArray = (JSONArray) jsonParser.nextValue();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String scenicAdvertId = jsonArray.getJSONObject(i).getString("id");
                                if (!TextUtils.isEmpty(scenicAdvertId)) {
                                    if (scenicAdvertDataHelper.getModelById(scenicAdvertId) == null) {
                                        ScenicAdvertOldModel ScenicAdvertOldModel = new ScenicAdvertOldModel();
                                        ScenicAdvertOldModel.setScenicAdvertId(scenicAdvertId);
                                        ScenicAdvertOldModel.setScenicId(jsonArray.getJSONObject(i).getString("scenicId"));
                                        ScenicAdvertOldModel.setScenicName(jsonArray.getJSONObject(i).getString("scenicName"));
                                        ScenicAdvertOldModel.setPubTime(jsonArray.getJSONObject(i).getString("pubTime"));
                                        ScenicAdvertOldModel.setAdvertAdmin(jsonArray.getJSONObject(i).getString("advertAdmin"));
                                        ScenicAdvertOldModel.setAdvertPic(jsonArray.getJSONObject(i).getString("advertPic"));
                                        ScenicAdvertOldModel.setAdvertLink(jsonArray.getJSONObject(i).getString("advertLink"));
                                        ScenicAdvertOldModel.setAdvertRemark(jsonArray.getJSONObject(i).getString("advertRemark"));
                                        ScenicAdvertOldModel.setAdvertscenicId(jsonArray.getJSONObject(i).getString("advertscenicId"));
                                        ScenicAdvertOldModel.setAdvertscenicName(jsonArray.getJSONObject(i).getString("advertscenicName"));
                                        scenicAdvertDataHelper.saveModel(ScenicAdvertOldModel);
                                    }
                                }
                            }
                            ScenicRecommendLineDataHelper scenicRecommendLineDataHelper = new ScenicRecommendLineDataHelper(activity);
                            scenicRecommendLineModelList = scenicRecommendLineDataHelper.getListByScenicId(scenicId);
                            scenicRecommendLineDataHelper.close();
                            if (scenicRecommendLineModelList.size() > 0) {
//                                linearlayoutScieninfoDownload.setVisibility(View.GONE);
//                                linearlayoutScieninfoGoto.setVisibility(View.VISIBLE);
                            } else {
//                                linearlayoutScieninfoDownload.setVisibility(View.VISIBLE);
//                                linearlayoutScieninfoGoto.setVisibility(View.GONE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(activity, R.string.index_loading_fail, Toast.LENGTH_SHORT).show();
                        } finally {
                            scenicAdvertDataHelper.close();
                            progressDialog.dismiss();
                        }
                    }
                    progressDialog.dismiss();
            }
        }
    };
}
