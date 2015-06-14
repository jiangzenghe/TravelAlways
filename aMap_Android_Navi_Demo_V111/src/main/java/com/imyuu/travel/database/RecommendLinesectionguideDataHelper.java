package com.imyuu.travel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.imyuu.travel.bean.RecommendLinesectionguideModel;
import com.imyuu.travel.util.ConstantsOld;

import java.util.ArrayList;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 20:36 $
 *
 * @author Frank
 * @since 1.0
 */
public class RecommendLinesectionguideDataHelper {
    private SQLiteDatabase db;

    private RecommendLinesectionguideSqliteHelper dbHelper;

    public RecommendLinesectionguideDataHelper(Context context) {
        dbHelper = new RecommendLinesectionguideSqliteHelper(context, ConstantsOld.DATABASE_NAME, null, ConstantsOld.DATABASE_VERSION);
        db = dbHelper.getReadableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    //添加记录
    public Long saveModel(RecommendLinesectionguideModel model) {
        ContentValues values = new ContentValues();
        values.put(RecommendLinesectionguideModel.RecommendLinesectionguideId, model.getRecommendLinesectionguideId());
        values.put(RecommendLinesectionguideModel.RecommendrouteId, model.getRecommendrouteId());
        values.put(RecommendLinesectionguideModel.RouteOrder, model.getRouteOrder());
        values.put(RecommendLinesectionguideModel.RelativeLongitude, model.getRelativeLongitude());
        values.put(RecommendLinesectionguideModel.RelativeLatitude, model.getRelativeLatitude());
        values.put(RecommendLinesectionguideModel.AbsoluteLongitude, model.getAbsoluteLongitude());
        values.put(RecommendLinesectionguideModel.AbsoluteLatitude, model.getAbsoluteLatitude());
        values.put(RecommendLinesectionguideModel.RecommendrouteDetailid, model.getRecommendrouteDetailid());
        values.put(RecommendLinesectionguideModel.RecommendrouteDetailname, model.getRecommendrouteDetailname());
        values.put(RecommendLinesectionguideModel.RecommendrouteName, model.getRecommendrouteName());

        values.put(RecommendLinesectionguideModel.CREATED, System.currentTimeMillis());

        Long uid = db.insert(RecommendLinesectionguideSqliteHelper.TB_NAME, RecommendLinesectionguideModel.KEY, values);

        Log.d("saveModel", uid + "");
        return uid;
    }

    public RecommendLinesectionguideModel getModelById(String id) {
        Cursor cursor = null;
        try {
        	cursor = db.query(RecommendLinesectionguideSqliteHelper.TB_NAME, null, RecommendLinesectionguideModel.RecommendLinesectionguideId + " == ? ", new String[]{id}, null, null, RecommendLinesectionguideModel.RecommendLinesectionguideId + " ASC");
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            RecommendLinesectionguideModel model = new RecommendLinesectionguideModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setRecommendLinesectionguideId(cursor.getString(2));
	            model.setRecommendrouteId(cursor.getString(3));
	            model.setRouteOrder(cursor.getInt(4));
	            model.setRelativeLongitude(cursor.getDouble(5));
	            model.setRelativeLatitude(cursor.getDouble(6));
	            model.setAbsoluteLongitude(cursor.getDouble(7));
	            model.setAbsoluteLatitude(cursor.getDouble(8));
	            model.setRecommendrouteDetailid(cursor.getString(9));
	            model.setRecommendrouteDetailname(cursor.getString(10));
	            model.setRecommendrouteName(cursor.getString(11));
	            return model;
	        }
        }
        finally {
        	cursor.close();
        }
        return null;
    }

    public ArrayList<RecommendLinesectionguideModel> getListByDetailId(String detailid) {
        ArrayList<RecommendLinesectionguideModel> list = new ArrayList<RecommendLinesectionguideModel>();
        Cursor cursor = null;
        try {
        	cursor = db.query(RecommendLinesectionguideSqliteHelper.TB_NAME, null, RecommendLinesectionguideModel.RecommendrouteDetailid + " == ? ", new String[]{detailid}, null, null, RecommendLinesectionguideModel.RouteOrder + " ASC");
        
	        cursor.moveToFirst();
	        while (!cursor.isAfterLast()) {
	            RecommendLinesectionguideModel model = new RecommendLinesectionguideModel();
	            model.setId(cursor.getInt(0));
	            model.setCreatedTime(cursor.getLong(1));
	            model.setRecommendLinesectionguideId(cursor.getString(2));
	            model.setRecommendrouteId(cursor.getString(3));
	            model.setRouteOrder(cursor.getInt(4));
	            model.setRelativeLongitude(cursor.getDouble(5));
	            model.setRelativeLatitude(cursor.getDouble(6));
	            model.setAbsoluteLongitude(cursor.getDouble(7));
	            model.setAbsoluteLatitude(cursor.getDouble(8));
	            model.setRecommendrouteDetailid(cursor.getString(9));
	            model.setRecommendrouteDetailname(cursor.getString(10));
	            model.setRecommendrouteName(cursor.getString(11));
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
    public Integer deleteByRouteId(String routeId) {
        String[] args = {routeId};
        return db.delete(RecommendLinesectionguideSqliteHelper.TB_NAME, RecommendLinesectionguideModel.RecommendrouteId + "=?", args);
    }

    //删除信息
    public Integer deleteAll() {
        return db.delete(RecommendLinesectionguideSqliteHelper.TB_NAME, null, null);
    }
}
