package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

@Table(name="scenic_transport")
public class ScenicTransportJson extends Model{
    @Expose
    @Column(name = "scenicId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String scenicId;
    @Expose
    @Column(name = "desc")
    private String desc;
    @Expose
    @Column(name = "imageURL")
    private String imageURL;

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ScenicTransportJson{" +
                "scenicId='" + scenicId + '\'' +
                ", desc='" + desc + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public static ScenicTransportJson load(String ScenicId) {
        try {

            List<ScenicTransportJson> lineList = new Select().from(ScenicTransportJson.class)
                    .where("scenicId = ?", ScenicId).execute();
            if (null != lineList && lineList.size()>0)
                return lineList.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int count()
    {
        return new Select().from(ScenicTransportJson.class).execute().size();
    }
    public void remove(String ScenicId) {
        try {

            List<ScenicTransportJson> scenicList = new Select().from(ScenicTransportJson.class)
                    .where("scenicId = ?", ScenicId).execute();
            if (null != scenicList && scenicList.size() > 0)
                scenicList.get(0).delete();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}