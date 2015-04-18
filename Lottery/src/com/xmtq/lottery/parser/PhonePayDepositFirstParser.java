package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.PhonePayDepositFirstResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class PhonePayDepositFirstParser extends
		BaseParser<PhonePayDepositFirstResponse> {

	@Override
	public PhonePayDepositFirstResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		PhonePayDepositFirstResponse response = new PhonePayDepositFirstResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		return response;
	}
}
