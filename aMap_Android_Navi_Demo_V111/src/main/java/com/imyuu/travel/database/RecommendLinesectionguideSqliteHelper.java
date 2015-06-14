package com.imyuu.travel.database;

import com.imyuu.travel.bean.RecommendLinesectionguideModel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 20:34 $
 *
 * @author Frank
 * @since 1.0
 */
public class RecommendLinesectionguideSqliteHelper extends IuuSQLiteOpenHelper {

    //表名
    public static final String TB_NAME = "recommend_linesectionguide";


    public RecommendLinesectionguideSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        onCreate(getReadableDatabase());
    }

    //创建表

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                        TB_NAME + "(" +
                        RecommendLinesectionguideModel.KEY + " integer primary key," +
                        RecommendLinesectionguideModel.CREATED + " integer," +
                        RecommendLinesectionguideModel.RecommendLinesectionguideId + " varchar," +
                        RecommendLinesectionguideModel.RecommendrouteId + " varchar," +
                        RecommendLinesectionguideModel.RouteOrder + " integer," +
                        RecommendLinesectionguideModel.RelativeLongitude + " double," +
                        RecommendLinesectionguideModel.RelativeLatitude + " double," +
                        RecommendLinesectionguideModel.AbsoluteLongitude + " double," +
                        RecommendLinesectionguideModel.AbsoluteLatitude + " double, " +
                        RecommendLinesectionguideModel.RecommendrouteDetailid + " varchar," +
                        RecommendLinesectionguideModel.RecommendrouteDetailname + " varchar," +
                        RecommendLinesectionguideModel.RecommendrouteName + " varchar " +
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
