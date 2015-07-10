package com.imyuu.travel.api;

import com.imyuu.travel.model.AdvertReportJson;
import com.imyuu.travel.model.RecommendLine;
import com.imyuu.travel.model.ScenicPointJson;
import com.imyuu.travel.model.ScenicAdvertJson;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.ProvinceInfoJson;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by java on 2015/5/22.
 */
public interface MapApiService {
    @GET("/map/allspot.do")
    void queryScenicSpotLists(@Query("scenicId") String scenicId, Callback<List<ScenicPointJson>> callback);

    @GET("/map/recommendLine.do")
    void queryRecommendLines(@Query("scenicId") String scenicId, Callback<List<RecommendLine>> callback);

    @GET("/map/download.do")
    void downloadMap(@Query("scenicId") String scenicId, Callback<List<RecommendLine>> callback);

    @GET("/map/citymap.do")
    void queryCityMap( Callback<List<ProvinceInfoJson>> callback);

    @GET("/map/adverts.do")
    void queryScenicAdverts(@Query("scenicId") String scenicId, Callback<List<ScenicAdvertJson>> callback);

    //http://www.imyuu.com:8900/api/map/ reportadvert.do
    @POST("/map/reportadvert.do")
    void reportAdvertClick(@Body AdvertReportJson advertReportJson, Callback<ServiceState> callback);


}
