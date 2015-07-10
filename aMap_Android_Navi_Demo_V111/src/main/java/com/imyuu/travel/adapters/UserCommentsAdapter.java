package com.imyuu.travel.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.callback.LoadFinishCallBack;
import com.imyuu.travel.model.CommentsInfoJson;
import com.imyuu.travel.ui.GreetingActivity;
import com.imyuu.travel.util.AndroidUtils;
import com.imyuu.travel.util.LogUtil;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserCommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder> {


    private int page = -1;
    private List<CommentsInfoJson> data;
    private  LoadFinishCallBack mLoadFinisCallBack;
     private Context context;
    public UserCommentsAdapter(Context ctx,  List<CommentsInfoJson> dlist, LoadFinishCallBack mLoadFinisCallBack) {
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
    public void notifyUpdate(CommentsInfoJson commentsInfoJson) {
        commentsInfoJson.setCommentTime("刚才");
        data.add(commentsInfoJson);
        notifyDataSetChanged();
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usercomment, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        //  holder.tv_favourNum.setText(data.get(position).getId());
        final CommentsInfoJson commentsInfoJson = data.get(position);

        holder.tv_content.setText(commentsInfoJson.getContent());
        String time = commentsInfoJson.getCommentTime();
        if( time!= null && time.length()>12) {
            holder.tv_commenttime.setText(AndroidUtils.format(time));
        }
        else
            holder.tv_commenttime.setText(time);
     //   LogUtil.d("format", ""+holder.tv_commenttime.getText());
        holder.tv_username.setText(commentsInfoJson.getRemark());
        if(null != holder.tv_usergender)
        holder.tv_usergender.setText(" ");
        if(null != holder.tv_userage)
        holder.tv_userage.setText(" ");
//        holder.tv_username.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, GreetingActivity.class);
//                intent.putExtra("userId", commentsInfoJson.getUserId());
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//
//             }
//        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private void loadData() {

                 data.addAll(CommentsInfoJson.load(10,page * 10));
                mLoadFinisCallBack.loadFinish(null);
                notifyDataSetChanged();

    }
}




