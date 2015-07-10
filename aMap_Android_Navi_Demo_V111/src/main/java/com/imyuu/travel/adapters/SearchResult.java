package com.imyuu.travel.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.imyuu.travel.R;
import com.imyuu.travel.model.CityScenesJson;
import com.imyuu.travel.model.ScenicAreaJson;

import java.util.List;

public class SearchResult {
	private Dialog mDialog;
	private ExpandableListView mUserInfoListView;
    private List<CityScenesJson>  searchResultList;
    public SearchResult(final Activity context,List<CityScenesJson>  searchResultList) {
        mDialog = new Dialog(context, R.style.dialog);
        this.searchResultList =   searchResultList;
        Window window = mDialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = -30;
        wl.y = 240;

//        Display d = window.getWindowManager().getDefaultDisplay();
//        wl.width = d.getWidth();
        window.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);

        window.setAttributes(wl);

//        // window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mDialog.setContentView(R.layout.map_add_dialog);
        mDialog.setTitle("查询结果");
        mDialog.setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
        mDialog.setCanceledOnTouchOutside(true);

        mUserInfoListView = (ExpandableListView) mDialog
                .findViewById(R.id.userInfoList);
        // 这里要把系统自带的图标去掉
        mUserInfoListView.setBackgroundColor(0x696969);
        mUserInfoListView.setGroupIndicator(null);
        mUserInfoListView.setDividerHeight(1);
        mUserInfoListView.setAdapter(new ScenicSearchAdapter(context, searchResultList));
        if(searchResultList.size()>0)
        for (int i = 0; i < 1; i++) {
            mUserInfoListView.expandGroup(i);
        }
        mUserInfoListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(
//                        context,
//                        groupPosition
//                                + " : "
//                                +
//
//                                childPosition, Toast.LENGTH_SHORT)
//                        .show();
                return false;
            }


        });

    }

	public void show() {
		mDialog.show();
	}

}
