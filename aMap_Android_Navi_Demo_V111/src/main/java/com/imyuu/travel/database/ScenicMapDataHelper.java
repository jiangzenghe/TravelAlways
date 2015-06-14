package com.imyuu.travel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.imyuu.travel.bean.ScenicMapOldModel;
import com.imyuu.travel.util.ConstantsOld;

import java.util.ArrayList;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 19:24 $
 *
 * @author Frank
 * @since 1.0
 */
public class ScenicMapDataHelper {
    private SQLiteDatabase db;

    private ScenicMapSqliteHelper dbHelper;

    public ScenicMapDataHelper(Context context) {
        dbHelper = new ScenicMapSqliteHelper(context, ConstantsOld.DATABASE_NAME, null, ConstantsOld.DATABASE_VERSION);
        db = dbHelper.getReadableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    //添加记录
    public Long saveModel(ScenicMapOldModel model) {
        ContentValues values = new ContentValues();
        values.put(ScenicMapOldModel.ScenicMapId, model.getScenicMapId());
        values.put(ScenicMapOldModel.ScenicId, model.getScenicId());
        values.put(ScenicMapOldModel.ScenicName, model.getScenicName());
        values.put(ScenicMapOldModel.ScenicspotName, model.getScenicspotName());
        values.put(ScenicMapOldModel.RelativeLongitude, model.getRelativeLongitude());
        values.put(ScenicMapOldModel.RelativeLatitude, model.getRelativeLatitude());
        values.put(ScenicMapOldModel.ScenicspotNote, model.getScenicspotNote());
        values.put(ScenicMapOldModel.ScenicspotVoice, model.getScenicspotVoice());
        values.put(ScenicMapOldModel.ScenicspotMarkertype, model.getScenicspotMarkertype());
        values.put(ScenicMapOldModel.ScenicspotSmallpic, model.getScenicspotSmallpic());
        values.put(ScenicMapOldModel.AbsoluteLongitude, model.getAbsoluteLongitude());
        values.put(ScenicMapOldModel.AbsoluteLatitude, model.getAbsoluteLatitude());
        values.put(ScenicMapOldModel.RelativeHeight, model.getRelativeHeight());
        values.put(ScenicMapOldModel.RelativeWidth, model.getRelativeWidth());

        values.put(ScenicMapOldModel.CREATED, System.currentTimeMillis());

        Long uid = db.insert(ScenicMapSqliteHelper.TB_NAME, ScenicMapOldModel.KEY, values);

        Log.d("saveModel", uid + "");
        return uid;
    }

    public ScenicMapOldModel getModelById(String id) {
        Cursor cursor = null;
        try {
	        cursor = db.query(ScenicMapSqliteHelper.TB_NAME, null, ScenicMapOldModel.ScenicMapId + " == ? ", new String[]{id}, null, null, ScenicMapOldModel.ScenicMapId + " ASC");
	
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            ScenicMapOldModel model = new ScenicMapOldModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setScenicMapId(cursor.getString(2));
	            model.setScenicId(cursor.getString(3));
	            model.setScenicName(cursor.getString(4));
	            model.setScenicspotName(cursor.getString(5));
	            model.setRelativeLongitude(cursor.getDouble(6));
	            model.setRelativeLatitude(cursor.getDouble(7));
	            model.setScenicspotNote(cursor.getString(8));
	            model.setScenicspotVoice(cursor.getString(9));
	            model.setScenicspotMarkertype(cursor.getString(10));
	            model.setScenicspotSmallpic(cursor.getString(11));
	            model.setAbsoluteLongitude(cursor.getDouble(12));
	            model.setAbsoluteLatitude(cursor.getDouble(13));
	            model.setRelativeHeight(cursor.getDouble(14));
	            model.setRelativeWidth(cursor.getDouble(15));
	            
	            return model;
	        }
        }
        finally {
        	cursor.close();
        }
        return null;
    }

    public ArrayList<ScenicMapOldModel> getListByScenicId(String scenicId) {
        ArrayList<ScenicMapOldModel> list = new ArrayList<ScenicMapOldModel>();
        Cursor cursor = null;
        try {
	        cursor = db.query(ScenicMapSqliteHelper.TB_NAME, null, ScenicMapOldModel.ScenicId + " == ? ", new String[]{scenicId}, null, null, ScenicMapOldModel.ScenicMapId + " ASC");
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            ScenicMapOldModel model = new ScenicMapOldModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setScenicMapId(cursor.getString(2));
	            model.setScenicId(cursor.getString(3));
	            model.setScenicName(cursor.getString(4));
	            model.setScenicspotName(cursor.getString(5));
	            model.setRelativeLongitude(cursor.getDouble(6));
	            model.setRelativeLatitude(cursor.getDouble(7));
	            model.setScenicspotNote(cursor.getString(8));
	            model.setScenicspotVoice(cursor.getString(9));
	            model.setScenicspotMarkertype(cursor.getString(10));
	            model.setScenicspotSmallpic(cursor.getString(11));
	            model.setAbsoluteLongitude(cursor.getDouble(12));
	            model.setAbsoluteLatitude(cursor.getDouble(13));
	            model.setRelativeHeight(cursor.getDouble(14));
	            model.setRelativeWidth(cursor.getDouble(15));
	            list.add(model);
	            cursor.moveToNext();
	        }
        }
        finally {
        	cursor.close();
        }
        return list;
    }

    //删除信息
    public Integer deleteByScenicId(String scenicId) {
        String[] args = {scenicId};
        return db.delete(ScenicMapSqliteHelper.TB_NAME, ScenicMapOldModel.ScenicId + "=?", args);
    }

    //删除信息
    public Integer deleteAll() {
        return db.delete(ScenicMapSqliteHelper.TB_NAME, null, null);
    }
}
