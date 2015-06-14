package com.imyuu.travel.bean;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 20:29 $
 *
 * @author Frank
 * @since 1.0
 */
public class RecommendLinesectionguideModel {
    private Integer id;
    private Long createdTime;
    private String recommendLinesectionguideId;
    private String recommendrouteId;
    private String recommendrouteDetailid;
    private Integer routeOrder;
    private Double relativeLongitude;
    private Double relativeLatitude;
    private Double absoluteLongitude;
    private Double absoluteLatitude;
    private String recommendrouteDetailname;
    private String recommendrouteName;

    public static final String KEY = "_id";
    public static final String CREATED = "createdTime";
    public static final String RecommendLinesectionguideId = "recommendLinesectionguideId";
    public static final String RecommendrouteId = "recommendrouteId";
    public static final String RecommendrouteDetailid = "recommendrouteDetailid";
    public static final String RouteOrder = "routeOrder";
    public static final String RelativeLongitude = "relativeLongitude";
    public static final String RelativeLatitude = "relativeLatitude";
    public static final String AbsoluteLongitude = "absoluteLongitude";
    public static final String AbsoluteLatitude = "absoluteLatitude";
    public static final String RecommendrouteDetailname = "recommendrouteDetailname";
    public static final String RecommendrouteName = "recommendrouteName";

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

    public String getRecommendLinesectionguideId() {
        return recommendLinesectionguideId;
    }

    public void setRecommendLinesectionguideId(String recommendLinesectionguideId) {
        this.recommendLinesectionguideId = recommendLinesectionguideId;
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

    public Double getRelativeLongitude() {
        return relativeLongitude;
    }

    public void setRelativeLongitude(Double relativeLongitude) {
        this.relativeLongitude = relativeLongitude;
    }

    public Double getRelativeLatitude() {
        return relativeLatitude;
    }

    public void setRelativeLatitude(Double relativeLatitude) {
        this.relativeLatitude = relativeLatitude;
    }

    public Double getAbsoluteLongitude() {
        return absoluteLongitude;
    }

    public void setAbsoluteLongitude(Double absoluteLongitude) {
        this.absoluteLongitude = absoluteLongitude;
    }

    public Double getAbsoluteLatitude() {
        return absoluteLatitude;
    }

    public void setAbsoluteLatitude(Double absoluteLatitude) {
        this.absoluteLatitude = absoluteLatitude;
    }

    public String getRecommendrouteDetailid() {
        return recommendrouteDetailid;
    }

    public void setRecommendrouteDetailid(String recommendrouteDetailid) {
        this.recommendrouteDetailid = recommendrouteDetailid;
    }

    public String getRecommendrouteDetailname() {
        return recommendrouteDetailname;
    }

    public void setRecommendrouteDetailname(String recommendrouteDetailname) {
        this.recommendrouteDetailname = recommendrouteDetailname;
    }

    public String getRecommendrouteName() {
        return recommendrouteName;
    }

    public void setRecommendrouteName(String recommendrouteName) {
        this.recommendrouteName = recommendrouteName;
    }
}
