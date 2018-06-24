package com.am.cs12.commu.protocol.amRtu206.common;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.common.data.DateTime206;
import com.am.cs12.commu.protocol.amRtu206.common.protocol.DateTimeProtocol206;

public class ProtocolAbstract {
	
	protected long clockDifference_minute ;//测控器时钟与本地时钟差值(分钟)
	protected int meterClock_hour ;//当前测控器时钟(时)
	
	public int getMeterClock_hour() {
		return meterClock_hour;
	}
	public long getClockDifference_minute_abs() {
		clockDifference_minute = clockDifference_minute < 0 ?(-clockDifference_minute):clockDifference_minute ;
		return clockDifference_minute;
	}
	
	/**
	 * 分析数据 RTU ID
	 * @param d
	 * @param b
	 * @param cp
	 * @throws Exception
	 */
//	protected void parseId(Data d , byte[] b, ControlProtocol cp)throws Exception {
//		//分析ID
//		int fromSite = Constant.Site_RTUID ;
//		if(cp.hasDIVS){
//			this.hasDIVS = true ;
//			fromSite += 1 ;
//		}else{
//			this.hasDIVS = false ;
//		}
//		String rtuId = new RtuIdProtocol().parseRtuId(b, fromSite) ;
//		d.setId(rtuId);
//
//	}
	/**
	 * 分析数据 的功能码
	 * @param d
	 * @param b
	 * @param cp
	 */
//	protected void parseDataCode(Data d , byte[] b, ControlProtocol cp)throws Exception {
//		//分析功能码
//		int fromSite = Constant.Site_Code ;
//		if(cp.hasDIVS){
//			fromSite += 1 ;
//		}
//		String code = new CodeProtocol().parseCode(b, fromSite) ;
//		d.setCode(code) ;
//	}
	/**
	 * 分析日期时间部分
	 */
	protected void parseTime(Data d , byte[] b , int index) throws Exception {
		
		DateTimeProtocol206 pro = new DateTimeProtocol206() ;
		pro.parse(b, index) ;
		DateTime206 dateTime = pro.getDateTime() ;
		
		d.setClock(dateTime.getClock()) ;
		this.meterClock_hour = dateTime.getMeterClock_hour() ;
		this.clockDifference_minute = dateTime.getClockDifference_minute() ;
	}



}
