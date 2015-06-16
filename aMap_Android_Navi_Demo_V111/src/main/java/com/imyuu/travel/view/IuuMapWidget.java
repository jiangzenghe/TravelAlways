package com.imyuu.travel.view;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.interfaces.Layer;

public class IuuMapWidget extends MapWidget {
    List<Point> points;
    private Map<Integer, Layer> lineLayers = null;
	public IuuMapWidget(Bundle bundle, Context context, File rootMapFolder,
			int initialZoomLevel) {
		super(bundle, context, rootMapFolder, initialZoomLevel);
		MapWidget.logo = null;
		// TODO Auto-generated constructor stub
	}

	public IuuMapWidget(Bundle bundle, Context context, String rootMapFolder,
			int initialZoomLevel) {
		super(bundle, context, rootMapFolder, initialZoomLevel);
		MapWidget.logo = null;
		// TODO Auto-generated constructor stub
	}

	public IuuMapWidget(Context context, File rootMapFolder,
			int initialZoomLevel) {
		super(context, rootMapFolder, initialZoomLevel);
		MapWidget.logo = null;
		// TODO Auto-generated constructor stub
	}

	public IuuMapWidget(Context context, File rootMapFolder) {
		super(context, rootMapFolder);
		MapWidget.logo = null;
		// TODO Auto-generated constructor stub
	}

	public IuuMapWidget(Context context, String rootMapFolder,
			int initialZoomLevel) {
		super(context, rootMapFolder, initialZoomLevel);
		MapWidget.logo = null;
		// TODO Auto-generated constructor stub
	}

	public IuuMapWidget(Context context, String rootMapFolder) {
		super(context, rootMapFolder);
		MapWidget.logo = null;
		// TODO Auto-generated constructor stub
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

}
