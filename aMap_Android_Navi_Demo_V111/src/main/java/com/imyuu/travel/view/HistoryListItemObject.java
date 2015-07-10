package com.imyuu.travel.view;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoryListItemObject implements Parcelable{
	public int iconRes;
    public String title;
    public String msg;
    public String fileSize;
    public String scenicId;

    public String getCanNavi() {
        return canNavi;
    }

    public void setCanNavi(String canNavi) {
        this.canNavi = canNavi;
    }

    public String canNavi;
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    public String imagePath;
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String time;
    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String filePath;
//    public SwipeItemView slideView;
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public int getIconRes() {
		return iconRes;
	}
	public void setIconRes(int iconRes) {
		this.iconRes = iconRes;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
//	public SwipeItemView getSlideView() {
//		return slideView;
//	}
//	public void setSlideView(SwipeItemView slideView) {
//		this.slideView = slideView;
//	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
