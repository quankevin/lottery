package com.xmtq.lottery.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class SDcardUtil {

	/**
	 * 判断是否插入SD卡，SD卡是否可用
	 * 
	 * @return
	 */
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)
				&& Environment.getExternalStorageDirectory().canWrite()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 文件目录：如sensecp/pic
	 * 
	 * @param fileDir
	 * @return
	 */
	public static String getSDPath(String fileDir) {
		File sdcardDir = Environment.getExternalStorageDirectory();
		String path = sdcardDir.getParent() + "/" + sdcardDir.getName();
		final String fileDirectory = path + "/" + fileDir;
		return fileDirectory;
	}

	/**
	 * 保存图片到SD卡�?
	 * 
	 * @param bitmap
	 * @param fileDir
	 *            如：sensecp/pic
	 * @param fileName
	 *            如：123.png
	 */
	public static void saveBitmapToSDCard(final Bitmap bitmap,
			final String fileDir, final String fileName,
			final CompressFormat format) {
		/**
		 * �?���?��异步线程去保存图�?
		 */
		new Thread() {

			@Override
			public void run() {
				super.run();

				// 如果没有SD卡就不去保存图片
				if (!isHasSdcard()) {
					return;
				}
				// 如果有SD卡就将图片保存在SD卡里�?
				String fileDirectory = getSDPath(fileDir);
				FileOutputStream fos = null;
				try {
					// 创建图片的目�?
					File dir = new File(fileDirectory);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					// 创建图片的文�?
					File file = new File(fileDirectory, fileName);
					if (file.exists()) {
						file.delete();
					}
					file.createNewFile();
					fos = new FileOutputStream(file);
					bitmap.compress(format, 100, fos);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.flush();
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}

	/**
	 * 判断SD卡上的文件是否存�?
	 * 
	 * @param fileDir
	 *            文件目录 如：sensecp/pic
	 * @param fileName
	 *            文件�?如：123.png
	 * @return
	 */
	public static boolean isFileOnSDCard(String fileDir, String fileName) {
		/**
		 * 如果连SD卡都没有就别提还存文件了
		 */
		if (!isHasSdcard()) {
			return false;
		}

		String fileDirectory = getSDPath(fileDir);
		// 创建图片的文�?
		File file = new File(fileDirectory, fileName);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

}
