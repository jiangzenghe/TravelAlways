package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

@Table(name = "scenic_recommend_line")
public class RecommendLine extends Model implements Serializable {
    @Expose
    @Column(name = "lineid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String lineId;
    @Expose
    @Column(name = "linename")
    private String lineName;
    @Expose
    @Column(name = "scenicId")
    private String scenicId;
    @Expose
    private List<SpotInfo> lineSectionList;

    public RecommendLine() {
        super();
    }

    public static List<RecommendLine> getAll(String scenicId) {
        try {
            return new Select()
                    .from(RecommendLine.class)
                    .where("scenicId = ?", scenicId)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public List<SpotInfo> getLineSectionList() {
        return lineSectionList;
    }

    public void setLineSectionList(List<SpotInfo> lineSectionList) {
        this.lineSectionList = lineSectionList;
    }

    public List<SpotInfo> getLineSpots() {

        try {
            lineSectionList = new Select()
                    .from(SpotInfo.class)
                    .where("lineId = ?", lineId)
                            //  .orderBy("order ASC")    //order 不能作为字段名存在
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lineSectionList;
    }

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    @Override
    public String toString() {
        return "RecommendLine [lineId=" + lineId + ", lineName="
                + lineName + ", lineSectionList=" + lineSectionList
                + "]";
    }

    public RecommendLine load(String scenicId) {
        try {

            List<RecommendLine> lineList = new Select().from(RecommendLine.class)
                    .where("scenicId = ?", scenicId).execute();
            if (null != lineList)
                return lineList.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void remove(String scenicId) {
        try {

            List<RecommendLine> lineList = new Select().from(RecommendLine.class)
                    .where("scenicId = ?", scenicId).execute();
            if (null != lineList && lineList.size() > 0)
                for (RecommendLine recommendLine : lineList) {
                    SpotInfo spotInfo = new SpotInfo();
                    spotInfo.remove(recommendLine.getLineId());
                    recommendLine.delete();
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
