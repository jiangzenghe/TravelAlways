package com.imyuu.travel.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class ScenicHotelJson implements Serializable {
    @Expose
    private String hotelName;
    @Expose
    private String address;
    @Expose
    private String avgPrice;
    @Expose
    private String imageUrl;
    @Expose
    private int star;
    @Expose
    private Double lat;
    @Expose
    private Double lng;
    @Expose
    private String link;

    @Override
    public String toString() {
        return "ScenicHotelJson [hotelName=" + hotelName + ", address="
                + address + ", avgPrice=" + avgPrice + ", imageUrl=" + imageUrl
                + ", star=" + star + ", lat=" + lat + ", lng=" + lng
                + ", link=" + link + "]";
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
