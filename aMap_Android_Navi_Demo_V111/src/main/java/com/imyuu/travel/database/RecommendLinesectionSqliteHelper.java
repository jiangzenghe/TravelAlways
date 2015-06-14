package com.imyuu.travel.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.imyuu.travel.bean.RecommendLinesectionModel;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 20:23 $
 *
 * @author Frank
 * @since 1.0
 */
public class RecommendLinesectionSqliteHelper extends IuuSQLiteOpenHelper {
	
    //表名
    public static final String TB_NAME = "recommend_linesection";


    public RecommendLinesectionSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        onCreate(getReadableDatabase());
    }

    //创建表

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                        TB_NAME + "(" +
                        RecommendLinesectionModel.KEY + " integer primary key," +
                        RecommendLinesectionModel.CREATED + " integer," +
                        RecommendLinesectionModel.RecommendLinesectionId + " varchar," +
                        RecommendLinesectionModel.ScenicId + " varchar," +
                        RecommendLinesectionModel.RecommendrouteId + " varchar," +
                        RecommendLinesectionModel.RouteOrder + " integer," +
                        RecommendLinesectionModel.AspotId + " varchar," +
                        RecommendLinesectionModel.BspotId + " varchar," +
                        RecommendLinesectionModel.AbTotaltime + " varchar, " +
                        RecommendLinesectionModel.AscenicspotName + " varchar, " +
                        RecommendLinesectionModel.BscenicspotName + " varchar, " +
                        RecommendLinesectionModel.RecommendRoutename + " varchar, " +
                        RecommendLinesectionModel.ScenicName + " varchar " +
                        ")"
        );
        super.onCreate(db);
    }

    //更新表

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
        super.onUpgrade(db, oldVersion, newVersion);
    }

    //更新列

    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn) {
        try {
            db.execSQL("ALTER TABLE " +
                            TB_NAME + " CHANGE " +
                            oldColumn + " " + newColumn +
                            " " + typeColumn
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
