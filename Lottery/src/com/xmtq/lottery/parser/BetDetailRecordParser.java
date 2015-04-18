package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.BetDetailAllResponse;
import com.xmtq.lottery.bean.BetDetailBean;
import com.xmtq.lottery.bean.BetOddBean;
import com.xmtq.lottery.utils.JsonUtil;

public class BetDetailRecordParser extends BaseParser<BetDetailAllResponse> {

	@Override
	public BetDetailAllResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		BetDetailAllResponse response = new BetDetailAllResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("0")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			JSONObject elementsObj = bodyObj.getJSONObject("elements");

			// BetDetailAllBean mBetDetailAllBean = new BetDetailAllBean();
			if (elementsObj.containsKey("element")) {
				try {

					JSONArray elementObj = elementsObj.getJSONArray("element");
					// JSONArray usedbank = usedList.getJSONArray("usedbank");
					if (elementObj != null) {
						for (int i = 0; i < elementObj.size(); i++) {
							JSONObject j = elementObj.getJSONObject(i);
							saveBetDetailRecord(response, j);
						}
					}
				} catch (Exception e) {
					JSONObject j = elementsObj.getJSONObject("element");
					saveBetDetailRecord(response, j);

				}
			}

			response.mBetDetailAllBean.setBonus(elementsObj.getString("bonus"));
			response.mBetDetailAllBean.setGuoguantype(elementsObj
					.getString("guoguantype"));
			response.mBetDetailAllBean.setMultiple(elementsObj
					.getString("multiple"));
			response.mBetDetailAllBean.setPlaytype(elementsObj
					.getString("playtype"));

			response.mBetDetailAllBean.setProjectno(elementsObj
					.getString("projectno"));
			response.mBetDetailAllBean.setProjectprize(elementsObj
					.getString("projectprize"));
			response.mBetDetailAllBean.setProjecttime(elementsObj
					.getString("projecttime"));
			response.mBetDetailAllBean.setState(elementsObj.getString("state"));
			response.mBetDetailAllBean.setUserballot(elementsObj
					.getString("userballot"));

		}
		return response;
	}

	private void saveBetDetailRecord(BetDetailAllResponse response, JSONObject j) {

		BetDetailBean mBetDetailBean = new BetDetailBean();
		mBetDetailBean.setMatchteam(j.getString("matchteam"));
		mBetDetailBean.setNumber(j.getString("number"));
		mBetDetailBean.setName(j.getString("name"));
		mBetDetailBean.setHostteam(j.getString("hostteam"));
		try {
			if (j.containsKey("odd")) {
				JSONArray odds = j.getJSONArray("odd");
				if (odds != null) {
					for (int k = 0; k < odds.size(); k++) {
						JSONObject odd = odds.getJSONObject(k);
						BetOddBean mOddBean = new BetOddBean();
						mOddBean.setBetinfo(odd.getString("betinfo"));
						mOddBean.setGameresult(odd.getString("gameresult"));
						mOddBean.setPlayname(odd.getString("playname"));
						mBetDetailBean.getmBetOddBeans().add(mOddBean);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject odd = j.getJSONObject("odd");
			if (odd != null) {
				BetOddBean mOddBean = new BetOddBean();
				mOddBean.setBetinfo(odd.getString("betinfo"));
				mOddBean.setGameresult(odd.getString("gameresult"));
				mOddBean.setPlayname(odd.getString("playname"));
				mBetDetailBean.getmBetOddBeans().add(mOddBean);
			}
		}

		response.mBetDetailAllBean.getmBetDetailBeans().add(mBetDetailBean);
	}

}
