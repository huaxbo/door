package com.am.cs12.commu.protocolSate.tianHong.$TINF;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocolSate.DataSate;
import com.am.cs12.commu.protocolSate.tianHong.CodeTH;

public class Answer_TINF {
	private static Logger log = LogManager.getLogger(Answer_TINF.class);
	
	/**
	 * 分析数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public DataSate parseData(byte[] b) throws Exception {
		String s = new String(b) ;
		//$TINF,11:38:03.00,?
		log.info("卫星授时应答转成ASCII：" + s) ;

		String[] s1 = s.split(",") ;
		String[] ss = s1[1].split(":") ;

		
		DataSate d = new DataSate() ;
		
		d.setCode(CodeTH.$TINF) ;
		
		Data_TINF dd = new Data_TINF() ;
		
		d.setSubData(dd) ;
		
		dd.setHour(ss[0]) ;
		dd.setMinute(ss[1]) ;
		dd.setSecond(ss[2].substring(0,2)) ;
		
		
		log.info("分析卫星终端授时数据:" + d.getSubData().toString());
		return d;
	}
}

