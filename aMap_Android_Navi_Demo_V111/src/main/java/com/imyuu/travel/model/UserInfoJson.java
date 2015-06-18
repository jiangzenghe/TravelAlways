package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

@Table(name="UserInfo")
public class UserInfoJson extends Model implements Serializable {
    @Expose
    @Column(name="userId")
    private String userId;
    @Expose
    @Column(name="loginName")
    private String loginName;
    @Expose
    @Column(name="password")
    private String password;  //MD5加密
    @Expose
    @Column(name="imageUrl")
    private String imageUrl;  //头像
    @Expose
    @Column(name="nickName")
    private String nickName;
    @Expose
    @Column(name="address")
    private String address;
    @Expose
    @Column(name="city")
    private String city;
    @Expose
    @Column(name="gender")
    private String gender;
    @Expose
    @Column(name="birthday")
    private String birthday;
    @Expose
    @Column(name="desc")
    private String desc;
    @Expose
    @Column(name="level")
    private String level;  //级别

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    @Expose
    @Column(name="age")
    private Integer  age;
    @Expose
    @Column(name="tel")
    private String tel;
    @Expose
    @Column(name="email")
    private String email;
    private String verifyCode;  //短信验证码
    @Expose
    private String ssoSource;//  集成登录的来源
    @Expose
    private String ssoAccount;  //集成登录账号
    @Expose
    private Integer platform; //0:android 1:ios
    @Expose
    private Double lat;
    @Expose
    private Double lng;
    @Expose
    @Column(name="credits")
    private int credits;//积分


    public UserInfoJson() {
        this.userId = "";
        this.password = "";
        this.imageUrl = "";
        this.nickName = "";
        this.city = "";
        this.gender = "";
        this.desc = "";
        this.level = "1";
        this.tel = "";
        this.email = "";
        this.verifyCode = "";
        this.ssoSource = "0";
        this.ssoAccount = "";
        this.credits = 0;
    }

    @Override
    public String toString() {
        return "UserInfoJson [loginName=" + loginName + ", password=" + password
                + ", imageUrl=" + imageUrl + ", nickName=" + nickName
                + ", city=" + city + ", gender=" + gender + ", desc=" + desc
                + ", level=" + level + ", tel=" + tel + ", email=" + email
                + ", verifyCode=" + verifyCode + ", ssoSource=" + ssoSource
                + ", ssoAccount=" + ssoAccount + ", credits=" + credits + "]";
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getSsoSource() {
        return ssoSource;
    }

    public void setSsoSource(String ssoSource) {
        this.ssoSource = ssoSource;
    }

    public String getSsoAccount() {
        return ssoAccount;
    }

    public void setSsoAccount(String ssoAccount) {
        this.ssoAccount = ssoAccount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }


    public static UserInfoJson load(String loginName) {
        try {

            List<UserInfoJson> lineList = new Select().from(UserInfoJson.class)
                    .where(" tel=?", loginName).execute();
            if (null== lineList || lineList.size()<1)
                lineList = new Select().from(UserInfoJson.class)
                        .where(" loginName = ? ", loginName).execute();
            if (null != lineList && lineList.size()>0)
            {
                UserInfoJson json = lineList.get(0);
//                UserInfoJson json = new UserInfoJson();
//                json.setLoginName(userInfoBean.getLoginName());
//                json.setUserId(userInfoBean.getUserId());
//                json.setPassword(userInfoBean.getPassword());
//                json.setAge(userInfoBean.getAge());
//                json.setGender(userInfoBean.getGender());
//                json.setNickName(userInfoBean.getNickName());
//                json.setBirthday(userInfoBean.getBirthday());
//                json.setImageUrl(userInfoBean.getImageUrl());
//                json.setLevel(userInfoBean.getLevel());
//                json.setCredits(userInfoBean.getCredits());
//                json.setEmail(userInfoBean.getEmail());
                return json;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserInfoJson load() {
        try {

            List<UserInfoJson> lineList = new Select().from(UserInfoJson.class)
                    .execute();
            if (null != lineList && lineList.size()>0)
            {
                UserInfoJson json = lineList.get(0);
//                UserInfoJson json = new UserInfoJson();
//                json.setLoginName(userInfoBean.getLoginName());
//                json.setUserId(userInfoBean.getUserId());
//                json.setPassword(userInfoBean.getPassword());
//                json.setAge(userInfoBean.getAge());
//                json.setGender(userInfoBean.getGender());
//                json.setNickName(userInfoBean.getNickName());
//                json.setBirthday(userInfoBean.getBirthday());
//                json.setImageUrl(userInfoBean.getImageUrl());
//                json.setLevel(userInfoBean.getLevel());
//                json.setCredits(userInfoBean.getCredits());
//                json.setEmail(userInfoBean.getEmail());
                return json;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean  clear() {
        try {

            new Delete().from(UserInfoJson.class).execute();


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}


