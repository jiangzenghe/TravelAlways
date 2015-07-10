package com.imyuu.travel.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imyuu.travel.util.Config;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class ApiClient {
    private static IuuApiInterface iuuApiInterfaceService;
    private static SocialApiService socialApiSerivce;
    private static UserApiService userApiSerivce;
    private static MapApiService mapApiSerivce;
    private static SysApiService sysApiSerivce;
    private static RestAdapter restAdapter = null;

    static {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(//"http://192.168.1.100:8080/api")
                         Config.SERVER_ADDR)
                .setConverter(new GsonConverter(gson)).build();
    }

    public static IuuApiInterface getIuuApiClient() {
        if (iuuApiInterfaceService == null) {

            iuuApiInterfaceService = restAdapter.create(IuuApiInterface.class);
        }
        return iuuApiInterfaceService;
    }

    public static SocialApiService getSocialService() {
        if (socialApiSerivce == null) {
            socialApiSerivce = restAdapter.create(SocialApiService.class);
        }
        return socialApiSerivce;
    }

    public static UserApiService getUserService() {
        if (userApiSerivce == null) {
            userApiSerivce = restAdapter.create(UserApiService.class);
        }
        return userApiSerivce;
    }

    public static MapApiService getMapService() {
        if (mapApiSerivce == null) {
            mapApiSerivce = restAdapter.create(MapApiService.class);
        }
        return mapApiSerivce;
    }


    public static SysApiService getSysService() {
        if (sysApiSerivce == null) {
            sysApiSerivce = restAdapter.create(SysApiService.class);
        }
        return sysApiSerivce;
    }


}
