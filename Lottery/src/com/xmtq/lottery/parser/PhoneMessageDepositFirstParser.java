package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.PhoneMessageDepositResponse;
import com.xmtq.lottery.utils.JsonUtil;

/**
 * 储蓄卡短信解析
 * 
 * @author mwz123
 * 
 */
public class PhoneMessageDepositFirstParser extends
		BaseParser<PhoneMessageDepositResponse> {

	@Override
	public PhoneMessageDepositResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		PhoneMessageDepositResponse response = new PhoneMessageDepositResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		return response;
	}
}
