package com.am.cs12.commu.protocol.amRtu206.common;

import com.am.cs12.commu.protocol.util.CRC8_for206;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class CRCProtocol {
	/**
	 * 生成CRC，并把CRC放入原字节数组中
	 * @param b
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public byte[] bytesCrc_forRtu(byte b[], int startIndex, int endIndex) {
		CRC8_for206 crc8 = new CRC8_for206();
		int crc = crc8.CRC8(b , startIndex, endIndex);
		b[endIndex + 1] = (byte)crc;
		return b;
	}
	/**
	 * 生成CRC，并把两字节数组的CRC返回
	 * @param b
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public byte byteCrc2(byte b[], int startIndex, int endIndex) {
		CRC8_for206 crc8 = new CRC8_for206();
		int crc = crc8.CRC8(b , startIndex, endIndex);
		return (byte)crc;
	}

	/**
	 * 校验和检查
	 * 
	 * @param b
	 * @return
	 */
	public boolean checkCrc(byte[] b, int startIndex, int endIndex, int crcIndex) throws Exception {
		byte crcb = this.byteCrc2(b, startIndex, endIndex);

		if ((crcb == b[crcIndex])) {
			return true;
		} else {
			UtilProtocol u = new UtilProtocol();
			byte[] che = new byte[] {b[crcIndex]};
			String hex = u.byte2Hex(new byte[]{crcb} , true);
			String hexche = u.byte2Hex(che , true);
			throw new Exception("校验和验证失败,放弃无效数据！" + " 计算是" + hex + " 返回是" + hexche, null);
		}
	}


}
