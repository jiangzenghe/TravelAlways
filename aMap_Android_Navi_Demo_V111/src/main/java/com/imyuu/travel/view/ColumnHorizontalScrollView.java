package com.imyuu.travel.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 水平滚动组件
 *
 * @author <p>Modification History:</p>
 *         <p>Date             Author      Description</p>
 *         <p>--------------------------------------------------------------</p>
 *         <p>              new</p>
 *         <p>  </p>
 */
public class ColumnHorizontalScrollView extends HorizontalScrollView {
    /**
     * 传入整体布局
     */
    private View ll_content;
    /**
     * 传入拖动栏布局
     */
    private View rl_column;

    private View ll_titel;
    /**
     * 屏幕宽度
     */
    private int mScreenWitdh = 0;
    /**
     * 父类的活动activity
     */
    private Activity activity;

    public ColumnHorizontalScrollView(Context context) {
        super(context);
    }

    public ColumnHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColumnHorizontalScrollView(Context context, AttributeSet attrs,
                                      int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 在拖动的时候执行
     */
    @Override
    protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    }

    /**
     * 传入父类布局中的资源文件
     */
    public void setParam(Activity activity, int mScreenWitdh, View content, View column) {
        this.activity = activity;
        this.mScreenWitdh = mScreenWitdh;
        this.ll_content = content;
        this.rl_column = column;
    }
}
