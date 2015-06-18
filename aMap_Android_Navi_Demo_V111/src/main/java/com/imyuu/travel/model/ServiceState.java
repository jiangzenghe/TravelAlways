package com.imyuu.travel.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ServiceState implements Serializable {
    @Expose
    public static String OK = "Success";
    @Expose
    public static int OKCODE = 200;
    @Expose
    private Integer stateCode;
    @Expose
    private String serviceMsg;

    public ServiceState(Integer code, String msg) {
        stateCode = code;
        serviceMsg = msg;
    }

    public ServiceState() {
        stateCode = 200;
        serviceMsg = "ok";
    }

    public Integer getStateCode() {
        return stateCode;
    }

    public void setStateCode(Integer stateCode) {
        this.stateCode = stateCode;
    }

    public String getServiceMsg() {
        return serviceMsg;
    }

    public void setServiceMsg(String serviceMsg) {
        this.serviceMsg = serviceMsg;
    }

    @Override
    public String toString() {
        return "servicestate:" + this.stateCode + " msg:" + this.serviceMsg;
    }
}
