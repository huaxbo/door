package com.am.util;


public class NumberUtil {
	/**
	 * 判断是否是整数
	 * 
	 * @param str String
	 * @return boolean
	 */
	public static boolean isIntNumber(String str) {
		// 判断是否是数字
		if(str == null || str.trim().equals(""))
			return false ;
		
		if(str.startsWith("-")){
			str = str.substring(1 , str.length()) ;
		}
		String token = new String("9876543210") ;
		for (int i = 0; i < str.length(); i++) {
			if (token.indexOf(str.substring(i, i + 1)) == -1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断是否是整数
	 * 
	 * @param str String
	 * @return boolean
	 */
	public static boolean isHex(String str) {
		// 判断是否是数字
		if(str == null || str.trim().equals(""))
			return false ;
		if(str.length()%2 != 0)
			return false ;
		
		String token = new String("9876543210abcdefABCDEF") ;
		for (int i = 0; i < str.length(); i++) {
			if (token.indexOf(str.substring(i, i + 1)) == -1) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 字节数组转换成十六进制的字符串
	 * converts a byte array to a hex string consisting of
	 * two-digit hex values for each byte in the array
	 * @param b byte[]
	 * @return String
	 */
	public static String byte2Hex(byte[] b) throws Exception {
		String rString = "";
		String temp = "";
		try {
			for (int i = 0; i < b.length; i++) {
				int c = b[i];
				temp = Integer.toHexString(c & 0XFF) + "";
				if (temp.length() == 1) {
					temp = "0" + temp;
				}
				rString += " " + temp;
			}
		} catch (Exception e) {
			throw new Exception("字节数组转换成十六进制的字符串出错！", null);
		}
		return rString;
	}

	/**
	 * 判断是否是正整数
	 * @param str String
	 * @return boolean
	 */
	public static boolean isPlusIntNumber(String str) {
		// 判断是否是数字
		if(str == null || str.trim().equals("")){
			return false ;
		}
		int n = 0 ;
		String token = new String("9876543210") ;
		for (int i = n; i < str.length(); i++) {
			if (token.indexOf(str.substring(i, i + 1)) == -1) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 判断是否是数字
	 * @param str String
	 * @return boolean
	 */
	public static boolean isDoubleNumber(String str) {
		// 判断是否是数字
		if(str == null || str.trim().equals("")){
			return false ;
		}
		String token = new String("9876543210.-") ;
		for (int i = 0; i < str.length(); i++) {
			if (token.indexOf(str.substring(i, i + 1)) == -1) {
				return false;
			} 
		} 
		if(str.startsWith(".") || str.endsWith(".")){
			return false ;
		}else{
			if(str.indexOf('.') != str.lastIndexOf('.')){
				return false ;
			}
		}
		if(str.startsWith("-")){
			if(str.indexOf('-') != str.lastIndexOf('-')){
				return false ;
			}
		}
		return true;
	}
	
	/**
	 * 浮点数据四舍五入
	 * @param d
	 * @param scale
	 * @return
	 */
	public static Double roundDouble(Double d , int scale){
		if(d == null){
			return null ;
		}
		String temp = "##." ;
		for(int i = 0 ; i < scale ; i++){
			temp += "0" ;
		}
		return Double.valueOf(new java.text.DecimalFormat(temp).format(d)) ;
	}
	
	/**
	 * 浮点数据四舍五入
	 * @param d
	 * @param scale
	 * @return
	 */
	public static String roundDoubleStr(Double d , int scale){
		if(d == null){
			return null ;
		}
		String temp = "##." ;
		for(int i = 0 ; i < scale ; i++){
			temp += "0" ;
		}
		return String.valueOf(new java.text.DecimalFormat(temp).format(d)) ;
	}
	/**
	 * 冒泡排序，
	 * @param values 要排序的数组
	 * @param up true 从小到大  false 从大到小
	 * @return
	 */
	public static int[] sort(int[]  values , boolean up){
		boolean changed = false ;
		int temp;
		int end = 0 ;
		for (int i = 0; i < values.length; ++i) {
			changed = false ;
			end++ ;
			for (int j = 0; j < values.length - end ; ++j) {
				if(up){
					if (values[j] > values[j + 1]) {
						temp = values[j];
						values[j] = values[j + 1];
						values[j + 1] = temp;
						changed = true ;
					}
				}else{
					if (values[j] < values[j + 1]) {
						temp = values[j];
						values[j] = values[j + 1];
						values[j + 1] = temp;
						changed = true ;
					}
				}
			}
			if(!changed){
				break ;
			}
		}
		return values ;
	}
	/**
	 * 把数据前面的0去掉
	 * 
	 * @param s String
	 * @return String
	 */
	public String clean0FromStrNum(String s) {
		while (s.charAt(0) == '0' && s.length() > 1) {
			s = s.substring(1);
		}
		return s;
	}

	/**
	 * 去掉小数点
	 * 
	 * @param value String
	 * @return String
	 */
	public String cleanDotNumber(String value) {
		int dot = value.indexOf('.');
		if (dot < 0) {
			return value;
		}
		if (value.length() - dot - 1 > 2) {
			value = value.substring(0, dot);
		}
		return value;
	}
	

}
