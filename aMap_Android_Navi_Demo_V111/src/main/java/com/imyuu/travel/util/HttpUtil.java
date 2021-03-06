package com.imyuu.travel.util;

import com.imyuu.travel.model.Food;
import com.imyuu.travel.model.Hotel;
import com.imyuu.travel.model.UserInfoJson;
import com.imyuu.travel.ui.MapAddActivity;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by administor on 2015/5/11.
 */
public class HttpUtil {
    public static final String APP_KEY = "9699551594"; //dianping

    public static final String API_URL = "http://api.dianping.com/v1/business/find_businesses";

    public static final String APP_SECRET = "f2acc603b7d248b7b0521023496a0a1a";

    /**
     * 获取请求字符串
     *
     * @param appKey
     * @param secret
     * @param paramMap
     * @return
     */
    public static String getQueryString(String appKey, String secret, Map<String, String> paramMap) {
        String sign = sign(appKey, secret, paramMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("appkey=").append(appKey).append("&sign=").append(sign);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            stringBuilder.append('&').append(entry.getKey()).append('=').append(entry.getValue());
        }
        String queryString = stringBuilder.toString();
        return queryString;
    }

    /**
     * 获取请求字符串，参数值进行UTF-8处理
     *
     * @param appKey
     * @param secret
     * @param paramMap
     * @return
     */
    public static String getUrlEncodedQueryString(String appKey, String secret, Map<String, String> paramMap) {
        String sign = sign(appKey, secret, paramMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("appkey=").append(appKey).append("&sign=").append(sign);
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            try {
                stringBuilder.append('&').append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(),
                        "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
        }
        String queryString = stringBuilder.toString();
        return queryString;
    }

    /**
     * 请求API
     *

     * @param paramMap
     * @return
     */
    public static String requestApi( HashMap<String, String> paramMap) {

        String apiUrl=HttpUtil.API_URL;
        String appKey=  HttpUtil.APP_KEY;
        String secret =  HttpUtil.APP_SECRET;
        String queryString = getQueryString(appKey, secret, paramMap);
        LogUtil.d("requestApi",queryString);
        StringBuffer response = new StringBuffer();
        HttpClientParams httpConnectionParams = new HttpClientParams();
        httpConnectionParams.setConnectionManagerTimeout(1000);
        HttpClient client = new HttpClient(httpConnectionParams);
        HttpMethod method = new GetMethod(apiUrl);

        try {
            if (queryString != null && !queryString.isEmpty()) {
                // Encode query string with UTF-8
                String encodeQuery = URIUtil.encodeQuery(queryString, "UTF-8");
                method.setQueryString(encodeQuery);
            }

            client.executeMethod(method);
            BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line).append(System.getProperty("line.separator"));
            }
            reader.close();
        } catch (URIException e) {
        } catch (IOException e) {
        } finally {
            method.releaseConnection();
        }
       // parseJson(response.toString());
        return response.toString();
    }


    public static UserInfoJson  requestWeiXinUserInfo(String apiUrl, String access_token, String openid,UserInfoJson userInfo) {

       StringBuilder stringBuilder = new StringBuilder(apiUrl);
       stringBuilder.append("access_token=").append(access_token).append("&openid=").append(openid);
       stringBuilder.append("&lang=zh_CN");
        String queryString = stringBuilder.toString();
        StringBuffer response = new StringBuffer(256);
        HttpClientParams httpConnectionParams = new HttpClientParams();
        httpConnectionParams.setConnectionManagerTimeout(2000);
        HttpClient client = new HttpClient(httpConnectionParams);
        HttpMethod method = new GetMethod(queryString);
        System.out.println("----queryString-from weixin----"+queryString);
        try {
//            if (queryString != null && !queryString.isEmpty()) {
//                // Encode query string with UTF-8
//                String encodeQuery = URIUtil.encodeQuery(queryString, "UTF-8");
//                method.setQueryString(encodeQuery);
//            }

            client.executeMethod(method);
            BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.append(line).append(System.getProperty("line.separator"));
            }
            reader.close();
        } catch (URIException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        LogUtil.d("parseWeixinJson",response.toString());
        UserInfoJson userInfoJson =  parseWeixinJson(response.toString(),userInfo);
        return userInfoJson;
    }

    private static UserInfoJson parseWeixinJson(String jsonString,UserInfoJson userInfoJson) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String errorCode = jsonObject.getString("errcode");
            if("0".equals(errorCode)) {
                userInfoJson.setNickName(jsonObject.getString("nickname"));
                userInfoJson.setAddress(jsonObject.getString("province") + jsonObject.getString("city"));
                userInfoJson.setImageUrl(jsonObject.getString("headimgurl"));
                String gender = jsonObject.getString("sex");
                if ("1".equals(gender))
                    userInfoJson.setGender("男");
                else if ("2".equals(gender))
                    userInfoJson.setGender("女");
                else
                    userInfoJson.setGender("未知");
            }
            else
                System.out.println("----errcode-from weixin----"+errorCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfoJson;
    }

    public static ArrayList<Food> parseFoodJson(String jsonString) {
        ArrayList<Food> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if ("OK".equals(jsonObject.get("status"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("businesses");
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String name = object.getString("name");
                    String s_photo_url = object.getString("s_photo_url");
                    String avg_price = object.getString("avg_price");
                    String rating_s_img_url = object.getString("rating_s_img_url");
                    String address = object.getString("address");
                    String distance = object.getString("distance");
                    String business_url = object.getString("business_url");
                    list.add(new Food(s_photo_url, avg_price, rating_s_img_url, name, address, distance,business_url));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Hotel> parseHotelJson(String jsonString) {
        ArrayList<Hotel> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if ("OK".equals(jsonObject.get("status"))) {
                JSONArray jsonArray = jsonObject.getJSONArray("businesses");
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String name = object.getString("name");
                    String s_photo_url = object.getString("s_photo_url");
                    String avg_price = object.getString("avg_price");
                    String rating_s_img_url = object.getString("rating_s_img_url");
                    String address = object.getString("address");
                    String distance = object.getString("distance");
                    String business_url = object.getString("business_url");
                    list.add(new Hotel(s_photo_url, avg_price, rating_s_img_url, name, address, distance,business_url));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 签名
     *
     * @param appKey
     * @param secret
     * @param paramMap
     * @return
     */
    public static String sign(String appKey, String secret, Map<String, String> paramMap) {
        // 参数名排序
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);

        // 拼接参数
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appKey);
        for (String key : keyArray) {
            stringBuilder.append(key).append(paramMap.get(key));
        }

        stringBuilder.append(secret);
        String codes = stringBuilder.toString();

        // SHA-1签名
        // For Android
        String sign = new String(Hex.encodeHex(DigestUtils.sha(codes))).toUpperCase();

        return sign;
    }
}
