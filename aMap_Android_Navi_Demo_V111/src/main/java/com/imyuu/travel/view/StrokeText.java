package com.imyuu.travel.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class StrokeText extends TextView {

    private float mBigFontBottom;
    private float mBigFontHeight;
    private Paint mPaint;
    private int strokeSize;
    private String text;

    public StrokeText(Context context) {
        super(context);
        strokeSize = 1;
        init();
    }

    public StrokeText(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        strokeSize = 1;
        init();
    }

    public StrokeText(Context context, AttributeSet attributeset, int i) {
        super(context, attributeset, i);
        strokeSize = 1;
        init();
    }

    private void init() {
        setLayoutParams(new android.widget.LinearLayout.LayoutParams(-2, -2));
        setLines(1);
        setTextSize(14F);
        setGravity(49);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(getTextSize());
        mPaint.setColor(getResources().getColor(0x106000b));
        Paint.FontMetrics fontmetrics = mPaint.getFontMetrics();
        mBigFontBottom = fontmetrics.bottom;
        mBigFontHeight = fontmetrics.bottom - fontmetrics.top;
    }

    protected void onDraw(Canvas canvas) {
        if (strokeSize > 0 && strokeSize < 4) {
            float f = ((float) getPaddingTop() + mBigFontHeight) - mBigFontBottom;
            canvas.drawText(text, 0.0F, f - (float) strokeSize, mPaint);
            canvas.drawText(text, 0.0F, f + (float) strokeSize, mPaint);
            canvas.drawText(text, 0 + strokeSize, f, mPaint);
            canvas.drawText(text, 0 + strokeSize, f + (float) strokeSize, mPaint);
            canvas.drawText(text, 0 + strokeSize, f - (float) strokeSize, mPaint);
            canvas.drawText(text, 0 - strokeSize, f, mPaint);
            canvas.drawText(text, 0 - strokeSize, f + (float) strokeSize, mPaint);
            canvas.drawText(text, 0 - strokeSize, f - (float) strokeSize, mPaint);
        }
        super.onDraw(canvas);
    }

    protected void onMeasure(int i, int j) {
        super.onMeasure(i, j);
        if (strokeSize > 0 && strokeSize < 4) {
            setMeasuredDimension(getMeasuredWidth() + strokeSize, getMeasuredHeight());
        }
    }

    public void setText(CharSequence charsequence, BufferType buffertype) {
        super.setText(charsequence, buffertype);
        text = charsequence.toString();
        invalidate();
    }
}
