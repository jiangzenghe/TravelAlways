package com.imyuu.travel.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.imyuu.travel.R;
import com.imyuu.travel.adapters.CommentAdapter;
import com.imyuu.travel.adapters.UserCommentsAdapter;
import com.imyuu.travel.callback.LoadFinishCallBack;
import com.imyuu.travel.model.CommentsInfoJson;
import com.imyuu.travel.view.AutoLoadRecyclerView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserCommentsActivity extends AppCompatActivity {

    UserCommentsAdapter mAdapter = null;
    @InjectView(R.id.recycler_view)
    AutoLoadRecyclerView mRecyclerView;

    private ArrayList<CommentsInfoJson> data;
    private LoadFinishCallBack mLoadFinisCallBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comments);
        data = new ArrayList<CommentsInfoJson>();
        ButterKnife.inject(this);
        initView();
    }


    public void initView( ) {

        //   View view = getLayoutInflater().inflate(R.layout.activity_comments_wall, null, false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLoadFinisCallBack = mRecyclerView;
        mAdapter = new UserCommentsAdapter(getBaseContext(),data,mLoadFinisCallBack);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.loadNextPage();
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                mAdapter.loadNextPage();
            }
        });

    }

    @OnClick(R.id.image_mycomenmt_cancel)
    public void sharingCancelClick(View view)
    {
        this.finish();
    }
}
