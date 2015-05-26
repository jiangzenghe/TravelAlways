package com.imyuu.travel.base;

import android.content.Context;
import android.location.Location;
import com.amap.api.maps.MapView;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.imyuu.travel.model.ScenicAreaJson;

import java.text.DecimalFormat;
import java.util.List;


public class AMapHelper  {

   // private GeocodeSearch geocoderSearch;
    private Context context;
    private AMap aMap;
    private String myCityCode = null;
    private static AMapHelper mapHelper;

    public static AMapHelper getInstance()
    {
       if(null == mapHelper)
           mapHelper = new AMapHelper();
        return  mapHelper;
    }


    public static String computeDistance(LatLng src,LatLng dest)
	{
		double d = AMapUtils.calculateLineDistance(src, dest);
        if (d != 0.0D)
        {
            if (d >= 500*1000D)
            {
                return  ">500K米";

            }
           else if (d >= 1000D)
            {
                String dx = new DecimalFormat("0.00").format(d/1000);
                 return  dx+"k米";
                
            } else
            {
            	return d+"米";
            }
        }
        return "未知";
	}

    public static void updateScenicAreaDistance(List<ScenicAreaJson>scenicAreaJsons)
    {
        if (AppApplication.getInstance().getMyLocation() != null) {
            LatLng src = AppApplication.getInstance().getMyLocation();
            for (ScenicAreaJson scenicAreaJson : scenicAreaJsons) {

                if (scenicAreaJson.getLat() != null
                        && scenicAreaJson.getLng() != null) {
                    LatLng dest = new LatLng(scenicAreaJson.getLat(), scenicAreaJson.getLng());
                    String distance = AMapHelper.computeDistance(src, dest);
                    scenicAreaJson.setDistance(distance);
                }
            }
        }
    }
    
}
