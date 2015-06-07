package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/*
 * ��������  ����home page�� ���������е���Ϣ
 * 
 * */

@Table(name = "scenicAreaJson")
public class ScenicAreaJson extends Model implements Serializable {
    @Expose
    @Column(name = "scenicId")
    private String scenicId;
    @Expose
    @Column(name = "scenicName")
    private String scenicName;
    @Expose
    @Column(name = "scenicLocation")
    private String scenicLocation;
    @Expose
    @Column(name = "smallImage")
    private String smallImage;
    @Expose
    @Column(name = "lat")
    private Double lat;  //γ��
    @Expose
    @Column(name = "lng")
    private Double lng;
    @Expose
    @Column(name = "right_lat")
    private Double right_lat;  //γ��

    public Double getRight_lat() {
        return right_lat;
    }

    public void setRight_lat(Double right_lat) {
        this.right_lat = right_lat;
    }

    public Double getRight_lng() {
        return right_lng;
    }

    public void setRight_lng(Double righ_lng) {
        this.right_lng = righ_lng;
    }

    @Expose
    @Column(name = "right_lng")
    private Double right_lng;
    @Expose
    @Column(name = "warning")
    private String warning;  // 0 ��ɫ 1����ɫ 2����ɫ 3����ɫԤ��
    /*��ֵ��ݾ��������������ѽ�������ȷ����
     * */
    @Expose
    @Column(name = "city")
    private String city;
    @Expose
    @Column(name = "mapsize")
    private String mapSize;
    @Expose
    @Column(name = "province")
    private String province;
    @Expose
    @Column(name = "desc")
    private String desc;

    public String getMapSize() {
        return mapSize;
    }

    public void setMapSize(String mapSize) {
        this.mapSize = mapSize;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Expose
    @Column(name = "scenicType")
    private String scenicType;   //��Ȼ�羰��
    @Expose
    @Column(name = "commentsNum")
    private Integer commentsNum;
    @Expose
    @Column(name = "favourNum")
    private Integer favourNum;
    @Expose
    @Column(name = "imageUrl")
    private String imageUrl;
    @Expose
    @Column(name = "scenicLevel")
    private String scenicLevel;  //5A 4A ��
    private String distance;

    public ScenicAreaJson() {
        distance = "";
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public ScenicAreaJson load(String ScenicId) {
        try {

            List<ScenicAreaJson> lineList = new Select().from(ScenicAreaJson.class)
                    .where("scenicId = ?", ScenicId).execute();
            if (null != lineList)
                return lineList.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void remove(String ScenicId) {
        try {

            List<ScenicAreaJson> scenicList = new Select().from(ScenicAreaJson.class)
                    .where("scenicId = ?", ScenicId).execute();
            if (null != scenicList && scenicList.size() > 0)
                scenicList.get(0).delete();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
