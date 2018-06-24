package com.am.cs12.commu.protocolSate.tianHong.$COUT;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocolSate.DataSate;
import com.am.cs12.commu.protocolSate.tianHong.CodeTH;

public class Report_COUT {
	
	private static Logger log = LogManager.getLogger(Report_COUT.class);
	
	/**
	 * 分析数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public DataSate parseData(byte[] b) throws Exception {
		String s = new String(b) ;
		//$COUT,0,1,0,218701,0,140,5,45667,?
		log.info("卫星上报数据转成ASCII：" + s) ;

		
		String[] ss = s.split(",") ;

		
		DataSate d = new DataSate() ;
		
		d.setCode(CodeTH.$COUT) ;
		
		d.setIdSate(ss[4]) ;
		
		String lenStr = ss[7] ;
		
		int len = Integer.parseInt(lenStr) ;
		
		int dataSite = 0 ;
		for(int i = 0 ; i <= 7 ; i++){
			dataSite += ss[i].length() + 1 ;
		}
		
		byte[] db = new byte[len] ;
		int count = 0 ;
		for(int i = dataSite ; i < dataSite + len ; i++){
			db[count++] = b[i] ;
		}

		
		Data_COUT dd = new Data_COUT() ;
		d.setSubData(dd) ;

		dd.setData(db) ;
		
		log.info("分析卫星终端上报测控数据:" + d.getSubData().toString());
		return d;
	}


}
