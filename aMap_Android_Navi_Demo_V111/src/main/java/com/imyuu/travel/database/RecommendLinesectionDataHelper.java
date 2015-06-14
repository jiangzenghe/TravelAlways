package com.imyuu.travel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.imyuu.travel.BuildConfig;
import com.imyuu.travel.bean.RecommendLinesectionModel;
import com.imyuu.travel.util.ConstantsOld;

import java.util.ArrayList;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 20:26 $
 *
 * @author Frank
 * @since 1.0
 */
public class RecommendLinesectionDataHelper {
    private SQLiteDatabase db;

    private RecommendLinesectionSqliteHelper dbHelper;

    public RecommendLinesectionDataHelper(Context context) {
        dbHelper = new RecommendLinesectionSqliteHelper(context, ConstantsOld.DATABASE_NAME, null, ConstantsOld.DATABASE_VERSION);
        db = dbHelper.getReadableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    //添加记录
    public Long saveModel(RecommendLinesectionModel model) {
        ContentValues values = new ContentValues();
        values.put(RecommendLinesectionModel.RecommendLinesectionId, model.getRecommendLinesectionId());
        values.put(RecommendLinesectionModel.ScenicId, model.getScenicId());
        values.put(RecommendLinesectionModel.RecommendrouteId, model.getRecommendrouteId());
        values.put(RecommendLinesectionModel.RouteOrder, model.getRouteOrder());
        values.put(RecommendLinesectionModel.AspotId, model.getAspotId());
        values.put(RecommendLinesectionModel.BspotId, model.getBspotId());
        values.put(RecommendLinesectionModel.AbTotaltime, model.getAbTotaltime());
        values.put(RecommendLinesectionModel.AscenicspotName, model.getAscenicspotName());
        values.put(RecommendLinesectionModel.BscenicspotName, model.getBscenicspotName());
        values.put(RecommendLinesectionModel.RecommendRoutename, model.getRecommendRoutename());
        values.put(RecommendLinesectionModel.ScenicName, model.getScenicName());

        values.put(RecommendLinesectionModel.CREATED, System.currentTimeMillis());

        Long uid = db.insert(RecommendLinesectionSqliteHelper.TB_NAME, RecommendLinesectionModel.KEY, values);
        if (BuildConfig.DEBUG) {
        	Log.d("saveModel", uid + "");
		}
        
        return uid;
    }

    public RecommendLinesectionModel getModelById(String id) {
        Cursor cursor = null;
        try {
        	cursor = db.query(RecommendLinesectionSqliteHelper.TB_NAME, null, RecommendLinesectionModel.RecommendLinesectionId + " == ? ", new String[]{id}, null, null, RecommendLinesectionModel.RecommendLinesectionId + " ASC");
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            RecommendLinesectionModel model = new RecommendLinesectionModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setRecommendLinesectionId(cursor.getString(2));
	            model.setScenicId(cursor.getString(3));
	            model.setRecommendrouteId(cursor.getString(4));
	            model.setRouteOrder(cursor.getInt(5));
	            model.setAspotId(cursor.getString(6));
	            model.setBspotId(cursor.getString(7));
	            model.setAbTotaltime(cursor.getString(8));
	            model.setAscenicspotName(cursor.getString(9));
	            model.setBscenicspotName(cursor.getString(10));
	            model.setRecommendRoutename(cursor.getString(11));
	            model.setScenicName(cursor.getString(12));
	           
	            return model;
	        }
        }
        finally {
        	 cursor.close();
        }
        return null;
    }

    public ArrayList<RecommendLinesectionModel> getListByRouteId(String routeId) {
        ArrayList<RecommendLinesectionModel> list = new ArrayList<RecommendLinesectionModel>();
        Cursor cursor = null;
        try {
        	cursor = db.query(RecommendLinesectionSqliteHelper.TB_NAME, null, RecommendLinesectionModel.RecommendrouteId + " == ? ", new String[]{routeId}, null, null, RecommendLinesectionModel.RouteOrder + " ASC");
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            RecommendLinesectionModel model = new RecommendLinesectionModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setRecommendLinesectionId(cursor.getString(2));
	            model.setScenicId(cursor.getString(3));
	            model.setRecommendrouteId(cursor.getString(4));
	            model.setRouteOrder(cursor.getInt(5));
	            model.setAspotId(cursor.getString(6));
	            model.setBspotId(cursor.getString(7));
	            model.setAbTotaltime(cursor.getString(8));
	            model.setAscenicspotName(cursor.getString(9));
	            model.setBscenicspotName(cursor.getString(10));
	            model.setRecommendRoutename(cursor.getString(11));
	            model.setScenicName(cursor.getString(12));
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
        return db.delete(RecommendLinesectionSqliteHelper.TB_NAME, RecommendLinesectionModel.ScenicId + "=?", args);
    }

    //删除信息
    public Integer deleteAll() {
        return db.delete(RecommendLinesectionSqliteHelper.TB_NAME, null, null);
    }
}
