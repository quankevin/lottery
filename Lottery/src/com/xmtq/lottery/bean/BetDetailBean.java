package com.xmtq.lottery.bean;

import java.util.ArrayList;
import java.util.List;

public class BetDetailBean extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8391624578292769336L;
	// "matchteam": "赫塔费",
	// "number": "001",
	// "odd": [
	// {
	// "gameresult": "平",
	// "betinfo": "胜,平",
	// "playname": "让球"
	// },
	// {
	// "gameresult": "负",
	// "betinfo": "胜,平",
	// "playname": "胜平负"
	// }
	// ],
	// "hostteam": "比利亚雷",
	// "name": "周四"
	// },

	private String hostteam;
	private String name;

	public String getHostteam() {
		return hostteam;
	}

	public void setHostteam(String hostteam) {
		this.hostteam = hostteam;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String matchteam;
	private String number;
	private List<BetOddBean> mBetOddBeans = new ArrayList<BetOddBean>();

	public String getMatchteam() {
		return matchteam;
	}

	public void setMatchteam(String matchteam) {
		this.matchteam = matchteam;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<BetOddBean> getmBetOddBeans() {
		return mBetOddBeans;
	}

	public void setmBetOddBeans(List<BetOddBean> mBetOddBeans) {
		this.mBetOddBeans = mBetOddBeans;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
