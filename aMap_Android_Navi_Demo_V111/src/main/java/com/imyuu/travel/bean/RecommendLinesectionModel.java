package com.imyuu.travel.bean;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 19:28 $
 *
 * @author Frank
 * @since 1.0
 */
public class RecommendLinesectionModel {
    private Integer id;
    private Long createdTime;
    private String recommendLinesectionId;
    private String scenicId;
    private String recommendrouteId;
    private Integer routeOrder;
    private String aspotId;
    private String bspotId;
    private String abTotaltime;
    private String ascenicspotName;
    private String bscenicspotName;
    private String recommendRoutename;
    private String scenicName;

    public static final String KEY = "_id";
    public static final String CREATED = "createdTime";
    public static final String ScenicId = "scenicId";
    public static final String RecommendLinesectionId = "recommendLinesectionId";
    public static final String RecommendrouteId = "recommendrouteId";
    public static final String RouteOrder = "routeOrder";
    public static final String AspotId = "aspotId";
    public static final String BspotId = "bspotId";
    public static final String AbTotaltime = "abTotaltime";
    public static final String AscenicspotName = "ascenicspotName";
    public static final String BscenicspotName = "bscenicspotName";
    public static final String RecommendRoutename = "recommendRoutename";
    public static final String ScenicName = "scenicName";

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

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

    public String getRecommendLinesectionId() {
        return recommendLinesectionId;
    }

    public void setRecommendLinesectionId(String recommendLinesectionId) {
        this.recommendLinesectionId = recommendLinesectionId;
    }

    public String getRecommendrouteId() {
        return recommendrouteId;
    }

    public void setRecommendrouteId(String recommendrouteId) {
        this.recommendrouteId = recommendrouteId;
    }

    public Integer getRouteOrder() {
        return routeOrder;
    }

    public void setRouteOrder(Integer routeOrder) {
        this.routeOrder = routeOrder;
    }

    public String getAspotId() {
        return aspotId;
    }

    public void setAspotId(String aspotId) {
        this.aspotId = aspotId;
    }

    public String getBspotId() {
        return bspotId;
    }

    public void setBspotId(String bspotId) {
        this.bspotId = bspotId;
    }

    public String getAbTotaltime() {
        return abTotaltime;
    }

    public void setAbTotaltime(String abTotaltime) {
        this.abTotaltime = abTotaltime;
    }

    public String getAscenicspotName() {
        return ascenicspotName;
    }

    public void setAscenicspotName(String ascenicspotName) {
        this.ascenicspotName = ascenicspotName;
    }

    public String getBscenicspotName() {
        return bscenicspotName;
    }

    public void setBscenicspotName(String bscenicspotName) {
        this.bscenicspotName = bscenicspotName;
    }

    public String getRecommendRoutename() {
        return recommendRoutename;
    }

    public void setRecommendRoutename(String recommendRoutename) {
        this.recommendRoutename = recommendRoutename;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }
}
