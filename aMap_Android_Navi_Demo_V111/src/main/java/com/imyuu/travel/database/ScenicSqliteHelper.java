package com.imyuu.travel.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.imyuu.travel.bean.ScenicOldModel;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 17:06 $
 *
 * @author Frank
 * @since 1.0
 */
public class ScenicSqliteHelper extends IuuSQLiteOpenHelper {

    //表名
    public static final String TB_NAME = "scenic_area";


    public ScenicSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        onCreate(getReadableDatabase());
    }

    //创建表

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                        TB_NAME + "(" +
                        ScenicOldModel.KEY + " integer primary key," +
                        ScenicOldModel.CREATED + " integer," +
                        ScenicOldModel.ScenicId + " varchar," +
                        ScenicOldModel.ScenicName + " varchar," +
                        ScenicOldModel.ScenicLocation + " varchar," +
                        ScenicOldModel.CenterabsoluteLongitude + " double," +
                        ScenicOldModel.CenterabsoluteLatitude + " double," +
                        ScenicOldModel.CenterrelativeLongitude + " double," +
                        ScenicOldModel.CenterrelativeLatitude + " double," +
                        ScenicOldModel.AbsoluteLongitude + " double," +
                        ScenicOldModel.AbsoluteLatitude + " double," +
                        ScenicOldModel.ScenicNote + " varchar," +
                        ScenicOldModel.ScenicMapurl + " varchar," +
                        ScenicOldModel.ScenicSmallpic + " varchar, " +
                        ScenicOldModel.ScenicmapMaxx + " integer, " +
                        ScenicOldModel.ScenicmapMaxy + " integer, " +
                        ScenicOldModel.LineColor + " varchar " +
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

