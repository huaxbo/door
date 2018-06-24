package com.am.cs12.commu.protocol.amRtu206.common.protocol;

import com.am.cs12.commu.protocol.amRtu206.common.data.*;
import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.util.DateTime;

public class DateTimeProtocol206 {
	
	private DateTime206 dateTime ;
	
	public DateTime206 getDateTime() {
		return dateTime;
	}

	/**
	 * 分析日期时间部分
	 */
	public void parse(byte[] b , int index) throws Exception {
		int n = index ; 
		dateTime = new DateTime206() ;
		UtilProtocol u = new UtilProtocol() ;

		int year = u.BCD2Int(b, n, n++) ;
		int month = u.BCD2Int(b, n, n++) ;
		int date = u.BCD2Int(b, n, n++) ;
		int hour = u.BCD2Int(b, n, n++) ;
		int minute = u.BCD2Int(b, n, n++) ;
		int second = u.BCD2Int(b, n, n++) ;


		String clock1 = DateTime.yyyy().substring(0,2) + (year < 10 ? ("0" + year) : year) + "-" +
		(month < 10 ? ("0" + month) : month) + "-" +
		(date < 10 ? ("0" + date) : date) + " " +
		(hour < 10 ? ("0" + hour) : hour) + ":" + 
		(minute < 10 ? ("0" + minute) : minute) ;
		
		String clock2 = clock1 + ":" +  
		(second < 10 ? ("0" + second) : second) ;
		
		dateTime.setClock(clock2) ;
		dateTime.setMeterClock_hour(hour) ;
		dateTime.setClockDifference_minute(DateTime.minutesBetweenyyyy_MM_dd_HH_mm(clock1, DateTime.yyyy_MM_dd_HH_mm())) ;
	}

}
