package com.imyuu.travel.api;

import com.imyuu.travel.model.Photo;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.SmsMessage;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.model.UserLocationModel;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by java on 2015/5/22.
 */
public interface UserApiService {
    ///--------------user register/sendverifycode/login------------------
    @POST("/user/reportlocation.do")
    void reportUserLocation(@Body UserLocationModel locationModel, Callback<ServiceState> callback);

    //WARNINg  userid在ServiceState中返回，之后提交的请求都通过userId作为主键
    @POST("/user/register.do")
    void userRegister(@Body UserInfoJson userInfoJson, Callback<ServiceState> callback);

    @POST("/user/login.do")
    void userLogin(@Body UserInfoJson userInfoJson, Callback<UserInfoJson> callback);

    @POST("/user/sendSMS.do")
    void requestVerifyCode(@Body SmsMessage telno, Callback<ServiceState> callback);

    @POST("/user/updateProfiler.do")
    void updateUserProfiler(@Body UserInfoJson userInfoJson, Callback<ServiceState> callback);

    @GET("/user/queryUser.do")
    void queryUserInfo(@Query("userId") String userId, Callback<UserInfoJson> callback);

    @POST("/user/modifyPassword.do")
    void modifyPassword(@Query("userId") String userId, @Query("password") String password, Callback<ServiceState> callback);

    //如果userId和telno与注册时相匹配，则发送短信，告知用户密码。
    @POST("/user/forgetPassword.do")
    void forgetPassword(@Query("userId") String userId, @Query("telno") String telno, Callback<ServiceState> callback);

    @GET("/image/user.do")
    void queryUserID(@Query("userID") String userId, Callback<String> callback);

    @Multipart
    @POST("/image/uploadPhoot.do")
    void uploadPhoto(@Part("image") TypedFile image,@Part("userId") String userId, Callback<ServiceState> callback);

    @POST("/image/upload.do")
    void sendPhoto(@Body Photo file, Callback<ServiceState> callback);

//@Part("userID") String userID,
}
