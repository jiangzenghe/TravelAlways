package com.imyuu.travel.api;

import com.imyuu.travel.model.ChatMessageJson;
import com.imyuu.travel.model.CommentsInfoJson;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.model.UserLocationModel;
import com.imyuu.travel.model.UserSharingModel;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface SocialApiService {
    //---------------------social operation
    @POST("/social/chat.do")
    void sendChatMessage(@Body ChatMessageJson chatMessageJson, Callback<ServiceState> callback);

    @POST("/social/comments.do")
    void sendCommentsMessage(@Body CommentsInfoJson chatMessageJson, Callback<ServiceState> callback);

    @POST("/social/share.do")
    void sendSharing(@Body UserSharingModel sharingModel, Callback<ServiceState> callback);

    @GET("/social/queryNeighbors.do")
    void queryNeighborUser(@Query("scenicId") String scenicId, @Query("lat") Double lat, @Query("lng") Double lng, Callback<List<UserLocationModel>> callback);

    @POST("/social/enter.do")
    void enterUU(@Body UserInfoJson userInfoJson, Callback<ServiceState> callback);

    @GET("/social/querycomments.do")
    void queryUserComments(@Query("scenicId") String scenicId,@Query("limit") int limit,@Query("offset") int offset, Callback<List<CommentsInfoJson>> callback);
    @POST("/social/favor.do")
    void sendFavors(@Query("scenicId") String scenicId, @Query("userId") String userId, Callback<ServiceState> callback);


}