package com.imyuu.travel.model;


import com.google.gson.annotations.Expose;

public class MessageJson implements java.io.Serializable {

    @Expose
    private String userId;
    @Expose
    private String telno;
    @Expose
    private String message;

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SysMessageModel [ userId=" + userId
                + ", telno=" + telno + ", message=" + message + "]";
    }


}


