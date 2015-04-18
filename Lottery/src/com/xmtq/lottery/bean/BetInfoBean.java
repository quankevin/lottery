package com.xmtq.lottery.bean;

/**
 * 投注信息
 */
public class BetInfoBean extends EntityBase {

	private static final long serialVersionUID = -8391624578292769336L;

	private String uid;
	private String lotteryid;
	private String votetype;
	private String votenums;
	private String multiple;
	private String voteinfo;
	private String totalmoney;
	private String playtype;
	private String passtype;
	private String buymoney;
	private String protype;
	private String accountBalance;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLotteryid() {
		return lotteryid;
	}

	public void setLotteryid(String lotteryid) {
		this.lotteryid = lotteryid;
	}

	public String getVotetype() {
		return votetype;
	}

	public void setVotetype(String votetype) {
		this.votetype = votetype;
	}

	public String getVotenums() {
		return votenums;
	}

	public void setVotenums(String votenums) {
		this.votenums = votenums;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getVoteinfo() {
		return voteinfo;
	}

	public void setVoteinfo(String voteinfo) {
		this.voteinfo = voteinfo;
	}

	public String getTotalmoney() {
		return totalmoney;
	}

	public void setTotalmoney(String totalmoney) {
		this.totalmoney = totalmoney;
	}

	public String getPlaytype() {
		return playtype;
	}

	public void setPlaytype(String playtype) {
		this.playtype = playtype;
	}

	public String getPasstype() {
		return passtype;
	}

	public void setPasstype(String passtype) {
		this.passtype = passtype;
	}

	public String getBuymoney() {
		return buymoney;
	}

	public void setBuymoney(String buymoney) {
		this.buymoney = buymoney;
	}

	public String getProtype() {
		return protype;
	}

	public void setProtype(String protype) {
		this.protype = protype;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getAccountBalance() {
		return accountBalance;
	}

}
