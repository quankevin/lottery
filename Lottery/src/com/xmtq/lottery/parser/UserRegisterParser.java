package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.UserRegisterResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class UserRegisterParser extends BaseParser<UserRegisterResponse> {

	@Override
	public UserRegisterResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		UserRegisterResponse response = new UserRegisterResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("0")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			JSONObject j = bodyObj.getJSONObject("element");
			response.userRegisterBean.setUid(j.getString("uid"));
			response.userRegisterBean.setUsername(j.getString("username"));
			response.userRegisterBean.setMoney(j.getString("money"));
			response.userRegisterBean.setPrizeMoney(j.getString("prizeMoney"));
			response.userRegisterBean
					.setPerfectFlag(j.getString("perfectFlag"));
		}

		return response;
	}
}
