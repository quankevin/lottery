package com.xmtq.lottery.bean;

public class VersionBean extends EntityBase {
	/**
	 * 
	 */
	// <element>
	// <version>1.1</version>
	// <dowload/>
	// <update>0</update>
	// </element>
	private static final long serialVersionUID = 4758913516819452194L;
	private String version;
	private String dowload;
	private String update;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDowload() {
		return dowload;
	}

	public void setDowload(String dowload) {
		this.dowload = dowload;
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
