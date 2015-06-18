package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by java on 2015/6/5.
 */
@Table(name="scenic_advert")
public class ScenicAdvertJson extends Model{
    public String getAdvertId() {
        return advertId;
    }

    public void setAdvertId(String advertId) {
        this.advertId = advertId;
    }

    @Expose
    @Column(name = "advert_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String advertId;
    @Expose
    @Column(name = "scenic_name")
    private String scenicName;
    @Expose
    @Column(name = "advert_pic")
    private String advertPic;
    @Expose
    @Column(name = "advertscenic_id")
    private String advertScenicId;
    @Expose
    @Column(name = "advertscenic_name")
    private String advertScenicName;
    @ Expose
    @Column(name = "latitude")
    private Double lat;  //纬度
    @ Expose
    @Column(name = "longtitude")
    private Double lng;  //经度
    @Expose
    @Column(name = "advert_type")
    private String advertType;
    @Expose
    @Column(name = "scenicId")
    private String scenicId;

    @Override
    public String toString() {
        return "ScenicAdvertJson{" +
                "scenicName='" + scenicName + '\'' +
                ", advertPic='" + advertPic + '\'' +
                ", advertScenicId='" + advertScenicId + '\'' +
                ", advertScenicName='" + advertScenicName + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", advertType='" + advertType + '\'' +
                ", scenicId='" + scenicId + '\'' +
                '}';
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public String getAdvertPic() {
        return advertPic;
    }

    public void setAdvertPic(String advertPic) {
        this.advertPic = advertPic;
    }

    public String getAdvertScenicId() {
        return advertScenicId;
    }

    public void setAdvertScenicId(String advertScenicId) {
        this.advertScenicId = advertScenicId;
    }

    public String getAdvertScenicName() {
        return advertScenicName;
    }

    public void setAdvertScenicName(String advertScenicName) {
        this.advertScenicName = advertScenicName;
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

    public String getAdvertType() {
        return advertType;
    }

    public void setAdvertType(String advertType) {
        this.advertType = advertType;
    }

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    public static List<ScenicAdvertJson> load(String scenicId) {
        try {

            List<ScenicAdvertJson> lineList = new Select().from(ScenicAdvertJson.class)
                    .where(" scenicId = ?", scenicId)
                    .execute();

            return lineList;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean  remove(String scenicId) {
        try {

            new Delete().from(ScenicAdvertJson.class).where("scenicId = ?", scenicId).execute();


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
