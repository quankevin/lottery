package com.xmtq.lottery.bean;

import java.util.ArrayList;
import java.util.List;

public class GameResultBean extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8391624578292769336L;

	private String matchteam;
	private String hostteam;
	private String number;
	private String name;
	private List<OddBean> oddBeans = new ArrayList<OddBean>();

	public String getMatchteam() {
		return matchteam;
	}

	public void setMatchteam(String matchteam) {
		this.matchteam = matchteam;
	}

	public String getHostteam() {
		return hostteam;
	}

	public void setHostteam(String hostteam) {
		this.hostteam = hostteam;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<OddBean> getOddBeans() {
		return oddBeans;
	}

	public void setOddBeans(List<OddBean> oddBeans) {
		this.oddBeans = oddBeans;
	}

}
