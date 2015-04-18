package com.xmtq.lottery.bean;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRecordsResponse extends BaseResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5375804597574885028L;

	public String count;
	public List<PurchaseRecordsBean> purchaseRecordsBeans = new ArrayList<PurchaseRecordsBean>();
}
