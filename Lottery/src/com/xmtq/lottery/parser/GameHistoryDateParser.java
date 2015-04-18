package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.GameHistoryDateBean;
import com.xmtq.lottery.bean.GameHistoryDateResponse;
import com.xmtq.lottery.utils.JsonUtil;

public class GameHistoryDateParser extends BaseParser<GameHistoryDateResponse> {

	@Override
	public GameHistoryDateResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		GameHistoryDateResponse response = new GameHistoryDateResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("0")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			if (bodyObj.containsKey("elements")) {
				JSONObject elementsObj = bodyObj.getJSONObject("elements");
				JSONArray ja = elementsObj.getJSONArray("element");
				if (ja != null) {
					for (int i = 0; i < ja.size(); i++) {
						GameHistoryDateBean mGameHistoryDateBean = new GameHistoryDateBean();
						JSONObject j = ja.getJSONObject(i);
						mGameHistoryDateBean.setCount(j.getString("count"));
						mGameHistoryDateBean.setDate(j.getString("date"));
						mGameHistoryDateBean.setHitcount(j
								.getString("hitcount"));
						response.mHistoryDateBeansList
								.add(mGameHistoryDateBean);
					}

				}
				response.count = elementsObj.getString("count");
			}

		}

		return response;
	}
}
