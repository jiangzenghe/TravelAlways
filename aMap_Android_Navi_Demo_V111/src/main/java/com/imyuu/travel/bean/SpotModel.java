package com.imyuu.travel.bean;


import com.amap.api.maps2d.model.LatLng;

public class SpotModel {

    public SpotModel(int routeIndex, LatLng latlng) {
//        this.id = id;
        this.routeIndex = routeIndex;
        this.latLng = latlng;
    }

    private Integer id;

	private int routeIndex;
    
    private LatLng latLng;

    public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRouteIndex() {
        return routeIndex;
    }

    public void setRouteIndex(int routeIndex) {
        this.routeIndex = routeIndex;
    }
}
