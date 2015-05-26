package com.imyuu.travel.model;

public class ScenicTransportJson
{
  private String scenicId;
  private String desc;
  public String getScenicId() {
	return scenicId;
}
public void setScenicId(String scenicId) {
	this.scenicId = scenicId;
}
public String getDesc() {
	return desc;
}
public void setDesc(String desc) {
	this.desc = desc;
}

    @Override
    public String toString() {
        return "ScenicTransportJson{" +
                "scenicId='" + scenicId + '\'' +
                ", desc='" + desc + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }

    public String getImageURL() {
	return imageURL;
}
public void setImageURL(String imageURL) {
	this.imageURL = imageURL;
}

private String imageURL;
}