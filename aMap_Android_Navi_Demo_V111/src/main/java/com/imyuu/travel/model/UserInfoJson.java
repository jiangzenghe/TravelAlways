package com.imyuu.travel.model;

import java.io.Serializable;

public class UserInfoJson  implements Serializable {
	private String userId;
	private String password;  //MD5����
	private String imageUrl;  //ͷ��
	private String nickName;  
	private String city;
	private String gender;
	private String desc;
	private int level;  //����
	
	private  String tel;
	private String email;
	private String verifyCode;  //������֤��
	private String ssoSource;//  ���ɵ�¼����Դ
	private String ssoAccount;  //���ɵ�¼�˺�

	@Override
	public String toString() {
		return "UserInfoJson [userId=" + userId + ", password=" + password
				+ ", imageUrl=" + imageUrl + ", nickName=" + nickName
				+ ", city=" + city + ", gender=" + gender + ", desc=" + desc
				+ ", level=" + level + ", tel=" + tel + ", email=" + email
				+ ", verifyCode=" + verifyCode + ", ssoSource=" + ssoSource
				+ ", ssoAccount=" + ssoAccount + ", credits=" + credits + "]";
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getSsoSource() {
		return ssoSource;
	}
	public void setSsoSource(String ssoSource) {
		this.ssoSource = ssoSource;
	}
	public String getSsoAccount() {
		return ssoAccount;
	}
	public void setSsoAccount(String ssoAccount) {
		this.ssoAccount = ssoAccount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	private int credits;//���
}
