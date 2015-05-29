package com.imyuu.travel.util;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.Log;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.imyuu.travel.R;
import com.imyuu.travel.model.ScenicPointJson;

public class MarkerUtilsFor2D {
	private Activity activity;
	private AMap aMap;
	/**
	 * 所有的point
	 */
	ArrayList<ScenicPointJson> pointsList;
	
	public MarkerUtilsFor2D(Activity activity, AMap aMap,
							ArrayList<ScenicPointJson> pointsList) {
		super();
		this.activity = activity;
		this.aMap = aMap;
		this.pointsList = pointsList;
	}
	
	public void setAllUnVisible() {
		ArrayList<Marker> markerList = (ArrayList)aMap.getMapScreenMarkers();
		if(markerList!=null) {
			for(Marker each:markerList) {
				if(each.getObject() != null) {
					each.setVisible(false);
				}
			}
		}
	}
	
	public void setAllVisible() {
		if(aMap.getMapScreenMarkers() != null) {
			ArrayList<Marker> markerList = (ArrayList)aMap.getMapScreenMarkers();
			if(markerList!=null) {
				for(Marker each:markerList) {
					if(each.getObject() != null) {
						each.setVisible(true);
					}
				}
			}
		}
	}
	
	public void setTypeUnVisible(String spotType) {
		ArrayList<Marker> markerList = (ArrayList)aMap.getMapScreenMarkers();
		if(markerList!=null) {
			for(Marker each:markerList) {
				if(each.getObject() != null) {
					ScenicPointJson point = (ScenicPointJson)each.getObject();
					if(point.getSpotType().equals(spotType))
					each.setVisible(false);
				}
			}
		}
	}
	
	public void removeAllAddition() {
		ArrayList<Marker> markerList = (ArrayList)aMap.getMapScreenMarkers();
		if(markerList!=null) {
			for(Marker each:markerList) {
				if(each.getObject() != null) {
					if(each.getObject() != null && each.getObject() instanceof ScenicPointJson) {
						ScenicPointJson point = (ScenicPointJson)each.getObject();
						if(!point.getSpotType().equals("1")) {
							each.remove();
							each.destroy();
						}
					}
				}
			}
		}
	}

	public LatLng moveToNearestPosition(LatLng curPosition, String type) {

		float temp = 0.0f;
		LatLng result = new LatLng(0, 0);
		if(pointsList!=null) {

			for(ScenicPointJson each:pointsList) {
				if(each.getSpotType().equals(type)) {
					LatLng end = new LatLng(each.getLat(), each.getLng());

					float distance  = AMapUtils.calculateLineDistance(curPosition, end);
					if(temp == 0.0) {
						temp = distance;
						result = new LatLng(each.getLat(), each.getLng());
					} else {
						temp = temp>distance?distance:temp;
						if(temp>distance) {
							result = new LatLng(each.getLat(), each.getLng());
						}
					}
				}
			}
			if(result.latitude != 0 && result.longitude != 0) {
//				aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//						result, 19));  //37.5206,121.358
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						result, 19), 1000, null);  //37.5206,121.358
			}

		}
		return result;
	}

	public void addMarkerGrphic(String spotType) {
		ArrayList<ScenicPointJson> selectPointsList = getPointList(spotType);
		if(spotType.equals("1")) {
			for (ScenicPointJson each : selectPointsList){
				MarkerOptions arg0 = new MarkerOptions().anchor(0.5f, 1.0f)
						.position(new LatLng(each.getLat(), each.getLng()));
				MarkerOptions arg1 = new MarkerOptions().anchor(0.0f, 0.5f)
						.position(new LatLng(each.getLat(), each.getLng()));
				arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.hotviewport_nosel_map));
				arg1.icon(BitmapDescriptorFactory.fromBitmap(
						getBitMap(each.getScenicPointName(), R.drawable.hotviewport_transparent_long)));
				arg0.title(each.getScenicPointName());
				Marker eachMarker = aMap.addMarker(arg0);
				Marker eachMarker_add = aMap.addMarker(arg1);
				eachMarker.setObject(each);//1--景区标志
				eachMarker_add.setObject(each);
			}
		} else if(spotType.equals("2")) {
			for (ScenicPointJson each : selectPointsList){
				MarkerOptions arg0 = new MarkerOptions().anchor(0.5f, 1.0f)
						.position(new LatLng(each.getLat(), each.getLng()));
				arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.anno_cesuo));
				arg0.title(each.getScenicPointName());
				Marker eachMarker = aMap.addMarker(arg0);
				eachMarker.setObject(each);//1--景区标志
			}
		} else if(spotType.equals("3")) {
			for (ScenicPointJson each : selectPointsList){
				MarkerOptions arg0 = new MarkerOptions().anchor(0.5f, 1.0f)
						.position(new LatLng(each.getLat(), each.getLng()));
				arg0.title(each.getScenicPointName());
				arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.anno_lanche));
				Marker eachMarker = aMap.addMarker(arg0);
				eachMarker.setObject(each);//1--景区标志
			}
		} else if(spotType.equals("4")) {
			for (ScenicPointJson each : selectPointsList){
				MarkerOptions arg0 = new MarkerOptions().anchor(0.5f, 1.0f)
						.position(new LatLng(each.getLat(), each.getLng()));
				arg0.title(each.getScenicPointName());
				arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.anno_matou));
				Marker eachMarker = aMap.addMarker(arg0);
				eachMarker.setObject(each);//1--景区标志
			}
		} else if(spotType.equals("5")) {
			for (ScenicPointJson each : selectPointsList){
				MarkerOptions arg0 = new MarkerOptions().anchor(0.5f, 1.0f)
						.position(new LatLng(each.getLat(), each.getLng()));
				arg0.title(each.getScenicPointName());
				arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.anno_kefu));
				Marker eachMarker = aMap.addMarker(arg0);
				eachMarker.setObject(each);//1--景区标志
			}
		} else if(spotType.equals("6")) {
			for (ScenicPointJson each : selectPointsList){
				MarkerOptions arg0 = new MarkerOptions().anchor(0.5f, 1.0f)
						.position(new LatLng(each.getLat(), each.getLng()));
				arg0.title(each.getScenicPointName());
				arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.anno_tingchechang));
				Marker eachMarker = aMap.addMarker(arg0);
				eachMarker.setObject(each);//1--景区标志
			}
		} else if(spotType.equals("7")) {
			for (ScenicPointJson each : selectPointsList){
				MarkerOptions arg0 = new MarkerOptions().anchor(0.5f, 1.0f)
						.position(new LatLng(each.getLat(), each.getLng()));
				arg0.title(each.getScenicPointName());
				arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.anno_huancheng));
				Marker eachMarker = aMap.addMarker(arg0);
				eachMarker.setObject(each);//1--景区标志
			}
		} else if(spotType.equals("8")) {
			for (ScenicPointJson each : selectPointsList){
				MarkerOptions arg0 = new MarkerOptions().anchor(0.5f, 1.0f)
						.position(new LatLng(each.getLat(), each.getLng()));
				arg0.title(each.getScenicPointName());
				arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.anno_shoupiao));
				Marker eachMarker = aMap.addMarker(arg0);
				eachMarker.setObject(each);//1--景区标志
			}
		} else if(spotType.equals("9")) {
			for (ScenicPointJson each : selectPointsList){
				MarkerOptions arg0 = new MarkerOptions().anchor(0.5f, 1.0f)
						.position(new LatLng(each.getLat(), each.getLng()));
				arg0.title(each.getScenicPointName());
				arg0.icon(BitmapDescriptorFactory.fromResource(R.drawable.anno_churukou));
				Marker eachMarker = aMap.addMarker(arg0);
				eachMarker.setObject(each);//1--景区标志
			}
		}
		
		
		//test the time to draw
		Log.e("draw end", new Date().getTime() + "");
	}
	
	private ArrayList<ScenicPointJson> getPointList(String spotType) {
		ArrayList<ScenicPointJson> result = new ArrayList<ScenicPointJson>();
		for(ScenicPointJson each:pointsList) {
			if(each.getSpotType().equals(spotType)) {
				result.add(each);
			}
		}
		return result;
	}
	
	 public Bitmap getBitMap(String text, int sourceId) {                
	 	Bitmap bitmap = BitmapDescriptorFactory.fromResource(sourceId).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
		             bitmap.getHeight());
				Canvas canvas = new Canvas(bitmap);
				TextPaint textPaint = new TextPaint();
				textPaint.setTextSize(20f);
				textPaint.setColor(Color.RED);
				canvas.drawText(text, 22, 15, textPaint);// 设置bitmap上面的文字位置
		return bitmap;
	}
	
}
