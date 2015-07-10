package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
@Table(name = "ProvinceCityJson")
public class ProvinceCityJson extends Model {
    @Expose
    @Column(name = "cityName", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String cityName;
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getMapSize() {
		return mapSize;
	}
	public void setMapSize(String mapSize) {
		this.mapSize = mapSize;
	}
	public List<CityScenesJson> getSceneList() {
		return sceneList;
	}
	public void setSceneList(List<CityScenesJson> sceneList) {
		this.sceneList = sceneList;
	}
    @Expose
    @Column(name = "mapSize")
	private String mapSize;
    @Expose
	private List<CityScenesJson> sceneList;
//	public ProvinceCityJson()
//	{
//
//        //sceneList = new ArrayList<CityScenesJson>();
//	}
}
