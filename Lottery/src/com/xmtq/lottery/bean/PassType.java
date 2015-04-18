package com.xmtq.lottery.bean;

public class PassType extends EntityBase {
	private static final long serialVersionUID = -8391624578292769336L;

	/**
	 * 过关类型（比如2串1）
	 */
	private String name;

	/**
	 * 过关类型值（2*1）
	 */
	private String value;

	/**
	 * 是否选中
	 */
	private boolean isChecked = false;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public boolean isChecked() {
		return isChecked;
	}

}
