package com.imyuu.travel.util;

import android.content.Context;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;

public class ApplicationHelper
{

    private static ApplicationHelper applicationHelper = null;
    private double Lat;
    private double Lng;
    private int WindowHeight;
    private int WindowWidth;
    Object argument;
    private int cityId;
    private String cityNameString;
    private boolean isLogin;
    LatLng latlng;
    private String locationtring;
    public boolean m_bKeyRight;
    private ArrayList pArrayList;
    Player palyer;
    Object photoargument;
    ArrayList questionlist;
    private int siteid;
    String viewpageIndex;
    private ArrayList views;
    
    
    public ApplicationHelper()
    {
        m_bKeyRight = true;
    }

    public static ApplicationHelper getInstance()
    {
        if (applicationHelper == null)
        {
            applicationHelper = new ApplicationHelper();
           
        }
        return applicationHelper;
    }

    
    public Object getArgument()
    {
        Object obj = argument;
        argument = null;
        return obj;
    }

    public int getCityId()
    {
        return cityId;
    }

    public String getCityNameString()
    {
        return cityNameString;
    }

    public double getLat()
    {
        return Lat;
    }

    public LatLng getLatlng()
    {
        return latlng;
    }

    public double getLng()
    {
        return Lng;
    }

    public String getLocationtring()
    {
        return locationtring;
    }

    public Player getPlayer()
    {
        if (palyer == null)
        {
            palyer = new Player();
        }
        return palyer;
    }

    public Object getPhotoargument()
    {
        Object obj = photoargument;
        photoargument = null;
        return obj;
    }

    public ArrayList getQuestionlist()
    {
        return questionlist;
    }

    public int getSiteid()
    {
        return siteid;
    }

    public String getViewpageIndex()
    {
        String s = viewpageIndex;
        viewpageIndex = null;
        return s;
    }

    public ArrayList getViews()
    {
        if (views == null)
        {
            views = new ArrayList();
        }
        return views;
    }

    public int getWindowHeight()
    {
        return WindowHeight;
    }

    public int getWindowWidth()
    {
        return WindowWidth;
    }

    public boolean getisLogin()
    {
        return isLogin;
    }

    public ArrayList getpArrayList()
    {
        if (pArrayList == null)
        {
            pArrayList = new ArrayList();
        }
        return pArrayList;
    }

    public boolean isM_bKeyRight()
    {
        return m_bKeyRight;
    }

    public boolean setArgument(Object obj)
    {
        if (argument != null)
        {
            return false;
        } else
        {
            argument = obj;
            return true;
        }
    }

    public void setCityId(int i)
    {
        cityId = i;
    }

    public void setCityNameString(String s)
    {
        cityNameString = s;
    }

    public void setLat(double d)
    {
        Lat = d;
    }

    public void setLatlng(LatLng latlng1)
    {
        latlng = latlng1;
    }

    public void setLng(double d)
    {
        Lng = d;
    }

    public void setLocationtring(String s)
    {
        locationtring = s;
    }

    public void setM_bKeyRight(boolean flag)
    {
        m_bKeyRight = flag;
    }

    public void setPlayer(Player palyer1)
    {
        palyer = palyer1;
    }

    public boolean setPhotoargument(Object obj)
    {
        if (photoargument != null)
        {
            return false;
        } else
        {
            photoargument = obj;
            return true;
        }
    }

    public void setQuestionlist(ArrayList arraylist)
    {
        questionlist = arraylist;
    }

    public void setSiteid(int i)
    {
        siteid = i;
    }

    public boolean setViewpageIndex(String s)
    {
        if (viewpageIndex != null)
        {
            return false;
        } else
        {
            viewpageIndex = s;
            return true;
        }
    }

    public void setViews(ArrayList arraylist)
    {
        views = arraylist;
    }

    public void setWindowHeight(int i)
    {
        WindowHeight = i;
    }

    public void setWindowWidth(int i)
    {
        WindowWidth = i;
    }

    public void setisLogin(boolean flag)
    {
        isLogin = flag;
    }

    public void setpArrayList(ArrayList arraylist)
    {
        pArrayList = arraylist;
    }

}
