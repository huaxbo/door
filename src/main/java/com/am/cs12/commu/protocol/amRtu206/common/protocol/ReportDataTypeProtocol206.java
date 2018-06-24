package com.am.cs12.commu.protocol.amRtu206.common.protocol;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.cs12.commu.protocol.amRtu206.common.data.*;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class ReportDataTypeProtocol206 {
	
	private static final Logger log = LogManager.getLogger(ReportDataTypeProtocol206.class.getName() ) ;
	
	private ReportDataType206 dataType ;
	
	public ReportDataType206 getDataType() {
		return dataType;
	}

	/**
	 * 分析数据种类部分
	 */
	public void parse(byte[] b , int index) throws Exception {
		int n = index ; 

		UtilProtocol u = new UtilProtocol() ;
		// 分析数据域
		this.dataType = new ReportDataType206() ;
		int h = u.byte2PlusInt(b[n++]) ;
		int h1 = (h & 0x80) >> 7 ;
		int h2 = (h & 0x40) >> 6 ;
		int h3 = (h & 0x20) >> 5 ;
		int h4 = (h & 0x10) >> 4 ;
		int h5 = (h & 0x8) >> 3 ;
		int h6 = (h & 0x4) >> 2 ;
		int h7 = (h & 0x2) >> 1 ;
		int h8 = (h & 0x1) ;
		this.dataType.setYuLiang(h1) ;//置“1”为主动上报雨量数据，清“0”为不上报雨量数据；
		this.dataType.setShuiWei(h2) ;//置“1”为主动上报水位数据，清“0”为不上报水位数据；
		this.dataType.setLiuLiang(h3) ;//置“1”为主动上报流量数据，清“0”为不上报流量数据；
		this.dataType.setLiuSu(h4) ;//置“1”为主动上报流速数据，清“0”为不上报流速数据；
		this.dataType.setZhaWei(h5) ;//置“1”为主动上报闸位数据，清“0”为不上报闸位数据；
		this.dataType.setGongLu(h6) ;//置“1”为主动上报功率数据，清“0”为不上报功率数据；
		this.dataType.setQiYa(h7) ;//置“1”为主动上报气压数据，清“0”为不上报气压数据；
		this.dataType.setFengSu(h8) ;//置“1”为主动上报风速（风向）数据，清“0”为不上报风速数据；
		
		h = u.byte2PlusInt(b[n++]) ;
		int h9 = (h & 0x80) >> 7 ;
		int h10 = (h & 0x40) >> 6 ;
		int h11 = (h & 0x20) >> 5 ;
		int h12 = (h & 0x10) >> 4 ;
		int h13 = (h & 0x8) >> 3 ;
		int h14 = (h & 0x4) >> 2 ;
//		int h15 = (h & 0x2) >> 1 ;
		this.dataType.setShuiWen(h9) ;//置“1”为主动上报水温数据，清“0”为不上报水温数据；
		this.dataType.setShuiZhi(h10) ;//置“1”为主动上报水质数据，清“0”为不上报水质数据；
		this.dataType.setTuRang(h11) ;//置“1”为主动上报土壤含水率数据，清“0”为不上报土壤含水率数据；
		this.dataType.setZhengFa(h12) ;//置“1”为主动上报蒸发量数据，清“0”为不上报蒸发量数据；
		this.dataType.setBaoJing(h13) ;//置“1”为上报报警数据，清“0”为不上报报警数据；
		this.dataType.setShuiYa(h14) ;//置“1”为上报水压数据，清“0”为不上报水压数据；


		log.info("\n分析得上报数据的种类：\n" + this.dataType.toString() );
	}

}
