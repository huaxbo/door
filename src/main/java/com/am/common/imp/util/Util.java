package com.am.common.imp.util;

public class Util {

	/**
	 * 判断是否是数字
	 * 
	 * @param str String
	 * @return boolean
	 */
	public boolean isIntNumber(String str) {
		// 判断是否是数字
		if(str == null || str.trim().equals(""))
			return false ;
		
		if(str.startsWith("-")){
			str = str.substring(1 , str.length()) ;
		}
		for (int i = 0; i < str.length(); i++) {
			if (new String("9876543210").indexOf(str.substring(i, i + 1)) == -1) {
				return false;
			}
		}
		return true;
	}


}
