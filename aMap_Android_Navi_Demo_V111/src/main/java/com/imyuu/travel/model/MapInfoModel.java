package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by java on 2015/6/1.
 */
@Table(name="Map_Info_Model")
public class MapInfoModel  extends Model{
    @Expose
    @Column(name="scenicId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String scenicId;
    @Expose
    @Column(name="scenicName")
    private String scenicName;
    @Expose
    @Column(name="city")
    private String city;
    @Expose
    @Column(name="downloadTime")
    private String downloadTime;
    @Expose
    @Column(name="fileSize")
    private String fileSize;
    @Expose
    @Column(name="imagePath")
    private String imagePath;
    @Expose
    @Column(name="filePath")
    private String filePath;

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    @Expose
    @Column(name="versionID")
    private String versionID;

    public MapInfoModel()
    {
        super();
    }

    public MapInfoModel(String scenicId, String scenicName, String city, String downloadTime, String fileSize, String imagePath, String filePath) {
        super();
        this.scenicId = scenicId;
        this.scenicName = scenicName;
        this.city = city;
        this.downloadTime = downloadTime;
        this.fileSize = fileSize;
        this.imagePath = imagePath;
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "MapInfoModel{" +
                "scenicId='" + scenicId + '\'' +
                ", scenicName='" + scenicName + '\'' +
                ", city='" + city + '\'' +
                ", downloadTime='" + downloadTime + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static int count()
    {
       return new Select().from(MapInfoModel.class).execute().size();
    }
    public static List<MapInfoModel>  load() {
        try {

            List<MapInfoModel> lineList = new Select().from(MapInfoModel.class)
                    .orderBy("downloadTime ASC")
                    .execute();

            return lineList;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean  isDownload(String scenicId) {
        try {

            List<MapInfoModel> lineList = new Select().from(MapInfoModel.class)
                    .where("scenicId = ?", scenicId).orderBy("downloadTime ASC")
                    .execute();
            if(lineList != null && lineList.size()>0)
            return true;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean  remove(String scenicId) {
        try {

            new Delete().from(MapInfoModel.class).where("scenicId = ?", scenicId).execute();


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
