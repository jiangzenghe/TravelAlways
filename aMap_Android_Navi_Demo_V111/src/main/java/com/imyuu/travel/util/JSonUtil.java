package com.imyuu.travel.util;

/**
 * Created by java on 2015/5/10.
 */

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.imyuu.travel.model.RecommendLine;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.model.ScenicPointJson;
import com.imyuu.travel.model.SpotInfo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class JSonUtil {
    public static String TAG = "JsonUtil";

    public static void main(String args[]) {
        String jsonText = FileUtils.readFile("e:/334.json");

    }

    public static int parseScenicInfo(String scenicId, Context context) {
        String json = FileUtils.readFile(Config.NEW_FILEPATH
                + scenicId + "/" + scenicId + Config.jsonFile);
        int code = -1;
        ScenicAreaJson scenicAreaJson = new ScenicAreaJson();
        if (!TextUtils.isEmpty(json)) {

            try {
                JSONTokener jsonParser = new JSONTokener(json);
                JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
                scenicAreaJson.setScenicId(jsonObject.getString("id"));
                Log.d("-----", "scenicId:" + scenicId);
                scenicAreaJson.setScenicName(jsonObject.getString("scenicName"));
                scenicAreaJson.setLat(jsonObject.getDouble("Latitude"));
                scenicAreaJson.setLng(jsonObject.getDouble("Longitude"));
                scenicAreaJson.setDesc(jsonObject.getString("scenicNote"));
                scenicAreaJson.setScenicLocation(jsonObject.getString("Location"));
                scenicAreaJson.setScenicLevel(jsonObject.getString("scenicLevel"));
                scenicAreaJson.setImageUrl(jsonObject.getString("mapUrl"));
                //scenicAreaJson.setCommentsNum(jsonObject.getInt(""));
                scenicAreaJson.setSmallImage(jsonObject.getString("smallpic"));
                //remove and save scenicAreaJson to sqllite
                scenicAreaJson.remove(scenicId);

                ScenicPointJson spotInfoRemove = new ScenicPointJson();
                spotInfoRemove.remove(scenicId);
                RecommendLine tline = new RecommendLine();
                tline.setScenicId(scenicId);
                tline.remove(scenicId);

                //begin save
                scenicAreaJson.save();
                JSONArray jsonArray = jsonObject.getJSONArray("scenicMap");
                List<ScenicPointJson> spotList = new ArrayList<ScenicPointJson>();
                for (int j = 0; j < jsonArray.length(); j++) {
                    ScenicPointJson scenicPointJson = new ScenicPointJson();
                    scenicPointJson.setSpotId(jsonArray.getJSONObject(j).getString("id"));
                    String spotName = jsonArray.getJSONObject(j).getString("scenicPointName");
                    scenicPointJson.setScenicPointName(spotName);

                    scenicPointJson.setAudioUrl(jsonArray.getJSONObject(j).getString("audioUrl"));
                    scenicPointJson.setDesc(jsonArray.getJSONObject(j).getString("desc"));
                    scenicPointJson.setSpotType(jsonArray.getJSONObject(j).getString("spotType"));
                    scenicPointJson.setImageUrl(jsonArray.getJSONObject(j).getString("imageUrl"));
                    scenicPointJson.setScenicId(scenicId);

                    scenicPointJson.setLat(jsonArray.getJSONObject(j).getDouble("lat"));
                    scenicPointJson.setLng(jsonArray.getJSONObject(j).getDouble("lng"));

                    spotList.add(scenicPointJson);
                    //begin save ScenicPointJson
                    scenicPointJson.save();
                }
                jsonArray = jsonObject.getJSONArray("scenicRecommendLine");
                List<RecommendLine> lineList = new ArrayList<RecommendLine>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject lineObject = jsonArray.getJSONObject(i);
                    RecommendLine lineInfo = new RecommendLine();
                    String lineId = lineObject.getString("lineId");
                    lineInfo.setLineId(lineId);
                    lineInfo.setLineName(lineObject.getString("lineName"));
                    //begin save RecommendLine
                    lineInfo.save();
                    List<SpotInfo> lineSectionList = new ArrayList<SpotInfo>();
                    JSONArray jsonArrayL = lineObject.getJSONArray("lineSectionList");
                    for (int j = 0; j < jsonArrayL.length(); j++) {
                        SpotInfo spotInfo = new SpotInfo();
                        spotInfo.setLineId(lineId);
                        spotInfo.setSpotid(jsonArrayL.getJSONObject(j).getString("spotid"));
                        spotInfo.setSpotName(jsonArrayL.getJSONObject(j).getString("spotName"));
                        spotInfo.setSpotType(jsonArrayL.getJSONObject(j).getString("spotType"));
                        spotInfo.setOrder(jsonArrayL.getJSONObject(j).getInt("order"));
                        spotInfo.setLat(jsonArrayL.getJSONObject(j).getDouble("lat"));
                        spotInfo.setLng(jsonArrayL.getJSONObject(j).getDouble("lng"));
                        lineSectionList.add(spotInfo);
                        //begin save RecommendLine
                        spotInfo.save();
                    }
                    lineInfo.setLineSectionList(lineSectionList);
                    lineList.add(lineInfo);
                }
                code = 0;
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e(TAG, "存储推荐景点信息失败！", ex);
                code = -2;
            } finally {

            }
        }
        return code;
    }

    public static boolean deleteScenicInfo(final String scenicId) {

        // 删除景区地图资料
        boolean b1 = FileUtils.deleteDir(Environment
                .getExternalStorageDirectory()
                + "/map/"

                + scenicId + "/");

        return b1;
    }

    public static boolean cleanScenicInfoFromDB(final String scenicId) {

        // 删除景区地图资料
        ScenicAreaJson areaJson = new ScenicAreaJson();
        areaJson.setScenicId(scenicId);
        areaJson.remove(scenicId);

        return true;
    }


    public static boolean checkMapExist(String scenicId) {
        if (!TextUtils.isEmpty(scenicId)) {

            String path = Config.Map_FILEPATH + "/" +
                    scenicId;
            File file = new File(path);
            return file != null && file.exists();
        }
        return false;
    }
}

