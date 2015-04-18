package com.xmtq.lottery.bean;

/**
 * 用户详细信息
 */
public class UserInfoBean extends EntityBase {

	// <realname>张三丰</realname>
	// <cardid>612325198804234523</cardid>
	// <account>10000</account>
	// <bankname>招商银行</bankname>
	// <bankaccount>8888888888888888</bankaccount>
	// <bankaddress>北京市</bankaddress>
	// <mobile>13888888888</mobile>
	private static final long serialVersionUID = -8391624578292769336L;
	private String realname;
	private String cardid;
	private String account;
	private String bankname;
	private String bankaccount;

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getCardid() {
		return cardid;
	}

	public void setCardid(String cardid) {
		this.cardid = cardid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}

	public String getBankaddress() {
		return bankaddress;
	}

	public void setBankaddress(String bankaddress) {
		this.bankaddress = bankaddress;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String bankaddress;
	private String mobile;
}
