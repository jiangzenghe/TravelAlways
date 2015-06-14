package com.imyuu.travel.database;

import com.imyuu.travel.bean.ScenicMapOldModel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 19:21 $
 *
 * @author Frank
 * @since 1.0
 */
public class ScenicMapSqliteHelper extends IuuSQLiteOpenHelper {

    //表名
    public static final String TB_NAME = "scenic_map";


    public ScenicMapSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        onCreate(getReadableDatabase());
    }

    //创建表

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                        TB_NAME + "(" +
                        ScenicMapOldModel.KEY + " integer primary key," +
                        ScenicMapOldModel.CREATED + " integer," +
                        ScenicMapOldModel.ScenicMapId + " varchar," +
                        ScenicMapOldModel.ScenicId + " varchar," +
                        ScenicMapOldModel.ScenicName + " varchar," +
                        ScenicMapOldModel.ScenicspotName + " varchar," +
                        ScenicMapOldModel.RelativeLongitude + " double," +
                        ScenicMapOldModel.RelativeLatitude + " double," +
                        ScenicMapOldModel.ScenicspotNote + " varchar," +
                        ScenicMapOldModel.ScenicspotVoice + " varchar, " +
                        ScenicMapOldModel.ScenicspotMarkertype + " varchar, " +
                        ScenicMapOldModel.ScenicspotSmallpic + " varchar, " +
                        ScenicMapOldModel.AbsoluteLongitude + " double, " +
                        ScenicMapOldModel.AbsoluteLatitude + " double, " +
                        ScenicMapOldModel.RelativeHeight + " double, " +
                        ScenicMapOldModel.RelativeWidth + " double " +
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
