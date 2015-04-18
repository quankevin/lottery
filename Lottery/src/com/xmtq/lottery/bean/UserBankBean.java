package com.xmtq.lottery.bean;

/**
 * <bankCodeUsed>6434343434</bankCodeUsed>
 * <bankCardTypeUsed>0</bankCardTypeUsed> <bankAccount>6663543543</bankAccount>
 * <bindId>3543543</bindId>
 */
public class UserBankBean extends EntityBase {

	//
	// <bankCodeUsed>6434343434</bankCodeUsed>
	// <bankCardTypeUsed>0</bankCardTypeUsed>
	// <bankAccount>6663543543</bankAccount>
	// <bindId>3543543</bindId>
	private static final long serialVersionUID = -8391624578292769336L;

	private String bankCodeUsed;
	private String bankCardTypeUsed;
	private String bankAccount;
	private String bindId;

	public String getBankCodeUsed() {
		return bankCodeUsed;
	}

	public void setBankCodeUsed(String bankCodeUsed) {
		this.bankCodeUsed = bankCodeUsed;
	}

	public String getBankCardTypeUsed() {
		return bankCardTypeUsed;
	}

	public void setBankCardTypeUsed(String bankCardTypeUsed) {
		this.bankCardTypeUsed = bankCardTypeUsed;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
