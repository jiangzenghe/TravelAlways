package com.imyuu.travel.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.ScenicTipJson;
import com.imyuu.travel.util.Config;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * ScenicTransportFragment 景点tips
 * Created by 图乐 on 2015/5/3.
 */
public class ScenicTipsFragment extends Fragment {
     private SimpleDraweeView tipsDraweeView;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_scenictips, container, false);
       // tipsDraweeView = (SimpleDraweeView) view.findViewById(R.id.item_scenicTips_image);
        textView = (TextView) view.findViewById(R.id.tv_tips_scenic);

        Intent intent=getActivity().getIntent();
       String scenicId=intent.getStringExtra("scenicId");

        initData(scenicId);
        return view;
    }

    /**
     * 向网络发起请求初始化数据
     * @param scenicId
     */
    private void initData(String scenicId) {

        ApiClient.getIuuApiClient().queryScenicTips(scenicId, new Callback<ScenicTipJson>() {
            @Override
            public void success(ScenicTipJson scenicTipsJson, Response response) {
               // tipsDraweeView.setImageURI(Uri.parse(Config.IMAGE_SERVER_ADDR + scenicTipsJson.getImageURL()));
                textView.setText(scenicTipsJson.getDesc());
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "加载小贴士数据失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
