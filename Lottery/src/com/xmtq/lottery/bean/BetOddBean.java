package com.xmtq.lottery.bean;

/**
 *  
 */
public class BetOddBean extends EntityBase {

	private static final long serialVersionUID = -8391624578292769336L;
	// "gameresult": "平",
	// "betinfo": "胜,平",
	// "playname": "让球"

	private String gameresult;
	private String betinfo;
	private String playname;

	public String getGameresult() {
		return gameresult;
	}

	public void setGameresult(String gameresult) {
		this.gameresult = gameresult;
	}

	public String getBetinfo() {
		return betinfo;
	}

	public void setBetinfo(String betinfo) {
		this.betinfo = betinfo;
	}

	public String getPlayname() {
		return playname;
	}

	public void setPlayname(String playname) {
		this.playname = playname;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
