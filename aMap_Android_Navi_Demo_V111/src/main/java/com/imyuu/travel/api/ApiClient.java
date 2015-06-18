package com.imyuu.travel.api;

import com.imyuu.travel.bean.ScenicAdvertOldModel;
import com.imyuu.travel.model.RecommendLine;
import com.imyuu.travel.model.ScenicAdvertJson;
import com.imyuu.travel.model.ScenicAreaJson;
import com.imyuu.travel.model.ScenicDetailJson;
import com.imyuu.travel.model.ScenicIntroductionJson;
import com.imyuu.travel.model.ScenicPointJson;
import com.imyuu.travel.model.ScenicTipJson;
import com.imyuu.travel.model.ScenicTransportJson;
import com.imyuu.travel.model.CityInfoJson;
import com.imyuu.travel.model.ServiceState;
import com.imyuu.travel.model.UserLocationModel;
import com.imyuu.travel.util.Config;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;


public class ApiClient {
    private static IuuApiInterface iuuApiInterfaceService;

    public static IuuApiInterface getIuuApiClient() {
        if (iuuApiInterfaceService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(//"http://192.168.1.104:8080/api")
                    Config.SERVER_ADDR)
                    .build();
            iuuApiInterfaceService = restAdapter.create(IuuApiInterface.class);
        }

        return iuuApiInterfaceService;
    }

//    public  static void uploadFile(File file,String imagename) {
//        String mimeType = "image/jpg";
//        TypedFile fileToSend = new TypedFile(mimeType, file);
//        IuuApiInterface fileWebService =  getIuuApiClient();
//        fileWebService.upload(fileToSend,imagename, Callback<Response> callback);
//     }

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
        @GET("/map/allspot.do")
        void queryScenicSpotLists(@Query("scenicId") String scenicId, Callback<List<ScenicPointJson>> callback);
        @GET("/map/recommendLine.do")
        void queryRecommendLines(@Query("scenicId") String scenicId, Callback<List<RecommendLine>> callback);
        @GET("/map/download.do")
        void downloadMap(@Query("scenicId") String scenicId, Callback<List<RecommendLine>> callback);
        @GET("/home/citylist.do")
        void getCityList( Callback<List<CityInfoJson>> callback);
        @GET("/home/hotcity.do")
        void getHotCityList( Callback<List<CityInfoJson>> callback);
        @GET("/detail/tips.do")
        void queryScenicTips(@Query("scenicId") String scenicId, Callback<ScenicTipJson> callback);

        @GET("/detail/updatefavor.do")
        void sendFavors(@Query("scenicId") String scenicId,@Query("userId") String userId, Callback<ServiceState> callback);

        @POST("user/reportlocation.do")
        void reportUserLocation(@Body UserLocationModel locationModel, Callback<ServiceState> callback);

        @GET("/map/adverts.do")
        void queryScenicAdvertLists(@Query("scenicId") String scenicId, Callback<List<ScenicAdvertJson>> callback);

        @Multipart
        @Headers({"Content-Type: image/jpeg"})
        @POST("/image/{imageName}/upload")
        void upload(@Part("fileContent") TypedFile file, @Path("imageName") String imageName, Callback<Response> callback);
    }
}
