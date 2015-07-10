package com.imyuu.travel.util;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.imyuu.travel.model.ScenicPointJson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by java on 2015/5/8.
 */
public class SpotDataSorter {
    public static List<ScenicPointJson> sortByDistance(List<ScenicPointJson> spotList, LatLng latlng) {
        List<ScenicPointJson> retList = new ArrayList<ScenicPointJson>();
        for (ScenicPointJson spj : spotList) {
            spj.setDistance(AMapUtils.calculateLineDistance(latlng, new LatLng(spj.getLat(), spj.getLng())));
            retList.add(spj);
        }
        Collections.sort(retList, new SortDataByDistance());
        return retList;
    }
}
