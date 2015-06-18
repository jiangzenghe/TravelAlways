package com.imyuu.travel.model;

import com.google.gson.annotations.Expose;

public class ChatMessageJson {
    @Expose
    private String chatId;
    @Expose
    private String userId;
    @Expose
    private String scenicId;
    @Expose
    private String commentTime;
    @Expose
    private String content;
    @Expose
    private String audioName;
    @Expose
    private String imageName;
    @Expose
    private String expressionName;
    @Expose
    private String remark;
    @Expose
    private String userName;
    @Expose
    private String gender;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    private Integer age;
    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "CommentInfoJson [userId=" + userId + ", scenicId=" + scenicId
                + ", commentTime=" + commentTime + ", content=" + content
                + ", audioName=" + audioName + ", imageName=" + imageName
                + ", expressionName=" + expressionName + ", remark=" + remark
                + "]";
    }

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

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getExpressionName() {
        return expressionName;
    }

    public void setExpressionName(String expressionName) {
        this.expressionName = expressionName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}