package com.xmtq.lottery.bean;

import java.util.ArrayList;
import java.util.List;

public class AccountDetailResponse extends BaseResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5375804597574885028L;

	private String pay;
	private String income;
	private String count;

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<AccountDetailBean> accountDetailList = new ArrayList<AccountDetailBean>();
}
