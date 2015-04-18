package com.xmtq.lottery.bean;

import java.util.ArrayList;
import java.util.List;

public class BetDetailAllBean extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8391624578292769336L;
	// "projectno": "JCFHTCC580BA",
	// "projectprize": 224,
	// "guoguantype": "2串1,3串1",
	// "state": "出票中",
	// "userballot": 112,
	// "bonus": 0,
	// "playtype": 6,
	// "projecttime": "2015-01-29 14:59",
	// "multiple": 1

	private String projectno;
	private String projectprize;
	private String guoguantype;
	private String state;
	private String userballot;
	private String bonus;
	private String playtype;
	private String projecttime;
	private String multiple;
	private List<BetDetailBean> mBetDetailBeans = new ArrayList<BetDetailBean>();

	public String getProjectno() {
		return projectno;
	}

	public void setProjectno(String projectno) {
		this.projectno = projectno;
	}

	public String getProjectprize() {
		return projectprize;
	}

	public void setProjectprize(String projectprize) {
		this.projectprize = projectprize;
	}

	public String getGuoguantype() {
		return guoguantype;
	}

	public void setGuoguantype(String guoguantype) {
		this.guoguantype = guoguantype;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUserballot() {
		return userballot;
	}

	public void setUserballot(String userballot) {
		this.userballot = userballot;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	public String getPlaytype() {
		return playtype;
	}

	public void setPlaytype(String playtype) {
		this.playtype = playtype;
	}

	public String getProjecttime() {
		return projecttime;
	}

	public void setProjecttime(String projecttime) {
		this.projecttime = projecttime;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public List<BetDetailBean> getmBetDetailBeans() {
		return mBetDetailBeans;
	}

	public void setmBetDetailBeans(List<BetDetailBean> mBetDetailBeans) {
		this.mBetDetailBeans = mBetDetailBeans;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
