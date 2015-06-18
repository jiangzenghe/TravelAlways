package com.imyuu.travel.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

@Table(name="user_sharing_model")
public class UserSharingModel extends Model {

    @Expose
    @Column(name="userId")
    private String userId;
    @Expose
    @Column(name="scenicId")
    private String scenicId;
    @Expose
    @Column(name="sharingTime")
    private String sharingTime;
    @Expose
    @Column(name="sharingPlatform")
    private String sharingPlatform;
    @Expose
    @Column(name="sharingAccount")
    private String sharingAccount;
    @Expose
    @Column(name="remark")
    private String remark;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    public String getSharingTime() {
        return sharingTime;
    }

    public void setSharingTime(String sharingTime) {
        this.sharingTime = sharingTime;
    }

    public String getSharingPlatform() {
        return sharingPlatform;
    }

    public void setSharingPlatform(String sharingPlatform) {
        this.sharingPlatform = sharingPlatform;
    }

    public String getSharingAccount() {
        return sharingAccount;
    }

    public void setSharingAccount(String sharingAccount) {
        this.sharingAccount = sharingAccount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "UserSharingJson [userId=" + userId + ", scenicId=" + scenicId
                + ", sharingTime=" + sharingTime + ", sharingPlatform="
                + sharingPlatform + ", sharingAccount=" + sharingAccount
                + ", remark=" + remark + "]";
    }

    public static List<UserSharingModel> load() {
        try {

            List<UserSharingModel> lineList = new Select().from(UserSharingModel.class)
                    .orderBy(" sharingTime DESC")
                    .execute();

            return lineList;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserSharingModel> load(int limit,int offset) {
        try {

            List<UserSharingModel> lineList = new Select().from(UserSharingModel.class)
                    .limit(limit).offset(offset).orderBy(" sharingTime DESC")
                    .execute();
            Log.d("UserSharingModel",lineList.toString());
            return lineList;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean  remove(String userId) {
        try {
            new Delete().from(UserSharingModel.class).where(" userId = ?", userId).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int count()
    {
        return new Select().from(UserSharingModel.class).execute().size();
    }
}


