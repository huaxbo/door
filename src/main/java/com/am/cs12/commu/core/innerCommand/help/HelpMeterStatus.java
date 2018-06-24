package com.am.cs12.commu.core.innerCommand.help;

import java.util.HashMap;

import com.am.cs12.commu.core.remoteStatus.MeterStatus;
import com.am.cs12.commu.core.remoteStatus.MeterStatusDataProvider;
import com.am.cs12.commu.protocol.amLocal.MeterStatuses;

public class HelpMeterStatus {

	/**
	 * 得到所有测控终端的状态
	 * @return
	 */
	public MeterStatuses allMeterStatuses(){
		MeterStatuses ms = new MeterStatuses() ;
		ms.setMeterStatusMap(new MeterStatusDataProvider().meterStatuses()) ;
		return ms ;
	}
	/**
	 * 得到一些测控终端的状态
	 * @return
	 */
	public MeterStatuses someMeterStatuses(String[] rtuIds){
		HashMap<String, MeterStatus> map = new HashMap<String, MeterStatus>() ;
		if(rtuIds != null){
			for(int i = 0 ; i < rtuIds.length ; i++){
				map.put(rtuIds[i], new MeterStatusDataProvider().meterStatus(rtuIds[i])) ;
			}
		}
		MeterStatuses ms = new MeterStatuses() ;
		ms.setMeterStatusMap(map) ;
		return ms ;
	}
}
