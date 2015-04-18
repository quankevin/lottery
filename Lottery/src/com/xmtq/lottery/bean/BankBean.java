package com.xmtq.lottery.bean;

/**
 * 储蓄卡
 */
public class BankBean extends EntityBase {

	// <bank >
	// <bankCode>icbc</bankCode>
	// <bankName>工商银行</bankName>
	// </bank >
	private static final long serialVersionUID = -8391624578292769336L;

	private String bankCode;
	private String bankName;

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
