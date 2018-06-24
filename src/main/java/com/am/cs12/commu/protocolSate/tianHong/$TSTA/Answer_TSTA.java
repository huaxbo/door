package com.am.cs12.commu.protocolSate.tianHong.$TSTA;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocolSate.DataSate;
import com.am.cs12.commu.protocolSate.tianHong.CodeTH;
import com.am.cs12.commu.protocolSate.tianHong.util.Constant;
import com.am.cs12.commu.protocolSate.tianHong.common.* ;


public class Answer_TSTA {
	
	private static Logger log = LogManager.getLogger(Answer_TSTA.class);
	
	/**
	 * 分析数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public DataSate parseData(byte[] b) throws Exception {
		DataSate d = new DataSate() ;
		
		d.setCode(CodeTH.$TSTA) ;
		
		Data_TSTA dd = new Data_TSTA() ;
		d.setSubData(dd) ;

		int n = Constant.Bits_Code + 1 ;
		dd.setChannelOneEleLevel((int)b[n]) ;
		n += 2 ;
		dd.setChannelTwoEleLevel((int)b[n]) ;
		n += 2 ;
		n += 2 ;
		n += 2 ;
		n += 2 ;
		n += 2 ;
		if(b[n] == 0){
			dd.setPowerStatus(false) ;
		}else{
			dd.setPowerStatus(true) ;
		}
		n += 2 ;

		d.setIdSate(new IdProtocol().parseId(b, n)) ;
		dd.setThisSateId(d.getIdSate()) ;
		n += 7 ;
		n += 3 ;
		
		dd.setServerFrequency((b[n++]-48) * 10 + (b[n++] - 48)) ;
		
		log.info("分析查询卫星终端状态应答:" + d.getSubData().toString());
		return d;
	}

}
