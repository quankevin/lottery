package com.xmtq.lottery.bean;

import java.util.ArrayList;
import java.util.List;

public class GameCanBetBean extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8391624578292769336L;

	private String companyId;
	private String league;
	private String matchTeam;
	private String matchId;
	private String hostTeam;
	private String buyEndTime;
	private String gameTime;
	/**
	 * 让球胜负平
	 */
	private String rqOdds;
	/**
	 * 胜负平
	 */
	private String spOdds;
	/**
	 * 总比分
	 */
	private String bfOdds;
	/**
	 * 半场全场胜负平
	 */
	private String bqOdds;
	/**
	 * 总进球
	 */
	private String jqOdds;
	private String commendUser;
	private String commendId;
	private String supportVotes;
	private String againstVotes;
	private String content;
	private String rqDg;
	private String spDg;
	private String bfDg;
	private String bqDg;
	private String jqDg;
	private String rqContent;
	private String spContent;
	private String bfContent;
	private String bqContent;
	private String jqContent;
	private boolean isWinChecked = false;
	private boolean isDrawChecked = false;
	private boolean isLoseChecked = false;
	
	private List<Odds> spOddsList = new ArrayList<Odds>();
	private List<Odds> rqOddsList = new ArrayList<Odds>();
	private List<Odds> bfOddsList = new ArrayList<Odds>();
	private List<Odds> bqOddsList = new ArrayList<Odds>();
	private List<Odds> jqOddsList = new ArrayList<Odds>();
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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

	public String getBuyEndTime() {
		return buyEndTime;
	}

	public void setBuyEndTime(String buyEndTime) {
		this.buyEndTime = buyEndTime;
	}

	public String getGameTime() {
		return gameTime;
	}

	public void setGameTime(String gameTime) {
		this.gameTime = gameTime;
	}

	public String getRqOdds() {
		return rqOdds;
	}

	public void setRqOdds(String rqOdds) {
		this.rqOdds = rqOdds;
	}

	public String getSpOdds() {
		return spOdds;
	}

	public void setSpOdds(String spOdds) {
		this.spOdds = spOdds;
	}

	public String getBfOdds() {
		return bfOdds;
	}

	public void setBfOdds(String bfOdds) {
		this.bfOdds = bfOdds;
	}

	public String getBqOdds() {
		return bqOdds;
	}

	public void setBqOdds(String bqOdds) {
		this.bqOdds = bqOdds;
	}

	public String getJqOdds() {
		return jqOdds;
	}

	public void setJqOdds(String jqOdds) {
		this.jqOdds = jqOdds;
	}

	public String getCommendUser() {
		return commendUser;
	}

	public void setCommendUser(String commendUser) {
		this.commendUser = commendUser;
	}

	public String getCommendId() {
		return commendId;
	}

	public void setCommendId(String commendId) {
		this.commendId = commendId;
	}

	public String getSupportVotes() {
		return supportVotes;
	}

	public void setSupportVotes(String supportVotes) {
		this.supportVotes = supportVotes;
	}

	public String getAgainstVotes() {
		return againstVotes;
	}

	public void setAgainstVotes(String againstVotes) {
		this.againstVotes = againstVotes;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRqDg() {
		return rqDg;
	}

	public void setRqDg(String rqDg) {
		this.rqDg = rqDg;
	}

	public String getSpDg() {
		return spDg;
	}

	public void setSpDg(String spDg) {
		this.spDg = spDg;
	}

	public String getBfDg() {
		return bfDg;
	}

	public void setBfDg(String bfDg) {
		this.bfDg = bfDg;
	}

	public String getBqDg() {
		return bqDg;
	}

	public void setBqDg(String bqDg) {
		this.bqDg = bqDg;
	}

	public String getJqDg() {
		return jqDg;
	}

	public void setJqDg(String jqDg) {
		this.jqDg = jqDg;
	}

	public String getRqContent() {
		return rqContent;
	}

	public void setRqContent(String rqContent) {
		this.rqContent = rqContent;
	}

	public String getSpContent() {
		return spContent;
	}

	public void setSpContent(String spContent) {
		this.spContent = spContent;
	}

	public String getBfContent() {
		return bfContent;
	}

	public void setBfContent(String bfContent) {
		this.bfContent = bfContent;
	}

	public String getBqContent() {
		return bqContent;
	}

	public void setBqContent(String bqContent) {
		this.bqContent = bqContent;
	}

	public String getJqContent() {
		return jqContent;
	}

	public void setJqContent(String jqContent) {
		this.jqContent = jqContent;
	}

	public boolean isWinChecked() {
		return isWinChecked;
	}

	public void setWinChecked(boolean isWinChecked) {
		this.isWinChecked = isWinChecked;
	}

	public boolean isDrawChecked() {
		return isDrawChecked;
	}

	public void setDrawChecked(boolean isDrawChecked) {
		this.isDrawChecked = isDrawChecked;
	}

	public boolean isLoseChecked() {
		return isLoseChecked;
	}

	public void setLoseChecked(boolean isLoseChecked) {
		this.isLoseChecked = isLoseChecked;
	}

	public List<Odds> getSpOddsList() {
		return spOddsList;
	}

	public void setSpOddsList(List<Odds> spOddsList) {
		this.spOddsList = spOddsList;
	}

	public List<Odds> getRqOddsList() {
		return rqOddsList;
	}

	public void setRqOddsList(List<Odds> rqOddsList) {
		this.rqOddsList = rqOddsList;
	}

	public List<Odds> getBfOddsList() {
		return bfOddsList;
	}

	public void setBfOddsList(List<Odds> bfOddsList) {
		this.bfOddsList = bfOddsList;
	}

	public List<Odds> getBqOddsList() {
		return bqOddsList;
	}

	public void setBqOddsList(List<Odds> bqOddsList) {
		this.bqOddsList = bqOddsList;
	}

	public List<Odds> getJqOddsList() {
		return jqOddsList;
	}

	public void setJqOddsList(List<Odds> jqOddsList) {
		this.jqOddsList = jqOddsList;
	}
	
}
