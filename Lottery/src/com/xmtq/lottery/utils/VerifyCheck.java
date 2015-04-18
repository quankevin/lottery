package com.xmtq.lottery.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证类，用来验证账号，密码，手机号码，邮箱，身份证等
 */
public class VerifyCheck {
	private static int[] idsArray = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3,
			7, 9, 10, 5, 8, 4, 2 };

	/**
	 * 账号是否验证通过 支持2-16位英文�?中文、数字�?下划线�?�?
	 * 
	 * @return
	 */
	public static boolean isAccountVerify(String account) {

		if (account == null || "".equals(account.trim())) {
			return false;
		} else {
			String accountTrim = account.trim();
			Pattern patternAccount = Pattern
					.compile("^([a-zA-Z0-9_.\u4e00-\u9fa5]{2,16})+$");
			Matcher matcherAccount = patternAccount.matcher(accountTrim);
			if (!matcherAccount.matches()) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 验证密码是否通过,支持�?��分特殊字符�?英文字母、数字�?不支持中�? 支持6-16�?
	 */
	public static boolean isPasswordVerify(String passwordString) {
		if (passwordString == null || "".equals(passwordString.trim())) {
			return false;
		} else {
			String passwordTrim = passwordString.trim();
			Pattern patternAccount = Pattern
					.compile("^([a-zA-Z0-9_-`~!@#$%^&*()+\\|\\\\=,./?><\\{\\}\\[\\]]{6,11})+$");
			Matcher matcherAccount = patternAccount.matcher(passwordTrim);
			if (!matcherAccount.matches()) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 验证输入的密码和输入的确认密码是否是�?���?
	 * 
	 * @param pwd
	 *            当前密码
	 * @param confirmPwd
	 *            确认密码
	 * @return
	 */
	public static boolean isPwdEqualsConfirmPwd(String pwd, String confirmPwd) {
		if (pwd.equals(confirmPwd)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证手机号码的格式是否正�?
	 * 
	 * @param mobileString
	 * @return
	 */
	public static boolean isMobilePhoneVerify(String mobileString) {
		if (mobileString == null || "".equals(mobileString.trim())) {
			return false;
		} else {
			String mobileTrim = mobileString.trim();
			Pattern patternMobile = Pattern.compile("^1[0-9]{10}$");
			Matcher matcherMobile = patternMobile.matcher(mobileTrim);
			if (!matcherMobile.matches()) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 验证邮箱格式是否正确
	 * 
	 * @param emailString
	 * @return
	 */
	public static boolean isEmailVerify(String emailString) {
		if (emailString == null || "".equals(emailString.trim())) {
			return false;
		} else {
			String emailTrim = emailString.trim();
			Pattern patternEmail = Pattern
					.compile("^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$");
			Matcher matcherEmail = patternEmail.matcher(emailTrim);
			if (!matcherEmail.matches()) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 验证真是姓名的格式是否正确，是否是中�?
	 * 
	 * @param realitynameString
	 * @return
	 */
	public static boolean isRealnameVerify(String realitynameString) {
		if (realitynameString == null || "".equals(realitynameString.trim())) {
			return false;
		} else {
			String realitynameTrim = realitynameString.trim();
			Pattern patternRealityname = Pattern
					.compile("[\u4e00-\u9fa5]{2,10}");// 2~10个中文汉�?
			Matcher matcherRealityname = patternRealityname
					.matcher(realitynameTrim);
			if (!matcherRealityname.matches()) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 验证身份证的号码是否是格式正确的
	 * 
	 * @param idCardNumber
	 * @return
	 */
	public static boolean isIDCardVerify(String idCardNumber) {
		if (null == idCardNumber) {
			return false;
		}

		if ("".equals(idCardNumber.trim())) {
			return false;
		}

		String idCardNumberTrim = idCardNumber.trim();
		String idPattern = "^((1[1-5])|(2[1-3])|(3[1-7])|(4[1-6])|(5[0-4])|(6[1-5])|71|(8[12])|91)\\d{4}((19\\d{2}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))|(19\\d{2}(0[13578]|1[02])31)|(19\\d{2}02(0[1-9]|1\\d|2[0-8]))|(19([13579][26]|[2468][048]|0[48])0229))\\d{3}(\\d|X|x)?$";
		Pattern patternId = Pattern.compile(idPattern);// 身份证号�?
		Matcher matcherId = patternId.matcher(idCardNumberTrim);
		if (!matcherId.matches()) {
			return false;
		}

		// 下面是验�?8位身份证的最后一位字�?数字是否正确
		int temp = 0;
		if (idCardNumberTrim.length() == 18) {
			char[] idArray = idCardNumberTrim.toCharArray();

			for (int i = 0; i < idArray.length - 1; i++) {
				String valueOf = String.valueOf(idArray[i]);
				int parseInt = Integer.parseInt(valueOf);
				temp += parseInt * idsArray[i];
			}
			int temp2 = temp % 11;
			String lastChar = "";
			switch (temp2) {
			case 0:
				lastChar = "1";
				break;
			case 1:
				lastChar = "0";
				break;
			case 2:
				lastChar = "X";
				break;
			case 3:
				lastChar = "9";
				break;
			case 4:
				lastChar = "8";
				break;
			case 5:
				lastChar = "7";
				break;
			case 6:
				lastChar = "6";
				break;
			case 7:
				lastChar = "5";
				break;
			case 8:
				lastChar = "4";
				break;
			case 9:
				lastChar = "3";
				break;
			case 10:
				lastChar = "2";
				break;
			}
			char charAtLast = idCardNumberTrim.charAt(17);
			if (!("" + charAtLast).equalsIgnoreCase(lastChar)) {
				return false;
			}
		}
		// 以上条件都不符合才会返回true
		return true;
	}
}
