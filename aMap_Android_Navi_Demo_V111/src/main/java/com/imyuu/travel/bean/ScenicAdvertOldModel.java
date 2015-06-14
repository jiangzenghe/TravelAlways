package com.imyuu.travel.bean;

/**
 * $Author: Frank $
 * $Date: 2015/1/8 20:05 $
 *
 * @author Frank
 * @since 1.0
 */
public class ScenicAdvertOldModel {
    private Integer id;
    private Long createdTime;
    private String scenicAdvertId;
    private String scenicId;
    private String scenicName;
    private String pubTime;
    private String advertAdmin;
    private String advertPic;
    private String advertLink;
    private String advertRemark;
    private String advertscenicId;
    private String advertscenicName;

    public static final String KEY = "_id";
    public static final String CREATED = "createdTime";
    public static final String ScenicAdvertId = "scenicAdvertId";
    public static final String ScenicId = "scenicId";
    public static final String ScenicName = "scenicName";
    public static final String PubTime = "pubTime";
    public static final String AdvertAdmin = "advertAdmin";
    public static final String AdvertPic = "advertPic";
    public static final String AdvertLink = "advertLink";
    public static final String AdvertRemark = "advertRemark";
    public static final String AdvertscenicId = "advertscenicId";
    public static final String AdvertscenicName = "advertscenicName";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getScenicAdvertId() {
        return scenicAdvertId;
    }

    public void setScenicAdvertId(String scenicAdvertId) {
        this.scenicAdvertId = scenicAdvertId;
    }

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getAdvertAdmin() {
        return advertAdmin;
    }

    public void setAdvertAdmin(String advertAdmin) {
        this.advertAdmin = advertAdmin;
    }

    public String getAdvertPic() {
        return advertPic;
    }

    public void setAdvertPic(String advertPic) {
        this.advertPic = advertPic;
    }

    public String getAdvertLink() {
        return advertLink;
    }

    public void setAdvertLink(String advertLink) {
        this.advertLink = advertLink;
    }

    public String getAdvertRemark() {
        return advertRemark;
    }

    public void setAdvertRemark(String advertRemark) {
        this.advertRemark = advertRemark;
    }

    public String getAdvertscenicId() {
        return advertscenicId;
    }

    public void setAdvertscenicId(String advertscenicId) {
        this.advertscenicId = advertscenicId;
    }

    public String getAdvertscenicName() {
        return advertscenicName;
    }

    public void setAdvertscenicName(String advertscenicName) {
        this.advertscenicName = advertscenicName;
    }
}
