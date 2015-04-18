package com.xmtq.lottery.utils;

import android.content.Context;

public class DensityUtil {

	/**
	 * 根据手机的分辨率�?dp 的单�?转成�?px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率�?px(像素) 的单�?转成�?dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @date 2013年7月23日
	 * @param context
	 * @return
	 */
	public static int getWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @date 2013年7月23日
	 * @param context
	 * @return
	 */
	public static int getHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}
}