package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.CheckUserResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class CheckUserParser extends BaseParser<CheckUserResponse> {

	@Override
	public CheckUserResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		CheckUserResponse response = new CheckUserResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("0")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			JSONObject j = bodyObj.getJSONObject("element");
			response.checkUserBean.setPstate(j.getString("pstate"));
			response.checkUserBean.setUstate(j.getString("ustate"));
		}

		return response;
	}
}
