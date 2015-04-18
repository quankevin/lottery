package com.xmtq.lottery.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建支付订单
 */
public class CreateOrderBean extends EntityBase {

	private static final long serialVersionUID = -8391624578292769336L;

	private List<UserBankBean> userBankList = new ArrayList<UserBankBean>();
	private List<BankBean> bankList = new ArrayList<BankBean>();
	private List<BankCBean> bankCList = new ArrayList<BankCBean>();

	public List<UserBankBean> getUserBankList() {
		return userBankList;
	}

	public void setUserBankList(List<UserBankBean> userBankList) {
		this.userBankList = userBankList;
	}

	public List<BankBean> getBankList() {
		return bankList;
	}

	public void setBankList(List<BankBean> bankList) {
		this.bankList = bankList;
	}

	public List<BankCBean> getBankCList() {
		return bankCList;
	}

	public void setBankCList(List<BankCBean> bankCList) {
		this.bankCList = bankCList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
