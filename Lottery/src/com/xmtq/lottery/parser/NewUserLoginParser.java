package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.NewUserLoginResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class NewUserLoginParser extends BaseParser<NewUserLoginResponse> {

	@Override
	public NewUserLoginResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		NewUserLoginResponse response = new NewUserLoginResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("0")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			JSONObject j = bodyObj.getJSONObject("element");
			response.newUserLoginBean.setUid(j.getString("uid"));
			response.newUserLoginBean.setUsername(j.getString("username"));
			response.newUserLoginBean.setMoney(j.getString("money"));
			response.newUserLoginBean.setPrizeMoney(j.getString("prizeMoney"));
			response.newUserLoginBean
					.setPerfectFlag(j.getString("perfectFlag"));
		}

		return response;
	}
}
