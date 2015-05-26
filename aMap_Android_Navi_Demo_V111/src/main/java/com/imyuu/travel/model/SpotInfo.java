package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;

@Table(name="SpotInfos")
public class SpotInfo  extends Model implements Serializable {
	@Column(name="spotid")
    private String spotid;
    @Column(name="spotname")
    private String spotName;
	public String getSpotid() {
		return spotid;
	}

	public void setSpotid(String spotid) {
		this.spotid = spotid;
	}

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
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

	public String getSpotType() {
		return spotType;
	}

	public void setSpotType(String spotType) {
		this.spotType = spotType;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

    public SpotInfo() {

        this.spotid = "";
    }

    @Override
	public String toString() {
		return "SpotInfo [spotid=" + spotid + ", lat=" + lat + ", lng=" + lng
				+ ", spotType=" + spotType + ", order=" + order + "]";
	}
    @Column(name="lat")
	private Double lat; // γ��
    @Column(name="lng")
	private Double lng; // ����
    @Column(name="spotType")
	private String spotType;
    @Column(name="lineorder")
	private int order;// -----����·�е�˳���

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    @Column(name = "lineId")
    private String lineId;


    public static List<SpotInfo> getAll(String lineId) {
        try {
            return new Select()
                    .from(SpotInfo.class)
                    .where("lineId = ?", lineId)
                    .execute();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public  SpotInfo load(String lineId) {
        try {

            List<SpotInfo> lineList   = new Select().from(SpotInfo.class)
                    .where("lineId = ?", lineId).execute();
            if(null != lineList)
                return lineList.get(0);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public  void remove(String LineId) {
        try {

            List<SpotInfo> lineList   = new Select().from(SpotInfo.class)
                    .where("lineId = ?", LineId).execute();
            if(null != lineList && lineList.size()>0)
                lineList.get(0).delete();

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
