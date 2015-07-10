package com.imyuu.travel.util;

import android.content.Context;

import com.imyuu.travel.database.RecommendLinesectionDataHelper;
import com.imyuu.travel.database.RecommendLinesectionguideDataHelper;
import com.imyuu.travel.database.ScenicMapDataHelper;
import com.imyuu.travel.database.ScenicRecommendLineDataHelper;
import com.imyuu.travel.model.MapInfoModel;
import com.imyuu.travel.model.RecommendLine;

/**
 * Created by java on 2015/6/1.
 */
public class MapUtil {
    public static boolean deleteMap(String canNav,String scenicId,Context activity)
    {
        if("0".equals(canNav)) {
            FileUtils.deleteFile(Config.Map_FILEPATH+ scenicId + ".zip");
            FileUtils.deleteFile(Config.Map_FILEPATH +"scenicAdvert"+ scenicId + ".zip");
            FileUtils.deleteDir(Config.UU_FILEPATH + "scenic" + scenicId);
            FileUtils.deleteDir(Config.UU_FILEPATH +"scenicAdvert"+ scenicId);
            RecommendLinesectionguideDataHelper linesectionguideDataHelper =  new RecommendLinesectionguideDataHelper(activity);
            linesectionguideDataHelper.deleteByScenicId(scenicId);
            ScenicMapDataHelper scenicMapDataHelper = new ScenicMapDataHelper(activity);
            scenicMapDataHelper.deleteByScenicId(scenicId);
            ScenicRecommendLineDataHelper scenicRecommendLineDataHelper = new ScenicRecommendLineDataHelper(activity);
            scenicRecommendLineDataHelper.deleteByScenicId(scenicId);
            RecommendLinesectionDataHelper recommendLinesectionDataHelper = new RecommendLinesectionDataHelper(activity);
            recommendLinesectionDataHelper.deleteByScenicId(scenicId);
            MapInfoModel.remove(scenicId);

        }
        else
        {
            FileUtils.deleteFile(Config.Map_FILEPATH + scenicId + ".zip");
            FileUtils.deleteDir(Config.UU_FILEPATH + scenicId);
            MapInfoModel.remove(scenicId);
            RecommendLine.remove(scenicId);
        }
        return true;
    }
}
