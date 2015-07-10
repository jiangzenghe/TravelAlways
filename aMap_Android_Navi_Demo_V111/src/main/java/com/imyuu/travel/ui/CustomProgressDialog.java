package com.imyuu.travel.ui;

import android.app.Dialog;
import android.content.Context;  
import android.view.Gravity;  
import android.widget.TextView;

import com.imyuu.travel.R;

public class CustomProgressDialog extends Dialog {  
    public CustomProgressDialog(Context context, String strMessage) {  
        this(context, R.style.CustomProgressDialog, strMessage);
    }  
  
    public CustomProgressDialog(Context context, int theme, String strMessage) {  
        super(context, theme);  
        this.setContentView(R.layout.dialog_progress_download);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;  
        TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);  
        if (tvMsg != null) {  
            tvMsg.setText(strMessage);  
        }  
    }  
  
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) {  
  
        if (!hasFocus) {  
            dismiss();  
        }  
    }  
}  