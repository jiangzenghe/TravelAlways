package com.imyuu.travel.database;

import com.imyuu.travel.bean.ScenicRecommendLineModel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * $Author: Frank $
 * $Date: 2014/12/28 14:52 $
 *
 * @author Frank
 * @since 1.0
 */
public class ScenicRecommendLineSqliteHelper extends IuuSQLiteOpenHelper {

    //表名
    public static final String TB_NAME = "scenic_recommend_line";


    public ScenicRecommendLineSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        onCreate(getReadableDatabase());
    }

    //创建表

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                        TB_NAME + "(" +
                        ScenicRecommendLineModel.KEY + " integer primary key," +
                        ScenicRecommendLineModel.CREATED + " integer," +
                        ScenicRecommendLineModel.ScenicId + " varchar," +
                        ScenicRecommendLineModel.ScenicRecommendLineId + " varchar," +
                        ScenicRecommendLineModel.ScenicName + " varchar," +
                        ScenicRecommendLineModel.RecommendRoutename + " varchar," +
                        ScenicRecommendLineModel.RouteTotaltime + " varchar," +
                        ScenicRecommendLineModel.RecommendRoutenote + " varchar " +
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
