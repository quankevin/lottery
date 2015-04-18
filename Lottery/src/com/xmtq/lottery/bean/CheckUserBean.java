package com.xmtq.lottery.bean;

/**
 * 校验用户名，登录手机号，登录邮箱是否存在
 */
public class CheckUserBean extends EntityBase {

	// <ustate>1</ ustate>
	// <pstate>1</ pstate>
	private static final long serialVersionUID = -8391624578292769336L;
	private String ustate;
	private String pstate;

	public String getUstate() {
		return ustate;
	}

	public void setUstate(String ustate) {
		this.ustate = ustate;
	}

	public String getPstate() {
		return pstate;
	}

	public void setPstate(String pstate) {
		this.pstate = pstate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
