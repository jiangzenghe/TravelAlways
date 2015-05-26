package com.imyuu.travel.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.imyuu.travel.model.ScenicTransportJson;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.LogUtil;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * ScenicTransportFragment 景点交通简介
 * Created by 图乐 on 2015/5/3.
 */
public class ScenicTransportFragment extends Fragment {
     private SimpleDraweeView scenicTransport;
    TextView textView;

    /**
     * 景区简介图
     */
    private ArrayList<Recommend>  imageList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_scenictransport, container, false);
        scenicTransport = (SimpleDraweeView) view.findViewById(R.id.item_scenictransport_image);
        textView = (TextView) view.findViewById(R.id.tv_transport_scenic);

        Intent intent=getActivity().getIntent();
        String url=intent.getStringExtra("URL");
        String scenicId=intent.getStringExtra("scenicId");

        initData(scenicId);
        return view;
    }

    /**
     * 向网络发起请求初始化数据
     * @param scenicId
     */
    private void initData(String scenicId) {

        ApiClient.getIuuApiClient().queryScenicTransport(scenicId, new Callback<ScenicTransportJson>() {
            @Override
            public void success(ScenicTransportJson scenicTransportJson, Response response) {
                scenicTransport.setImageURI(Uri.parse(Config.IMAGE_SERVER_ADDR+scenicTransportJson.getImageURL()));
                textView.setText(scenicTransportJson.getDesc());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "加载交通数据失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
