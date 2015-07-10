package com.imyuu.travel.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imyuu.travel.R;
import com.imyuu.travel.callback.LoadFinishCallBack;
import com.imyuu.travel.model.CommentsInfoJson;
import com.imyuu.travel.model.UserSharingModel;
import com.imyuu.travel.ui.GreetingActivity;

import java.util.List;

/**
 * Created by java on 2015/5/28.
 */



public class UserSharingAdapter extends RecyclerView.Adapter<CommentsViewHolder> {


    private int page = -1;
    private List<UserSharingModel> data;
    private  LoadFinishCallBack mLoadFinisCallBack;
     private Context context;
    public UserSharingAdapter(Context ctx, List<UserSharingModel> dlist, LoadFinishCallBack mLoadFinisCallBack) {
        data = dlist;
        context = ctx;
        this.mLoadFinisCallBack = mLoadFinisCallBack;
    }

    public void loadNextPage() {
        page++;
        loadData();
    }

    public void notifyUpdate() {
        notifyDataSetChanged();
    }
    public void notifyUpdate(UserSharingModel commentsInfoJson) {

        data.add(commentsInfoJson);
        notifyDataSetChanged();
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        //  holder.tv_favourNum.setText(data.get(position).getId());
        final UserSharingModel sharingModel = data.get(position);


        String time = sharingModel.getSharingTime();
        if( time!= null && time.length()>12) {
            holder.tv_commenttime.setText(time.substring(5, 7) + "月" +
                    time.substring(8, 10) + "日"
                  //  + time.substring(10, 12)
            );
        }
        else
            holder.tv_commenttime.setText(time);
        holder.tv_content.setText("我在"+sharingModel.getSharingPlatform()+"分享了到"+sharingModel.getRemark()+"游玩的经历！");
        holder.tv_username.setText( " ");
        holder.tv_usergender.setText( " ");
       holder.tv_userage.setText(" ");

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private void loadData() {
                data.clear();
                data.addAll(UserSharingModel.load(9997,0));
                mLoadFinisCallBack.loadFinish(null);
                notifyDataSetChanged();

    }
}




