package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;

/*
 * ��������  ����home page�� ���������е���Ϣ
 * 
 * */

 @Table(name="scenicAreaJson")
 public class ScenicAreaJson  extends Model implements Serializable {

    @Column(name="scenicId")
	private String scenicId;
    @Column(name="scenicName")
	private String scenicName;
    @Column(name="scenicLocation")
	private String scenicLocation;
     @Column(name="smallImage")
	private String smallImage;
     @Column(name="lat")
	private Double lat;  //γ��
     @Column(name="lng")
	private Double lng;
    @Column(name="warning")
	private String warning;  // 0 ��ɫ 1����ɫ 2����ɫ 3����ɫԤ�� 
	/*��ֵ��ݾ��������������ѽ�������ȷ����
	 * */

     @Column(name="city")
     private String city;
     @Column(name="desc")
	private String desc;
     @Column(name="scenicType")
	private String scenicType;   //��Ȼ�羰��
     @Column(name="commentsNum")
	private Integer  commentsNum;
     @Column(name="favourNum")
	private Integer favourNum;
     @Column(name="imageUrl")
	private String  imageUrl;
     @Column(name="scenicLevel")
	private  String scenicLevel;  //5A 4A ��

     public String getDistance() {
         return distance;
     }

     public void setDistance(String distance) {
         this.distance = distance;
     }

     private String distance;
	public ScenicAreaJson(){
        distance="";
	}
	public ScenicAreaJson(String id, String scenicName, String scenicLocation,
			String smallImage, Double lat, Double lng, String warning,
			String city, String desc, String scenicType, Integer commentsNum) {
		this.scenicId = id;
		this.scenicName = scenicName;
		this.scenicLocation = scenicLocation;
		this.smallImage = smallImage;
		this.lat = lat;
		this.lng = lng;
		this.warning = warning;
		this.city = city;
		this.desc = desc;
		this.scenicType = scenicType;
		this.commentsNum = commentsNum;
	}


	@Override
	public String toString() {
		return "ScenicAreaJson [Id=" + scenicId + ", scenicName=" + scenicName
				+ ", scenicLocation=" + scenicLocation + ", smallImage="
				+ smallImage + ", lat=" + lat + ", lng=" + lng + ", warning="
				+ warning + ", city=" + city + ", desc=" + desc
				+ ", scenicType=" + scenicType + ", commentsNum=" + commentsNum
				+ "]";
	}


	public String getScenicType() {
		return scenicType;
	}


	public void setScenicType(String scenicType) {
		this.scenicType = scenicType;
	}


	public Integer getCommentsNum() {
		return commentsNum;
	}


	public void setCommentsNum(Integer commentsNum) {
		this.commentsNum = commentsNum;
	}


	public String getSmallImage() {
		return smallImage;
	}


	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}


	public Double getLat() {
		return lat;
	}


	public void setLat(Double lat) {
		this.lat = lat;
	}


	public Double getLng() {
		return lng;
	}


	public void setLng(Double lng) {
		this.lng = lng;
	}


	public String getWarning() {
		return warning;
	}


	public void setWarning(String warning) {
		this.warning = warning;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getScenicId() {
		return scenicId;
	}


	public void setScenicId(String id) {
		this.scenicId = id;
	}


	public String getScenicName() {
		return scenicName;
	}


	public void setScenicName(String scenicName) {
		this.scenicName = scenicName;
	}


	public String getScenicLocation() {
		return scenicLocation;
	}


	public void setScenicLocation(String scenicLocation) {
		this.scenicLocation = scenicLocation;
	}


	public Integer getFavourNum() {
		return favourNum;
	}


	public void setFavourNum(Integer favourNum) {
		this.favourNum = favourNum;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String getScenicLevel() {
		return scenicLevel;
	}


	public void setScenicLevel(String scenicLevel) {
		this.scenicLevel = scenicLevel;
	}

     public  ScenicAreaJson load(String ScenicId) {
         try {

             List<ScenicAreaJson> lineList   = new Select().from(ScenicAreaJson.class)
                     .where("scenicId = ?", ScenicId).execute();
             if(null != lineList)
                return lineList.get(0);

         }catch(Exception e)
         {
             e.printStackTrace();
         }
        return null;
     }

     public  void remove(String ScenicId) {
         try {

             List<ScenicAreaJson> scenicList   = new Select().from(ScenicAreaJson.class)
                     .where("scenicId = ?", ScenicId).execute();
             if(null != scenicList && scenicList.size()>0)
                 scenicList.get(0).delete();

         }catch(Exception e)
         {
             e.printStackTrace();
         }

     }
}
