package com.imyuu.travel.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;


/**
 * City信息
 * 
 * @author 
 * 
* <p>Date       Author      Description</p>
 *<p>------------------------------------------------------------------</p>
 *<p>         创建  </p>
 *
 */
public class City implements Parcelable{

	public City() {
	}
	
	public City(String name,String py) {
		this.cityName = name;
		this.cityPY =py;
	}
	
	private String cityCode;
	private String cityName;
	private String cityPY;
	private LatLng cityPosition;
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityPY() {
		return cityPY;
	}
	public void setCityPY(String cityPY) {
		this.cityPY = cityPY;
	}
	public LatLng getCityPosition() {
		return cityPosition;
	}
	public void setCityPosition(LatLng cityPosition) {
		this.cityPosition = cityPosition;
	}

	public static final Creator<City> CREATOR
	    = new Creator<City>() {
		public City createFromParcel(Parcel in) {  
		    return new City(in);  
		}  
		
		public City[] newArray(int size) {  
		    return new City[size];  
		}  
	}; 
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(cityCode);  
		dest.writeString(cityName); 
		dest.writeString(cityPY); 
		dest.writeParcelable(cityPosition, flags); 
	}
	
	//inyuout sequence
	private City(Parcel in) {  
		cityCode = in.readString();  
		cityName = in.readString();  
		cityPY = in.readString();
		cityPosition = in.readParcelable(LatLng.class.getClassLoader());
    } 
	
//	public int compare(City s1, City s2) {
//		return s1.getShortPName().compareToIgnoreCase(s2.getPName());
//	}
}
