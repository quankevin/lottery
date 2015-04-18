package com.xmtq.lottery.bean;

/**
 * 推荐历史
 */
public class RecomendHistoryBean extends EntityBase {
	private static final long serialVersionUID = -8391624578292769336L;

	// <companyId>150105005</companyId>
	// <num>005</num>
	// <league>法国杯</league>
	// <matchTeam>蒙彼利埃</matchTeam>
	// <matchId>62422</matchId>
	// <hostTeam>巴黎圣曼</hostTeam>
	// <gameTime>2015-01-06 03:44:00</gameTime>
	// <bfkjBc>2:1</bfkjBc>
	// <bfkj>3:3</bfkj>
	// <hit>0</hit>
	private String companyId;
	private String num;
	private String league;
	private String matchTeam;
	private String matchId;
	private String hostTeam;
	private String gameTime;
	private String bfkjBc;
	private String bfkj;
	private String hit;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public String getMatchTeam() {
		return matchTeam;
	}

	public void setMatchTeam(String matchTeam) {
		this.matchTeam = matchTeam;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public String getHostTeam() {
		return hostTeam;
	}

	public void setHostTeam(String hostTeam) {
		this.hostTeam = hostTeam;
	}

	public String getGameTime() {
		return gameTime;
	}

	public void setGameTime(String gameTime) {
		this.gameTime = gameTime;
	}

	public String getBfkjBc() {
		return bfkjBc;
	}

	public void setBfkjBc(String bfkjBc) {
		this.bfkjBc = bfkjBc;
	}

	public String getBfkj() {
		return bfkj;
	}

	public void setBfkj(String bfkj) {
		this.bfkj = bfkj;
	}

	public String getHit() {
		return hit;
	}

	public void setHit(String hit) {
		this.hit = hit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
