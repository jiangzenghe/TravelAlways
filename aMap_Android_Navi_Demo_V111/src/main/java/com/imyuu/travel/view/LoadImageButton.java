package com.imyuu.travel.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ImageButton;

public class LoadImageButton extends ImageButton
{

    public LoadImageButton(Context context)
    {
        super(context);
    }

    public LoadImageButton(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    public boolean dispatchTouchEvent(MotionEvent motionevent)
    {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(motionevent);
    }
}
