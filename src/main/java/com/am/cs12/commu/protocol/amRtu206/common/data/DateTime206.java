package com.am.cs12.commu.protocol.amRtu206.common.data;


public class DateTime206 {

	
	protected String clock ;//测控器数据中的日期与时间
	protected int meterClock_hour ;//当前测控器时钟(时)
	protected long clockDifference_minute ;//测控器时钟与本地时钟差值(分钟)
	
	public String getClock() {
		return clock;
	}
	public void setClock(String clock) {
		this.clock = clock;
	}
	public int getMeterClock_hour() {
		return meterClock_hour;
	}
	public void setMeterClock_hour(int meterClockHour) {
		meterClock_hour = meterClockHour;
	}
	public long getClockDifference_minute() {
		return clockDifference_minute;
	}
	public void setClockDifference_minute(long clockDifferenceMinute) {
		clockDifference_minute = clockDifferenceMinute;
	}
	
	
}
