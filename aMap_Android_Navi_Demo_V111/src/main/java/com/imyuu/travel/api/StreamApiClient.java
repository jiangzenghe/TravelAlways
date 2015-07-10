package com.imyuu.travel.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imyuu.travel.model.Photo;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.model.UserLocationModel;
import com.imyuu.travel.util.Config;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
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

public class StreamApiClient {
    private static RestAdapter restAdapter = null;
    private static StreamApiService streamInterfaceService;
    static {
         restAdapter = new RestAdapter.Builder()
                .setEndpoint(//"http://192.168.1.100:8080/api")
                        Config.SERVER_ADDR)
                .build();
    }

    public static StreamApiService getStreamInterfaceService() {
        if (streamInterfaceService == null) {

            streamInterfaceService = restAdapter.create(StreamApiService.class);
        }
        return streamInterfaceService;
    }

public interface StreamApiService {

    //send crash file
    @POST("/sys/crashreport.do")
    void sendCrash(@Body Photo file, Callback<ServiceState> callback);

    @Multipart
    @POST("/image/uploadPhoto.do")
    void uploadPhoto(@Part("image") TypedFile image, @Part("userId") String userId, Callback<ServiceState> callback);

    @POST("/image/upload.do")
    void sendPhoto(@Body Photo file, Callback<ServiceState> callback);

//@Part("userID") String userID,
}
}