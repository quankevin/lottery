package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.PhoneMessageResponse;
import com.xmtq.lottery.utils.JsonUtil;

/**
 * 信用卡短信
 * 
 * @author mwz123
 * 
 */
public class PhoneMessageFirstParser extends BaseParser<PhoneMessageResponse> {

	@Override
	public PhoneMessageResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		PhoneMessageResponse response = new PhoneMessageResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		return response;
	}
}
