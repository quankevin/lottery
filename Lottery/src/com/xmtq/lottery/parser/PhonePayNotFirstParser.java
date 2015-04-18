package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.PhonePayNotFirstResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class PhonePayNotFirstParser extends
		BaseParser<PhonePayNotFirstResponse> {

	@Override
	public PhonePayNotFirstResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		PhonePayNotFirstResponse response = new PhonePayNotFirstResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		return response;
	}
}
