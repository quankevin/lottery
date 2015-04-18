package com.xmtq.lottery.bean;

/**
 * 用户注册信息
 */
public class UserBean extends EntityBase {

	private String username;
	private String password;
	private String phoneNum;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	 
}
