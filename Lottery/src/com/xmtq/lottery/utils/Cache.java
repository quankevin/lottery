package com.xmtq.lottery.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.text.TextUtils;

public class Cache {
	public static final String TAG = "Cache";

	public static boolean writeCacheData(String filePath, String data) {
		if (TextUtils.isEmpty(data)) {
			return false;
		}

		File txt = new File(filePath);
		FileOutputStream fos = null;
		boolean success = false;

		if (txt != null) {
			try {
				fos = new FileOutputStream(txt);
				fos.write(data.getBytes());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null) {
						fos.close();

						success = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return success;
	}

	public static String getCacheData(String filePath) {

		File txt = new File(filePath);

		if (!txt.exists()) {
			return null;
		}

		StringBuffer sBuffer = new StringBuffer();
		String line = null;
		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(txt)));

			while ((line = bufferedReader.readLine()) != null) {
				sBuffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return sBuffer.toString();
	}
}
