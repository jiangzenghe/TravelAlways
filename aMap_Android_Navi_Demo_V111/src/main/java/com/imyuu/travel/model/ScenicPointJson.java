package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

/*
 * ����������Ϣ
 * */

@Table(name = "scenicpoints")
public class ScenicPointJson extends Model {
    @Expose
    @Column(name = "spotId")
    private String id;
    @Expose
    @Column(name = "scenicPointName")
    private String scenicPointName;
    @Expose
    @Column(name = "desc")
    private String desc;
    @Expose
    @Column(name = "audioUrl")
    private String audioUrl;
    @Expose
    @Column(name = "imageUrl")
    private String imageUrl;
    @Expose
    @Column(name = "lat")
    private Double lat;  //γ��
    @Expose
    @Column(name = "lng")
    private Double lng;  //����
    @Expose
    @Column(name = "px")
    private Integer px;  //ͼ�ϵ�λ��
    private Integer py;
    @Expose
    @Column(name = "spotType")
    private String spotType;
    @Expose
    @Column(name = "scenicId", index = true)
    private String scenicId;
    private double distance;

    public ScenicPointJson() {


    }

    public ScenicPointJson(String id, String scenicPointName, String desc,
                           String audioUrl, String imageUrl, Double lat, Double lng, int px,
                           int py, String spotType) {
        this.id = id;
        this.scenicPointName = scenicPointName;
        this.desc = desc;
        this.audioUrl = audioUrl;
        this.imageUrl = imageUrl;
        this.lat = lat;
        this.lng = lng;
        this.px = px;
        this.py = py;
        this.spotType = spotType;
    }

    public static List<ScenicPointJson> loadAll(String ScenicId) {
        try {
            return new Select()
                    .from(ScenicPointJson.class)
                    .where("scenicId = ?", ScenicId)
                    .orderBy("spotId ASC")
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    public String getSpotId() {
        return this.id;
    }

    public void setSpotId(String id) {
        this.id = id;
    }

    public String getScenicPointName() {
        return scenicPointName;
    }

    public void setScenicPointName(String scenicPointName) {
        this.scenicPointName = scenicPointName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Integer getPx() {
        return px;
    }

    public void setPx(Integer px) {
        this.px = px;
    }

    public int getPy() {
        return py;
    }

    public void setPy(Integer py) {
        this.py = py;
    }

    public String getSpotType() {
        return spotType;
    }

    public void setSpotType(String spotType) {
        this.spotType = spotType;
    }

    @Override
    public String toString() {
        return "ScenicPointJson [id=" + id + ", scenicPointName="
                + scenicPointName + ", desc=" + desc + ", audioUrl=" + audioUrl
                + ", imageUrl=" + imageUrl + ", lat=" + lat + ", lng=" + lng
                + ", px=" + px + ", py=" + py + ", spotType=" + spotType + "]";
    }

    public void remove(String ScenicId) {
        try {

            List<ScenicPointJson> lineList = new Select().from(ScenicPointJson.class)
                    .where("scenicId = ?", ScenicId).execute();
            if (lineList != null && lineList.size() > 0)
                for (ScenicPointJson spotInfo : lineList)
                    spotInfo.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
