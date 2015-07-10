package com.imyuu.travel.ui.fragment;

import android.content.Intent;
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
import com.imyuu.travel.model.ScenicAdvertJson;
import com.imyuu.travel.model.ScenicTipJson;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * ScenicTransportFragment 景点tips
 * Created by 图乐 on 2015/5/3.
 */
public class ScenicTipsFragment extends Fragment {
    com.imyuu.travel.view.JustifyTextView textView;
    private SimpleDraweeView tipsDraweeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_scenictips, container, false);
        // tipsDraweeView = (SimpleDraweeView) view.findViewById(R.id.item_scenicTips_image);
        textView = (com.imyuu.travel.view.JustifyTextView) view.findViewById(R.id.tv_tips_scenic);

        Intent intent = getActivity().getIntent();
        String scenicId = intent.getStringExtra("scenicId");

        initData(scenicId);
        return view;
    }

    /**
     * 向网络发起请求初始化数据
     *
     * @param scenicId
     */
    private void initData(final String scenicId) {

        ApiClient.getIuuApiClient().queryScenicTips(scenicId, new Callback<ScenicTipJson>() {
            @Override
            public void success(ScenicTipJson scenicTipsJson, Response response) {
                // tipsDraweeView.setImageURI(Uri.parse(Config.IMAGE_SERVER_ADDR + scenicTipsJson.getImageURL()));
                if(null ==scenicTipsJson )
                    return;
                textView.setText(scenicTipsJson.getDesc());
                scenicTipsJson.save();
            }

            @Override
            public void failure(RetrofitError error) {
                ScenicTipJson scenicTipsJson = ScenicTipJson.load(scenicId);
                if(null != scenicTipsJson)
                    textView.setText(scenicTipsJson.getDesc());
                else
                Toast.makeText(getActivity(), "加载小贴士数据失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
