package com.imyuu.travel.bean;

/**
 * $Author: Frank $
 * $Date: 2014/12/28 14:49 $
 *
 * @author Frank
 * @since 1.0
 */
public class ScenicRecommendLineModel {
    private Integer id;
    private Long createdTime;
    private String scenicRecommendLineId;
    private String scenicId;
    private String scenicName;
    private String recommendRoutename;
    private String routeTotaltime;
    private String recommendRoutenote;

    public static final String KEY = "_id";
    public static final String CREATED = "createdTime";
    public static final String ScenicId = "scenicId";
    public static final String ScenicRecommendLineId = "scenicRecommendLineId";
    public static final String ScenicName = "scenicName";
    public static final String RecommendRoutename = "recommendRoutename";
    public static final String RouteTotaltime = "routeTotaltime";
    public static final String RecommendRoutenote = "recommendRoutenote";

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

    public String getScenicRecommendLineId() {
        return scenicRecommendLineId;
    }

    public void setScenicRecommendLineId(String scenicRecommendLineId) {
        this.scenicRecommendLineId = scenicRecommendLineId;
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

    public String getRecommendRoutename() {
        return recommendRoutename;
    }

    public void setRecommendRoutename(String recommendRoutename) {
        this.recommendRoutename = recommendRoutename;
    }

    public String getRouteTotaltime() {
        return routeTotaltime;
    }

    public void setRouteTotaltime(String routeTotaltime) {
        this.routeTotaltime = routeTotaltime;
    }

    public String getRecommendRoutenote() {
        return recommendRoutenote;
    }

    public void setRecommendRoutenote(String recommendRoutenote) {
        this.recommendRoutenote = recommendRoutenote;
    }
}
