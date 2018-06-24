package com.am.cs12.commu.protocolSate.beiDou;

import com.am.cs12.commu.protocolSate.* ;
import com.am.cs12.commu.protocolSate.beiDou.$BBXX.Answer_BBXX;
import com.am.cs12.commu.protocolSate.beiDou.$DWXX.Answer_DWXX;
import com.am.cs12.commu.protocolSate.beiDou.$FKXX.Answer_FKXX;
import com.am.cs12.commu.protocolSate.beiDou.$ICXX.Answer_ICXX;
import com.am.cs12.commu.protocolSate.beiDou.$SJXX.Answer_SJXX;
import com.am.cs12.commu.protocolSate.beiDou.$TXXX.Answer_TXXX;
import com.am.cs12.commu.protocolSate.beiDou.$ZJXX.Answer_ZJXX;



public class DataProtocol {
	
	private static final int dataMinLen = 11 ;//卫星终端数据最小长度(指令(5)+长度(2)+用户地址(3)+校验和(1))

	/**
	 * 检验数据合法性
	 * @return
	 */
	public void chechData(byte[] b) throws Exception{
		if(b == null){
			throw new Exception("数据不合法，为空值！") ;
		}
		if(b.length < dataMinLen){
			throw new Exception("数据长度(" + b.length + ")不合法！") ;
		}
		byte crc = b[b.length - 1] ;
		int crcData = 0 ;
		for(int i = 0 ; i < (b.length - 1) ; i++){
			crcData = crcData ^ b[i] ;
		}
		if(crc != (byte)crcData){
			throw new Exception("数据校验合验证失败！") ;
		}
	}
	
	/**
	 * 从数据中得到功能码
	 * @param b
	 * @return
	 */
	public String getCode(byte[] b){
		String s = new String(b) ;
		String c = s.substring(0, 5);
		String[] ss = c.split(",") ;
		return ss[0] ;
	}
	
	/**
	 * 分析数据---版本信息
	 * @param b
	 * @throws Exception 
	 */
//	public DataSate parse_BBXX(byte[] b) throws Exception{
//		DataSate d = new Answer_BBXX().pareData(b) ;
//		return d ;
//	}
	/**
	 * 分析数据---定位信息
	 * @param b
	 * @throws Exception 
	 */
//	public DataSate parse_DWXX(byte[] b) throws Exception{
//		DataSate d = new Answer_DWXX().parseData(b) ;
//		return d ;
//	}
	/**
	 * 分析数据---反馈信息
	 * @param b
	 * @throws Exception 
	 */
//	public DataSate parse_FKXX(byte[] b) throws Exception{
//		DataSate d = new Answer_FKXX().parseData(b) ;
//		return d ;
//	}
	/**
	 * 分析数据---IC信息
	 * @param b
	 * @throws Exception 
	 */
//	public DataSate parse_ICXX(byte[] b) throws Exception{
//		DataSate d = new Answer_ICXX().parseData(b) ;
//		return d ;
//	}
	
	/**
	 * 分析数据---时间信息
	 * @param b
	 * @throws Exception 
	 */
//	public DataSate parse_SJXX(byte[] b) throws Exception{
//		DataSate d = new Answer_SJXX().parseData(b) ;
//		return d ;
//	}
	
	/**
	 * 分析数据---通信信息
	 * @param b
	 * @throws Exception 
	 */
	public DataSate parse_TXXX(byte[] b) throws Exception{
		DataSate d = new Answer_TXXX().parseData(b) ;
		return d ;
	}
	
	/**
	 * 分析数据---自检信息
	 * @param b
	 * @throws Exception 
	 */
//	public DataSate parse_ZJXX(byte[] b) throws Exception{
//		DataSate d = new Answer_ZJXX().parseData(b) ;
//		return d ;
//	}

}
