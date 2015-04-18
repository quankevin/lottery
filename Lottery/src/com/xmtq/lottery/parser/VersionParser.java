package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.VersionResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class VersionParser extends BaseParser<VersionResponse> {

	@Override
	public VersionResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		VersionResponse response = new VersionResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("1")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			JSONObject j = bodyObj.getJSONObject("element");
			response.versionBean.setDowload(j.getString("dowload"));
			response.versionBean.setUpdate(j.getString("update"));
			response.versionBean.setVersion(j.getString("version"));
			response.versionBean.setMessage(j.getString("message"));
		}

		return response;
	}
}
