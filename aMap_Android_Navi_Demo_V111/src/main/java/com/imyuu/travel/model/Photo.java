package com.imyuu.travel.model;

/**
 * Created by java on 2015/5/23.
 */
public class Photo {
    byte[] buf;
    private String userId;
    private String path;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getBuf() {
        return buf;
    }

    public void setBuf(byte[] tbuf) {
        buf = new byte[tbuf.length];
        System.arraycopy(tbuf,0,this.buf,0,buf.length);

    }
    public Photo()
    {
        this.path="image";
    }
}
