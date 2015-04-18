package com.xmtq.lottery.bean;

/**
 * 新用户登录
 * 
 */
public class NewUserLoginBean extends EntityBase {

	// <uid>14138</uid>
	// <username>wangcaster</username>
	// <money>9920749461.85</money>
	// <prizeMoney>0.00</prizeMoney>
	// <perfectFlag>1</perfectFlag>
	private static final long serialVersionUID = -8391624578292769336L;
	private String uid;
	private String username;
	private String money;
	private String prizeMoney;
	private String perfectFlag;

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getMoney() {
		return money;
	}

	public void setPrizeMoney(String prizeMoney) {
		this.prizeMoney = prizeMoney;
	}

	public String getPrizeMoney() {
		return prizeMoney;
	}

	public void setPerfectFlag(String perfectFlag) {
		this.perfectFlag = perfectFlag;
	}

	public String getPerfectFlag() {
		return perfectFlag;
	}

}
