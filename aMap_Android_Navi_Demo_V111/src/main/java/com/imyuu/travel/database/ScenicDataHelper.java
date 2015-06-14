package com.imyuu.travel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.imyuu.travel.bean.ScenicOldModel;
import com.imyuu.travel.util.ConstantsOld;

import java.util.ArrayList;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 17:07 $
 *
 * @author Frank
 * @since 1.0
 */
public class ScenicDataHelper {
    private SQLiteDatabase db;

    private ScenicSqliteHelper dbHelper;

    public ScenicDataHelper(Context context) {
        dbHelper = new ScenicSqliteHelper(context, ConstantsOld.DATABASE_NAME, null, ConstantsOld.DATABASE_VERSION);
        db = dbHelper.getReadableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    //添加记录
    public Long saveModel(ScenicOldModel model) {
        ContentValues values = new ContentValues();
        values.put(ScenicOldModel.ScenicId, model.getScenicId());
        values.put(ScenicOldModel.ScenicName, model.getScenicName());
        values.put(ScenicOldModel.ScenicLocation, model.getScenicLocation());
        values.put(ScenicOldModel.CenterabsoluteLongitude, model.getCenterabsoluteLongitude());
        values.put(ScenicOldModel.CenterabsoluteLatitude, model.getCenterabsoluteLatitude());
        values.put(ScenicOldModel.CenterrelativeLongitude, model.getCenterrelativeLongitude());
        values.put(ScenicOldModel.CenterrelativeLatitude, model.getCenterrelativeLatitude());
        values.put(ScenicOldModel.AbsoluteLongitude, model.getAbsoluteLongitude());
        values.put(ScenicOldModel.AbsoluteLatitude, model.getAbsoluteLatitude());
        values.put(ScenicOldModel.ScenicNote, model.getScenicNote());
        values.put(ScenicOldModel.ScenicMapurl, model.getScenicMapurl());
        values.put(ScenicOldModel.ScenicSmallpic, model.getScenicSmallpic());
        values.put(ScenicOldModel.ScenicmapMaxx, model.getScenicmapMaxx());
        values.put(ScenicOldModel.ScenicmapMaxy, model.getScenicmapMaxy());
        values.put(ScenicOldModel.LineColor, model.getLineColor());

        values.put(ScenicOldModel.CREATED, System.currentTimeMillis());

        Long uid = db.insert(ScenicSqliteHelper.TB_NAME, ScenicOldModel.KEY, values);

        Log.d("saveModel", uid + "");
        return uid;
    }

    public ArrayList<ScenicOldModel> searchByName(String name) {
        ArrayList<ScenicOldModel> list = new ArrayList<ScenicOldModel>();
        Cursor cursor = null;
        try {
	        cursor = db.query(ScenicSqliteHelper.TB_NAME, null, ScenicOldModel.ScenicName + " like ? or " + ScenicOldModel.ScenicLocation + " like ? ", new String[]{"%" + name + "%", "%" + name + "%"}, null, null, ScenicOldModel.ScenicName + " ASC");
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            ScenicOldModel model = new ScenicOldModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setScenicId(cursor.getString(2));
	            model.setScenicName(cursor.getString(3));
	            model.setScenicLocation(cursor.getString(4));
	            model.setCenterabsoluteLongitude(cursor.getDouble(5));
	            model.setCenterabsoluteLatitude(cursor.getDouble(6));
	            model.setCenterrelativeLongitude(cursor.getDouble(7));
	            model.setCenterrelativeLatitude(cursor.getDouble(8));
	            model.setAbsoluteLongitude(cursor.getDouble(9));
	            model.setAbsoluteLatitude(cursor.getDouble(10));
	            model.setScenicNote(cursor.getString(11));
	            model.setScenicMapurl(cursor.getString(12));
	            model.setScenicSmallpic(cursor.getString(13));
	            model.setScenicmapMaxx(cursor.getInt(14));
	            model.setScenicmapMaxy(cursor.getInt(15));
	            model.setLineColor(cursor.getString(16));
	            list.add(model);
	            cursor.moveToNext();
	        }
        }
        finally {
        	cursor.close();
        }
        return list;
    }

    public ScenicOldModel getModelByScenicId(String sscenicId) {
        Cursor cursor = null;

        try {
        	cursor = db.query(ScenicSqliteHelper.TB_NAME, null, ScenicOldModel.ScenicId + " == ? ", new String[]{sscenicId}, null, null, ScenicOldModel.ScenicId + " ASC");
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            ScenicOldModel model = new ScenicOldModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setScenicId(cursor.getString(2));
	            model.setScenicName(cursor.getString(3));
	            model.setScenicLocation(cursor.getString(4));
	            model.setCenterabsoluteLongitude(cursor.getDouble(5));
	            model.setCenterabsoluteLatitude(cursor.getDouble(6));
	            model.setCenterrelativeLongitude(cursor.getDouble(7));
	            model.setCenterrelativeLatitude(cursor.getDouble(8));
	            model.setAbsoluteLongitude(cursor.getDouble(9));
	            model.setAbsoluteLatitude(cursor.getDouble(10));
	            model.setScenicNote(cursor.getString(11));
	            model.setScenicMapurl(cursor.getString(12));
	            model.setScenicSmallpic(cursor.getString(13));
	            model.setScenicmapMaxx(cursor.getInt(14));
	            model.setScenicmapMaxy(cursor.getInt(15));
	            model.setLineColor(cursor.getString(16));
	            return model;
	        }
        }
        finally {
        	cursor.close();
        }
        return null;
    }

    //删除信息
    public Integer deleteModel(String scenicId) {
        String[] args = {scenicId};
        return db.delete(ScenicSqliteHelper.TB_NAME, ScenicOldModel.ScenicId + "=?", args);
    }

    //删除信息
    public Integer deleteAll() {
        return db.delete(ScenicSqliteHelper.TB_NAME, null, null);
    }
}
