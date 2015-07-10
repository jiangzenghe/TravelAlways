/**
 *
 */
package com.imyuu.travel.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void show(Context context, String info) {
        try {
            Toast.makeText(context, info, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void show(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }
}
