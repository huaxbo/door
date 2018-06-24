package com.am.cs12.commu.protocol.amRtu206.cd11_51;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.common.*;
import com.am.cs12.commu.protocol.amRtu206.util.*;
import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.util.DateTime;

public class ReadAnswer_51 {

	private static Logger log = LogManager.getLogger(ReadAnswer_51.class);
	
	private long clockDifference_minute ;//测控器时钟与本地时钟差值(分钟)
	private int meterClock_hour ;//当前测控器时钟(时)
	
	public int getMeterClock_hour() {
		return meterClock_hour;
	}
	public long getClockDifference_minute() {
		return clockDifference_minute;
	}

	/**
	 * 分析RTU 数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Data parseData(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception {
		Data d = this.doParse(cp, b) ;
		d.setId(rtuId) ;
		d.setCode(dataCode) ;

		log.info("分析<读取RTU时钟>应答: RTU ID=" + d.getId() + " 时钟：" + d.getClock()  );
		return d;
	}
	/**
	 * 分析RTU 数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private Data doParse(ControlProtocol cp, byte[] b) throws Exception {
		Data d = new Data() ;

		int n = Constant.Site_Data ;
		if(cp.hasDIVS){
			n += 1 ;
		}
		UtilProtocol u = new UtilProtocol() ;
		// 分析数据域
		
//		int month = u.BCD2Int(b, n, n++) ;
		int second = u.BCD2Int(b, n, n++) ;
		int minute = u.BCD2Int(b, n, n++) ;
		int hour = u.BCD2Int(b, n, n++) ;
		int date = u.BCD2Int(b, n, n++) ;

		
		int value = u.byte2PlusInt(b[n++]) ;
		value = value & 31;
		byte[] bb = u.int2bytes(value);
		String str = "";
		str = u.decodeBCD(bb, 0, 1);
		int month = 0;
		try {
			month = Integer.parseInt(str);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), null);
		}
		int year = u.BCD2Int(b, n, n++) ;
		
		String clock1 = DateTime.yyyy().substring(0,2) + (year < 10 ? ("0" + year) : year) + "-" +
		(month < 10 ? ("0" + month) : month) + "-" +
		(date < 10 ? ("0" + date) : date) + " " +
		(hour < 10 ? ("0" + hour) : hour) + ":" + 
		(minute < 10 ? ("0" + minute) : minute) ;
		
		String clock2 = clock1 + ":" +  
		(second < 10 ? ("0" + second) : second) ;
		
		d.setClock(clock2) ;
		
		this.meterClock_hour = hour ;
		
		this.clockDifference_minute = DateTime.minutesBetweenyyyy_MM_dd_HH_mm(clock1, DateTime.yyyy_MM_dd_HH_mm()) ;

		return d;
	}

	public static void main(String asd[]){
		UtilProtocol u = new UtilProtocol() ;
		// 分析数据域
		byte[] b = {0x53};
		
		int value = (short)u.byte2Int(b[0]) ;
		value = value & 31;
		byte[] bb = u.int2bytes(value);
		String str = "";
		str = u.decodeBCD(bb, 0, 1);
		int month = 0;
		try {
			month = Integer.parseInt(str);
		} catch (Exception e) {
		}
		System.out.println(month);
	}
}
