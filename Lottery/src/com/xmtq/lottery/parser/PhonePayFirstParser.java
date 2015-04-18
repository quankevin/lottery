package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.PhonePayFirstResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class PhonePayFirstParser extends BaseParser<PhonePayFirstResponse> {

	@Override
	public PhonePayFirstResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		PhonePayFirstResponse response = new PhonePayFirstResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		return response;
	}
}
