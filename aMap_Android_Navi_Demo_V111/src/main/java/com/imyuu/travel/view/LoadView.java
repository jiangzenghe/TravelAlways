package com.imyuu.travel.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.imyuu.travel.R;

public class LoadView extends RelativeLayout
{

    public LoadView(Context context)
    {
        super(context);
        init();
    }

    public LoadView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        init();
    }

    public LoadView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        init();
    }

    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.load_view, this, true);
        ((AnimationDrawable)((ImageView)findViewById(R.id.loadingview)).getDrawable()).start();
    }
}
