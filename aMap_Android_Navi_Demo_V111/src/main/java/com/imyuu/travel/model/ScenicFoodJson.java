package com.imyuu.travel.model;

import java.io.Serializable;

public class ScenicFoodJson implements Serializable {
	private String foodName;
	private String address;
	private String avgPrice;
	private String imageUrl;
	private Double lat;

	public String getFoodName() {
		return foodName;
	}

	@Override
	public String toString() {
		return "ScenicFoodJson [foodName=" + foodName + ", address=" + address
				+ ", avgPrice=" + avgPrice + ", imageUrl=" + imageUrl
				+ ", lat=" + lat + ", lng=" + lng + ", star=" + star
				+ ", link=" + link + "]";
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
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

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	private Double lng;
	private int star;
	private String link;
}
