package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;
@Table(name="scenic_introduction")
public class ScenicIntroductionJson extends Model implements Serializable {
    @Expose
    @Column(name = "scenicId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String scenicId;
    @Expose
    @Column(name = "scenicLevel")
    private String scenicLevel;
    @Expose
    @Column(name = "scenicType")
    private String scenicType;
    @Expose
    @Column(name = "desc")
    private String desc;
    @Expose
    private List<RecommendImage> imageList;

    public String getScenicLevel() {
        return scenicLevel;
    }

    public void setScenicLevel(String scenicLevel) {
        this.scenicLevel = scenicLevel;
    }

    public String getScenicType() {
        return scenicType;
    }

    public void setScenicType(String scenicType) {
        this.scenicType = scenicType;
    }

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

    public List<RecommendImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<RecommendImage> imageList) {
        this.imageList = imageList;
    }

    public static ScenicIntroductionJson load(String ScenicId) {
        try {

            List<ScenicIntroductionJson> lineList = new Select().from(ScenicIntroductionJson.class)
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
        return new Select().from(ScenicIntroductionJson.class).execute().size();
    }

    public void remove(String ScenicId) {
        try {

            List<ScenicIntroductionJson> scenicList = new Select().from(ScenicIntroductionJson.class)
                    .where("scenicId = ?", ScenicId).execute();
            if (null != scenicList && scenicList.size() > 0)
            {
                scenicList.get(0).delete();
                RecommendImage.remove(scenicId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
