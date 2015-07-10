package com.imyuu.travel.view;

import com.amap.api.maps.model.LatLng;

import com.imyuu.travel.model.ScenicPointJson;

public class RegionItem {

    private LatLng mLatLng;
    private ScenicPointJson mObject;
    private String mtypeName;

    public RegionItem(LatLng latlng, String s, ScenicPointJson obj) {
        mLatLng = latlng;
        mtypeName = s;
        mObject = obj;
    }

    public Object getObject() {
        return mObject;
    }

    public LatLng getPosition() {
        return mLatLng;
    }

    public String getTypeName() {
        return mtypeName;
    }

    public void setMtypeName(String s) {
        mtypeName = s;
    }

    public void setmObject(ScenicPointJson obj) {
        mObject = obj;
    }
}
