package com.xmtq.lottery.utils;

import java.security.MessageDigest;

/**
 * @author linan
 * @version 创建时间：2012-8-20 下午5:07:27 类说明
 */
public class CrcUtil {

	public static String getCrc(String timeStamp, String IMEI, String UID,
			String passwordWithMd5, String infoString) throws Exception {

		String crc = MD5(timeStamp + IMEI + UID + passwordWithMd5 + infoString);

		return crc;

	}

	public static String MD5(String source) throws Exception {
		String resultHash = null;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] result = new byte[md5.getDigestLength()];
		md5.reset();
		md5.update(source.getBytes("UTF-8"));
		result = md5.digest();

		StringBuffer buf = new StringBuffer(result.length * 2);

		for (int i = 0; i < result.length; i++) {
			int intVal = result[i] & 0xff;
			if (intVal < 0x10) {
				buf.append("0");
			}
			buf.append(Integer.toHexString(intVal));
		}

		resultHash = buf.toString();

		return resultHash.toString();

	}

}
