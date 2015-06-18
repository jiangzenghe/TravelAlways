package com.imyuu.travel.model;

import java.io.Serializable;

/**
 * Created by administor on 2015/5/11.
 */
public class Food implements Serializable {
    private String s_photo_url;
    private String avg_price;
    private String rating_s_img_url;
    private String name;
    private String address;
    private String distance;

    public Food() {
    }

    public Food(String s_photo_url, String avg_price, String rating_s_img_url, String name, String address, String distance) {
        this.s_photo_url = s_photo_url;
        this.avg_price = avg_price;
        this.rating_s_img_url = rating_s_img_url;
        this.name = name;
        this.address = address;
        this.distance = distance;
    }

    public String getS_photo_url() {
        return s_photo_url;
    }

    public void setS_photo_url(String s_photo_url) {
        this.s_photo_url = s_photo_url;
    }

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(String avg_price) {
        this.avg_price = avg_price;
    }

    public String getRating_s_img_url() {
        return rating_s_img_url;
    }

    public void setRating_s_img_url(String rating_s_img_url) {
        this.rating_s_img_url = rating_s_img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Food{" +
                "s_photo_url='" + s_photo_url + '\'' +
                ", avg_price='" + avg_price + '\'' +
                ", rating_s_img_url='" + rating_s_img_url + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
