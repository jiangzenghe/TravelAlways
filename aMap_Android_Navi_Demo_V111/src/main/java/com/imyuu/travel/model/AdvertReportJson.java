package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by java on 2015/5/12.
 */

@Table(name="advert_reports")
public class AdvertReportJson extends Model {
    public AdvertReportJson() {
        super();
    }
    @Expose
    @Column(name = "scenicId")
    private String scenicId;
    @Expose
    @Column(name = "advertId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String advertId;
    @Expose
    @Column(name = "advertScenicId ")
    private String advertScenicId ;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdvertScenicId() {
        return advertScenicId;
    }

    public void setAdvertScenicId(String advertScenicId) {
        this.advertScenicId = advertScenicId;
    }

    public String getAdvertId() {
        return advertId;
    }

    public void setAdvertId(String advertId) {
        this.advertId = advertId;
    }

    @Expose
    @Column(name = "userId ")
    private String userId ;

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    @Override
    public String toString() {
        return "AdvertReportJson{" +
                "scenicId='" + scenicId + '\'' +
                ", advertId='" + advertId + '\'' +
                ", advertScenicId='" + advertScenicId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public static AdvertReportJson load(String ScenicId) {
        try {

            List<AdvertReportJson> lineList = new Select().from(AdvertReportJson.class)
                    .where("scenicId = ?", ScenicId).execute();
            if (null != lineList)
                return lineList.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int count()
    {
        return new Select().from(AdvertReportJson.class).execute().size();
    }
    public void remove(String ScenicId) {
        try {

            List<AdvertReportJson> scenicList = new Select().from(AdvertReportJson.class)
                    .where("scenicId = ?", ScenicId).execute();
            if (null != scenicList && scenicList.size() > 0)
                scenicList.get(0).delete();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
