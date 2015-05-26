package com.imyuu.travel.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.imyuu.travel.R;
import com.imyuu.travel.activity.DownloadActivity;

import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.Recommend;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.model.ScenicDetailJson;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.ui.MapActivity;
import com.imyuu.travel.ui.ScenicAreaActivity;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.LogUtil;

import java.util.ArrayList;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailFragments extends Fragment {

    private String TAG="DetailFragment";
    private ScenicSmallAdaptor adaptor;
    private ArrayList<Recommend> data;
    private   RecyclerView recyclerView ;
    private ScenicAreaJson scenicAreaJson;
    @InjectView(R.id.download_progress)
     ProgressBar progressBar;
    @InjectView(R.id.text_download)
     TextView resultView;
    @InjectView(R.id.bt_favor)
    Button bt_favor;
    private int reportSuccess;
    String scenicId ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment, container, false);
        ButterKnife.inject(this, view);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.sdv_image);
         Intent intent=getActivity().getIntent();
        String url=intent.getStringExtra("URL");
        scenicId=intent.getStringExtra("scenicId");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_scincsmall);
        initData(scenicId);
        simpleDraweeView.setImageURI(Uri.parse(url));
        return view;
    }

    private void initData( final String scenicId) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        adaptor = new ScenicSmallAdaptor(new ArrayList<Recommend>());
        recyclerView.setAdapter(adaptor);
        ApiClient.getIuuApiClient().queryScenicDetail(scenicId, new Callback<ScenicDetailJson>() {
            @Override
            public void success(ScenicDetailJson scenicDetailJson, Response response) {
              //  LogUtil.v(scenicDetailJson.toString());
                scenicAreaJson = new ScenicAreaJson();
                scenicAreaJson.setScenicId(scenicId);
                scenicAreaJson.setScenicName(scenicDetailJson.getScenicName());
                scenicAreaJson.setImageUrl(scenicDetailJson.getImageUrl());
                scenicAreaJson.setLat(scenicDetailJson.getLat());
                scenicAreaJson.setLat(scenicDetailJson.getLng());
                // to be continued
                if (true)
                    resultView.setText(scenicDetailJson.getMapSize());
                else {
                    resultView.setText("已下载");
                }
                adaptor.data.addAll(scenicDetailJson.getRecommendScenicList());
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "加载景区详情失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    class  ScenicSmallAdaptor extends  RecyclerView.Adapter<MyViewHolder>{
        private ArrayList<Recommend> data;

        public ScenicSmallAdaptor(ArrayList<Recommend> data) {
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_scenicdetail_small,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Recommend recommend =  data.get(position);
            holder.tv_ecommend_name.setText(data.get(position).getName());
            holder.simpleDraweeView_recommend_pic.setImageURI(Uri.parse(Config.IMAGE_SERVER_ADDR +recommend.getImageUrl()));
            holder.simpleDraweeView_recommend_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //     Log.v("----tule----", scenicAreaJson.getId());
                    Intent intent = new Intent(getActivity(),ScenicAreaActivity.class);
                    intent.putExtra("URL",Config.IMAGE_SERVER_ADDR + recommend.getImageUrl());
                    intent.putExtra("scenicId", recommend.getIntentLink());
                    intent.putExtra("scenicName",recommend.getName());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (data!=null)
                return data.size();
            return 0;
        }
    }

    /**
     * 推荐等ViewHolder
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_ecommend_name;
        private SimpleDraweeView simpleDraweeView_recommend_pic;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_ecommend_name = (TextView) itemView.findViewById(R.id.item_tv_scenicdetailname);
            simpleDraweeView_recommend_pic = (SimpleDraweeView) itemView.findViewById(R.id.item_smp_scenicdetail);
        }
    }


    @OnClick(R.id.bt_enter)
    public void enterClick() {
        Log.d(TAG,"enterClick");
        Intent intent = new Intent(getActivity(),MapActivity.class);
        intent.putExtra("scenicInfo",scenicAreaJson);
        startActivity(intent);
    }
    @OnClick(R.id.bt_favor)
    public void bt_favorClick() {
        final String favor_num = bt_favor.getText().toString();
        String userId = "123456";
         LogUtil.d("1111","bt_favorClick");

        ApiClient.getIuuApiClient().sendFavors(scenicId,userId, new Callback<ServiceState>() {
            @Override
            public void success(ServiceState state, Response response) {
                if(state.getStateCode()==0)
                   bt_favor.setText(""+(Integer.parseInt(favor_num)+1));
                else
                    Toast.makeText(getActivity(), "不能重复点赞", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "点赞失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    DownloadActivity   downloadActivity = null;
    @OnClick(R.id.button_download)
    public void downloadClick() {
         downloadActivity = new DownloadActivity();
        downloadActivity.download(scenicAreaJson.getScenicId(), myhandler,progressBar);
    }

    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    progressBar.setProgress(msg.getData().getInt("size"));
                    float num = (float)progressBar.getProgress()/(float)progressBar.getMax();
                    int result = (int)(num*100);
                    resultView.setText(result+ "%");
                    break;
                case 2:
                    //显示下载成功信息
                    reportSuccess++;
                    if(reportSuccess>= Config.THREAD_NUM){
                        resultView.setText("100%");
                        resultView.setText( "数据解析中");
                        downloadActivity.parseAndSaveJson(scenicAreaJson.getScenicId(),resultView);
                    }

                    break;
                case -1:
                    //显示下载错误信息
                    Toast.makeText(getActivity(), "下载错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
