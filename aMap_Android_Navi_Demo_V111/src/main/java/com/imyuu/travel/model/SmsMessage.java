package com.imyuu.travel.model;

import com.google.gson.annotations.Expose;

/**
 * Created by java on 2015/6/7.
 */
public class SmsMessage {
    @Expose
    private String telno;
    @Expose
    private String content;

    public String getTelno() {
        return telno;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }


}
