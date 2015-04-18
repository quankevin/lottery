package com.xmtq.lottery.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class UpdateDownUtil {

	public static final int NO_SD_CARD = 1;
	public static final int PROGRESS_UPDATE = 2;
	public static final int PROGRESS_COMPLETE = 3;

	private static int hadDownloadSize;
	private static boolean isAppUpgrading;
	private static boolean cancelDownload = false;

	public static void downLoadNewApp(Handler handler, String downLoadUrl,
			File downloadAppFile) {
		InputStream is = null;
		FileOutputStream fos = null;
		boolean isSDCardExists = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
		// SD卡不存在就不去升级
		if (!isSDCardExists) {
			isAppUpgrading = false;// 下载终止
			handler.sendEmptyMessage(NO_SD_CARD);
			return;
		}
		try {
			if (downloadAppFile.exists()) {
				downloadAppFile.delete();
			}
			File directory = downloadAppFile.getParentFile();
			if (!directory.exists()) {
				directory.mkdirs();
			}
			URL url = new URL(downLoadUrl);
			// 打开连接
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// 获得输入流
			is = connection.getInputStream();
			// 文件总大小
			double fileSize = connection.getContentLength();
			fos = new FileOutputStream(downloadAppFile);
			long notifUpdataPeriod = System.currentTimeMillis();
			byte[] buffer = new byte[1024];
			int len = 0;
			double hasDownLoadFileSize = 0;
			do {
				if ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					hasDownLoadFileSize += len;
					hadDownloadSize = (int) (hasDownLoadFileSize / fileSize * 100);
					// 1s通知2次
					if (System.currentTimeMillis() - notifUpdataPeriod > 500) {
						// 发送消息更新进度条
						Message msg = new Message();
						msg.what = PROGRESS_UPDATE;
						msg.arg1 = hadDownloadSize;
						handler.sendMessage(msg);
						notifUpdataPeriod = System.currentTimeMillis();
					}
				} else {
					/*
					 * 下载完成
					 */
					handler.sendEmptyMessage(PROGRESS_COMPLETE);
					cancelDownload = true;
					fos.flush();
				}
			} while (!cancelDownload);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 下载终止
			isAppUpgrading = false;
			try {
				if (is != null) {
					is.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
