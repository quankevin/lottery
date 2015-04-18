package com.xmtq.lottery.bean;

public class OddBean extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8391624578292769336L;
	private String playname;
	private String betinfo;
	private String gameresult;

	public String getPlayname() {
		return playname;
	}

	public void setPlayname(String playname) {
		this.playname = playname;
	}

	public String getBetinfo() {
		return betinfo;
	}

	public void setBetinfo(String betinfo) {
		this.betinfo = betinfo;
	}

	public String getGameresult() {
		return gameresult;
	}

	public void setGameresult(String gameresult) {
		this.gameresult = gameresult;
	}

}
