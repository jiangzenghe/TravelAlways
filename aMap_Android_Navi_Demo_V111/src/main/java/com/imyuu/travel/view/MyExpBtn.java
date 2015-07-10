package com.imyuu.travel.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class MyExpBtn extends ImageButton {

    private String expUrl;

    public MyExpBtn(Context context) {
        super(context);
    }

    public MyExpBtn(Context context, AttributeSet attributeset) {
        super(context, attributeset);
    }

    public String getExpUrl() {
        return expUrl;
    }

    public void setExpUrl(String s) {
        expUrl = s;
    }
}
