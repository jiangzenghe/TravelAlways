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

import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.Recommend;
import com.imyuu.travel.model.RecommendImage;
import com.imyuu.travel.model.ScenicIntroductionJson;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FrescoFactory;
import com.imyuu.travel.util.LogUtil;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by administor on 2015/5/3.
 */
public class ScenicSummaryFragment extends Fragment {
    private com.imyuu.travel.view.JustifyTextView scenicSummary;
    private TextView scenicName;
    private TextView scenicLevel;
    private TextView scenicType;
    private RecyclerView recyclerView;
    private ScenicSummaryAdaptor adaptor;

    /**
     * 景区简介图
     */
    private ArrayList<RecommendImage> imageList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_scenicsummary, container, false);
        scenicSummary = (com.imyuu.travel.view.JustifyTextView) view.findViewById(R.id.tv_summary_scenic);
        scenicName = (TextView) view.findViewById(R.id.tv_summary_scenicname);
        scenicLevel = (TextView) view.findViewById(R.id.tv_summary_sceniclevel);
        scenicType = (TextView) view.findViewById(R.id.tv_summary_scenictype);
        Intent intent = getActivity().getIntent();
        String scenicId = intent.getStringExtra("scenicId");
        Log.d("ScenicSummaryFragment", scenicId + " is called");
        scenicName.setText(intent.getStringExtra("scenicName"));
        imageList = new ArrayList<>();
        adaptor = new ScenicSummaryAdaptor();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_summary_scenic);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptor);
        initData(scenicId);
        return view;
    }

    /**
     * 向网络发起请求初始化数据
     *
     * @param scenicId
     */
    private void initData(final String scenicId) {

        ApiClient.getIuuApiClient().queryScenicIntro(scenicId, new Callback<ScenicIntroductionJson>() {
            @Override
            public void success(ScenicIntroductionJson scenicIntroductionJson, Response response) {
                if(null == scenicIntroductionJson)
                    return;
                LogUtil.d("ScenicSummaryFragment",scenicIntroductionJson.toString());
                setIntroductionInfo(scenicIntroductionJson);
                imageList.addAll(scenicIntroductionJson.getImageList());
                for(RecommendImage recommend: scenicIntroductionJson.getImageList())
                {
                    recommend.setScenicId(scenicId);
                    LogUtil.d("ScenicSummaryFragment",recommend.toString());
                    recommend.save();
                }
                scenicIntroductionJson.save();
            }

            @Override
            public void failure(RetrofitError error) {
                ScenicIntroductionJson scenicIntroductionJson = ScenicIntroductionJson.load(scenicId);
                if(null != scenicIntroductionJson) {
                    setIntroductionInfo(scenicIntroductionJson);
                    imageList.clear();
                    imageList.addAll(RecommendImage.load(scenicId));
                }else
                Toast.makeText(getActivity(), "在线加载景区简介数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setIntroductionInfo(ScenicIntroductionJson scenicIntroductionJson) {
        scenicLevel.setText(scenicIntroductionJson.getScenicLevel());
        scenicType.setText(scenicIntroductionJson.getScenicType());
        scenicSummary.setText(scenicIntroductionJson.getDesc());
        Log.d("ScenicIntroductionJson", scenicIntroductionJson.toString());
        adaptor.notifyDataSetChanged();
    }

    /**
     * 自定义ViewHolder
     */
    private static class ViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView scenic;

        public ViewHolder(View itemView) {
            super(itemView);
            scenic = (SimpleDraweeView) itemView.findViewById(R.id.item_scenic_image);
        }
    }

    class ScenicSummaryAdaptor extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scenicsummary, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
           // holder.scenic.setImageURI(Uri.parse(Config.IMAGE_SERVER_ADDR + imageList.get(position).getImageUrl()));

            ImageRequest request = FrescoFactory.getImageRequest(holder.scenic, Config.IMAGE_SERVER_ADDR + imageList.get(position).getImageUrl());

            DraweeController draweeController =  FrescoFactory.getPipelineControllder(request, holder.scenic);
            holder.scenic.setController(draweeController);


        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }
    }
}
