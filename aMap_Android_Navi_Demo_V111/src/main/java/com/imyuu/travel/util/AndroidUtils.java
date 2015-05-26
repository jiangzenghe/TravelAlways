package com.imyuu.travel.util;

import android.content.Context;

/**
 * Created by 图乐 on 2015/4/29.
 */
public class AndroidUtils {
    /**
     * 将dp转化为px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dp2px(Context context, float dipValue){
        final  float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
}
