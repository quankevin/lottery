package com.xmtq.lottery.bean;

/**
 * 用户信息完善
 * 
 */
public class ImproveUserInfoBean extends EntityBase {
	// <realname>wanglasce</realname>
	// <phone>1312313</phone>
	// <cardid>145343454354343</cardid>
	// <bankname>中国银行</bankname>
	// <bankcardid>20494319713676136</bankcardid>
	// <bankaddress>的山坡哦少</bankaddress>
	private static final long serialVersionUID = -8391624578292769336L;
	private String realName;
	private String phone;
	private String cardId;
	private String bankName;
	private String bankCardId;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCardId() {
		return bankCardId;
	}

	public void setBankCardId(String bankCardId) {
		this.bankCardId = bankCardId;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String bankAddress;

}
