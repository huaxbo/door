package com.am.cs12.commu.protocolSate.tianHong.$CASS;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocolSate.DataSate ;
import com.am.cs12.commu.protocolSate.tianHong.CodeTH;
import com.am.cs12.commu.protocolSate.tianHong.util.Constant;

public class Answer_CASS {
	private static Logger log = LogManager.getLogger(Answer_CASS.class);
	/**
	 * 分析数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public DataSate parseData(byte[] b) throws Exception {
		DataSate d = new DataSate() ;
		
		d.setCode(CodeTH.$CASS) ;
		
		Data_CASS dd = new Data_CASS() ;
		d.setSubData(dd) ;

		int n = b[Constant.Bits_Code + 1] ;
		if(n == '1'){
			dd.setReceivedSuccess(true) ;
		}else if(n == '0'){
			dd.setReceivedSuccess(false) ;
		}
		
		log.info("分析卫星终端接收外设命令情况:" + d.getSubData().toString());
		return d;
	}
}
