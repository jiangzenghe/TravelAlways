package com.imyuu.travel.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.imyuu.travel.R;
import com.imyuu.travel.adapters.InstrumentedDraweeView;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.base.AMapHelper;
import com.imyuu.travel.base.AppApplication;
import com.imyuu.travel.callback.LoadFinishCallBack;
import com.imyuu.travel.model.ScenicAdvertJson;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.ui.ScenicAreaActivity;
import com.imyuu.travel.ui.UserHomeActivity;
import com.imyuu.travel.util.ApplicationHelper;
import com.imyuu.travel.util.Config;

import com.imyuu.travel.util.FrescoFactory;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.util.ToastUtil;
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
public class ScenicAreaFragment extends Fragment
      //  implements        SwipeRefreshLayout.OnRefreshListener
{
    @InjectView(R.id.recycler_view)
    AutoLoadRecyclerView mRecyclerView;
    @InjectView(R.id.bt_home)
    Button home;
    @InjectView(R.id.bt_message)
    Button message;
    @InjectView(R.id.bt_my)
    Button my;
    @InjectView(R.id.bt_search)
    EditText bt_search;
    private ArrayList<ScenicAreaJson> data;
//    @InjectView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isRefresh = false;//是否刷新中
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
        home.setBackgroundResource(R.drawable.tab_home_press);
        my.setBackgroundResource(R.drawable.ic_tab_my);
        message.setBackgroundResource(R.drawable.ic_tab_message);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLoadFinisCallBack = mRecyclerView;
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
//        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }
//    public void onRefresh() {
//        if(mAdapter.page>0) {
//            mSwipeRefreshLayout.setRefreshing(false);
//            return;
//        }
//        if(!isRefresh){
//            isRefresh = true;
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//
//                mAdapter.loadFirst();
//                mAdapter.notifyDataSetChanged();
//                mSwipeRefreshLayout.setRefreshing(false);
//                isRefresh= false;
//            }
//        }, 2000); }
//    }

    @Override
    public void onResume()
    {
        super.onResume();
        home.setBackgroundResource(R.drawable.tab_home_press);
        my.setBackgroundResource(R.drawable.ic_tab_my);
        message.setBackgroundResource(R.drawable.ic_tab_message);
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
        Intent intent = new Intent(getActivity(), UserHomeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tab)
    public void tabClick(View v) {
    }

    @OnClick(R.id.bt_home)
    public void homeClick(View v) {
        home.setBackgroundResource(R.drawable.ic_tab_home_press);
        my.setBackgroundResource(R.drawable.ic_tab_my);
        message.setBackgroundResource(R.drawable.ic_tab_message);

    }

    @OnClick(R.id.bt_search)
    public void searchClick() {
        if (null == bt_search.getText())
            return;
        String keyword = bt_search.getText().toString();
        searchScenicInfo(keyword);
    }

    public  void searchScenicInfo(String keyword) {
        if (null == keyword)
            return;
        ApiClient.getIuuApiClient().queryByKeyWord(keyword, new Callback<List<ScenicAreaJson>>() {
            @Override
            public void success(List<ScenicAreaJson> scenicAreaJsons, Response response) {

                bt_search.setText("");
                if (scenicAreaJsons.size() > 0) {
                    data.clear();
                    if (AppApplication.getInstance().getMyLocation() != null) {
                        AMapHelper.updateScenicAreaDistance(scenicAreaJsons);
                        data.addAll(scenicAreaJsons);
                        mLoadFinisCallBack.loadFinish(null);
                        mAdapter.notifyUpdate();
                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {
                if( null != error && null != error.getResponse() && error.getResponse().getStatus()==500)
                {
                    data.clear();

                    data.addAll(ScenicAreaJson.load());
                    AMapHelper.updateScenicAreaDistance(data);
                    mLoadFinisCallBack.loadFinish(null);
                    mAdapter.notifyUpdate();
                }
                else
                {
                    ToastUtil.show(getActivity(), "搜索景区加载失败");
                    mLoadFinisCallBack.loadFinish(null);
                }
            }
        });
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

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_favourNum;
        private TextView tv_distance;
        private TextView tv_jibie;
        private TextView tv_type;
        private TextView tv_name;
        private SimpleDraweeView image;
        private ImageView tv_warning;

        //用于处理多次点击造成的网络访问
        private boolean isClickFinish;

        public ViewHolder(View contentView) {
            super(contentView);

            isClickFinish = true;
            tv_name = (TextView) contentView.findViewById(R.id.tv_name);
            tv_favourNum = (TextView) contentView.findViewById(R.id.tv_favourNum);
            tv_distance = (TextView) contentView.findViewById(R.id.tv_distance);
            tv_jibie = (TextView) contentView.findViewById(R.id.tv_jibie);
            tv_type = (TextView) contentView.findViewById(R.id.tv_scenic_type);
            image = (SimpleDraweeView) contentView.findViewById(R.id.image);
            tv_warning = (ImageView) contentView.findViewById(R.id.tv_warning);
            tv_warning.setImageResource(R.drawable.h215);
        }
    }

    private class ScenicFragmentAdapter extends RecyclerView.Adapter<ViewHolder> {

        private int page = -1;

        public ScenicFragmentAdapter() {
            data = new ArrayList<>();
        }

        public void loadNextPage() {
            page++;
            loadData();
        }

        public void notifyUpdate() {
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scenicarea, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //  holder.tv_favourNum.setText(data.get(position).getId());
            final ScenicAreaJson scenicAreaJson = data.get(position);

            ImageRequest request = FrescoFactory.getImageRequest(holder.image,Config.IMAGE_SERVER_ADDR + scenicAreaJson.getSmallImage());

            DraweeController  draweeController =  FrescoFactory.getPipelineControllder(request, holder.image);
            holder.image.setController(draweeController);

            //        holder.image.setImageURI(Uri.parse(Config.IMAGE_SERVER_ADDR + scenicAreaJson.getSmallImage()));
            holder.tv_name.setText(scenicAreaJson.getScenicName());
            holder.tv_type.setText(scenicAreaJson.getScenicType());
            holder.tv_favourNum.setText("\t" + scenicAreaJson.getFavourNum());
            holder.tv_favourNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String favor_num =  holder.tv_favourNum.getText().toString();
                    if(null == ApplicationHelper.getInstance().getLoginUser())
                    {
                        ToastUtil.show(getActivity(), "请先登录" );
                        return ;
                    }
                    String userId = ApplicationHelper.getInstance().getLoginUser().getUserId();
                    LogUtil.d("1111", "bt_favorClick");

                    ApiClient.getSocialService().sendFavors(scenicAreaJson.getScenicId(), userId, new Callback<ServiceState>() {
                        @Override
                        public void success(ServiceState state, Response response) {
                            if (state.getStateCode() == 0)
                                holder.tv_favourNum.setText("" + (Integer.parseInt(favor_num.trim()) + 1));
                            else
                                ToastUtil.show(getActivity(), "不能重复点赞" );
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            ToastUtil.show(getActivity(), "点赞失败" );
                        }
                    });
                }
            });
            holder.tv_jibie.setText(scenicAreaJson.getScenicLevel());
            holder.tv_distance.setText("距离" + scenicAreaJson.getDistance());
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Log.v("----tule----", scenicAreaJson.toString());
                    Intent intent = new Intent(getActivity(), ScenicAreaActivity.class);
                    intent.putExtra("URL", Config.IMAGE_SERVER_ADDR + scenicAreaJson.getSmallImage());
                    intent.putExtra("scenicId", scenicAreaJson.getScenicId());
                    intent.putExtra("scenicName", scenicAreaJson.getScenicName());
                    try {
                        intent.putExtra("scenic_lat", (scenicAreaJson.getLat() + scenicAreaJson.getRight_lat()) / 2);
                        intent.putExtra("scenic_lng", (scenicAreaJson.getLng()+scenicAreaJson.getRight_lng())/2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return data.size();
        }
        public void loadFirst() {
            page = 0;
            loadData();
        }
        private void loadData() {

            ApiClient.getIuuApiClient().getScenicListbyPage(10, page * 10, new Callback<List<ScenicAreaJson>>() {
                @Override
                public void success(List<ScenicAreaJson> scenicAreaJsons, Response response) {
                    if (AppApplication.getInstance().getMyLocation() != null)
                        AMapHelper.updateScenicAreaDistance(scenicAreaJsons);
                    data.addAll(scenicAreaJsons);
                    mLoadFinisCallBack.loadFinish(null);
                    notifyDataSetChanged();
                   for (ScenicAreaJson areaJson:scenicAreaJsons)
                       areaJson.save();
                }

                @Override
                public void failure(RetrofitError error) {
                   // Log.d("Fresco","error state:"+error.getResponse().getStatus());
                    //if(error.getResponse().getStatus()==500)
                    {
                        data.addAll(ScenicAreaJson.load(10, page * 10));
                        AMapHelper.updateScenicAreaDistance(data);
                        mLoadFinisCallBack.loadFinish(null);
                        mAdapter.notifyUpdate();
                    }

                  //  Toast.makeText(getActivity(), "网络故障，推荐景区在线加载失败", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
