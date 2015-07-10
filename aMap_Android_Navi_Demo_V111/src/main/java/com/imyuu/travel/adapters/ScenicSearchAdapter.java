package com.imyuu.travel.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imyuu.travel.R;
import com.imyuu.travel.activity.DownloadActivity;
import com.imyuu.travel.activity.DownloadOldActivity;
import com.imyuu.travel.model.CityScenesJson;
import com.imyuu.travel.model.MapInfoModel;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.LogUtil;

import java.util.List;

public class ScenicSearchAdapter extends BaseExpandableListAdapter {
    private String[] group = { "查询结果" };
   private DownloadActivity downloader  = new DownloadActivity();
    private Activity mContext;
    private List<CityScenesJson> searchResultList;
    int reportSuccess = 0;
    public ScenicSearchAdapter(Activity mContext, List<CityScenesJson> searchResultList) {
        super();
        this.mContext = mContext;
        this.searchResultList =  searchResultList;
    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 2;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return searchResultList.get(groupPosition).getScenicName();
       // return child[groupPosition][childPosition];
    }

    public Object getMap1(int groupPosition, int childPosition) {
        return searchResultList.get(groupPosition).getMapSize();
       // return child1[groupPosition][childPosition];
    }

//    public Object getMap2(int groupPosition, int childPosition) {
//        return child2[groupPosition][childPosition];
//    }
//
//    public Object getMap3(int groupPosition, int childPosition) {
//        return child3[groupPosition][childPosition];
//    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // 实例化布局文件
        LinearLayout glayout = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.treeview_item, null);

        TextView tv = (TextView) glayout.findViewById(R.id.contentText);
        tv.setText(this.getGroup(groupPosition).toString());
        return glayout;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // 实例化布局文件
        LinearLayout clayout = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.child, null);
        TextView name = (TextView) clayout.findViewById(R.id.map_mapname);
        TextView mapsize = (TextView) clayout.findViewById(R.id.map_mapsize);
        final CityScenesJson areaJson = searchResultList.get(childPosition);

       final TextView download=(TextView) clayout.findViewById(R.id.map_download);
        if(MapInfoModel.isDownload(areaJson.getScenicId()))
            download.setText("已下载");
        final ProgressBar progressBar = (ProgressBar)clayout.findViewById(R.id.map_progress);
        name.setText(areaJson.getScenicName());
        mapsize.setText(areaJson.getMapSize());

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("element is called",element.getScenicId());
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(100);
                final Handler myhandler  = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 1:
                                progressBar.setProgress(msg.getData().getInt("size"));
                                //  Log.d("TreeViewAdapater",msg.getData().getInt("size")+"----"+result);
                                download.setText(msg.getData().getInt("size") + "%");
                                break;
                            case 2:
                                //显示下载成功信息
                                reportSuccess++;
                                if (reportSuccess >= Config.THREAD_NUM) {
                                    download.setText("100%");
                                    Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
                                    downloader.parseAndSaveJson(areaJson.getScenicId());
                                    download.setText("解析完成");
                                }
                                break;
                            case -1:
                                //显示下载错误信息
                                //  Toast.makeText(DownloadActivity.this, "", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };

                if(areaJson.isCanNav()) {

                    downloader.download(areaJson.getScenicId(), myhandler);
                }
                else
                {
                    LogUtil.d("TreeViewAdapater", areaJson.getScenicId());
                    downloader = new DownloadOldActivity(mContext);
                    downloader.download(areaJson.getScenicId(),myhandler);
                }
            }
        });
        return clayout;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

