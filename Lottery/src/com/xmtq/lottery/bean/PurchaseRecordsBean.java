package com.xmtq.lottery.bean;

public class PurchaseRecordsBean extends EntityBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8391624578292769336L;

	private String guoguan;
	private String addtime;
	private String serialid;
	private String playtype;
	private String state;
	private String bonusAfterfax;
	private String bonusBeforeFax;
	private String projectPrize;

	public String getGuoguan() {
		return guoguan;
	}

	public void setGuoguan(String guoguan) {
		this.guoguan = guoguan;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getSerialid() {
		return serialid;
	}

	public void setSerialid(String serialid) {
		this.serialid = serialid;
	}

	public String getPlaytype() {
		return playtype;
	}

	public void setPlaytype(String playtype) {
		this.playtype = playtype;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBonusAfterfax() {
		return bonusAfterfax;
	}

	public void setBonusAfterfax(String bonusAfterfax) {
		this.bonusAfterfax = bonusAfterfax;
	}

	public String getBonusBeforeFax() {
		return bonusBeforeFax;
	}

	public void setBonusBeforeFax(String bonusBeforeFax) {
		this.bonusBeforeFax = bonusBeforeFax;
	}

	public void setProjectPrize(String projectPrize) {
		this.projectPrize = projectPrize;
	}

	public String getProjectPrize() {
		return projectPrize;
	}

}
