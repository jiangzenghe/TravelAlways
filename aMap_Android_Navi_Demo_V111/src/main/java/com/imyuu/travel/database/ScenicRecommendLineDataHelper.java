package com.imyuu.travel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.imyuu.travel.bean.ScenicRecommendLineModel;
import com.imyuu.travel.util.ConstantsOld;

import java.util.ArrayList;

/**
 * $Author: Frank $
 * $Date: 2014/12/28 14:55 $
 *
 * @author Frank
 * @since 1.0
 */
public class ScenicRecommendLineDataHelper {
    private SQLiteDatabase db;

    private ScenicRecommendLineSqliteHelper dbHelper;

    public ScenicRecommendLineDataHelper(Context context) {
        dbHelper = new ScenicRecommendLineSqliteHelper(context, ConstantsOld.DATABASE_NAME, null, ConstantsOld.DATABASE_VERSION);
        db = dbHelper.getReadableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    //添加记录
    public Long saveModel(ScenicRecommendLineModel model) {
        ContentValues values = new ContentValues();
        values.put(ScenicRecommendLineModel.ScenicId, model.getScenicId());
        values.put(ScenicRecommendLineModel.ScenicRecommendLineId, model.getScenicRecommendLineId());
        values.put(ScenicRecommendLineModel.ScenicName, model.getScenicName());
        values.put(ScenicRecommendLineModel.RecommendRoutename, model.getRecommendRoutename());
        values.put(ScenicRecommendLineModel.RouteTotaltime, model.getRouteTotaltime());
        values.put(ScenicRecommendLineModel.RecommendRoutenote, model.getRecommendRoutenote());

        values.put(ScenicRecommendLineModel.CREATED, System.currentTimeMillis());

        Long uid = db.insert(ScenicRecommendLineSqliteHelper.TB_NAME, ScenicRecommendLineModel.KEY, values);

        Log.d("saveModel", uid + "");
        return uid;
    }

    public ScenicRecommendLineModel getModelById(String id) {
        Cursor cursor = null;
        try {
	        cursor = db.query(ScenicRecommendLineSqliteHelper.TB_NAME, null, ScenicRecommendLineModel.ScenicRecommendLineId + " == ? ", new String[]{id}, null, null, ScenicRecommendLineModel.ScenicRecommendLineId + " ASC");
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            ScenicRecommendLineModel model = new ScenicRecommendLineModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setScenicId(cursor.getString(2));
	            model.setScenicRecommendLineId(cursor.getString(3));
	            model.setScenicName(cursor.getString(4));
	            model.setRecommendRoutename(cursor.getString(5));
	            model.setRouteTotaltime(cursor.getString(6));
	            model.setRecommendRoutenote(cursor.getString(7));
	            
	            return model;
	        }
        }
        finally {
        	cursor.close();
        }
        return null;
    }

    public ArrayList<ScenicRecommendLineModel> getListByScenicId(String scenicId) {
        ArrayList<ScenicRecommendLineModel> list = new ArrayList<ScenicRecommendLineModel>();
        Cursor cursor = null;
        try {
	        cursor = db.query(ScenicRecommendLineSqliteHelper.TB_NAME, null, ScenicRecommendLineModel.ScenicId + " == ? ", new String[]{scenicId}, null, null, ScenicRecommendLineModel.ScenicRecommendLineId + " ASC");
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            ScenicRecommendLineModel model = new ScenicRecommendLineModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setScenicId(cursor.getString(2));
	            model.setScenicRecommendLineId(cursor.getString(3));
	            model.setScenicName(cursor.getString(4));
	            model.setRecommendRoutename(cursor.getString(5));
	            model.setRouteTotaltime(cursor.getString(6));
	            model.setRecommendRoutenote(cursor.getString(7));
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
        return db.delete(ScenicRecommendLineSqliteHelper.TB_NAME, ScenicRecommendLineModel.ScenicId + "=?", args);
    }
    //删除信息
    public Integer deleteAll() {
        return db.delete(ScenicRecommendLineSqliteHelper.TB_NAME, null, null);
    }
}
