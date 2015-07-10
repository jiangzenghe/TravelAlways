package com.imyuu.travel.util;

import android.os.Environment;
import android.os.StatFs;

public class SDcardUtil {

    public SDcardUtil() {
    }

    public static long getSDFreeSize() {
        StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) statfs.getBlockSize() * (long) statfs.getAvailableBlocks()) / 1024L / 1024L;
    }

    public static boolean hasSDCard() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }
}
