package com.imyuu.travel.util;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.imyuu.travel.R;
import com.imyuu.travel.bean.*;
import com.imyuu.travel.model.ScenicAreaJson;


public class ClusterUtils {
	private int gridSize = 25;
	private int height;// 屏幕高度(px)
	private int width;// 屏幕宽度(px)
	private Activity activity;
	private AMap aMap;
	/**
	 * 所有的point
	 */
//	ArrayList<ScenicModel> pointsList;
	ArrayList<ScenicAreaJson> pointsList;
	
	Handler handler;

	public ClusterUtils(Activity activity, AMap aMap,
			ArrayList<ScenicAreaJson> pointsList) {
		super();
		this.activity = activity;
		this.aMap = aMap;
		handler = new Handler();
		this.pointsList = pointsList;
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
	}

	/**
	 * 获取视野内的  根据聚合算法合成自定义的 显示视野内的
	 * 
	 */
	public ArrayList<PointsClusterEntity> resetMarks() {
		// 自定义的聚合类MarkerCluster
		ArrayList<PointsClusterEntity> clustersList = new ArrayList<PointsClusterEntity>();
		// 开始刷新界面
		for (ScenicAreaJson fp : pointsList) {
			
			if (clustersList.size() == 0) {
				add2Cluster(fp, clustersList);
			} else {
				boolean isIn = false;
				for (PointsClusterEntity cluster : clustersList) {
					if (cluster.getBoundsEnv() != null && cluster.getBoundsEnv().contains(new LatLng(fp.getLat(), fp.getLng()))) {
						cluster.setClusterCount(cluster.getClusterCount() + 1);
						if(cluster.getClusterCount()==2) {
							String replaceText = "该地区共有"+cluster.getClusterCount()+"个景区，依次为"
									+cluster.getText()+"和"+fp.getScenicName();
							cluster.setText(replaceText);
						} else {
							String addText = "和" + fp.getScenicName();
							cluster.setText(cluster.getText()+addText);
						}
//						cluster.setTitle("该地区共有"+cluster.getClusterCount()+"个景区");
//						ScenicAreaJson object = new ScenicAreaJson();
//						object.setLatLng(new LatLng(fp.getLat(), fp.getLng()));
						cluster.getSubScenicEntity().add(fp);
						isIn = true;
						break;
					}
				}
				if (!isIn) {
					add2Cluster(fp, clustersList);
				}
			}
		}
		
		for (PointsClusterEntity pointCluster : clustersList) {
			pointCluster.setPosition();// 设置聚合点的位置和icon
		}
		
		addClusterGrphic(clustersList);
		return clustersList;
	}

	private void add2Cluster(ScenicAreaJson each, ArrayList<PointsClusterEntity> normalClustList) {
		LatLng point = new LatLng(each.getLat(), each.getLng());
		PointsClusterEntity arg0 = new PointsClusterEntity(aMap, point, 
				gridSize);
		arg0.setClusterCount(1);
		arg0.setClusterId(normalClustList.size() + 1 + "");
//		ScenicModel object = new ScenicModel();
//		object.setLatLng(point);
//		object.setScenicName(each.getScenicName());
		arg0.getSubScenicEntity().add(each);
		arg0.setText(each.getScenicName());
		normalClustList.add(arg0);
	}
	
	private void addClusterGrphic(ArrayList<PointsClusterEntity> mClusterDatas) {
		//test the time to draw
		Log.e("draw start", new Date().getTime() + "");
		ArrayList<Marker> markerList = (ArrayList)aMap.getMapScreenMarkers();
		if(markerList!=null) {
			for(Marker each:markerList) {
				if(each.getObject() != null) {
					each.remove();
					each.destroy();
				}
			}
		}
		
		for (PointsClusterEntity each : mClusterDatas){
			MarkerOptions arg0 = new MarkerOptions().anchor(0.5f, 1.0f)
					.position(new LatLng(each.getLat(), each.getLng()));
			arg0.title(each.getText());
//			if(each.getClusterCount() > 1) {
//				if(each.getClusterCount() == 2) {
//					arg0.icon(BitmapDescriptorFactory.fromBitmap(getBitMap("2", R.drawable.h_middle)));
//				} else if(each.getClusterCount() == 3) {
//					arg0.icon(BitmapDescriptorFactory.fromBitmap(getBitMap("3", R.drawable.h_high)));
//				} else {
//					arg0.icon(BitmapDescriptorFactory.fromBitmap(getBitMap(each.getClusterCount()+"",
//							R.drawable.h_highest)));
//				}
//			} else {
//				arg0.icon(BitmapDescriptorFactory.fromBitmap(getBitMap("1", R.drawable.h_low)));
//			}
			if(each.getClusterCount() > 1) {
				if(each.getClusterCount() == 2) {
					arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.h_middle));
				} else if(each.getClusterCount() == 3) {
					arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.h_high));
				} else {
					arg0.icon(BitmapDescriptorFactory.fromResource(
							R.drawable.h_highest));
				}
			} else {
				arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.h_low));
			}
			Marker eachMarker = aMap.addMarker(arg0);
			eachMarker.setObject(each);//1--景区聚合标志
		}
		
		//test the time to draw
		Log.e("draw end", new Date().getTime() + "");
	}
	
	 public Bitmap getBitMap(String text, int sourceId) {                
		 	Bitmap bitmap = BitmapDescriptorFactory.fromResource(sourceId).getBitmap();
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
			             bitmap.getHeight());
			Canvas canvas = new Canvas(bitmap);
			TextPaint textPaint = new TextPaint();
			textPaint.setTextSize(20f);
			textPaint.setColor(Color.RED);
			canvas.drawText(text, 18, 30, textPaint);// 设置bitmap上面的文字位置
			return bitmap;
			}
	
}
