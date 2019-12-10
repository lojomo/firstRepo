package com.suncreate.system.common;


import java.util.ArrayList;
import java.util.List;

public final class StringUtil implements java.io.Serializable {
	private static final long serialVersionUID = -6797551630821181968L;

	public static boolean isNullOrBlank(String value) {
		return value == null || "".equals(value.trim());
	}

	public static boolean isNotEmpty(String value) {
		return !isNullOrBlank(value);
	}

	/**
	 * 将字符串按照指定的间隔符组成list
	 * @param str
	 * @param split
	 * @return
	 */
	public static List<String> strToList(String str, String split) {
		String[] strs = {};
		if (str != null && !str.equals("")) {
			strs = str.split(split);
		}
		List tokenList = new ArrayList();
		for (int i = 0; i < strs.length; i++) {
			String temp = strs[i];
			if (temp != null) {
				temp = strs[i].trim();
				if (!temp.equals("")) {
					tokenList.add(temp);
				}
			}
		}
		return tokenList;
	}

	/**
	 * 获取Long型主键值
	 * @return
	 */
	public static synchronized long getLongId() {
		return System.nanoTime();
	}
	
	/**
	 * 获取String 型主键值
	 * @return
	 */
	public static String getStringId(){
		return String.valueOf(getLongId());
	}
}