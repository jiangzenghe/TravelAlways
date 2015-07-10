package com.imyuu.travel.util;


import net.sf.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class WeChatSystemContext {
  private String accessToken;//接口访问凭据
  private long createTime;//接口访问凭据创建时间，理论上是2小时后过期
      static class WeChatSystemContextHolder {
      static WeChatSystemContext instance = new WeChatSystemContext();
  }   
  public static WeChatSystemContext getInstance() {
      return WeChatSystemContextHolder.instance;
  }   
  //是否过期
  public boolean isExpired() {
      long time = new Date().getTime();
      //如果当前记录时间为0
      if(this.createTime <= 0) {
          return true;
      }
      //判断记录时间是否超过7200s
      if(this.createTime/1000 + 7200 < time/1000) {
          return true;
      }
      return false;
  }
      //记录接口访问凭证
  public void saveLocalAccessonToke(String accessToken) {
      this.accessToken = accessToken;
      this.createTime = new Date().getTime();
  }
      public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
  }
  public String getAccessToken() {
      return accessToken;
  }
    public void setCreateTime(long createTime) {
      this.createTime = createTime;
  }
  public long getCreateTime() {
      return createTime;
  }

    public static String getAccessToken2() throws Exception {
        //首先判断本地有无记录，记录是否过期 7200s
        boolean isExpired = WeChatSystemContext.getInstance().isExpired();
        if(isExpired) {           //拼接url,APPID和APPSECRET从开发者中心获取
            String APPID = "";//APPID
            String APPSECRET = "";//APPSECRET
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + APPSECRET;
            //发起HTTPS的GET请求
//            String jsonStr = WeChatHTTPClient.get(url);
//            Map<String, Object> map = JSONObject.parseObject(jsonStr);
//            String accessToken = map.get("access_token").toString();
//            //记录到配置 access_token 当前时间
//            WeChatSystemContext.getInstance().saveLocalAccessonToke(accessToken);
//            return accessToken;
//        } else {
//            //从配置中直接获取access_token
//            return WeChatConfigSingleton.getInstance().getAccessToken();
        }
            return "";

    }

}