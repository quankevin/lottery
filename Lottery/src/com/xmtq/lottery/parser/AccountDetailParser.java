package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.AccountDetailBean;
import com.xmtq.lottery.bean.AccountDetailResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class AccountDetailParser extends BaseParser<AccountDetailResponse> {

	@Override
	public AccountDetailResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		AccountDetailResponse response = new AccountDetailResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("0")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			JSONObject elementsObj = bodyObj.getJSONObject("elements");
			try {
				JSONArray ja = elementsObj.getJSONArray("element");
				if (ja != null) {
					for (int i = 0; i < ja.size(); i++) {
						JSONObject j = ja.getJSONObject(i);
						getParser(response, j);
					}
				}
			} catch (Exception e) {
				JSONObject j = elementsObj.getJSONObject("element");
				if (j != null) {
					getParser(response, j);
				}
			}

			if (elementsObj.containsKey("pay")) {
				response.setPay(elementsObj.getString("pay"));
			}
			if (elementsObj.containsKey("income")) {
				response.setIncome(elementsObj.getString("income"));
			}
			if (elementsObj.containsKey("count")) {
				response.setCount(elementsObj.getString("count"));
			}

		}

		return response;
	}

	private void getParser(AccountDetailResponse response, JSONObject j) {
		AccountDetailBean accountBean = new AccountDetailBean();
		accountBean.setEntertime(j.getString("entertime"));
		accountBean.setMflag(j.getString("mflag"));
		accountBean.setMoney(j.getString("money"));
		accountBean.setRemark(j.getString("remark"));
		response.accountDetailList.add(accountBean);
	}
}
