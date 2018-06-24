package com.am.cs12.commu.protocol.util;

import com.am.util.NumberUtil;

public class UtilProtocol {
	/**
	 * 字节转存二进制
	 * 
	 * @param b
	 *            byte
	 * @throws Exception
	 * @return String
	 */
	public String byte2Binary(byte b) throws Exception {
		int n = (b + 256) % 256 + 256;
		try {
			return Integer.toBinaryString(n).substring(1);
		} catch (Exception e) {
			throw new Exception("字节转换成二进制的字符串出错！", null);
		}
	}

	/**
	 * 字节转存8位二进制
	 * 
	 * @param b
	 *            byte
	 * @throws Exception
	 * @return String
	 */
	public String byte2bit8Binary(byte b) throws Exception {
		String s = this.byte2Binary(b);
		int len = s.length();
		for (int i = 0; i < 8 - len; i++) {
			s = "0" + s;
		}
		return s;
	}

	/**
	 * 字节转成字符
	 * 
	 * @param b
	 * @return
	 */
	public char byte2char(byte b) {
		return (char) byte2PlusInt(b);
	}

	/**
	 * 字节转成字符串
	 * 
	 * @param b
	 * @return
	 */
	public String bytes2chars2String(byte[] b, int start, int end) {
		String s = "";
		for (int i = start; i <= end; i++) {
			s += this.byte2char(b[i]);
		}
		return s.trim();
	}

	/**
	 * 字节转成无符号整形数字 限正整数
	 * 
	 * @param b
	 * @return
	 */
	public int byte2PlusInt(byte b) {
		int n = (b + 256) % 256;
		return n;
	}
	
	public int byte2Int(byte b) {
		return b ;
	}

	public int bytes2Int(byte[] src, int start, int length) {
		int value = 0;
		for (int i = start; i < start + length; i++) {
			value = (value << 8) ^ (src[i] & 0xFF);
		}
		return value;
	}
	/**
	 * 有符号转换整数
	 * @param str
	 * @param start
	 * @param length
	 * @return
	 */
	public int bytes2IntSign(byte[] str,int start,int length){
		int flag = 1;
		if((str[start] & (byte)0x80) == (byte)0x80){
			for(int i = 0;i<length;i++){
				str[start + i] = (byte)~str[start + i];	
			}
			str[start + length -1] += 1;
			flag = -1;
		}
		
		return flag * bytes2Int(str,start,length);
	}
	
	public int bytes2Int(byte[] src, int start, int length,String desc) {
		if(desc!= null && desc.equals("DESC")){
			int value = 0;
			for (int i = (start + length-1); i >= start; i--) {
				value = (value << 8) ^ (src[i] & 0xFF);
			}
			return value;
		}else{
			int value = 0;
			for (int i = start; i < start + length; i++) {
				value = (value << 8) ^ (src[i] & 0xFF);
			}
			return value;
		}
	}
	/**
	 * byte数组转Integer
	 * @param src
	 * @param start
	 * @param length
	 * @return
	 */
	public Integer bytes2Int2(byte[] src, int start, int length) {
		int value = 0;
		for (int i = start; i < start + length; i++) {
			value = (value << 8) ^ (src[i] & 0xFF);
		}
		//无效数据判断。全FF视为无效
		String st = "";
		for(int i=0;i<length;i++){
			st += "ff";
		}
		if(st.equals(Integer.toHexString(value))){
			return null;
		}
		
		return value;
	}
	public long byte2Long(byte b) {
		return bytes2Long(new byte[] { b }, 0, 1);
	}

	public long bytes2Long(byte[] src, int start, int length) {
		long value = 0;
		for (int i = start; i < start + length; i++) {
			value = (value << 8) ^ (src[i] & 0xFF);
		}
		return value;
	}
	
	public long bytes2Long(byte[] src, int start, int length,String desc) {
		if(desc!= null && desc.equals("DESC")){
			long value = 0;
			for (int i = (start + length-1); i >= start; i--) {
				value = (value << 8) ^ (src[i] & 0xFF);
			}
			return value;
		}else{
			long value = 0;
			for (int i = start; i < start + length; i++) {
				value = (value << 8) ^ (src[i] & 0xFF);
			}
			return value;
		}
	}


	/**
	 * 字节数组转换成十六进制的字符串 
	 *  converts a byte array to a hex string consisting of
	 * two-digit hex values for each byte in the array
	 * 
	 * @param b byte[]
	 * @param hasBlank 16进制是否用空格分隔
	 * @return String
	 */
	public String byte2Hex(byte[] b, boolean hasBlank) throws Exception {
		String rString = "";
		String temp = "";
		try {
			for (int i = 0; i < b.length; i++) {
				int c = b[i];
				temp = Integer.toHexString(c & 0XFF);
				if (temp.length() == 1) {
					temp = "0" + temp;
				}
				if(hasBlank){
					if (i == 0) {
						rString += temp;
					} else {
						rString += " " + temp;
					}
				}else{
					rString += temp;
				}
			}
		} catch (Exception e) {
			throw new Exception("字节数组转换成十六进制的字符串出错！", null);
		}
		return rString;
	}

	/**
	 * 字符转成字节
	 * 
	 * @param c
	 * @return
	 */
	public byte char2byte(char c) {
		return (byte) c;
	}
	
	
	public static void main(String[] args) throws Exception{
		UtilProtocol up = new UtilProtocol() ;
		byte[] bt = up.int2bytes(1536);
		System.out.println("fe");
//		byte[] b  = {0x7,0x1,0x0,0x0};
//		System.out.println(up.BCD2Int(b,0,2,"DESC"));
//		System.out.println(up.BCD2Int(b,1,1));
//		byte b = (byte)255;
//		int n = (b + 256) % 256 + 256;
//		try {
//			System.out.println(Integer.toBinaryString(n).substring(1));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		/*System.out.println("b:=="+up.byte2Binary(b[0])+" "+up.byte2Binary(b[1])+" "+up.byte2Binary(b[2]));
		
		int v=up.bytes2Int(b, 0, 4);
		System.out.println("int:="+v);
		byte[] rlt=up.int2bytes(v);
		System.out.println("rlt:=="+up.byte2Binary(rlt[0])+" "+up.byte2bit8Binary(rlt[1])+" "+up.byte2Binary(rlt[2]));
*/
	}
	
	/**
	 * 十进制转换成字节数组
	 * 
	 * @param s  String
	 * @return byte[]
	 */		
	public byte[] int2bytes(int value) {
		int len = 4 ;
		byte[] b = new byte[len]; 
		for(int i = b.length-1; i > -1; i--){ 
           b[i] = (byte)(value & 0xff); 
           value = value >> 8; 
   		} 
		
		int count = 0 ;
		for(int i = 0 ; i < b.length ; i++){
			if(b[i] == 0){
				count++ ;
			}else{
				break ;
			}
		}
		if(count == len){
			count = len - 1 ;
		}
		byte[] bb = new byte[len - count] ;
		int index = 0 ;
		for(int i = count; i < len ; i++){
			bb[index++] = b[i] ;
		}
		return  bb; 
	}
	/**
	 * 转成有符号型
	 * 
	 * @param l
	 * @return
	 */
	public byte[] long2bytes(long value) {
		int len = 8 ;
		byte[] b = new byte[len]; 
		for(int i = b.length-1; i > -1; i--){ 
           b[i] = (byte)(value & 0xff); 
           value = value >> 8; 
   		} 
		int count = 0 ;
		for(int i = 0 ; i < b.length ; i++){
			if(b[i] == 0){
				count++ ;
			}else{
				break ;
			}
		}
		if(count == len){
			count = len - 1 ;
		}
		byte[] bb = new byte[len - count] ;
		int index = 0 ;
		for(int i = count; i < len ; i++){
			bb[index++] = b[i] ;
		}
		return  bb; 
	}
	
	/**
	 * 字符串型数字转成byte
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public byte string2byte(String s) throws Exception {
		int n = 0;
		try {
			n = Integer.parseInt(s);
		} catch (Exception e) {
			throw new Exception("字符串型数字字节时出错，不是合法数字:" + s, null);
		}
		return (byte) n;
	}
	/**
	 * 字符串转成字符的字节数组
	 * 
	 * @param s
	 * @return
	 */
	public byte[] string2chars2bytes(String s) {
		s = s.trim();
		byte[] b = new byte[s.length()];
		for (int i = 0; i < b.length; i++) {
			b[i] = this.char2byte(s.charAt(i));
		}
		return b;
	}


	/**
	 * 
	 * @param s
	 * @return
	 */
	public int string2Int(String s) throws Exception {
		if (!NumberUtil.isPlusIntNumber(s)) {
			throw new Exception("将十进制字符串数转成十进制数出错，不是合法数字:" + s, null);
		}
		return Integer.parseInt(s);
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public double string2Double(String s) {
		double d = Double.parseDouble(s);
		d = Math.round(d * 100) / (double) 100;
		return d;
	}
	/**
	 * 十六进制串转字节数组
	 * @param src
	 * @return
	 */
	public byte[] hex2Bytes(String src) {
		if(src == null || src.length()%2 != 0){
			return null ;
		}
		byte[] ret = new byte[src.length()/2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < ret.length ; ++i) {
			ret[i] = this.uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	private byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 | _b1);
		return ret;
	}
	
	/**
	 * 将字十六进制符串数转成十进制数
	 * 
	 * @param s String
	 * @return int
	 */
	public int hex2Int(String s) throws Exception {
		int result = 0;
		for (int i = 0; i < s.length(); i++) {
			result += Math.pow(16, i)
					* this.hex2Int(s.charAt(s.length() - 1 - i));
		}
		return result;
	}
	/**
	 * 十六进制数据转在十进制数
	 * 
	 * @param c char
	 * @return int
	 */
	public int hex2Int(char c) throws Exception {
		if (c == '0')
			return 0;
		if (c == '1')
			return 1;
		if (c == '2')
			return 2;
		if (c == '3')
			return 3;
		if (c == '4')
			return 4;
		if (c == '5')
			return 5;
		if (c == '6')
			return 6;
		if (c == '7')
			return 7;
		if (c == '8')
			return 8;
		if (c == '9')
			return 9;
		if (c == 'a' || c == 'A')
			return 10;
		if (c == 'b' || c == 'B')
			return 11;
		if (c == 'c' || c == 'C')
			return 12;
		if (c == 'd' || c == 'D')
			return 13;
		if (c == 'e' || c == 'E')
			return 14;
		if (c == 'f' || c == 'F')
			return 15;
		throw new Exception("不是合法的十六进制数！", null);
	}
	
	

	/**
	 * BCD编码转成整型
	 * 
	 * @param b
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws Exception
	 */
	public int BCD2Int(byte b) throws Exception {
		String str = "";
		str = this.decodeBCD(new byte[]{b}, 0, 1);
		int n = 0;
		try {
			n = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), null);
		}
		return n;
	}
	/**
	 * BCD编码转成整型
	 * 
	 * @param b
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws Exception
	 */
	public int BCD2Int(byte[] b, int startIndex, int endIndex) throws Exception {
		String str = "";
		str = this.decodeBCD(b, startIndex, endIndex - startIndex + 1);
		int n = 0;
		try {
			n = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), null);
		}
		return n;
	}
	/**
	 * BCD编码转成整型
	 * 
	 * @param b
	 * @param startIndex
	 * @param length
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int BCD2Int(byte[] b, int startIndex, int length,String order) throws Exception {
		String str = "";
		if(order != null && order.equals("DESC")){
			for(int i=length -1;i >= 0;i--){
				str += this.decodeBCD(b, startIndex+i, 1);
			}
		}
		int n = 0;
		try {
			n = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), null);
		}
		return n;
	}
	/**
	 * BCD编码转成字符串
	 * 
	 * @param b
	 * @param startIndex
	 * @param length
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public String BCD2String(byte[] b, int startIndex, int length,String order) throws Exception {
		String str = "";
		if(order != null && order.equals("DESC")){
			for(int i=length -1;i >= 0;i--){
				str += this.decodeBCD(b, startIndex+i, 1);
			}
		}
		return str;
	}
	/**
	 * BCD编码转成Long
	 * 
	 * @param b
	 * @param startIndex
	 * @param length
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public long BCD2Long(byte[] b, int startIndex, int length,String order) throws Exception {
		String str = "";
		if(order != null && order.equals("DESC")){
			for(int i=length -1;i >= 0;i--){
				str += this.decodeBCD(b, startIndex+i, 1);
			}
		}
		if(str.startsWith("aa") && str.endsWith("aa"))//主动上报数据域中全是aa时，代表异常值
		   return -9999;//异常值
		long n = 0;
		try {
			n = Long.parseLong(str);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), null);
		}
		return n;
	}
	/**
	 * BCD编码转成字符串型
	 * 
	 * @param b
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws Exception
	 */
	public long BCD2Long(byte[] b, int startIndex, int endIndex)
			throws Exception {
		String str = "";
		str = this.decodeBCD(b, startIndex, endIndex - startIndex + 1);
		long n = 0;
		try {
			n = Long.parseLong(str);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), null);
		}
		return n;
	}

	/**
	 * BCD编码转成字符串型
	 * 
	 * @param b
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws Exception
	 */
	public String BCD2String(byte[] b, int startIndex, int endIndex)
			throws Exception {
		String str = "";
		str = this.decodeBCD(b, startIndex, endIndex - startIndex + 1);
		return str;
	}


	/**
	 * 整形转成BCD编码
	 * 
	 * @param l
	 * @return
	 */
	public byte[] int2BCD(int i) {
		String str = "" + i;
		byte[] b = null;
		if (str.length() % 2 == 0) {
			b = new byte[str.length() / 2];
		} else {
			b = new byte[(str.length() / 2) + 1];
		}
		this.encodeBCD(str, b, 0, b.length);

		return b;
	}
	/**
	 * 长整形转成BCD编码
	 * 
	 * @param l
	 * @return
	 */
	public byte[] long2BCD(long l) {
		String str = "" + l;
		byte[] b = null;
		if (str.length() % 2 == 0) {
			b = new byte[str.length() / 2];
		} else {
			b = new byte[(str.length() / 2) + 1];
		}
		this.encodeBCD(str, b, 0, b.length);

		return b;
	}
	/**
	 * 字符串型数字转成BCD编码
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public byte[] string2BCD(String s) throws Exception {
		if (!NumberUtil.isPlusIntNumber(s)) {
			throw new Exception("字符串型数字转成BCD编码时出错，不是合法数字:" + s, null);
		}
		long l = 0l;
		try {
			l = Long.parseLong(s);
		} catch (Exception e) {
			throw new Exception("字符串型数字转成BCD编码时出错，不是合法数字:" + s, null);
		}
		return this.long2BCD(l);
	}

	/**
	 * 
	 * @param value
	 * @param dest
	 * @param startIndex
	 * @param length
	 */
	private void encodeBCD(String value, byte[] dest, int startIndex, int length) {
		if (value == null || !value.matches("\\d*")) {
			throw new java.lang.IllegalArgumentException();
		}
		int[] tmpInts = new int[2 * length];
		int index = value.length() - 1;
		for (int i = tmpInts.length - 1; i >= 0 && index >= 0; i--, index--) {
			tmpInts[i] = value.charAt(index) - '0';
		}
		for (int i = startIndex, j = 0; i < startIndex + length; i++, j++) {
			dest[i] = (byte) (tmpInts[2 * j] * 16 + tmpInts[2 * j + 1]);
		}
	}

	/**
	 * 
	 * @param src
	 * @param startIndex
	 * @param length
	 * @return
	 */
	public String decodeBCD(byte[] src, int startIndex, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = startIndex; i < startIndex + length; i++) {
			int value = (src[i] + 256) % 256;
			if(value>=170)
			{
				return "aa";//170在16十六进制下的值，代表异常值
			}
			sb.append((char) (value / 16 + '0')).append(
					(char) (value % 16 + '0'));
			value++;
		}
		String result = sb.toString();
		if (!result.matches("\\d*")) {
			throw new java.lang.IllegalArgumentException();
		}
		return result;
	}
	

}
