package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Table(name = "ProvinceInfoJson")
public class ProvinceInfoJson extends Model implements Serializable {

	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public List<ProvinceCityJson> getCityList() {
		return cityList;
	}
	public void setCityList(List<ProvinceCityJson> cityList) {
		this.cityList = cityList;
	}
	public String getMapSize() {
		return mapSize;
	}
	public void setMapSize(String mapSize) {
		this.mapSize = mapSize;
	}
	
//	public ProvinceInfoJson() {
//		super();
//		this.province = "";
//		//this.cityList = new ArrayList<ProvinceCityJson>();
//		this.mapSize = "0";
//	}
    @Expose
    @Column(name = "province", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String province;
    @Expose
	private List<ProvinceCityJson> cityList;
    @Expose
    @Column(name = "mapSize")
	private String mapSize;
}
