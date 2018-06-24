package com.am.cs12.commu.core.remoteStatus;

import java.util.*;
import com.am.util.DateTime;

public class MeterStatus {

	// 当前在线情况，只在缓存中有意义，在持久化中不存在
	public boolean onLining;

	// 测控器当前IP，只在缓存中有意义，在持久化中不存在
	public String ip;

	// 测控器当前端口号，只在缓存中有意义，在持久化中不存在
	public int port;
	
	// 下发给测控器命令连续失败次数，只在缓存中有意义，在持久化中不存在
	public int continueFailCommandTimes;
	// 最后收到测控器上传数据时刻，只在缓存中有意义，在持久化中不存在
	public long lastMeterDataMoment;

	// ////////////////////
	// 以下属性在持久化中存在

	// 测控器 ID
	public String id;

	// 测控工作模式
	public String workModel;
	
	public String date ;//日期，以备控制当日数据，例如昨日关闭通信服务器，今日把通信服务器开启，则这时要把当日数据清空

	// 在线时长(单位分钟)
	public long on;
	public long onTd;// td=today
	
	// 不在线时长(单位分钟)
	public long off;
	public long offTd;

	// 成功命令次数
	public long success;
	public long successTd;

	// 失败命令次数
	public long fail;
	public long failTd;

	// 主动上报数据次数
	public long report;
	public long reportTd;
	// 一天内主动上报数据的时刻,不同时刻用@分隔
	// 只记录时分，例如8:30@9:35@11:20等等
	public String reportTime;

	// 通信--入数据累计量(字节)
	public long inTotal;
	public long inTotalTd;

	// 通信--出数据累计量(字节)
	public long outTotal;
	public long outTotalTd;
	
	//发送短信次数(次)
	public long outSmTotal ;
	public long outSmTotalTd ;
	
	//接收短信次数(次)
	public long inSmTotal ;
	public long inSmTotalTd ;
	
	
	//发送卫星数据次数(次)
	public long outSateTotal ;
	public long outSateTotalTd ;
	
	//接收卫星数据次数(次)
	public long inSateTotal ;
	public long inSateTotalTd ;
	
	public String getLastMeterDataMoment(){
		if(lastMeterDataMoment > 0){
			return DateTime.yyyy_MM_dd_HH_mm_ss(new Date(lastMeterDataMoment)) ;
		}
		return "" ;
	}
}
