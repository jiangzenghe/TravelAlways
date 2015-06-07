package com.imyuu.travel.model;


import com.google.gson.annotations.Expose;

public class UserLocationModel implements java.io.Serializable {
    @Expose
    private String reportId;
    @Expose
    private Double lat;
    @Expose
    private Double lng;
    @Expose
    private String userId;
    @Expose
    private String scenicId;
    @Expose
    private String reportTime;
    @Expose
    private String loginName;
    //user portait
    @Expose
    private String icon_path;
    @Expose
    private String remark;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    @Override
    public String toString() {
        return "UserLocationModel{" +
                "reportId='" + reportId + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", userId='" + userId + '\'' +
                ", scenicId='" + scenicId + '\'' +
                ", reportTime='" + reportTime + '\'' +
                ", loginName='" + loginName + '\'' +
                ", icon_path='" + icon_path + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String nickName) {
        this.loginName = nickName;
    }

    public String getIcon_path() {
        return icon_path;
    }

    public void setIcon_path(String icon_path) {
        this.icon_path = icon_path;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

}


