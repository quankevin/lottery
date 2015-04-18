package com.xmtq.lottery.utils;

/**
 * String类型的数字串转换成其他类�?
 */
public class StringConvertUtil {
	/**
	 * 转换成double类型
	 * 
	 * @param number
	 * @return
	 */
	public static double parseStringToDouble(String number) {
		if (null == number || "".equals(number.trim())) {
			return 0;
		}
		// 假如不是数字的字符串就不可能解析成double类型的，会出现异�?
		try {
			double doubleNumber = Double.parseDouble(number);
			return doubleNumber;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 转换成int类型
	 * 
	 * @param number
	 * @return
	 */
	public static int parseStringToInteger(String number) {
		if (null == number || "".equals(number.trim())) {
			return 0;
		}
		// 假如不是数字的字符串就不可能解析成int类型的，会出现异�?
		try {
			int intNumber = Integer.parseInt(number);
			return intNumber;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 转化成float类型
	 * 
	 * @param number
	 * @return
	 */
	public static float parseStringToFloat(String number) {
		if (null == number || "".equals(number.trim())) {
			return 0;
		}
		// 假如不是数字的字符串就不可能解析成float类型的，会出现异�?
		try {
			float floatNumber = Float.parseFloat(number);
			return floatNumber;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 转化成long类型
	 * 
	 * @param number
	 * @return
	 */
	public static long parseStringToLong(String number) {
		if (null == number || "".equals(number.trim())) {
			return 0;
		}
		// 假如不是数字的字符串就不可能解析成long类型的，会出现异�?
		try {
			long longNumber = Long.parseLong(number);
			return longNumber;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
