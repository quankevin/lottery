package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.UserInfoResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class UserInfoParser extends BaseParser<UserInfoResponse> {

	@Override
	public UserInfoResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		UserInfoResponse response = new UserInfoResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("0")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			JSONObject j = bodyObj.getJSONObject("elements");
			response.userInfoBean.setAccount(j.getString("account"));
			response.userInfoBean.setBankaccount(j.getString("bankaccount"));
			response.userInfoBean.setBankaddress(j.getString("bankaddress"));
			response.userInfoBean.setBankname(j.getString("bankname"));
			response.userInfoBean.setCardid(j.getString("cardid"));
			response.userInfoBean.setMobile(j.getString("mobile"));
			response.userInfoBean.setRealname(j.getString("realname"));
		}

		return response;
	}
}
