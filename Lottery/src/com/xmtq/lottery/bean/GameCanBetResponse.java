package com.xmtq.lottery.bean;

import java.util.ArrayList;
import java.util.List;

public class GameCanBetResponse extends BaseResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5375804597574885028L;

	public String count;
	public String comCount;
	public List<GameCanBetBean> gameCanBetBeans = new ArrayList<GameCanBetBean>();
}
