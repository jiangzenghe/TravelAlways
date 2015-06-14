package com.imyuu.travel.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.imyuu.travel.bean.ScenicAdvertOldModel;

/**
 * $Author: Frank $
 * $Date: 2014/12/27 17:06 $
 *
 * @author Frank
 * @since 1.0
 */
public class ScenicAdvertSqliteHelper extends IuuSQLiteOpenHelper {

    //表名
    public static final String TB_NAME = "scenic_advert";


    public ScenicAdvertSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        onCreate(getReadableDatabase());
    }

    //创建表

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                        TB_NAME + "(" +
                        ScenicAdvertOldModel.KEY + " integer primary key," +
                        ScenicAdvertOldModel.CREATED + " integer," +
                        ScenicAdvertOldModel.ScenicAdvertId + " varchar," +
                        ScenicAdvertOldModel.ScenicId + " varchar," +
                        ScenicAdvertOldModel.ScenicName + " varchar," +
                        ScenicAdvertOldModel.PubTime + " varchar," +
                        ScenicAdvertOldModel.AdvertAdmin + " varchar," +
                        ScenicAdvertOldModel.AdvertPic + " varchar," +
                        ScenicAdvertOldModel.AdvertLink + " varchar," +
                        ScenicAdvertOldModel.AdvertRemark + " varchar," +
                        ScenicAdvertOldModel.AdvertscenicId + " varchar," +
                        ScenicAdvertOldModel.AdvertscenicName + " varchar " +
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

