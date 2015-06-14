package com.imyuu.travel.bean;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 19:10 $
 *
 * @author Frank
 * @since 1.0
 */
public class ScenicMapOldModel {
    private Integer id;
    private Long createdTime;
    private String scenicMapId;
    private String scenicId;
    private String scenicName;
    private String scenicspotName;
    private Double relativeLongitude;
    private Double relativeLatitude;
    private Double absoluteLongitude;
    private Double absoluteLatitude;
    private Double relativeHeight;
    private Double relativeWidth;
    private String scenicspotNote;
    private String scenicspotVoice;
    private String scenicspotMarkertype;
    private String scenicspotSmallpic;

    public static final String KEY = "_id";
    public static final String CREATED = "createdTime";
    public static final String ScenicMapId = "scenicMapId";
    public static final String ScenicId = "scenicId";
    public static final String ScenicspotName = "scenicspotName";
    public static final String ScenicName = "scenicName";
    public static final String RelativeLongitude = "relativeLongitude";
    public static final String RelativeLatitude = "relativeLatitude";
    public static final String AbsoluteLongitude = "absoluteLongitude";
    public static final String AbsoluteLatitude = "absoluteLatitude";
    public static final String ScenicspotNote = "scenicspotNote";
    public static final String ScenicspotVoice = "scenicspotVoice";
    public static final String ScenicspotMarkertype = "scenicspotMarkertype";
    public static final String ScenicspotSmallpic = "scenicspotSmallpic";
    public static final String RelativeHeight = "relativeHeight";
    public static final String RelativeWidth = "relativeWidth";

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

    public String getScenicMapId() {
        return scenicMapId;
    }

    public void setScenicMapId(String scenicMapId) {
        this.scenicMapId = scenicMapId;
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

    public String getScenicspotName() {
        return scenicspotName;
    }

    public void setScenicspotName(String scenicspotName) {
        this.scenicspotName = scenicspotName;
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

    public String getScenicspotNote() {
        return scenicspotNote;
    }

    public void setScenicspotNote(String scenicspotNote) {
        this.scenicspotNote = scenicspotNote;
    }

    public String getScenicspotVoice() {
        return scenicspotVoice;
    }

    public void setScenicspotVoice(String scenicspotVoice) {
        this.scenicspotVoice = scenicspotVoice;
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

    public Double getRelativeHeight() {
        return relativeHeight;
    }

    public void setRelativeHeight(Double relativeHeight) {
        this.relativeHeight = relativeHeight;
    }

    public Double getRelativeWidth() {
        return relativeWidth;
    }

    public void setRelativeWidth(Double relativeWidth) {
        this.relativeWidth = relativeWidth;
    }

    public String getScenicspotMarkertype() {
        return scenicspotMarkertype;
    }

    public void setScenicspotMarkertype(String scenicspotMarkertype) {
        this.scenicspotMarkertype = scenicspotMarkertype;
    }

    public String getScenicspotSmallpic() {
        return scenicspotSmallpic;
    }

    public void setScenicspotSmallpic(String scenicspotSmallpic) {
        this.scenicspotSmallpic = scenicspotSmallpic;
    }
}
