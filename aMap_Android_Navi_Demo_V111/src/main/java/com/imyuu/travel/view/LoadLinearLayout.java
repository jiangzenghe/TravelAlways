package com.imyuu.travel.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.LinearLayout;

public class LoadLinearLayout extends LinearLayout
{

    public LoadLinearLayout(Context context)
    {
        super(context);
    }

    public LoadLinearLayout(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public boolean dispatchTouchEvent(MotionEvent motionevent)
    {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(motionevent);
    }
}