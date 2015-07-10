package com.imyuu.travel.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.imyuu.travel.R;
import com.imyuu.travel.adapters.UserCommentsAdapter;
import com.imyuu.travel.adapters.UserSharingAdapter;
import com.imyuu.travel.callback.LoadFinishCallBack;
import com.imyuu.travel.model.CommentsInfoJson;
import com.imyuu.travel.model.UserSharingModel;
import com.imyuu.travel.view.AutoLoadRecyclerView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserSharingActivity extends AppCompatActivity {
    UserSharingAdapter mAdapter = null;
    @InjectView(R.id.recycler_view)
    AutoLoadRecyclerView mRecyclerView;

    private ArrayList<UserSharingModel> data;
    private LoadFinishCallBack mLoadFinisCallBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sharing);
        data = new ArrayList<UserSharingModel>();
        ButterKnife.inject(this);
        initView();
    }


    public void initView( ) {

        //   View view = getLayoutInflater().inflate(R.layout.activity_comments_wall, null, false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLoadFinisCallBack = mRecyclerView;
        mAdapter = new UserSharingAdapter(getBaseContext(),data,mLoadFinisCallBack);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.loadNextPage();
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                mAdapter.loadNextPage();
            }
        });

    }

    @OnClick(R.id.image_mysharing_cancel)
    public void sharingCancelClick(View view)
    {
       this.finish();
    }

}
