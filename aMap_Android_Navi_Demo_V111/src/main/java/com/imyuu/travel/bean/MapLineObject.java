package com.imyuu.travel.bean;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.ls.widgets.map.model.MapObject;

public class MapLineObject extends MapObject {

	public MapLineObject(Object id, Drawable drawable, int x, int y,
			boolean isTouchable, boolean isScalable) {
		super(id, drawable, x, y, isTouchable, isScalable);
		// TODO Auto-generated constructor stub
	}

	public MapLineObject(Object id, Drawable drawable, int x, int y,
			boolean isTouchable) {
		super(id, drawable, x, y, isTouchable);
		// TODO Auto-generated constructor stub
	}

	public MapLineObject(Object id, Drawable drawable, int x, int y,
			int pivotX, int pivotY, boolean isTouchable, boolean isScalable) {
		super(id, drawable, x, y, pivotX, pivotY, isTouchable, isScalable);
		// TODO Auto-generated constructor stub
	}

	public MapLineObject(Object id, Drawable drawable, int x, int y,
			int pivotX, int pivotY) {
		super(id, drawable, x, y, pivotX, pivotY);
		// TODO Auto-generated constructor stub
	}

	public MapLineObject(Object id, Drawable drawable, int x, int y) {
		super(id, drawable, x, y);
		// TODO Auto-generated constructor stub
	}

	public MapLineObject(Object id, Drawable drawable, Point position,
			boolean isTouchable, boolean isScalable) {
		super(id, drawable, position, isTouchable, isScalable);
		// TODO Auto-generated constructor stub
	}

	public MapLineObject(Object id, Drawable drawable, Point position,
			boolean isTouchable) {
		super(id, drawable, position, isTouchable);
		// TODO Auto-generated constructor stub
	}

	public MapLineObject(Object id, Drawable drawable, Point position,
			Point pivotPoint, boolean isTouchable, boolean isScalable) {
		super(id, drawable, position, pivotPoint, isTouchable, isScalable);
		// TODO Auto-generated constructor stub
	}

	public MapLineObject(Object id, Drawable drawable, Point position,
			Point pivotPoint, boolean isTouchable) {
		super(id, drawable, position, pivotPoint, isTouchable);
		// TODO Auto-generated constructor stub
	}

	public MapLineObject(Object id, Drawable drawable, Point position,
			Point pivotPoint) {
		super(id, drawable, position, pivotPoint);
		// TODO Auto-generated constructor stub
	}

	public MapLineObject(Object id, Drawable drawable, Point position) {
		super(id, drawable, position);
		// TODO Auto-generated constructor stub
	}

	public MapLineObject(Object id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

}
