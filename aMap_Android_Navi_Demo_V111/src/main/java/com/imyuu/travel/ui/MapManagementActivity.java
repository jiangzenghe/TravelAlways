package com.imyuu.travel.ui;

import android.content.Intent;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import com.imyuu.travel.R;
import com.imyuu.travel.adapters.MapListViewAdapter;
import com.imyuu.travel.model.MapInfoModel;
import com.imyuu.travel.view.CustomSwipeListView;
import com.imyuu.travel.view.HistoryListItemObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapManagementActivity extends Activity implements OnItemClickListener{

    private static final String TAG = "MapManagementActivity";

    private CustomSwipeListView mListView;

    private List<HistoryListItemObject> mMessageItems = new ArrayList<HistoryListItemObject>();
    List<MapInfoModel> mapList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_management);
        initView();
        ButterKnife.inject(this);
    }

    @OnClick(R.id.tb_addmap)
    public void addMapClick(View v)
    {
        Intent intent = new Intent(getBaseContext(), MapAddActivity.class);

        startActivity(intent);
    }

    @OnClick(R.id.map_image_advicancel)
    public void cancelClick(View v)
    {
        this.finish();
    }
    private void initView() {
        mListView = (CustomSwipeListView) findViewById(R.id.list);
        HistoryListItemObject item = null;
        mapList = MapInfoModel.load();

        for (MapInfoModel map:mapList) {
            item= new HistoryListItemObject();
            item.scenicId = map.getScenicId();
            item.iconRes = R.drawable.m104;
            item.title = map.getScenicName();
            item.msg = "文件大小："+map.getFileSize();
            item.time = "下载时间："+map.getDownloadTime();
            item.filePath = map.getFilePath();
            item.imagePath = map.getImagePath();
            item.setCanNavi(map.getCanNav());
            mMessageItems.add(item);
        }

        mListView.setAdapter(new MapListViewAdapter(this,mMessageItems));
        mListView.setOnItemClickListener(this);
    }




    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

    }
}

