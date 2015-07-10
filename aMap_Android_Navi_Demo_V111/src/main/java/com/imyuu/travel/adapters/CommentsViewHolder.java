package com.imyuu.travel.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.imyuu.travel.R;

public class CommentsViewHolder extends RecyclerView.ViewHolder {
         TextView tv_commenttime;
         TextView tv_content;
        TextView tv_usergender;
        TextView tv_userage;
        TextView tv_username;

        //用于处理多次点击造成的网络访问
        private boolean isClickFinish;

        public CommentsViewHolder(View contentView) {
            super(contentView);

            isClickFinish = true;
            tv_commenttime = (TextView) contentView.findViewById(R.id.tv_commenttime);
            tv_content = (TextView) contentView.findViewById(R.id.tv_content);
            tv_usergender = (TextView) contentView.findViewById(R.id.tv_usergender);
            tv_userage = (TextView) contentView.findViewById(R.id.tv_userage);
            tv_username = (TextView) contentView.findViewById(R.id.tv_username);

        }
    }