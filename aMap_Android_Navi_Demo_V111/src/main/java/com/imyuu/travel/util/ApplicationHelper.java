package com.imyuu.travel.util;

import com.amap.api.maps.model.LatLng;

import com.imyuu.travel.model.UserInfoJson;

import java.util.ArrayList;

public class ApplicationHelper {

    private static ApplicationHelper applicationHelper = null;
    public boolean m_bKeyRight;
    Object argument;
    LatLng latlng;
    Player palyer;
    Object photoargument;
    ArrayList questionlist;
    String viewpageIndex;
    private double Lat;
    private double Lng;
    private int WindowHeight;
    private int WindowWidth;
    private int cityId;
    private String cityNameString;
    private boolean isLogin;
    private String locationtring;
    private ArrayList pArrayList;
    private int siteid;
    private ArrayList views;

    private UserInfoJson  loginUser = null;
    public ApplicationHelper() {
        m_bKeyRight = true;
    }

    public static ApplicationHelper getInstance() {
        if (applicationHelper == null) {
            applicationHelper = new ApplicationHelper();

        }
        return applicationHelper;
    }

    public void logOut()
    {
        UserInfoJson.clear();
        loginUser = null;
    }

    public UserInfoJson getLoginUser()
    {
        if(loginUser == null)
        {

            loginUser = UserInfoJson.load();
            if(loginUser != null)
                return loginUser;
        }


        return loginUser;
    }

    public void refreshUserInfo(String loginName)
    {

        loginUser = UserInfoJson.load(loginName);
    }

    public Object getArgument() {
        Object obj = argument;
        argument = null;
        return obj;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int i) {
        cityId = i;
    }

    public String getCityNameString() {
        return cityNameString;
    }

    public void setCityNameString(String s) {
        cityNameString = s;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double d) {
        Lat = d;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng1) {
        latlng = latlng1;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double d) {
        Lng = d;
    }

    public String getLocationtring() {
        return locationtring;
    }

    public void setLocationtring(String s) {
        locationtring = s;
    }

    public Player getPlayer() {
        if (palyer == null) {
            palyer = new Player();
        }
        return palyer;
    }

    public void setPlayer(Player palyer1) {
        palyer = palyer1;
    }

    public Object getPhotoargument() {
        Object obj = photoargument;
        photoargument = null;
        return obj;
    }

    public ArrayList getQuestionlist() {
        return questionlist;
    }

    public void setQuestionlist(ArrayList arraylist) {
        questionlist = arraylist;
    }

    public int getSiteid() {
        return siteid;
    }

    public void setSiteid(int i) {
        siteid = i;
    }

    public String getViewpageIndex() {
        String s = viewpageIndex;
        viewpageIndex = null;
        return s;
    }

    public ArrayList getViews() {
        if (views == null) {
            views = new ArrayList();
        }
        return views;
    }

    public void setViews(ArrayList arraylist) {
        views = arraylist;
    }

    public int getWindowHeight() {
        return WindowHeight;
    }

    public void setWindowHeight(int i) {
        WindowHeight = i;
    }

    public int getWindowWidth() {
        return WindowWidth;
    }

    public void setWindowWidth(int i) {
        WindowWidth = i;
    }

    public boolean getisLogin() {
        return isLogin;
    }

    public ArrayList getpArrayList() {
        if (pArrayList == null) {
            pArrayList = new ArrayList();
        }
        return pArrayList;
    }

    public void setpArrayList(ArrayList arraylist) {
        pArrayList = arraylist;
    }

    public boolean isM_bKeyRight() {
        return m_bKeyRight;
    }

    public void setM_bKeyRight(boolean flag) {
        m_bKeyRight = flag;
    }

    public boolean setArgument(Object obj) {
        if (argument != null) {
            return false;
        } else {
            argument = obj;
            return true;
        }
    }

    public boolean setPhotoargument(Object obj) {
        if (photoargument != null) {
            return false;
        } else {
            photoargument = obj;
            return true;
        }
    }

    public boolean setViewpageIndex(String s) {
        if (viewpageIndex != null) {
            return false;
        } else {
            viewpageIndex = s;
            return true;
        }
    }

    public void setisLogin(boolean flag) {
        isLogin = flag;
    }

}
