package com.xmtq.lottery;

public class Consts {

	/**
	 * 服务器地址（正式上线后，需修改）
	 */
	public static final String host = "http://220.167.29.50:8380/lotteryxml.php";

	/**
	 * 密码密钥
	 */
	public static final String passwordkey = "97d2dcb2ea177246d44f1114e2d5e9c5";

	/**
	 * Agent密钥（加密请求数据）
	 */
	public static final String agenterkey = "c5d1eb92ced441225554a64e07694fa0";

	/**
	 * 渠道ID
	 */
	public static final String agenterid = "10000001";

	/**
	 * 手机注册
	 */
	public static final String PHONE_REGISTER = "1";

	/**
	 * 邮箱注册
	 */
	public static final String EMAIL_REGISTER = "2";

	/**
	 * 手机注册验证
	 */
	public static final String PHONE_REGISTER_VERI = "01";

	/**
	 * 找回密码验证
	 */
	public static final String FIND_PASSWORD_VERI = "02";
	
	/**
	 * 竞彩混投（客户端现只支持混投）
	 */
	public static final String Lottery_ID = "136";
	
	/**
	 * 购买方式（1:合买，2：代购，暂只支持代购	）
	 */
	public static final String VOTE_TYPE = "2";
	
	/**
	 * 方案类型（混投=6）
	 */
	public static final String PLAY_TYPE = "6";
	
	/**
	 * 方案类型（0：单式，1：复式，竞彩手机端只有复式）
	 */
	public static final String PRO_TYPE = "1";
	
	/**
	 * 请求网络错误
	 */
	public static final String REQUEST_ERROR = "网络错误";
	
	/**
	 * 上拉刷新
	 */
	public static final int LOAD_DATA_FINISH = 10;
	
	/**
	 * 下拉加载
	 */
	public static final int REFRESH_DATA_FINISH = 11;
	
	/**
	 * 广播--更新用户信息
	 */
	public static final String ACTION_REFRESH_USERINFO = "com.action.refresh.userinfo";
	
	/**
	 * 广播--注册完成自动登录
	 */
	public static final String ACTION_AUTO_LOGIN = "com.action.auto.login";
}
