package com.am.licence.key;

import java.util.*;
import java.text.SimpleDateFormat;

public class Util {
	public Util() {
	}

	public static String getYear() {
		return new SimpleDateFormat("yyyy").format(new Date(System
				.currentTimeMillis()));
	}

	/**
	 * 字节数组转换成十六进制的字符串 converts a byte array to a hex string consisting of
	 * two-digit hex values for each byte in the array
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 */
	public static String byte2Hex(byte[] b) {
		String rString = "";
		String temp = "";
		for (int i = 0; i < b.length; i++) {
			int c = b[i];
			// c += 128;
			// temp = Integer.toHexString(c) + "";
			temp = Integer.toHexString(c & 0XFF) + "";
			if (temp.length() == 1) {
				temp = "0" + temp;
			}
			rString += temp;
		}
		return rString;
	}

	/**
	 * 十六进制的字符串转换成字节数组
	 * 
	 * @param s
	 *            String
	 * @return byte[]
	 */
	public static byte[] hex2Byte(String s) {

		int len = s.length() / 2;

		int b = 0;
		int e = 2;
		String[] st = new String[len];
		for (int i = 0; i < len; i++) {
			st[i] = s.substring(b, e);
			b += 2;
			e += 2;
		}

		byte[] bb = new byte[len];
		int i = 0;
		for (int j = 0; j < len; j++) {
			String token = st[j];
			int tokenValue = hex2Int(token);
			// byte byteValue = (byte) (tokenValue - 128);
			byte byteValue = (byte) (tokenValue);
			bb[i] = byteValue;
			i++;
		}

		return bb;
	}

	// converts a two digit hex string to a byte
	private static int hex2Int(String s) {
		int result = 0;
		for (int i = 0; i < s.length(); i++) {
			result += Math.pow(16, i) * hex2Int(s.charAt(s.length() - 1 - i));
		}
		return result;
	}

	// converts the hex base to integer values.
	private static int hex2Int(char c) {
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

		System.out.println("should not occur! " + c);

		return -1;

	}

}
