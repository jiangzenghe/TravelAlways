package com.imyuu.travel.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class MyPlayBtn extends ImageButton {

    private String TourName;
    private String mp3Url;

    public MyPlayBtn(Context context) {
        super(context);
    }

    public MyPlayBtn(Context context, AttributeSet attributeset) {
        super(context, attributeset);
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String s) {
        mp3Url = s;
    }

    public String getTourName() {
        return TourName;
    }

    public void setTourName(String s) {
        TourName = s;
    }
}
