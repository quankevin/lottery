package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.BettingBusinessResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class BettingBusinessParser extends BaseParser<BettingBusinessResponse> {

	@Override
	public BettingBusinessResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		BettingBusinessResponse response = new BettingBusinessResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("0")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			JSONObject j = bodyObj.getJSONObject("element");
			response.pno = j.getString("pno");
		}

		return response;
	}
}
