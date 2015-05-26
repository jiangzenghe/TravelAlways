package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name="cityinfo")
public class CityInfoJson extends Model implements java.io.Serializable{
 
		public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
		@Override
	public String toString() {
		return "CityInfoJson [cityid=" + cityid + ", cityname=" + cityname
				+ "]";
	}


    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pingyin) {
        this.pinyin = pingyin;
    }
    @Column(name="cityid")
    private String cityid;
    @Column(name="cityname")
    private String cityname;
    @Column(name="pinyin")
    private String pinyin;
	}


