package com.imyuu.travel.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

@Table(name="Comments_Info_Model")
public class CommentsInfoJson extends Model {
   // @Column(name="comentsId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)

    @Expose
    @Column(name="userId")
    private String userId;
    @Expose
    @Column(name="scenicId")
    private String scenicId;
    @Expose
    @Column(name="commentTime")
    private String commentTime;
    @Expose
    @Column(name="content")
    private String content;
    @Expose
    @Column(name="audioName")
    private String audioName;
    @Expose
    @Column(name="imageName")
    private String imageName;
    @Expose
    @Column(name="expressionName")
    private String expressionName;
    @Expose
    @Column(name="remark")
    private String remark;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    @Expose
    @Column(name="gender")
    private String gender;
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    @Expose
    @Column(name="userName")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Expose
    @Column(name="age")
    private Integer age;

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

    public static List<CommentsInfoJson> load() {
        try {

            List<CommentsInfoJson> lineList = new Select().from(CommentsInfoJson.class)
                    .orderBy("commentTime DESC")
                    .execute();

            return lineList;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<CommentsInfoJson> load(int limit,int offset) {
        try {

            List<CommentsInfoJson> lineList = new Select().from(CommentsInfoJson.class)
                    .limit(limit).offset(offset)
                    .orderBy("commentTime DESC")
                    .execute();

            return lineList;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean  remove(String scenicId) {
        try {
            new Delete().from(CommentsInfoJson.class).where("scenicId = ?", scenicId).execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int count()
    {
        return new Select().from(CommentsInfoJson.class).execute().size();
    }
}