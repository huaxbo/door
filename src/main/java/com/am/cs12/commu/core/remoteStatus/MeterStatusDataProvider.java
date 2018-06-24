package com.am.cs12.commu.core.remoteStatus;

import java.util.HashMap;

import com.am.cs12.util.AmConstant;

public class MeterStatusDataProvider {

	/**
	 * 查询是否在线
	 * @param rtuId
	 * @return
	 */
	public boolean isOnLine(String rtuId){
		HashMap<String , MeterStatus> map = MeterStatusManager.id_status ;
		MeterStatus vo = map.get(rtuId) ;
		if(vo != null){
			if(vo.onLining){
				return true ;
			}
		}
		return false ;
	}
	
	/**
	 * 查询最近测控终端的工作模式
	 * @param rtuId
	 * @return
	 */
	public Integer lastWorkModel(String rtuId){
		HashMap<String , MeterStatus> map = MeterStatusManager.id_status ;
		MeterStatus vo = map.get(rtuId) ;
		if(vo != null && vo.workModel != null && !vo.workModel.equals("")){
			if(vo.workModel.equals(AmConstant.workModel_str_0)){
				return AmConstant.workModel_int_0 ;
			}else if(vo.workModel.equals(AmConstant.workModel_str_1)){
				return AmConstant.workModel_int_1 ;
			}else if(vo.workModel.equals(AmConstant.workModel_str_2)){
				return AmConstant.workModel_int_2 ;
			}else if(vo.workModel.equals(AmConstant.workModel_str_3)){
				return AmConstant.workModel_int_3 ;
			}
		}
		return null ;
	}
	
	/**
	 * 所有测控终端状态
	 */
	public HashMap<String , MeterStatus> meterStatuses(){
		return MeterStatusManager.id_status ;
	}
	/**
	 * 一个测控终端状态
	 */
	public MeterStatus meterStatus(String rtuId){
		return MeterStatusManager.id_status.get(rtuId) ;
	}
}
