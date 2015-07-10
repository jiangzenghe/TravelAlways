package com.imyuu.travel.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.base.AMapHelper;
import com.imyuu.travel.base.AppApplication;
import com.imyuu.travel.callback.LoadFinishCallBack;
import com.imyuu.travel.model.CommentsInfoJson;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.ui.GreetingActivity;
import com.imyuu.travel.ui.ScenicAreaActivity;
import com.imyuu.travel.util.Config;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by java on 2015/5/28.
 */



public class CommentAdapter extends RecyclerView.Adapter<CommentsViewHolder> {


    private int page = -1;
    private List<CommentsInfoJson> data;
    private  LoadFinishCallBack mLoadFinisCallBack;
    private String scenicId;
    private Context context;
    public CommentAdapter(Context ctx,String scenicId,List<CommentsInfoJson> dlist,LoadFinishCallBack mLoadFinisCallBack) {
        data = dlist;
        context = ctx;

        this.scenicId = scenicId;
        this.mLoadFinisCallBack = mLoadFinisCallBack;
    }

    public void loadNextPage() {
        page++;
        loadData();
    }

    public void notifyUpdate() {
        notifyDataSetChanged();
    }
    public void notifyUpdate(CommentsInfoJson commentsInfoJson) {
        commentsInfoJson.setCommentTime("刚才");
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
        final CommentsInfoJson commentsInfoJson = data.get(position);

        holder.tv_content.setText(commentsInfoJson.getContent());
        String time = commentsInfoJson.getCommentTime();
        if( time!= null && time.length()>12) {
            holder.tv_commenttime.setText(time.substring(6, 8) + "日" +
                    time.substring(8, 10) + ":"
                    + time.substring(10, 12));
        }
        else
            holder.tv_commenttime.setText(time);
        Log.d("CommentsAdapter",commentsInfoJson.getUserName());
        holder.tv_username.setText(commentsInfoJson.getUserName()+",");
        holder.tv_usergender.setText(commentsInfoJson.getGender()+",");
        holder.tv_userage.setText(commentsInfoJson.getAge()+"岁");
        holder.tv_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GreetingActivity.class);
                intent.putExtra("userId", commentsInfoJson.getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

             }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private void loadData() {

        ApiClient.getSocialService().queryUserComments(scenicId,10, page * 10, new Callback<List<CommentsInfoJson>>() {
            @Override
            public void success(List<CommentsInfoJson> commentsInfoJsons, Response response) {

                data.addAll(commentsInfoJsons);
                mLoadFinisCallBack.loadFinish(null);
                notifyDataSetChanged();
                Log.d("queryUserComments",commentsInfoJsons.toString() + scenicId );
            }

            @Override
            public void failure(RetrofitError error) {

                mLoadFinisCallBack.loadFinish(null);
            }
        });
    }
}




