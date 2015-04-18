package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.PurchaseRecordsBean;
import com.xmtq.lottery.bean.PurchaseRecordsResponse;
import com.xmtq.lottery.utils.JsonUtil;

/**
 * 竞彩购买记录查询
 * 
 */
public class PurchaseRecordsParser extends BaseParser<PurchaseRecordsResponse> {

	@Override
	public PurchaseRecordsResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		PurchaseRecordsResponse response = new PurchaseRecordsResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("0")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			JSONObject elementsObj = bodyObj.getJSONObject("elements");
			response.count = elementsObj.getString("count");

			try {

				JSONArray elementArray = elementsObj.getJSONArray("element");

				if (elementArray != null) {
					for (int i = 0; i < elementArray.size(); i++) {
						JSONObject j = elementArray.getJSONObject(i);
						getPurchaseRecord(response, j);
					}
				}

			} catch (Exception e) {

				JSONObject j = elementsObj.getJSONObject("element");
				getPurchaseRecord(response, j);
				// TODO: handle exception
			}

		}

		return response;
	}

	private void getPurchaseRecord(PurchaseRecordsResponse response,
			JSONObject j) {
		PurchaseRecordsBean bean = new PurchaseRecordsBean();
		bean.setGuoguan(j.getString("guoguan"));
		bean.setAddtime(j.getString("addtime"));
		bean.setSerialid(j.getString("serialid"));
		bean.setPlaytype(j.getString("playtype"));
		bean.setState(j.getString("state"));
		bean.setBonusAfterfax(j.getString("bonus_after_fax"));
		bean.setBonusBeforeFax(j.getString("bonus_before_fax"));
		bean.setProjectPrize(j.getString("project_prize"));
		response.purchaseRecordsBeans.add(bean);
	}
}
