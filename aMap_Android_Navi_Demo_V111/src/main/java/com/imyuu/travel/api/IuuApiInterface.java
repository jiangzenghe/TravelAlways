package com.imyuu.travel.api;

import com.imyuu.travel.model.CityInfoJson;
import com.imyuu.travel.model.CommentsInfoJson;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.model.ScenicDetailJson;
import com.imyuu.travel.model.ScenicIntroductionJson;
import com.imyuu.travel.model.ScenicTipJson;
import com.imyuu.travel.model.ScenicTransportJson;
import com.imyuu.travel.model.ServiceState;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface IuuApiInterface {

    @GET("/home/pagelist.do")
    void getScenicListbyPage(@Query("limit") int limit, @Query("offset") int offset, Callback<List<ScenicAreaJson>> callback);

    @GET("/home/scenicquery.do")
    void queryByKeyWord(@Query("keyword") String keyword, Callback<List<ScenicAreaJson>> callback);

    @GET("/detail/detail.do")
    void queryScenicDetail(@Query("scenicId") String scenicId, Callback<ScenicDetailJson> callback);

    @GET("/detail/intro.do")
    void queryScenicIntro(@Query("scenicId") String scenicId, Callback<ScenicIntroductionJson> callback);

    @GET("/detail/transport.do")
    void queryScenicTransport(@Query("scenicId") String scenicId, Callback<ScenicTransportJson> callback);

    @GET("/home/citylist.do")
    void getCityList(Callback<List<CityInfoJson>> callback);

    @GET("/detail/tips.do")
    void queryScenicTips(@Query("scenicId") String scenicId, Callback<ScenicTipJson> callback);


    @GET("/home/hotcity.do")
    void getHotCityList( Callback<List<CityInfoJson>> callback);

}