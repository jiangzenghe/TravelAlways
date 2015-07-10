package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.google.gson.annotations.Expose;

import java.util.List;

@Table(name = "CityScenesJson")
public class CityScenesJson extends Model {
    @Expose
    @Column(name = "scenicId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
  private String scenicId;
    @Expose
    @Column(name = "scenicName")
  private String scenicName;
    @Expose
    @Column(name = "mapSize")
  private String mapSize;
    @Expose
    @Column(name = "canNav")
  private boolean canNav;

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getCityName() {
        return cityName;
    }
    @Expose
    @Column(name = "smallImage")
    private String smallImage;
    public String isCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Expose
    @Column(name = "cityName")
    private String cityName;
public String getMapSize() {
	return mapSize;
}
public void setMapSize(String mapSize) {
	this.mapSize = mapSize;
}
public String getScenicName() {
	return scenicName;
}
public void setScenicName(String scenicName) {
	this.scenicName = scenicName;
}
public String getScenicId() {
	return scenicId;
}
public void setScenicId(String scenicId) {
	this.scenicId = scenicId;
}
public boolean isCanNav() {
	return canNav;
}
public void setCanNav(boolean canNav) {
	this.canNav = canNav;
}

    public static List<CityScenesJson> find(String key) {
        try {

//            List<ScenicAreaJson> lineList = new Select().from(ScenicAreaJson.class)
//                    .where("city = ? or province=? or scenicName LIKE ?", key).execute();
//
//                return lineList;

            List<CityScenesJson>  mRecordList = SQLiteUtils.rawQuery(CityScenesJson.class,
                    "SELECT * from CityScenesJson where cityName LIKE ? or scenicName LIKE ?",
                    new String[]{'%' + key + '%','%' + key + '%'});
            return mRecordList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CityScenesJson load(String scenicId) {
        try {

            List<CityScenesJson> lineList = new Select().from(CityScenesJson.class)
                    .where(" scenicId = ?", scenicId)
                    .execute();
            if(lineList != null && lineList.size()>0)
            return lineList.get(0);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
