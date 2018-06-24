package com.am.cs12.commu.protocolSate.tianHong;

import com.am.cs12.commu.protocolSate.* ;
import com.am.cs12.commu.protocolSate.tianHong.$CASS.Answer_CASS;
import com.am.cs12.commu.protocolSate.tianHong.$COUT.Report_COUT;
import com.am.cs12.commu.protocolSate.tianHong.$TSTA.Answer_TSTA;
import com.am.cs12.commu.protocolSate.tianHong.$TINF.Answer_TINF;


public class DataProtocol {
	
	private static final int dataMinLen = 8 ;//卫星终端数据最小长度(功能码长(5)+校验合长(1)+数据结尾长(2))

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
		byte crc = b[b.length - 3] ;
		int crcData = 0 ;
		for(int i = 0 ; i < (b.length - 3) ; i++){
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
		String[] ss = s.split(",") ;
		return ss[0] ;
	}
	
	/**
	 * 分析数据---卫星终端收到命令应答
	 * @param b
	 * @throws Exception 
	 */
	public DataSate parse_CASS(byte[] b) throws Exception{
		DataSate d = new Answer_CASS().parseData(b) ;
		return d ;
	}
	/**
	 * 分析数据---卫星状态
	 * @param b
	 * @throws Exception 
	 */
	public DataSate parse_TSTA(byte[] b) throws Exception{
		DataSate d = new Answer_TSTA().parseData(b) ;
		return d ;
	}
	/**
	 * 分析数据---卫星上报测控数据
	 * @param b
	 * @throws Exception 
	 */
	public DataSate parse_COUT(byte[] b) throws Exception{
		DataSate d = new Report_COUT().parseData(b) ;
		return d ;
	}
	/**
	 * 分析数据---卫星授时
	 * @param b
	 * @throws Exception 
	 */
	public DataSate parse_TINF(byte[] b) throws Exception{
		DataSate d = new Answer_TINF().parseData(b) ;
		return d ;
	}

}
