package com.imyuu.travel.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.facebook.drawee.view.SimpleDraweeView;
import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.base.AMapHelper;
import com.imyuu.travel.base.AppApplication;
import com.imyuu.travel.callback.LoadFinishCallBack;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.ui.ScenicAreaActivity;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.view.AutoLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tule on 2015/4/27.
 */
public class ScenicAreaSmallFragment extends Fragment {



    @InjectView(R.id.recycler_view)
    AutoLoadRecyclerView mRecyclerView;
    @InjectView(R.id.bt_home)
    Button home ;
    @InjectView(R.id.bt_message)
    Button message ;
    @InjectView(R.id.bt_my)
    Button my ;
//    @InjectView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout mSwipeRefreshLayout;

    private ScenicFragmentAdapter mAdapter;
    private LoadFinishCallBack mLoadFinisCallBack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scenicarea, container, false);
        ButterKnife.inject(this, view);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLoadFinisCallBack = mRecyclerView;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                mAdapter.loadNextPage();
            }
        });

//        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mAdapter.loadFirst();
//            }
//        });

        return view;
    }

    @OnClick(R.id.bt_message)
    public void messageClick(View v) {
        home.setBackgroundResource(R.drawable.ic_tab_home);
        my.setBackgroundResource(R.drawable.ic_tab_my);
        message.setBackgroundResource(R.drawable.ic_tab_message_press);
    }

    @OnClick(R.id.bt_my)
    public void myClick(View v) {
        home.setBackgroundResource(R.drawable.ic_tab_home);
        my.setBackgroundResource(R.drawable.ic_tab_my_press);
        message.setBackgroundResource(R.drawable.ic_tab_message);
    }

    @OnClick(R.id.tab)
    public void tabClick(View v) {}

    @OnClick(R.id.bt_home)
    public void homeClick(View v) {
        home.setBackgroundResource(R.drawable.ic_tab_home_press);
        my.setBackgroundResource(R.drawable.ic_tab_my);
        message.setBackgroundResource(R.drawable.ic_tab_message);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ScenicFragmentAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.loadNextPage();
       // mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private class ScenicFragmentAdapter extends RecyclerView.Adapter<ViewHolder> {

        public ScenicFragmentAdapter(){
            data = new ArrayList<>();
        }

        private int page=-1;
        private ArrayList<ScenicAreaJson> data ;

        public void loadNextPage() {
            page++;
            loadData();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scenicareasmall, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //  holder.tv_favourNum.setText(data.get(position).getId());
            final ScenicAreaJson scenicAreaJson =data.get(position);
            holder.image.setImageURI(Uri.parse(Config.IMAGE_SERVER_ADDR + scenicAreaJson.getSmallImage()));
            holder.tv_name.setText(scenicAreaJson.getScenicName());
            holder.tv_distance.setText("距离:" + scenicAreaJson.getDistance());
            holder.tv_favourNum.setText("" + scenicAreaJson.getFavourNum());
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //     Log.v("----tule----", scenicAreaJson.getId());
                    Intent intent = new Intent(getActivity(),ScenicAreaActivity.class);
                    intent.putExtra("URL",Config.IMAGE_SERVER_ADDR + scenicAreaJson.getSmallImage());
                    intent.putExtra("scenicId", scenicAreaJson.getScenicId());
                    intent.putExtra("scenicName",scenicAreaJson.getScenicName());
                    startActivity(intent);
                }
            });
        }



        @Override
        public int getItemCount() {
            return data.size();
        }

        public void loadFirst() {
            page = 1;
            loadData();
        }

        private void loadData() {
            ApiClient.getIuuApiClient().getScenicListbyPage(10, page*10, new Callback<List<ScenicAreaJson>>() {
                @Override
                public void success(List<ScenicAreaJson> scenicAreaJsons, Response response) {
                    if(AppApplication.getInstance().getMyLocation() != null)
                        AMapHelper.updateScenicAreaDistance(scenicAreaJsons);
                    data.addAll(scenicAreaJsons);
                    mLoadFinisCallBack.loadFinish(null);
                    notifyDataSetChanged();
                    // LogUtil.v(data.size()+" ");
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                    mLoadFinisCallBack.loadFinish(null);
                }
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_favourNum;
        private TextView tv_name;
        private ImageView tv_warning;
        private TextView tv_distance;
        private SimpleDraweeView image;


        //用于处理多次点击造成的网络访问
        private boolean isClickFinish;

        public ViewHolder(View contentView) {
            super(contentView);
            isClickFinish = true;
            tv_favourNum = (TextView) contentView.findViewById(R.id.tv_favourNum);
            tv_name = (TextView) contentView.findViewById(R.id.tv_name);
            tv_distance = (TextView) contentView.findViewById(R.id.tv_distance);
            image = (SimpleDraweeView) contentView.findViewById(R.id.image);
            tv_warning = (ImageView) contentView.findViewById(R.id.tv_warning);
            tv_warning.setImageResource(R.drawable.h215);
        }
    }
}
