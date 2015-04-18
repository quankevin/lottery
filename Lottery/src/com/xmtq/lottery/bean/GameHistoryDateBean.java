package com.xmtq.lottery.bean;

/**
 * 推荐历史
 */
public class GameHistoryDateBean extends EntityBase {
	private static final long serialVersionUID = -8391624578292769336L;
	// <date>2015-01-05</date>
	// <count>5</count>
	// <hitcount>0</hitcount>

	private String date;
	private String count;
	private String hitcount;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getHitcount() {
		return hitcount;
	}

	public void setHitcount(String hitcount) {
		this.hitcount = hitcount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
