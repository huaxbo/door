package com.am.util;

public class StringUtil {
	/**
	 * 将字符串以逗号分割(逗号可以是中文逗号或英文逗号)
	 * @param s
	 * @return
	 */
	public static String[] splitStringByComma(String str){
		str = str.replaceAll("，", ",") ;
		String[] strg = str.split(",") ;
		if(strg == null){
			return null ;
		}
		for(int i = 0 ; i < strg.length ; i++){
			strg[i] = strg[i].trim() ;
		}
		return strg ;
	}

}
