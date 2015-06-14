package com.imyuu.travel.database;

import com.imyuu.travel.BuildConfig;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class IuuSQLiteOpenHelper extends SQLiteOpenHelper {
	public static String TAG = "IuuSQLite";
	public IuuSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	public IuuSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, String.format("%s onCreate", getClass().getName()));
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, String.format("%s onUpgrade", getClass().getName()));
		}
	}

}
