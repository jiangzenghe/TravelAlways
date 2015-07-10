package com.imyuu.travel.api;

import com.imyuu.travel.model.CommentsInfoJson;
import com.imyuu.travel.model.MessageJson;
import com.imyuu.travel.model.Photo;
import com.imyuu.travel.model.ReportJson;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserLocationModel;
import com.imyuu.travel.model.UserSharingModel;

import java.util.*;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by java on 2015/5/22.
 */
public interface SysApiService {
    ///--------------sys config ------------------
    @POST("/sys/sysmsg.do")
    void sendSysMessage(@Body MessageJson message, Callback<ServiceState> callback);

    @POST("/sys/report.do")
    void reportProblem(@Body ReportJson message, Callback<ServiceState> callback);


    @GET("/social/mycomments.do")
    void queryMyComments(@Query("userId") String userId, @Query("limit") int limit, @Query("offset") int offset, Callback<List<CommentsInfoJson>> callback);

    @GET("/social/mysharing.do")
    void queryMySharing(@Query("userId") String userId, @Query("limit") int limit, @Query("offset") int offset, Callback<List<UserSharingModel>> callback);


    @GET("/sys/historyTrips.do")
    void queryHistoryTrips(@Query("userId") String userId, Callback<List<UserLocationModel>> callback);

    @GET("/sys/startadvert.do")
    void queryStartAdvert( Callback<ServiceState> callback);

    @GET("/sys/serveraddress.do")
    void queryImageServer( Callback<Map<String,String>> callback);
}
