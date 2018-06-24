package com.am.cs12.commu.protocol.amRtu206.common;

public class CRC16_forHydro {

	/**
	 * 水位监测CRC校验
	 * 
	 * @param b
	 * @param len
	 * @param crc_s
	 * @return
	 */
	public static int cal_crc16(byte[] b) {
		int crc = 0xFFFF;
		for (int k = 0; k < b.length; k++) {
			crc = crc ^ (b[k] & 0xFF);
			for (int i = 0; i < 8; i++) {
				if ((crc & 0x0001) == 0x0001) {
					crc >>= 1;
					crc ^= 0xA001;
				} else {
					crc >>= 1;
				}
			}
		}

		return crc;
	}
	
	public static void main(String[] args){
		byte[] b = new byte[]{};
		int crc = CRC16_forHydro.cal_crc16(b);
		System.out.println(Integer.toHexString(crc));
	}
}
