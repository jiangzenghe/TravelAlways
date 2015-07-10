package com.imyuu.travel.adapters;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.ImageRequest;
import com.imyuu.travel.R;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.util.Config;

import com.imyuu.travel.util.FileUtils;
import com.imyuu.travel.util.FrescoFactory;
import com.imyuu.travel.util.MapUtil;
import com.imyuu.travel.view.CustomSwipeListView;
import com.imyuu.travel.view.HistoryListItemObject;
import com.imyuu.travel.view.SwipeItemView;
import com.imyuu.travel.view.SwipeItemView.OnSlideListener;
public class MapListViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<HistoryListItemObject> mMessageItems;
    private Context context;
    private SwipeItemView mLastSlideViewWithStatusOn;
    public MapListViewAdapter(Context context, List<HistoryListItemObject> mMessageItems) {
        mInflater = LayoutInflater.from(context);
        this.mMessageItems=mMessageItems;
        this.context=context;
    }

    @Override
    public int getCount() {
        return mMessageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SwipeItemView slideView = (SwipeItemView) convertView;
        if (slideView == null) {
            View itemView = mInflater.inflate(R.layout.history_listview_items, null);

            slideView = new SwipeItemView(context);
            slideView.setContentView(itemView);

            holder = new ViewHolder(slideView);
            slideView.setOnSlideListener(new OnSlideListener() {
				
				@Override
				public void onSlide(View view, int status) {
				 if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
				            mLastSlideViewWithStatusOn.shrink();
		        }

		        if (status == SLIDE_STATUS_ON) {
				            mLastSlideViewWithStatusOn = (SwipeItemView) view;
		        }
				}
			});
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }
        final HistoryListItemObject item = mMessageItems.get(position);
//        item.slideView = slideView;
        if(CustomSwipeListView.mFocusedItemView!=null){
            CustomSwipeListView.mFocusedItemView.shrink();
        }

         Log.d("image",Config.UU_FILEPATH + item.scenicId+"/"+item.imagePath);
        if(FileUtils.isExist(Config.UU_FILEPATH + item.scenicId+"/"+item.imagePath) )
        {
            Uri uri_temp = Uri.fromFile(new File(Config.UU_FILEPATH + item.scenicId+"/"+item.imagePath));
            holder.icon.setImageURI(uri_temp);
        }
        else
        {
            ImageRequest request = FrescoFactory.getImageRequest(holder.icon, Config.IMAGE_SERVER_ADDR + item.imagePath);
            DraweeController draweeController = FrescoFactory.getPipelineControllder(request, holder.icon);
            holder.icon.setController(draweeController);
        }


       // holder.icon.setImageResource(item.getIconRes());
        holder.title.setText(item.getTitle());
        holder.msg.setText(item.getMsg());
        holder.time.setText(item.time);
        holder.deleteHolder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

                HistoryListItemObject itemObject =  mMessageItems.get(position);
                MapUtil.deleteMap(itemObject.getCanNavi(), itemObject.scenicId,context);
                mMessageItems.remove(position);
				Toast.makeText(context, "景区地图删除成功", Toast.LENGTH_SHORT).show();
				notifyDataSetChanged();
			}
		});

        return slideView;
    }
    private static class ViewHolder {
        public SimpleDraweeView icon;
        public TextView title;
        public TextView msg;
        public TextView time;
        public ViewGroup deleteHolder;

        ViewHolder(View view) {
            icon = (SimpleDraweeView) view.findViewById(R.id.icon);
            title = (TextView) view.findViewById(R.id.title);
            msg = (TextView) view.findViewById(R.id.msg);
            time = (TextView) view.findViewById(R.id.time);
            deleteHolder = (ViewGroup)view.findViewById(R.id.holder);
        }
    }
}