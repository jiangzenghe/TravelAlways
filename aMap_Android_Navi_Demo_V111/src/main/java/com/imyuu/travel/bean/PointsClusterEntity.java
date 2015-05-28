
package com.imyuu.travel.bean;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.imyuu.travel.model.ScenicAreaJson;


/**
 * 聚合模型.
 *
 * @author    Jiang
 * 
 * <p>Modification History:</p>
 * <p>Date              Author      Description</p>
 * <p>------------------------------------------------------------------</p>
 * <p>            new    </p>
 * <p>  </p>
 */
public class PointsClusterEntity {

	private AMap aMap;
	private LatLngBounds boundsEnv;// 创建区域
	private int clusterCount;
	private Double Lng;
	private Double Lat;
	private String text;
	private String title;
	private String clusterId;
	private List<ScenicAreaJson> subScenicEntity = new ArrayList<ScenicAreaJson>();

	public PointsClusterEntity() {

	}
	
	public PointsClusterEntity(String clusterId, String text) {
		this.clusterId = clusterId;
		this.text = text;
	}
	
	public PointsClusterEntity(AMap aMap, LatLng firstMarkers,
			int gridSize) {
		this.aMap = aMap;
		Point point = aMap.getProjection().toScreenLocation(firstMarkers);
		
		LatLng southwestPoint = aMap.getProjection().fromScreenLocation(new Point(point.x -gridSize, point.y +gridSize));
//		new LatLng(point.x - gridSize, point.y + gridSize);
		LatLng northeastPoint = aMap.getProjection().fromScreenLocation(new Point(point.x + gridSize, point.y - gridSize));
//		new LatLng(point.x + gridSize, point.y - gridSize);
		if(southwestPoint.longitude<northeastPoint.longitude && southwestPoint.latitude<northeastPoint.latitude) {
			boundsEnv = new LatLngBounds(southwestPoint, northeastPoint);
		}
	}

	/**
	 * 添加marker
	 */
	
	public LatLngBounds getBoundsEnv() {
		return boundsEnv;
	}

	public void setPosition() {
		int size = subScenicEntity.size();
		if (size == 1) {
			this.Lat = subScenicEntity.get(0).getLat();
			this.Lng = subScenicEntity.get(0).getLng();
			return;
		}
		double lat = 0.0;
		double lng = 0.0;
		for (ScenicAreaJson op : subScenicEntity) {
			lat += op.getLat();
			lng += op.getLng();
		}
		this.Lat = lat / size;
		this.Lng = lng / size;// 设置中心位置为聚集点的平均距离
		 
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Double getLng() {
		return Lng;
	}

	public void setLng(Double lng) {
		Lng = lng;
	}

	public Double getLat() {
		return Lat;
	}

	public void setLat(Double lat) {
		Lat = lat;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public int getClusterCount() {
		return clusterCount;
	}

	public void setClusterCount(int clusterCount) {
		this.clusterCount = clusterCount;
	}

	public List<ScenicAreaJson> getSubScenicEntity() {
		return subScenicEntity;
	}

	public void setSubEventEntity(List<ScenicAreaJson> subScenicEntity) {
		this.subScenicEntity = subScenicEntity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
