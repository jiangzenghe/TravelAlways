package com.imyuu.travel.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MySlipSwitch extends View
    implements View.OnTouchListener
{
    public static interface OnSwitchListener
    {

        public abstract void onSwitched(boolean flag);
    }


    private float currentX;
    private boolean isSlipping;
    private boolean isSwitchListenerOn;
    private boolean isSwitchOn;
    private Rect off_Rect;
    private OnSwitchListener onSwitchListener;
    private Rect on_Rect;
    private float previousX;
    private Bitmap slip_Btn;
    private Bitmap switch_off_Bkg;
    private Bitmap switch_on_Bkg;

    public MySlipSwitch(Context context)
    {
        super(context);
        isSlipping = false;
        isSwitchOn = false;
        isSwitchListenerOn = false;
        init();
    }

    public MySlipSwitch(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        isSlipping = false;
        isSwitchOn = false;
        isSwitchListenerOn = false;
        init();
    }

    private void init()
    {
        setOnTouchListener(this);
    }

    protected boolean getSwitchState()
    {
        return isSwitchOn;
    }

    protected void onDraw(Canvas canvas)
    {
        float f;
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        if (currentX < (float)(switch_on_Bkg.getWidth() / 2))
        {
            canvas.drawBitmap(switch_off_Bkg, matrix, paint);
        } else
        {
            canvas.drawBitmap(switch_on_Bkg, matrix, paint);
        }
        if (isSlipping)
        {
            if (currentX > (float)switch_on_Bkg.getWidth())
            {
                f = switch_on_Bkg.getWidth() - slip_Btn.getWidth();
            } else
            {
                f = currentX - (float)(slip_Btn.getWidth() / 2);
            }
        } else
        if (isSwitchOn)
        {
            f = on_Rect.left;
            canvas.drawBitmap(switch_on_Bkg, matrix, paint);
        } else
        {
            f = off_Rect.left;
            canvas.drawBitmap(switch_off_Bkg, matrix, paint);
        }
        if (f < 0.0F) {
            f = 0.0F;
        }


        if (f > (float)(switch_on_Bkg.getWidth() - slip_Btn.getWidth()))
        {
            f = switch_on_Bkg.getWidth() - slip_Btn.getWidth();
        }
        canvas.drawBitmap(slip_Btn, f, 0.0F, paint);

    }

    protected void onMeasure(int i, int j)
    {
        setMeasuredDimension(switch_on_Bkg.getWidth(), switch_on_Bkg.getHeight());
    }

    public boolean onTouch(View view, MotionEvent event)
    {

        switch (event.getAction()) {

//移动
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                break;

            //按下
            case MotionEvent.ACTION_DOWN:
                isSlipping = true;
                previousX = event.getX();
                currentX = previousX;
                break;
            //松开
            case MotionEvent.ACTION_UP:
                isSlipping = false;
                boolean flag = isSwitchOn;
                if (event.getX() >= (float) (switch_on_Bkg.getWidth() / 2)) {
                    isSwitchOn = true;
                } else {
                    isSwitchOn = false;
                }
                if (isSwitchListenerOn && flag != isSwitchOn) {
                    onSwitchListener.onSwitched(isSwitchOn);
                }
        }
        invalidate();
        return true;
    }

    public void setImageResource(int i, int j, int k)
    {
        switch_on_Bkg = BitmapFactory.decodeResource(getResources(), i);
        switch_off_Bkg = BitmapFactory.decodeResource(getResources(), j);
        slip_Btn = BitmapFactory.decodeResource(getResources(), k);
        on_Rect = new Rect(switch_off_Bkg.getWidth() - slip_Btn.getWidth(), 0, switch_off_Bkg.getWidth(), slip_Btn.getHeight());
        off_Rect = new Rect(0, 0, slip_Btn.getWidth(), slip_Btn.getHeight());
    }

    public void setOnSwitchListener(OnSwitchListener onswitchlistener)
    {
        onSwitchListener = onswitchlistener;
        isSwitchListenerOn = true;
    }

    protected void setSwitchState(boolean flag)
    {
        isSwitchOn = flag;
    }

    public void updateSwitchState(boolean flag)
    {
        isSwitchOn = flag;
        invalidate();
    }
}
