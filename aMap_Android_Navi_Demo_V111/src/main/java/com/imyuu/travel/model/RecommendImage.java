package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

@Table(name = "scenic_recommend_image")
public class RecommendImage extends Model implements Serializable {
    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    public String getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }

    @Expose
    @Column(name = "recommendId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String recommendId;
    @Expose
    @Column(name = "scenicId")
    private String scenicId;
    @Expose
    @Column(name = "name")
    private String name;
    @Expose
    @Column(name = "imageUrl")
    private String imageUrl;
    @Expose
    @Column(name = "intentLink")
    private String intentLink;// ��ת Ŀ��

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Recommend{" +
                "name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", intentLink='" + intentLink + '\'' +
                '}';
    }

    public String getIntentLink() {
        return intentLink;
    }

    public void setIntentLink(String intentLink) {
        this.intentLink = intentLink;
    }

    public static void remove(String ScenicId) {
        try {

            List<RecommendImage> lineList = new Select().from(RecommendImage.class)
                    .where("scenicId = ?", ScenicId).execute();
            if (lineList != null && lineList.size() > 0)
                for (RecommendImage recommend : lineList)
                    recommend.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<RecommendImage> load(String ScenicId) {
        try {

            List<RecommendImage> lineList = new Select().from(RecommendImage.class)
                    .where("scenicId = ?", ScenicId).execute();

                return lineList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}




