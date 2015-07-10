package com.imyuu.travel.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.imyuu.travel.R;
import com.imyuu.travel.base.AppApplication;
import com.imyuu.travel.model.Food;
import com.imyuu.travel.model.Hotel;
import com.imyuu.travel.util.Config;
import com.imyuu.travel.util.HttpUtil;
import com.imyuu.travel.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by administor on 2015/5/11.
 */
public class ScenicHotelFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private HotelAdaptor myAdaptor;
    private Double latitude;
    private Double lontitude;
    private List<Hotel> hotellist;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Intent intent = getActivity().getIntent();
        latitude = intent.getDoubleExtra("scenic_lat",37.001);
        lontitude = intent.getDoubleExtra("scenic_lng",120.114);
        if(null == latitude)
            latitude =   AppApplication.getInstance().getMyLocation().latitude;
        if(null == lontitude)
            lontitude = AppApplication.getInstance().getMyLocation().longitude;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister
        EventBus.getDefault().unregister(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scenichotel, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_scenichotel);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), LinearLayoutManager.VERTICAL));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void onEventMainThread(Object obj) {
        myAdaptor.data.clear();
        Hotel dianping = new Hotel();
        dianping.setS_photo_url(Config.IMAGE_SERVER_ADDR+"dianping.png");
        dianping.setDistance("0");
        dianping.setAvg_price("0");
        dianping.setBusiness_url("http://www.dianping.com");
        dianping.setName("以下数据来自大众点评");
        myAdaptor.data.add(dianping);
        myAdaptor.data.addAll(hotellist);
        myAdaptor.notifyDataSetChanged();

    }

    private void initData() {
        myAdaptor = new HotelAdaptor();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(myAdaptor);
        Thread mThread = new Thread() {
            @Override
            public void run() {
                HashMap<String, String> paramMap = new HashMap<>();
                paramMap.put("latitude", "" +latitude);
                paramMap.put("longitude", "" + lontitude);
                paramMap.put("limit", "30");
                paramMap.put("radius", "5000");
                paramMap.put("category", "酒店");
                String result =  HttpUtil.requestApi( paramMap);
                hotellist = HttpUtil.parseHotelJson(result);
               EventBus.getDefault().post(new Object());

            }
        };
        mThread.start();
    }

    private class MyHotelViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView foodImg;
        SimpleDraweeView star;
        TextView foodName;
        TextView foodPrice;
        TextView foodAddress;
        TextView foodDistance;

        public MyHotelViewHolder(View itemView) {
            super(itemView);
            foodImg = (SimpleDraweeView) itemView.findViewById(R.id.hotelimg);
            star = (SimpleDraweeView) itemView.findViewById(R.id.hotel_star);
            foodName = (TextView) itemView.findViewById(R.id.tv_hotelname);
            foodPrice = (TextView) itemView.findViewById(R.id.tv_hotelprice);
            foodAddress = (TextView) itemView.findViewById(R.id.tv_hoteladdress);
            foodDistance = (TextView) itemView.findViewById(R.id.tv_hoteldistance);
        }
    }

    class HotelAdaptor extends RecyclerView.Adapter<MyHotelViewHolder> {
        ArrayList<Hotel> data;

        public HotelAdaptor() {
            data = new ArrayList<Hotel>();
        }

        @Override
        public MyHotelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_scenichotel, parent, false);
            return new MyHotelViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHotelViewHolder holder, int position) {

            final Hotel food = data.get(position);
            //   LogUtil.v(food.toString());
            holder.foodName.setText(food.getName());
            holder.foodAddress.setText(food.getAddress());
            holder.foodPrice.setText("￥" + food.getAvg_price() + "起");
            holder.foodDistance.setText("距我"+food.getDistance() + "米");
            holder.foodImg.setImageURI(Uri.parse(food.getS_photo_url()));
            holder.foodImg.setOnClickListener(new View.OnClickListener() {
                                                  @Override
             public void onClick(View v) {
                  String business_url = food.getBusiness_url();
                  if(null != business_url) {
                      Uri uri = Uri.parse(business_url);
                      Intent it = new Intent(Intent.ACTION_VIEW, uri);
                      startActivity(it);
                  }
              }
            });
            if(null!=food.getRating_s_img_url())
            holder.star.setImageURI(Uri.parse(food.getRating_s_img_url()));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void add(ArrayList<Hotel> foods) {
            data.addAll(foods);
            notifyDataSetChanged();
        }
    }
}
