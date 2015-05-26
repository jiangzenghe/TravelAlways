package com.imyuu.travel.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.Recommend;
import com.imyuu.travel.model.ScenicIntroductionJson;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.LogUtil;
import com.imyuu.travel.view.JustifyTextView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by administor on 2015/5/3.
 */
public class ScenicSummaryFragment extends Fragment {
      private TextView scenicSummary;
    private TextView scenicName;
    private TextView scenicLevel;
    private TextView scenicType;
    private RecyclerView recyclerView;
    private ScenicSummaryAdaptor adaptor;

    /**
     * 景区简介图
     */
    private ArrayList<Recommend>  imageList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_scenicsummary, container, false);
        scenicSummary = (TextView) view.findViewById(R.id.tv_summary_scenic);
        scenicName = (TextView) view.findViewById(R.id.tv_summary_scenicname);
        scenicLevel = (TextView) view.findViewById(R.id.tv_summary_sceniclevel);
        scenicType = (TextView) view.findViewById(R.id.tv_summary_scenictype);
        Intent intent=getActivity().getIntent();
        String url=intent.getStringExtra("URL");
        String scenicId=intent.getStringExtra("scenicId");
        Log.d("ScenicSummaryFragment",scenicId+" is called");
        scenicName.setText(intent.getStringExtra("scenicName"));
        imageList = new ArrayList<>();
        adaptor = new ScenicSummaryAdaptor();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_summary_scenic);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptor);
        initData(scenicId);
        return view;
    }

    /**
     * 向网络发起请求初始化数据
     * @param scenicId
     */
    private void initData(String scenicId) {

        ApiClient.getIuuApiClient().queryScenicIntro(scenicId, new Callback<ScenicIntroductionJson>() {
            @Override
            public void success(ScenicIntroductionJson scenicIntroductionJson, Response response) {
                LogUtil.v(scenicIntroductionJson.toString());
                scenicLevel.setText(scenicIntroductionJson.getScenicLevel());
                scenicType.setText(scenicIntroductionJson.getScenicType());
                scenicSummary.setText(scenicIntroductionJson.getDesc());
                imageList.addAll(scenicIntroductionJson.getImageList());
                Log.d("ScenicIntroductionJson",scenicIntroductionJson.toString());
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "加载景区简介数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class  ScenicSummaryAdaptor extends RecyclerView.Adapter<ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scenicsummary,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.scenic.setImageURI(Uri.parse(Config.IMAGE_SERVER_ADDR+imageList.get(position).getImageUrl()));
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }


    /**
     * 自定义ViewHolder
     */
    private  static class ViewHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView scenic;

        public ViewHolder(View itemView) {
            super(itemView);
            scenic = (SimpleDraweeView) itemView.findViewById(R.id.item_scenic_image);
        }
    }
}
