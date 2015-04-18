package com.xmtq.lottery.bean;

/**
 * 账户明细
 */
public class AccountDetailBean extends EntityBase {
	// <element>
	// <money>672000.0</money>
	// <entertime>2014-12-25</entertime>
	// <remark>撤单返款(dfdfd)</remark>
	// <mflag>4</mflag>
	// </element>
	private static final long serialVersionUID = -8391624578292769336L;
	private String money;
	private String entertime;
	private String remark;
	private String mflag;

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getEntertime() {
		return entertime;
	}

	public void setEntertime(String entertime) {
		this.entertime = entertime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMflag() {
		return mflag;
	}

	public void setMflag(String mflag) {
		this.mflag = mflag;
	}

}
