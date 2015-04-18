package com.xmtq.lottery.utils;

import java.util.ArrayList;
import java.util.List;

import com.xmtq.lottery.bean.PassType;

/**
 * 过关类型--工具类
 * @author smile
 *
 */
public class PassTypeUtil {
	private static String simplePassArray[] = { "单关", "2串1", "3串1", "4串1", "5串1"};
	
	private static String morePassArray[] = { "3串3", "3串4", "4串4", "4串5", "4串6", "4串11",
			"5串5", "5串6", "5串10", "5串16", "5串20", "5串26", "6串6", "6串7",
			"6串15", "6串20", "6串22", "6串35", "6串42", "6串50", "6串57" };
	
	/**
	 * 获取简单过关列表（全部）
	 * 
	 */
	public static List<PassType> getSimplePassList(){
		return getSimplePassList(1);
	}
	
	/**
	 * 获取简单过关列表
	 * @param num 
	 * 说明：通过num参数生成过关列表
	 */
	public static List<PassType> getSimplePassList(int num){
		List<PassType> passList = new ArrayList<PassType>();
		for (int i = 0; i < simplePassArray.length; i++) {
			String name = simplePassArray[i];
			String value = getPassValue(name);
			if(String.valueOf(num).compareTo(value.substring(0,1)) >= 0){
				PassType passType = new PassType();
				passType.setName(name);
				passType.setValue(value);
				passList.add(passType);
			}
		}
		return passList;
	}
	
	/**
	 * 获取复杂过关列表(全部)
	 *
	 */
	public static List<PassType> getMorePassList(){
		return getMorePassList(1);
	}
	
	/**
	 * 获取复杂过关列表
	 * @param num
	 * 说明：通过num参数生成过关列表
	 */
	public static List<PassType> getMorePassList(int num){
		List<PassType> passList = new ArrayList<PassType>();
		for (int i = 0; i < morePassArray.length; i++) {
			String name = morePassArray[i];
			String value = getPassValue(name);
			
			if(String.valueOf(num).compareTo(value.substring(0,1)) >= 0){
				PassType passType = new PassType();
				passType.setName(name);
				passType.setValue(value);
				passList.add(passType);
			}
		}
		return passList;
	}
	
	/**
	 * 转换过关类型
	 * 例如：单关转换为1*1，2串1转换为2*1
	 * @param name
	 * @return
	 */
	private static String getPassValue(String name){
		String value = "";
		if(name.equals("单关")){
			value = "1*1";
		}else{
			value = name.replace("串", "*");
		}
		return value;
	}
	
}
