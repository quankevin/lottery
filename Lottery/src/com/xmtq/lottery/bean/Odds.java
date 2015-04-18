package com.xmtq.lottery.bean;

public class Odds extends EntityBase{
	private static final long serialVersionUID = -8391624578292769336L;
	
	/**
	 * 玩法结果
	 */
	private String result;

	/**
	 * 玩法赔率
	 */
	private String odds;

	/**
	 * 是否选中
	 */
	private boolean isChecked = false;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getOdds() {
		return odds;
	}

	public void setOdds(String odds) {
		this.odds = odds;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public boolean isChecked() {
		return isChecked;
	}
}
