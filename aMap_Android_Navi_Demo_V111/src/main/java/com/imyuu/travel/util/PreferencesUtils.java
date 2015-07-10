package com.imyuu.travel.util;

import android.content.Context;

public class PreferencesUtils {

    public static String PREFERENCE_NAME = "IuuPreference";

    public PreferencesUtils() {
    }

    public static void deletePreferencesUtilsValue(Context context) {
        context.getSharedPreferences(PREFERENCE_NAME, 0).edit().clear().commit();
    }

    public static boolean getBoolean(Context context, String s) {
        return getBoolean(context, s, false);
    }
    public static boolean getSettingBoolean(Context context, String s) {
        return getBoolean(context, s, true);
    }

    public static boolean getBoolean(Context context, String s, boolean flag) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getBoolean(s, flag);
    }

    public static float getFloat(Context context, String s) {
        return getFloat(context, s, -1F);
    }

    public static float getFloat(Context context, String s, float f) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getFloat(s, f);
    }

    public static int getInt(Context context, String s) {
        return getInt(context, s, -1);
    }

    public static int getInt(Context context, String s, int i) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getInt(s, i);
    }

    public static long getLong(Context context, String s) {
        return getLong(context, s, -1L);
    }

    public static long getLong(Context context, String s, long l) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getLong(s, l);
    }

    public static String getString(Context context, String s) {
        return getString(context, s, null);
    }

    public static String getString(Context context, String s, String s1) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString(s, s1);
    }

    public static boolean putBoolean(Context context, String s, boolean flag) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putBoolean(s, flag);
        return editor.commit();
    }

    public static boolean putFloat(Context context, String s, float f) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putFloat(s, f);
        return editor.commit();
    }

    public static boolean putInt(Context context, String s, int i) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putInt(s, i);
        return editor.commit();
    }

    public static boolean putLong(Context context, String s, long l) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putLong(s, l);
        return editor.commit();
    }

    public static boolean putString(Context context, String s, String s1) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putString(s, s1);
        return editor.commit();
    }

    public static void removeKey(Context context, String s) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.remove(s);
        editor.commit();
    }

}
