package com.xmtq.lottery.utils;

import java.io.File;

import android.os.Environment;

public class FileUtil {

	public static String getFileCacheDirectory() {
		File sdcardDir = Environment.getExternalStorageDirectory();
		String fileDirectory = "/mnt/sdcard";
		if (null != sdcardDir) {
			fileDirectory = sdcardDir.getAbsolutePath() + "/shafamovie/apk";
		}
		return fileDirectory;
	}
}
