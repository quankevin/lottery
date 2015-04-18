package com.xmtq.lottery.network;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

import com.dvt.lottery.util.MD5;
import com.xmtq.lottery.Consts;
import com.xmtq.lottery.parser.AccountDetailParser;
import com.xmtq.lottery.parser.BetDetailRecordParser;
import com.xmtq.lottery.parser.BettingBusinessParser;
import com.xmtq.lottery.parser.CheckUserParser;
import com.xmtq.lottery.parser.CreateOrderParser;
import com.xmtq.lottery.parser.ExtractCashParser;
import com.xmtq.lottery.parser.GameCanBetParser;
import com.xmtq.lottery.parser.GameHistoryDateParser;
import com.xmtq.lottery.parser.ImproveUserInfoParser;
import com.xmtq.lottery.parser.NewUserLoginParser;
import com.xmtq.lottery.parser.PhoneMessageDepositFirstParser;
import com.xmtq.lottery.parser.PhoneMessageFirstParser;
import com.xmtq.lottery.parser.PhoneMessageNotFirstParser;
import com.xmtq.lottery.parser.PhonePayDepositFirstParser;
import com.xmtq.lottery.parser.PhonePayFirstParser;
import com.xmtq.lottery.parser.PhonePayNotFirstParser;
import com.xmtq.lottery.parser.PurchaseRecordsParser;
import com.xmtq.lottery.parser.RecomendHistoryParser;
import com.xmtq.lottery.parser.RecomendWinRecordParser;
import com.xmtq.lottery.parser.RepasswordParser;
import com.xmtq.lottery.parser.SimpleParser;
import com.xmtq.lottery.parser.UserInfoParser;
import com.xmtq.lottery.parser.UserRegisterParser;
import com.xmtq.lottery.parser.VerificationCodeParser;
import com.xmtq.lottery.parser.VersionParser;
import com.xmtq.lottery.utils.LogUtil;

public class RequestMaker {

	private RequestMaker() {

	}

	private static RequestMaker requestMaker = null;

	/**
	 * 得到JsonMaker的实例
	 * 
	 * @param context
	 * @return
	 */
	public static RequestMaker getInstance() {
		if (requestMaker == null) {
			requestMaker = new RequestMaker();
			return requestMaker;
		} else {
			return requestMaker;
		}
	}

	/**
	 * @param sb
	 */
	protected String makeTag(String name, String value) {
		return "<" + name + ">" + (value == null ? "" : value) + "</" + name
				+ ">";
	}

	/**
	 * 生成XML请求参数
	 * 
	 * @param body
	 * @param transactiontype
	 *            请求操作类型代码
	 * @return
	 */
	public String makeXml(String body, String transactiontype) {
		String source = Consts.agenterkey;
		if (body != null) {
			source += body;
		}
		String digest = MD5.md5(source);

		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<message version=\"1.0\">");
		sb.append(makeHeader(digest, transactiontype));
		if (body != null) {
			sb.append(body);
		}
		sb.append("</message>");

		LogUtil.log("xmlBody:" + sb.toString());
		return sb.toString();
	}

	/**
	 * 生成HTTP POST请求头
	 */
	@SuppressLint("SimpleDateFormat")
	protected String makeHeader(String digest, String transactiontype) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = sdf.format(new Date());
		StringBuilder sb = new StringBuilder();

		sb.append("<header>");
		sb.append(makeTag("transactiontype", transactiontype));
		sb.append(makeTag("timestamp", timestamp));
		sb.append(makeTag("digest", digest));
		sb.append(makeTag("agenterid", Consts.agenterid));
		sb.append(makeTag("ipaddress", ""));
		sb.append(makeTag("source", "WEB"));
		sb.append("</header>");

		return sb.toString();
	}

	/**
	 * 3.1.1新用户注册（10001_1.1）
	 */
	public Request getUserRegister(String username, String mail,
			String actpassword, String mobile, String serialuid, String type,
			String code) {

		UserRegisterParser parser = new UserRegisterParser();
		String body = createUserRegister(username, mail, actpassword, mobile,
				serialuid, type, code);
		String xmlBody = makeXml(body, "10001_1.1");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createUserRegister(String username, String mail,
			String actpassword, String mobile, String serialuid, String type,
			String code) {

		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("username", username));
		sb.append(makeTag("mail", mail));
		sb.append(makeTag("actpassword", actpassword));
		sb.append(makeTag("mobile", mobile));
		sb.append(makeTag("serialuid", serialuid));
		sb.append(makeTag("type", type));
		sb.append(makeTag("code", code));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 完善用户信息(身份信息)
	 */
	public Request getPerfectUserInfo(String uid, String realname, String cardid) {
		ImproveUserInfoParser parser = new ImproveUserInfoParser();
		String body = createPerfectUserInfo(uid, realname, cardid);
		String xmlBody = makeXml(body, "10002_1.1");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createPerfectUserInfo(String uid, String realname,
			String cardid) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element drawaltype=\"0\">");
		sb.append(makeTag("uid", uid));
		sb.append(makeTag("realname", realname));
		sb.append(makeTag("cardid", cardid));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 完善用户信息(银行卡信息)
	 */
	public Request getPerfectUserInfo(String uid, String realname,
			String cardid, String phone, String bankname, String bankcardid,
			String bankaddress, String actpassword) {
		ImproveUserInfoParser parser = new ImproveUserInfoParser();
		String body = createPerfectUserInfo(uid, realname, cardid, phone,
				bankname, bankcardid, bankaddress, actpassword);
		String xmlBody = makeXml(body, "10002_1.1");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createPerfectUserInfo(String uid, String realname,
			String cardid, String phone, String bankname, String bankcardid,
			String bankaddress, String actpassword) {
		StringBuilder sb = new StringBuilder();
		actpassword = MD5.hmacSign(actpassword, Consts.passwordkey);

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("uid", uid));
		// sb.append(makeTag("realname", realname));
		// sb.append(makeTag("cardid", cardid));
		sb.append(makeTag("phone", phone));
		sb.append(makeTag("bankname", bankname));
		sb.append(makeTag("bankcardid", bankcardid));
		sb.append(makeTag("bankaddress", bankaddress));
		sb.append(makeTag("actpassword", actpassword));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 修改用户密码
	 */
	public Request getModifyPassword(String uid, String oldpassword,
			String newpassword) {
		RepasswordParser parser = new RepasswordParser();
		String body = createModifyPassword(uid, oldpassword, newpassword);
		String xmlBody = makeXml(body, "10003_1.1");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createModifyPassword(String uid, String oldpassword,
			String newpassword) {
		StringBuilder sb = new StringBuilder();
		oldpassword = MD5.hmacSign(oldpassword, Consts.passwordkey);
		newpassword = MD5.hmacSign(newpassword, Consts.passwordkey);

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("uid", uid));
		sb.append(makeTag("oldpassword", oldpassword));
		sb.append(makeTag("newpassword", newpassword));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 新用户登陆
	 */
	public Request getUserLogin(String username, String actpassword) {
		NewUserLoginParser parser = new NewUserLoginParser();
		String body = createUserLogin(username, actpassword);
		String xmlBody = makeXml(body, "10008_1.1");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createUserLogin(String username, String actpassword) {
		StringBuilder sb = new StringBuilder();
		// actpassword = MD5.hmacSign(actpassword, Consts.passwordkey);

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("username", username));
		sb.append(makeTag("actpassword", actpassword));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 检测用户名/手机/邮箱是否存在
	 */
	public Request getCheckUser(String parameter, String phone) {
		CheckUserParser parser = new CheckUserParser();
		String body = createCheckUser(parameter, phone);
		String xmlBody = makeXml(body, "10010");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createCheckUser(String parameter, String phone) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("username", parameter));
		sb.append(makeTag("phone", phone));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * /** 发送短信验证码
	 * 
	 * @param tel
	 *            手机号
	 * @param type
	 *            01：用户注册 02：修改密码
	 * @return
	 */
	public Request getMessageVerification(String tel, String type) {
		VerificationCodeParser parser = new VerificationCodeParser();
		String body = createMessageVerification(tel, type);
		String xmlBody = makeXml(body, "10011");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createMessageVerification(String tel, String type) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("tel", tel));
		sb.append(makeTag("type", type));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 3.1.7检测版本更新（10012）
	 * 
	 * @param version
	 * @return
	 */
	public Request getVersion(String version) {
		VersionParser parser = new VersionParser();
		String body = createVersion(version);
		String xmlBody = makeXml(body, "10012");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createVersion(String version) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("version", version));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 用户账户明细查询(no pass)
	 */
	public Request getAccountDetail(String startDate, String endDate,
			String uid, String mflag, String pageNum, String pageSize) {
		AccountDetailParser parser = new AccountDetailParser();
		String body = createAccountDetail(startDate, endDate, uid, mflag,
				pageNum, pageSize);
		String xmlBody = makeXml(body, "11008_1.1");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createAccountDetail(String startDate, String endDate,
			String uid, String mflag, String pageNum, String pageSize) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("startDate", startDate));
		sb.append(makeTag("endDate", endDate));
		sb.append(makeTag("uid", uid));
		sb.append(makeTag("mflag", mflag));
		sb.append(makeTag("pageNum", pageNum));
		sb.append(makeTag("pageSize", pageSize));

		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 用户详细信息查询
	 */
	public Request getUserInfo(String uid) {
		UserInfoParser parser = new UserInfoParser();
		String body = createUserInfo(uid);
		String xmlBody = makeXml(body, "20001_1.1");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createUserInfo(String uid) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("uid", uid));

		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 3.1.10提取现金（20003_1.1）
	 * 
	 * @param uid
	 * @param password
	 * @param drawalmoney
	 * @return
	 */
	public Request getExtractCash(String uid, String password,
			String drawalmoney) {
		ExtractCashParser parser = new ExtractCashParser();
		String body = createExtractCash(uid, password, drawalmoney);
		String xmlBody = makeXml(body, "20003_1.1");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createExtractCash(String uid, String password,
			String drawalmoney) {
		StringBuilder sb = new StringBuilder();

		password = MD5.hmacSign(password, Consts.passwordkey);
		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element drawaltype=\"0\">");
		sb.append(makeTag("uid", uid));
		sb.append(makeTag("password", password));
		sb.append(makeTag("drawalmoney", drawalmoney));

		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.1投注（12006_1.1）
	 * 
	 * @param uid
	 * @param lotteryid
	 * @param votetype
	 * @param votenums
	 * @param multiple
	 * @param voteinfo
	 * @param totalmoney
	 * @param playtype
	 * @param passtype
	 * @param buymoney
	 * @param protype
	 * @return
	 */
	public Request getBettingBusiness(String uid, String lotteryid,
			String votetype, String votenums, String multiple, String voteinfo,
			String totalmoney, String playtype, String passtype,
			String buymoney, String protype) {

		BettingBusinessParser parser = new BettingBusinessParser();
		String body = createBettingBusiness(uid, lotteryid, votetype, votenums,
				multiple, voteinfo, totalmoney, playtype, passtype, buymoney,
				protype);
		String xmlBody = makeXml(body, "12006_1.1");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createBettingBusiness(String uid, String lotteryid,
			String votetype, String votenums, String multiple, String voteinfo,
			String totalmoney, String playtype, String passtype,
			String buymoney, String protype) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("lotteryid", lotteryid));
		sb.append(makeTag("uid", uid));
		sb.append(makeTag("votetype", votetype));
		sb.append(makeTag("votenums", votenums));
		sb.append(makeTag("multiple", multiple));

		sb.append(makeTag("voteinfo", voteinfo));

		sb.append(makeTag("totalmoney", totalmoney));

		sb.append(makeTag("playtype", playtype));

		sb.append(makeTag("passtype", passtype));
		sb.append(makeTag("buymoney", buymoney));
		sb.append(makeTag("protype", protype));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.2竞彩购买记录查询（12021_1.1）
	 * 
	 * @param uid
	 * @param lotteryid
	 * @param startdate
	 * @param enddate
	 * @param investtype
	 * @param pageindex
	 * @param pagesize
	 * @param statue
	 * @return
	 */
	public Request getPurchaseRecords(String uid, String lotteryid,
			String startdate, String enddate, String investtype,
			String pageindex, String pagesize, String statue) {

		PurchaseRecordsParser parser = new PurchaseRecordsParser();
		String body = createPurchaseRecords(uid, lotteryid, startdate, enddate,
				investtype, pageindex, pagesize, statue);
		String xmlBody = makeXml(body, "12021_1.1");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createPurchaseRecords(String uid, String lotteryid,
			String startdate, String enddate, String investtype,
			String pageindex, String pagesize, String statue) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("uid", uid));
		sb.append(makeTag("lotteryid", lotteryid));
		sb.append(makeTag("startdate", startdate));
		sb.append(makeTag("enddate", enddate));
		sb.append(makeTag("investtype", investtype));
		sb.append(makeTag("pageindex", pageindex));
		sb.append(makeTag("pagesize", pagesize));
		sb.append(makeTag("statue", statue));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.3竞彩购买记录详情查询（12022_1.1）
	 * 
	 * @param serialid
	 * @return
	 */
	public Request getPurchaseRecordsDetail(String serialid) {
		BetDetailRecordParser parser = new BetDetailRecordParser();
		String body = createPurchaseRecordsDetail(serialid);
		String xmlBody = makeXml(body, "12022_1.1");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createPurchaseRecordsDetail(String serialid) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("serialid", serialid));

		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.4竞彩推荐赛事历史查询（12025）
	 * 
	 * @param date
	 * @return
	 */
	public Request getGameHistorySearch(String date) {
		RecomendHistoryParser parser = new RecomendHistoryParser();
		String body = createGameHistorySearch(date);
		String xmlBody = makeXml(body, "12025");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createGameHistorySearch(String date) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("date", date));

		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.5竞彩历史推荐日期列表（12026）
	 * 
	 * @param startdate
	 * @param enddate
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Request getGameHistoryDateList(String startdate, String enddate,
			String pageNum, String pageSize) {
		GameHistoryDateParser parser = new GameHistoryDateParser();
		String body = createGameHistoryDateList(startdate, enddate, pageNum,
				pageSize);
		String xmlBody = makeXml(body, "12026");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createGameHistoryDateList(String startdate, String enddate,
			String pageNum, String pageSize) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("startdate", startdate));
		sb.append(makeTag("enddate", enddate));
		sb.append(makeTag("pageNum", pageNum));
		sb.append(makeTag("pageSize", pageSize));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.6竞彩今天推荐赛事投票（12028）
	 * 
	 * @param uid
	 * @param matchId
	 * @param vote
	 * @param content
	 * @return
	 */
	public Request getGameTodayRecomend(String uid, String matchId,
			String vote, String content) {
		SimpleParser paser = new SimpleParser();
		String body = createGameTodayRecomend(uid, matchId, vote, content);
		String xmlBody = makeXml(body, "12028");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, paser);
		return request;
	}

	private String createGameTodayRecomend(String uid, String matchId,
			String vote, String content) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("uid", uid));
		sb.append(makeTag("matchId", matchId));
		sb.append(makeTag("vote", vote));
		sb.append(makeTag("content", content));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.7竞彩中奖记录查询（12029）
	 * 
	 * @param uid
	 * @param matchId
	 * @param vote
	 * @param content
	 * @return
	 */
	public Request getGameWinRecord(String size) {
		RecomendWinRecordParser parser = new RecomendWinRecordParser();
		String body = createGameWinRecord(size);
		String xmlBody = makeXml(body, "12029");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createGameWinRecord(String size) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("size", size));

		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.8竞彩可投注赛事查询（12030）
	 * 
	 * @param pagenum
	 * @param pagesize
	 * @return
	 */
	public Request getGameCanBet(String pagenum, String pagesize) {
		GameCanBetParser parse = new GameCanBetParser();

		String body = createGameCanBet(pagenum, pagesize);
		String xmlBody = makeXml(body, "12030");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parse);
		return request;
	}

	private String createGameCanBet(String pagenum, String pagesize) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("pagenum", pagenum));
		sb.append(makeTag("pagesize", pagesize));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.9创建丰付支付订单(15006)
	 * 
	 * @param userIdIdentity
	 * @param totalPrice
	 * @return
	 */
	public Request getFengPay(String userIdIdentity, String totalPrice) {
		CreateOrderParser parser = new CreateOrderParser();
		String body = createFengPay(userIdIdentity, totalPrice);
		String xmlBody = makeXml(body, "15006");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createFengPay(String userIdIdentity, String totalPrice) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("userIdIdentity", userIdIdentity));
		sb.append(makeTag("totalPrice", totalPrice));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.10丰付首次快捷支付 信用卡（15007）
	 * 
	 * @param requestOrderId
	 * @param bankCode
	 * @param bankAccount
	 * @param bankCardType
	 * @param validDate
	 * @param cvnCode
	 * @param idType
	 * @param idNumber
	 * @param name
	 * @param mobilePhone
	 * @param isNeedBind
	 * @param userIdIdentity
	 * @param randomValidateId
	 * @param randomCode
	 * @param tradeId
	 * @return
	 */
	public Request getFengPayFirst(String requestOrderId, String bankCode,
			String bankAccount, String bankCardType, String validDate,
			String cvnCode, String idType, String idNumber, String name,
			String mobilePhone, String isNeedBind, String userIdIdentity,
			String randomValidateId, String randomCode, String tradeId) {

		PhonePayFirstParser paser = new PhonePayFirstParser();
		String body = createFengPayFirst(requestOrderId, bankCode, bankAccount,
				bankCardType, validDate, cvnCode, idType, idNumber, name,
				mobilePhone, isNeedBind, userIdIdentity, randomValidateId,
				randomCode, tradeId);
		String xmlBody = makeXml(body, "15007");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, paser);
		return request;
	}

	private String createFengPayFirst(String requestOrderId, String bankCode,
			String bankAccount, String bankCardType, String validDate,
			String cvnCode, String idType, String idNumber, String name,
			String mobilePhone, String isNeedBind, String userIdIdentity,
			String randomValidateId, String randomCode, String tradeId) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("requestOrderId", requestOrderId));
		sb.append(makeTag("bankCode", bankCode));
		sb.append(makeTag("bankAccount", bankAccount));
		sb.append(makeTag("bankCardType", bankCardType));
		sb.append(makeTag("validDate", validDate));
		sb.append(makeTag("cvnCode", cvnCode));
		sb.append(makeTag("idType", idType));
		sb.append(makeTag("idNumber", idNumber));
		sb.append(makeTag("name", name));
		sb.append(makeTag("mobilePhone", mobilePhone));
		sb.append(makeTag("isNeedBind", isNeedBind));
		sb.append(makeTag("userIdIdentity", userIdIdentity));
		sb.append(makeTag("randomValidateId", randomValidateId));
		sb.append(makeTag("randomCode", randomCode));
		sb.append(makeTag("tradeId", tradeId));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 
	 * 4.1.10丰付首次快捷支付 储蓄卡（15007）
	 * 
	 * @param requestOrderId
	 * @param bankCode
	 * @param bankAccount
	 * @param bankCardType
	 * @param idType
	 * @param idNumber
	 * @param name
	 * @param mobilePhone
	 * @param isNeedBind
	 * @param userIdIdentity
	 * @param randomValidateId
	 * @param randomCode
	 * @param tradeId
	 * @return
	 */
	public Request getFengPayDepositFirst(String requestOrderId,
			String bankCode, String bankAccount, String bankCardType,
			String idType, String idNumber, String name, String mobilePhone,
			String isNeedBind, String userIdIdentity, String randomValidateId,
			String randomCode, String tradeId) {

		PhonePayDepositFirstParser paser = new PhonePayDepositFirstParser();
		String body = createFengPayDepositFirst(requestOrderId, bankCode,
				bankAccount, bankCardType, idType, idNumber, name, mobilePhone,
				isNeedBind, userIdIdentity, randomValidateId, randomCode,
				tradeId);
		String xmlBody = makeXml(body, "15007");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, paser);
		return request;
	}

	private String createFengPayDepositFirst(String requestOrderId,
			String bankCode, String bankAccount, String bankCardType,
			String idType, String idNumber, String name, String mobilePhone,
			String isNeedBind, String userIdIdentity, String randomValidateId,
			String randomCode, String tradeId) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("requestOrderId", requestOrderId));
		sb.append(makeTag("bankCode", bankCode));
		sb.append(makeTag("bankAccount", bankAccount));
		sb.append(makeTag("bankCardType", bankCardType));
		sb.append(makeTag("idType", idType));
		sb.append(makeTag("idNumber", idNumber));
		sb.append(makeTag("name", name));
		sb.append(makeTag("mobilePhone", mobilePhone));
		sb.append(makeTag("isNeedBind", isNeedBind));
		sb.append(makeTag("userIdIdentity", userIdIdentity));
		sb.append(makeTag("randomValidateId", randomValidateId));
		sb.append(makeTag("randomCode", randomCode));
		sb.append(makeTag("tradeId", tradeId));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.11丰付非首次快捷支付（15008）
	 */
	public Request getFengPayNotFirst(String requestOrderId, String bankCode,
			String bindId, String userIdIdentity, String randomValidateId,
			String randomCode, String tradeId) {
		PhonePayNotFirstParser parser = new PhonePayNotFirstParser();
		String body = createFengPayNotFirst(requestOrderId, bankCode, bindId,
				userIdIdentity, randomValidateId, randomCode, tradeId);
		String xmlBody = makeXml(body, "15008");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createFengPayNotFirst(String requestOrderId,
			String bankCode, String bindId, String userIdIdentity,
			String randomValidateId, String randomCode, String tradeId) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("requestOrderId", requestOrderId));
		sb.append(makeTag("bankCode", bankCode));
		sb.append(makeTag("bindId", bindId));
		sb.append(makeTag("userIdIdentity", userIdIdentity));
		sb.append(makeTag("randomValidateId", randomValidateId));
		sb.append(makeTag("randomCode", randomCode));
		sb.append(makeTag("tradeId", tradeId));

		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.12丰付首次快捷支付短信验证 信用卡（15009）
	 * 
	 * @param requestOrderId
	 * @param bankCode
	 * @param bankAccount
	 * @param validDate
	 * @param bankCardType
	 * @param cvnCode
	 * @param idType
	 * @param idNumber
	 * @param name
	 * @param mobilePhone
	 * @param userIdIdentity
	 * @return
	 */
	public Request getFengMessagePayFirst(String requestOrderId,
			String bankCode, String bankAccount, String validDate,
			String bankCardType, String cvnCode, String idType,
			String idNumber, String name, String mobilePhone,
			String userIdIdentity) {

		PhoneMessageFirstParser paser = new PhoneMessageFirstParser();
		String body = createFengMessagePayFirst(requestOrderId, bankCode,
				bankAccount, validDate, bankCardType, cvnCode, idType,
				idNumber, name, mobilePhone, userIdIdentity);
		String xmlBody = makeXml(body, "15009");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, paser);
		return request;
	}

	private String createFengMessagePayFirst(String requestOrderId,
			String bankCode, String bankAccount, String validDate,
			String bankCardType, String cvnCode, String idType,
			String idNumber, String name, String mobilePhone,
			String userIdIdentity) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("requestOrderId", requestOrderId));
		sb.append(makeTag("bankCode", bankCode));
		sb.append(makeTag("bankAccount", bankAccount));
		sb.append(makeTag("bankCardType", bankCardType));
		sb.append(makeTag("validDate", validDate));
		sb.append(makeTag("cvnCode", cvnCode));
		sb.append(makeTag("idType", idType));
		sb.append(makeTag("idNumber", idNumber));
		sb.append(makeTag("name", name));
		sb.append(makeTag("mobilePhone", mobilePhone));
		sb.append(makeTag("userIdIdentity", userIdIdentity));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 
	 * 4.1.12丰付首次快捷支付短信验证 储蓄卡（15009）
	 * 
	 * @param requestOrderId
	 * @param bankCode
	 * @param bankAccount
	 * @param bankCardType
	 * @param idType
	 * @param idNumber
	 * @param name
	 * @param mobilePhone
	 * @param userIdIdentity
	 * @return
	 */
	public Request getFengMessagePayDepositFirst(String requestOrderId,
			String bankCode, String bankAccount, String bankCardType,
			String idType, String idNumber, String name, String mobilePhone,
			String userIdIdentity) {

		PhoneMessageDepositFirstParser paser = new PhoneMessageDepositFirstParser();
		String body = createFengMessagePayDepositFirst(requestOrderId,
				bankCode, bankAccount, bankCardType, idType, idNumber, name,
				mobilePhone, userIdIdentity);
		String xmlBody = makeXml(body, "15009");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, paser);
		return request;
	}

	private String createFengMessagePayDepositFirst(String requestOrderId,
			String bankCode, String bankAccount, String bankCardType,
			String idType, String idNumber, String name, String mobilePhone,
			String userIdIdentity) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("requestOrderId", requestOrderId));
		sb.append(makeTag("bankCode", bankCode));
		sb.append(makeTag("bankAccount", bankAccount));
		sb.append(makeTag("bankCardType", bankCardType));

		sb.append(makeTag("idType", idType));
		sb.append(makeTag("idNumber", idNumber));
		sb.append(makeTag("name", name));
		sb.append(makeTag("mobilePhone", mobilePhone));
		sb.append(makeTag("userIdIdentity", userIdIdentity));
		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}

	/**
	 * 4.1.13丰付非首次快捷支付短信验证（15010）
	 * 
	 * @param requestOrderId
	 * @param bankCode
	 * @param bindId
	 * @param userIdIdentity
	 * @return
	 */
	public Request getFengMessagePayNotFirst(String requestOrderId,
			String bankCode, String bindId, String userIdIdentity) {
		PhoneMessageNotFirstParser parser = new PhoneMessageNotFirstParser();
		String body = createFengMessagePayNotFirst(requestOrderId, bankCode,
				bindId, userIdIdentity);
		String xmlBody = makeXml(body, "15010");

		Request request = new Request(
				ServerInterfaceDefinition.OPT_GETLOTTERYINFO, xmlBody, parser);
		return request;
	}

	private String createFengMessagePayNotFirst(String requestOrderId,
			String bankCode, String bindId, String userIdIdentity) {
		StringBuilder sb = new StringBuilder();

		sb.append("<body>");
		sb.append("<elements>");
		sb.append("<element>");
		sb.append(makeTag("requestOrderId", requestOrderId));
		sb.append(makeTag("bankCode", bankCode));
		sb.append(makeTag("bindId", bindId));
		sb.append(makeTag("userIdIdentity", userIdIdentity));

		sb.append("</element>");
		sb.append("</elements>");
		sb.append("</body>");

		return sb.toString();
	}
}
