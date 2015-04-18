package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.RecomendWinRecordBean;
import com.xmtq.lottery.bean.RecomendWinRecordResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class RecomendWinRecordParser extends
		BaseParser<RecomendWinRecordResponse> {

	@Override
	public RecomendWinRecordResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		RecomendWinRecordResponse response = new RecomendWinRecordResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("0")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			JSONObject elementsObj = bodyObj.getJSONObject("elements");

			try {
				JSONArray elementObj = elementsObj.getJSONArray("element");
				// JSONArray usedbank = usedList.getJSONArray("usedbank");
				if (elementObj != null) {
					for (int i = 0; i < elementObj.size(); i++) {
						JSONObject j = elementObj.getJSONObject(i);
						saveWinRecord(response, j);
					}
				}
			} catch (Exception e) {
				JSONObject j = elementsObj.getJSONObject("element");
				saveWinRecord(response, j);

			}

		}
		return response;
	}

	private void saveWinRecord(RecomendWinRecordResponse response, JSONObject j) {
		RecomendWinRecordBean mWinRecordBean = new RecomendWinRecordBean();
		mWinRecordBean.setBonus(j.getString("bonus"));
		mWinRecordBean.setGuoguan(j.getString("guoguan"));
		mWinRecordBean.setLotteryname(j.getString("lotteryname"));
		mWinRecordBean.setUsername(j.getString("username"));
		response.mWinRecordBeans.add(mWinRecordBean);
	}

}
