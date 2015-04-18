package com.xmtq.lottery.utils;

import android.text.TextPaint;
import android.widget.TextView;

public class MainUtil {
	/**
	 * 设置字体为黑体
	 * @param tv
	 */
	public static void setBold(TextView tv){
		TextPaint tp = tv.getPaint(); 
		tp.setFakeBoldText(true);
	}

}
