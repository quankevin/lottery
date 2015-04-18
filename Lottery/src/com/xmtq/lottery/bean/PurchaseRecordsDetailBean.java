package com.xmtq.lottery.bean;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRecordsDetailBean extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8391624578292769336L;

	private String playtype;
	private String projectprize;
	private String bonus;
	private String guoguantype;
	private String multiple;
	private String userballot;
	private String projectno;
	private String projecttime;
	private String state;
	List<GameResultBean> gameResultBeans = new ArrayList<GameResultBean>();

	public String getPlaytype() {
		return playtype;
	}

	public void setPlaytype(String playtype) {
		this.playtype = playtype;
	}

	public String getProjectprize() {
		return projectprize;
	}

	public void setProjectprize(String projectprize) {
		this.projectprize = projectprize;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	public String getGuoguantype() {
		return guoguantype;
	}

	public void setGuoguantype(String guoguantype) {
		this.guoguantype = guoguantype;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getUserballot() {
		return userballot;
	}

	public void setUserballot(String userballot) {
		this.userballot = userballot;
	}

	public String getProjectno() {
		return projectno;
	}

	public void setProjectno(String projectno) {
		this.projectno = projectno;
	}

	public String getProjecttime() {
		return projecttime;
	}

	public void setProjecttime(String projecttime) {
		this.projecttime = projecttime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
