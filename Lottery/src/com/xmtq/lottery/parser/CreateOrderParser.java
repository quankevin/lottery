package com.xmtq.lottery.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmtq.lottery.bean.BankBean;
import com.xmtq.lottery.bean.BankCBean;
import com.xmtq.lottery.bean.CreateOrderResponse;
import com.xmtq.lottery.bean.UserBankBean;
import com.xmtq.lottery.utils.JsonUtil;

public class CreateOrderParser extends BaseParser<CreateOrderResponse> {

	@Override
	public CreateOrderResponse parse(String xmlString) {
		String jsonString = JsonUtil.xml2JSON(xmlString);
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		CreateOrderResponse response = new CreateOrderResponse();
		JSONObject rootObj = JSON.parseObject(jsonString);
		JSONObject msgObj = rootObj.getJSONObject("message");
		parseMsg(msgObj, response);

		if (response.errorcode.equals("0")) {
			JSONObject bodyObj = msgObj.getJSONObject("body");
			JSONObject elementsObj = bodyObj.getJSONObject("elements");

			try {
				JSONObject usedList = elementsObj.getJSONObject("usedList");
				if (usedList != null) {
					JSONArray usedbank = usedList.getJSONArray("usedbank");
					if (usedbank != null) {
						for (int i = 0; i < usedbank.size(); i++) {
							JSONObject j = usedbank.getJSONObject(i);
							saveUserBankBean(response, j);
						}
					}
				}
			} catch (Exception e) {
				try {
					JSONObject usedList = elementsObj.getJSONObject("usedList");
					JSONObject j = usedList.getJSONObject("usedbank");
					saveUserBankBean(response, j);
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}

			try {
				JSONObject bankList_c = elementsObj.getJSONObject("bankList_c");
				JSONArray bank = bankList_c.getJSONArray("bank");
				if (bank != null) {
					for (int i = 0; i < bank.size(); i++) {
						JSONObject j = bank.getJSONObject(i);
						saveBankCBean(response, j);
					}
				}
			} catch (Exception e) {
				JSONObject bankList_c = elementsObj.getJSONObject("bankList_c");
				JSONObject j = bankList_c.getJSONObject("bank");
				saveBankCBean(response, j);
			}

			try {
				JSONObject bankList = elementsObj.getJSONObject("bankList");
				JSONArray bank = bankList.getJSONArray("bank");
				if (bank != null) {
					for (int i = 0; i < bank.size(); i++) {
						JSONObject j = bank.getJSONObject(i);
						saveBankBean(response, j);
					}
				}
			} catch (Exception e) {
				try {
					JSONObject bankList = elementsObj.getJSONObject("bankList");
					JSONObject j = bankList.getJSONObject("bank");
					saveBankBean(response, j);
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
				
			}

		}
		return response;
	}

	private void saveUserBankBean(CreateOrderResponse response, JSONObject j) {
		UserBankBean mUserBankBean = new UserBankBean();
		mUserBankBean.setBankAccount(j.getString("bankAccount"));
		mUserBankBean.setBankCardTypeUsed(j.getString("bankCardTypeUsed"));
		mUserBankBean.setBankCodeUsed(j.getString("bankCodeUsed"));
		mUserBankBean.setBindId(j.getString("bindId"));
		response.createOrderBean.getUserBankList().add(mUserBankBean);
	}

	private void saveBankBean(CreateOrderResponse response, JSONObject j) {
		BankBean mBankBean = new BankBean();
		mBankBean.setBankCode(j.getString("bankCode"));
		mBankBean.setBankName(j.getString("bankName"));
		response.createOrderBean.getBankList().add(mBankBean);
	}

	private void saveBankCBean(CreateOrderResponse response, JSONObject j) {
		BankCBean mBankCBean = new BankCBean();
		mBankCBean.setBankCode(j.getString("bankCode"));
		mBankCBean.setBankName(j.getString("bankName"));
		response.createOrderBean.getBankCList().add(mBankCBean);
	}

}
