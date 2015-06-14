package com.imyuu.travel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.imyuu.travel.bean.ScenicAdvertOldModel;
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
public class ScenicAdvertDataHelper {
    private SQLiteDatabase db;

    private ScenicAdvertSqliteHelper dbHelper;

    public ScenicAdvertDataHelper(Context context) {
        dbHelper = new ScenicAdvertSqliteHelper(context, ConstantsOld.DATABASE_NAME, null, ConstantsOld.DATABASE_VERSION);
        db = dbHelper.getReadableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    //添加记录
    public Long saveModel(ScenicAdvertOldModel model) {
        ContentValues values = new ContentValues();
        values.put(ScenicAdvertOldModel.ScenicAdvertId, model.getScenicAdvertId());
        values.put(ScenicAdvertOldModel.ScenicId, model.getScenicId());
        values.put(ScenicAdvertOldModel.ScenicName, model.getScenicName());
        values.put(ScenicAdvertOldModel.PubTime, model.getPubTime());
        values.put(ScenicAdvertOldModel.AdvertAdmin, model.getAdvertAdmin());
        values.put(ScenicAdvertOldModel.AdvertPic, model.getAdvertPic());
        values.put(ScenicAdvertOldModel.AdvertLink, model.getAdvertLink());
        values.put(ScenicAdvertOldModel.AdvertRemark, model.getAdvertRemark());
        values.put(ScenicAdvertOldModel.AdvertscenicId, model.getAdvertscenicId());
        values.put(ScenicAdvertOldModel.AdvertscenicName, model.getAdvertscenicName());

        values.put(ScenicOldModel.CREATED, System.currentTimeMillis());

        Long uid = db.insert(ScenicAdvertSqliteHelper.TB_NAME, ScenicOldModel.KEY, values);

        Log.d("saveModel", uid + "");
        return uid;
    }

    public ArrayList<ScenicAdvertOldModel> getListByScenicId(String scenicId) {
        ArrayList<ScenicAdvertOldModel> list = new ArrayList<ScenicAdvertOldModel>();
        Cursor cursor = null;
        try {
	        cursor = db.query(ScenicAdvertSqliteHelper.TB_NAME, null, ScenicAdvertOldModel.ScenicId + " == ? ", new String[]{scenicId}, null, null, ScenicAdvertOldModel.AdvertscenicId + " ASC");
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            ScenicAdvertOldModel model = new ScenicAdvertOldModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setScenicAdvertId(cursor.getString(2));
	            model.setScenicId(cursor.getString(3));
	            model.setScenicName(cursor.getString(4));
	            model.setPubTime(cursor.getString(5));
	            model.setAdvertAdmin(cursor.getString(6));
	            model.setAdvertPic(cursor.getString(7));
	            model.setAdvertLink(cursor.getString(8));
	            model.setAdvertRemark(cursor.getString(9));
	            model.setAdvertscenicId(cursor.getString(10));
	            model.setAdvertscenicName(cursor.getString(11));
	            list.add(model);
	            cursor.moveToNext();
	        }
        }
        finally {
        	cursor.close();
        }
        return list;
    }

    public ScenicAdvertOldModel getModelById(String id) {
        Cursor cursor = null;
        try {
        	cursor = db.query(ScenicAdvertSqliteHelper.TB_NAME, null, ScenicAdvertOldModel.ScenicAdvertId + " == ? ", new String[]{id}, null, null, ScenicAdvertOldModel.ScenicAdvertId + " ASC");
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            ScenicAdvertOldModel model = new ScenicAdvertOldModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setScenicAdvertId(cursor.getString(2));
	            model.setScenicId(cursor.getString(3));
	            model.setScenicName(cursor.getString(4));
	            model.setPubTime(cursor.getString(5));
	            model.setAdvertAdmin(cursor.getString(6));
	            model.setAdvertPic(cursor.getString(7));
	            model.setAdvertLink(cursor.getString(8));
	            model.setAdvertRemark(cursor.getString(9));
	            model.setAdvertscenicId(cursor.getString(10));
	            model.setAdvertscenicName(cursor.getString(11));
	            
	            return model;
	        }
        }
        finally {
        	cursor.close();
        }
        return null;
    }

    //删除信息
    public Integer deleteByScenicId(String scenicId) {
        String[] args = {scenicId};
        return db.delete(ScenicAdvertSqliteHelper.TB_NAME, ScenicAdvertOldModel.ScenicId + "=?", args);
    }

    //删除信息
    public Integer deleteAll() {
        return db.delete(ScenicAdvertSqliteHelper.TB_NAME, null, null);
    }
}
