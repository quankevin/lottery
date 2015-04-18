package com.xmtq.lottery.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class JsonUtil {
	public static String xml2JSON(String xml) {
		try {
			JSONObject obj = XML.toJSONObject(xml);
			LogUtil.log("jsonString:" + obj.toString());
			return obj.toString();
		} catch (JSONException e) {
			System.err.println("xml->json error" + e.getLocalizedMessage());
			return "";
		}
	}
}
