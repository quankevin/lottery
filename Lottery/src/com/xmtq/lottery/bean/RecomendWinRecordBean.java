package com.xmtq.lottery.bean;

/**
 * 储蓄卡
 */
public class RecomendWinRecordBean extends EntityBase {

	// <guoguan>2串1</guoguan>
	// <username>wangcaster</username>
	// <bonus>2047.02</bonus>
	// <lotteryname>竞彩足球混投</lotteryname>
	private static final long serialVersionUID = -8391624578292769336L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String guoguan;
	private String username;
	private String bonus;
	private String lotteryname;

	public String getGuoguan() {
		return guoguan;
	}

	public void setGuoguan(String guoguan) {
		this.guoguan = guoguan;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	public String getLotteryname() {
		return lotteryname;
	}

	public void setLotteryname(String lotteryname) {
		this.lotteryname = lotteryname;
	}

}
