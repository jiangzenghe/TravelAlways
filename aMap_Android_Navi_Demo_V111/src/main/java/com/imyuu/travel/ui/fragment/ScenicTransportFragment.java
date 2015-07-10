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

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.imyuu.travel.R;
import com.imyuu.travel.api.ApiClient;
import com.imyuu.travel.model.Recommend;
import com.imyuu.travel.model.ScenicTransportJson;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.FrescoFactory;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * ScenicTransportFragment 景点交通简介
 * Created by 图乐 on 2015/5/3.
 */
public class ScenicTransportFragment extends Fragment {
    TextView textView;
    private MapView mMapView;
    private AMap mAmap;
    /**
     * 景区简介图
     */
    private ArrayList<Recommend> imageList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_scenictransport, container, false);
        mMapView = (MapView) view.findViewById(R.id.transportmap);
        textView = (TextView) view.findViewById(R.id.tv_transport_scenic);
        mMapView.onCreate(savedInstanceState);
        mAmap = mMapView.getMap();
        //mAmap.moveCamera();
        Intent intent = getActivity().getIntent();
        Double scenic_lat = intent.getDoubleExtra("scenic_lat",37.001);
        Double scenic_lng = intent.getDoubleExtra("scenic_lng",120.114);
        mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(scenic_lat, scenic_lng), 16));



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

        ApiClient.getIuuApiClient().queryScenicTransport(scenicId, new Callback<ScenicTransportJson>() {
            @Override
            public void success(ScenicTransportJson scenicTransportJson, Response response) {
                initTransport(scenicTransportJson);
                if(scenicTransportJson != null)
                    scenicTransportJson.save();
              }

            @Override
            public void failure(RetrofitError error) {
                ScenicTransportJson transportJson = ScenicTransportJson.load(scenicId);
                if(null != transportJson)
                 initTransport(transportJson);
                else
                Toast.makeText(getActivity(), "加载交通数据失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void initTransport(ScenicTransportJson scenicTransportJson)
    {
       // scenicTransport.setImageURI(Uri.parse(Config.IMAGE_SERVER_ADDR + scenicTransportJson.getImageURL()));
        if(null == scenicTransportJson)
            return;
        try {
//            ImageRequest request = ImageRequestBuilder
//                    .newBuilderWithSource(Uri.parse(Config.IMAGE_SERVER_ADDR + scenicTransportJson.getImageURL()))
//                    .setProgressiveRenderingEnabled(true)
//                    .build();
//            scenicTransport.setDrawingCacheEnabled(true);
//            PipelineDraweeController controller =  (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
//                    .setImageRequest(request)
//                    .setOldController(scenicTransport.getController())
//                    .build();
//
//            scenicTransport.setController(controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        textView.setText(scenicTransportJson.getDesc());

    }

}
