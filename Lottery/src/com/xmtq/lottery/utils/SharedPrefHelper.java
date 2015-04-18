package com.xmtq.lottery.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefHelper {
	/**
	 * SharedPreferences的名字
	 */
	private static final String SP_FILE_NAME = "APPLICATION_SP";

	private static SharedPrefHelper sharedPrefHelper = null;
	private static SharedPreferences sharedPreferences;
	/**
	 * 新浪微博相关信息
	 */
	private static final String KEY_SINA_UID = "sina_uid";
	private static final String KEY_SINA_NAME = "sinaName";
	private static final String KEY_SINA_ACCESS_TOKEN = "sina_access_token";
	private static final String KEY_SINA_EXPIRES_IN = "sina_expires_in";
	/**
	 * QQ相关信息
	 */
	private static final String KEY_QQ_ID = "qqid";
	private static final String KEY_QQ_ACCESS_TOKEN = "qq_access_token";
	private static final String KEY_QQ_EXPIRES_IN = "qq_expires_in";
	private static final String KEY_QQ_NAME = "qqName";

	public static synchronized SharedPrefHelper getInstance(Context c) {
		if (null == sharedPrefHelper) {
			sharedPrefHelper = new SharedPrefHelper(c);
		}
		return sharedPrefHelper;
	}

	private SharedPrefHelper(Context c) {
		sharedPreferences = c.getSharedPreferences(SP_FILE_NAME,
				Context.MODE_PRIVATE);
	}

	public static SharedPreferences sp(Context c) {
		return c.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * 应用第一次登陆标识
	 * 
	 */
	public void setFirstLogin(boolean isFirst) {
		sharedPreferences.edit().putBoolean("firstlogin1", isFirst).commit();
	}

	public boolean getFirstLogin() {
		return sharedPreferences.getBoolean("firstlogin1", true);
	}

	// /**
	// * 新浪微博相关信息保存
	// */
	// public void writeSinaToken(Oauth2AccessToken token) {
	// Editor editor = sharedPreferences.edit();
	// editor.putString(KEY_SINA_UID, token.getUid());
	// editor.putString(KEY_SINA_ACCESS_TOKEN, token.getToken());
	// editor.putLong(KEY_SINA_EXPIRES_IN, token.getExpiresTime());
	// editor.commit();
	// }

	/**
	 * 新浪微博相关信息保存
	 */
	public void writeSinaName(String name) {
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_SINA_NAME, name);
		editor.commit();
	}

	public String sinaName() {
		return sharedPreferences.getString(KEY_SINA_NAME, "");
	}

	// public Oauth2AccessToken readSinaToken() {
	// Oauth2AccessToken token = new Oauth2AccessToken();
	// token.setUid(sharedPreferences.getString(KEY_SINA_UID, ""));
	// token.setToken(sharedPreferences.getString(KEY_SINA_ACCESS_TOKEN, ""));
	// token.setExpiresTime(sharedPreferences.getLong(KEY_SINA_EXPIRES_IN, 0));
	// return token;
	// }

	public void cleanSinaToken() {
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_SINA_UID, "");
		editor.putString(KEY_SINA_ACCESS_TOKEN, "");
		editor.putLong(KEY_SINA_EXPIRES_IN, 0);
		editor.commit();
	}

	// /**
	// * QQ相关信息保存
	// */
	//
	// public void writeQQToken(QQToken token) {
	// Editor editor = sharedPreferences.edit();
	// editor.putString(KEY_QQ_ID, token.getQqid());
	// editor.putString(KEY_QQ_ACCESS_TOKEN, token.getQqToken());
	// editor.putString(KEY_QQ_EXPIRES_IN, token.getQqExpires_in());
	// editor.putString(KEY_QQ_NAME, token.getQqName());
	//
	// editor.commit();
	// }
	//
	// public QQToken readQQToken() {
	// QQToken token = new QQToken();
	// token.setQqid(sharedPreferences.getString(KEY_QQ_ID, ""));
	// token.setQqToken(sharedPreferences.getString(KEY_QQ_ACCESS_TOKEN, ""));
	// token.setQqExpires_in(sharedPreferences
	// .getString(KEY_QQ_EXPIRES_IN, ""));
	// token.setQqName(sharedPreferences.getString(KEY_QQ_NAME, ""));
	// return token;
	// }

	// public void cleanQQToken() {
	// Editor editor = sharedPreferences.edit();
	// editor.putString(KEY_QQ_ID, "");
	// editor.putString(KEY_QQ_ACCESS_TOKEN, "");
	// editor.putLong(KEY_QQ_EXPIRES_IN, 0);
	// editor.commit();
	// }

	// /**
	// * 用户基本信息
	// */
	// public void writeUserBean(UserBean userBean) {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// ObjectOutputStream oos;
	// try {
	// oos = new ObjectOutputStream(baos);
	// oos.writeObject(userBean);
	//
	// String personBase64 = new String(Base64.encode(baos.toByteArray(),
	// Base64.DEFAULT));
	// Editor editor = sharedPreferences.edit();
	// editor.putString("userbean", personBase64);
	// editor.commit();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	//
	// public UserBean readUserBean() {
	// UserBean userBean = null;
	// String personBase64 = sharedPreferences.getString("userbean", "");
	// byte[] base64Bytes = Base64.decode(personBase64.getBytes(),
	// Base64.DEFAULT);
	// ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
	// ObjectInputStream ois;
	// try {
	// ois = new ObjectInputStream(bais);
	// userBean = (UserBean) ois.readObject();
	// } catch (StreamCorruptedException e) {
	// // TODO Auto-generated catch block
	// // e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// // e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// // TODO Auto-generated catch block
	// // e.printStackTrace();
	// }
	// return userBean;
	// }

	public void cleanUserBean() {
		Editor editor = sharedPreferences.edit();
		editor.putString("userbean", "");
		editor.commit();
	}

	/**
	 * 是否登录
	 * 
	 */
	public void setIsLogin(boolean isLogin) {
		sharedPreferences.edit().putBoolean("isLogin", isLogin).commit();
	}

	public boolean getIsLogin() {
		return sharedPreferences.getBoolean("isLogin", false);
	}

	/**
	 * 是否记住密码
	 * 
	 */
	public void setIsRememberPwd(boolean isLogin) {
		sharedPreferences.edit().putBoolean("isRememberPwd", isLogin).commit();
	}

	public boolean getIsRememberPwd() {
		return sharedPreferences.getBoolean("isRememberPwd", false);
	}

	/**
	 * 记住用户名
	 * 
	 */
	public void setUserName(String userName) {
		sharedPreferences.edit().putString("userName", userName).commit();
	}

	public String getUserName() {
		return sharedPreferences.getString("userName", "");
	}

	public void cleanUserName() {
		sharedPreferences.edit().putString("userName", "").commit();
	}
	
	/**
	 * 记住余额
	 * 
	 */
	public void setAccountBalance(String accountBalance) {
		sharedPreferences.edit().putString("accountBalance", accountBalance).commit();
	}

	public String getAccountBalance() {
		return sharedPreferences.getString("accountBalance", "");
	}

	public void cleanAccountBalance() {
		sharedPreferences.edit().putString("accountBalance", "").commit();
	}

	/**
	 * 记住密码
	 * 
	 */
	public void setUserPassward(String passward) {
		sharedPreferences.edit().putString("passward", passward).commit();
	}

	public String getUserPassward() {
		return sharedPreferences.getString("passward", "");
	}

	public void cleanUserPassward() {
		sharedPreferences.edit().putString("passward", "").commit();
	}

	/**
	 * 用户ID
	 * 
	 */
	public void setUid(String uid) {
		sharedPreferences.edit().putString("uid", uid).commit();
	}

	public String getUid() {
		return sharedPreferences.getString("uid", "");
	}
	
	public void cleanUid() {
		sharedPreferences.edit().putString("uid", "").commit();
	}
	
	/**
	 * 清除用户信息
	 */
	public void cleanUserInfo(){
		cleanRealName();
		cleanCardId();
	}
	
	
	/**
	 * 用户真实姓名
	 * 
	 */
	public void setRealName(String realName) {
		sharedPreferences.edit().putString("realName", realName).commit();
	}

	public String getRealName() {
		return sharedPreferences.getString("realName", "");
	}
	
	public void cleanRealName() {
		sharedPreferences.edit().putString("realName", "").commit();
	}
	
	/**
	 * 用户身份证卡号
	 * 
	 */
	public void setCardId(String cardId) {
		sharedPreferences.edit().putString("cardId", cardId).commit();
	}

	public String getCardId() {
		return sharedPreferences.getString("cardId", "");
	}
	
	public void cleanCardId() {
		sharedPreferences.edit().putString("cardId", "").commit();
	}
	
	/**
	 * 银行信息
	 * 
	 */
	public void setBankName(String bankName) {
		sharedPreferences.edit().putString("bankName", bankName).commit();
	}

	public String getBankName() {
		return sharedPreferences.getString("bankName", "");
	}
	
	public void cleanBankName() {
		sharedPreferences.edit().putString("bankName", "").commit();
	}
	
	/**
	 * 银行卡号
	 * 
	 */
	public void setBankCardId(String bankCardId) {
		sharedPreferences.edit().putString("bankCardId", bankCardId).commit();
	}

	public String getBankCardId() {
		return sharedPreferences.getString("bankCardId", "");
	}
	
	public void cleanBankCardId() {
		sharedPreferences.edit().putString("bankCardId", "").commit();
	}
	
	/**
	 * 银行开户地
	 * 
	 */
	public void setBankAddress(String bankAddress) {
		sharedPreferences.edit().putString("bankAddress", bankAddress).commit();
	}

	public String getBankAddress() {
		return sharedPreferences.getString("bankAddress", "");
	}
	
	public void cleanBankAddress() {
		sharedPreferences.edit().putString("bankAddress", "").commit();
	}
	
	/**
	 * 绑定银行卡-用户密码（仅用于测试，正式版删除）
	 * 
	 */
	public void setPassword(String password) {
		sharedPreferences.edit().putString("password", password).commit();
	}

	public String getPassword() {
		return sharedPreferences.getString("password", "");
	}
	
	public void cleanPassword() {
		sharedPreferences.edit().putString("password", "").commit();
	}
}
