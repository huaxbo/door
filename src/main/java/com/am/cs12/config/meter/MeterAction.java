package com.am.cs12.config.meter;

import java.util.HashMap;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.cs12.config.*;
import com.am.cs12.commu.core.remoteStatus.MeterStatusManager;
import com.am.cs12.util.AmConstant;

public class MeterAction {
	
	private static Logger log = LogManager.getLogger(MeterAction.class.getName()) ;

	private String error ;
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	
	/**
	 * 更新ID
	 */
	public void updateMeterId(String id, String newId) {
		if(id == null || newId == null){
			log.error("更新测控器配置中的rtuId，但提供的新旧ID中有空值。") ;
			return ;
		}
		if(id.equals(newId)){
			return ;
		}
		MeterHelp mh = new MeterHelp() ;
		if(mh.updateMeterId(id, newId)){
			//更新测控终端配置
			new XMLOrperator().updateMeterId(id, newId, false) ;
			//更新测控终端状态中的数据
			MeterStatusManager.instance().updateMeterId(newId, id) ;
		}else{
			log.error(mh.getError()) ;
		}
	}

	
	/**
	 * 添加新测控终端
	 * @param id
	 * @param phone
	 * @param satelliteId
	 * @param rtuProtocol
	 * @param sateProtocol
	 * @param channels
	 * @param mainChannel
	 * @param comId
	 * @param dataTo
	 * @param onlineAlways
	 * @return
	 */
	public boolean addMeter(
			String id,
			String phone, 
			String satelliteId,
			String rtuProtocol,
			String sateProtocol,
			String channels,
			String mainChannel,
			String comId,
			String dataTo , 
			String onlineAlways
			) {
		if(id == null 
				|| phone == null 
				|| rtuProtocol == null
				|| channels == null 
				|| mainChannel == null 
				|| dataTo == null
				|| onlineAlways == null 
				){
			this.error = "添加测控终端数据不全。" ;
			return false ;
		}

		MeterHelp mh = new MeterHelp() ;
		if(mh.hasId(id)){
			this.error = "此测控终端ID(" + id + ")已经在配置文件存在，不能重复配置!" ;
			return false ;
		}
		if(mh.hasPhone(phone , null)){
			this.error = "此测控终端手机号phone(" + phone + ")已经在配置文件存在，不能重复配置!" ;
			return false ;
		}
		if(satelliteId != null && !satelliteId.equals("") && mh.hasSatelliteId(satelliteId , null)){
			this.error = "此测控终端卫星用户ID(" + satelliteId + ")已经在配置文件存在，不能重复配置!" ;
			return false ;
		}
		if(satelliteId == null || satelliteId.equals("")){
			if(channels.indexOf(AmConstant.channel_serialPort) >= 0){
				this.error = "此测控终端未填写卫星用户ID，但RTU通信通道选择了串口(卫星)，这是不允许的!" ;
				return false ;
			}
		}
		if(channels.indexOf(mainChannel) < 0){
			this.error = "此测控终端选择的主通信通道不在所选择的通信通道中，这是不允许的!" ;
			return false ;
		}
		if(satelliteId != null && !satelliteId.equals("")){
			if(comId == null || comId.equals("")){
				this.error = "此测控终端填写了卫星用户ID，但未填写卫星终端串口，这是不允许的!" ;
				return false ;
			}
		}
		
		if(comId != null && !comId.equals("") && !isExistComId(comId)){
			this.error = "此测控终端ID(" + id + ")的" + comId + "在serialPortServer.xml中未配置！" ;
			return false ;
		}
		
		MeterVO vo = mh.addMeter(id,phone,satelliteId,rtuProtocol,sateProtocol,channels,mainChannel,comId,dataTo,onlineAlways) ;
		if(vo != null){
			boolean flag = true ;
			try{
				new XMLOrperator().addMeter(id,phone,satelliteId,rtuProtocol,sateProtocol,channels,mainChannel,comId,dataTo,onlineAlways,false) ;
			}catch(Exception e){
				this.error = "将添加的测控终端存储到配置文件中出错:" + e.getMessage() ;
				flag = false ;
				return flag ;
			}finally{
				//保存测控终端状态 
				if(flag){
					MeterStatusManager.instance().addMeter(id) ;
				}
			}
		}else{
			this.error = "将添加的测控终端添加到内存中出错" ;
			return false ;
		}
		return true ;
	}
	/**
	 * 修改测控终端
	 * @param oldId
	 * @param id
	 * @param phone
	 * @param satelliteId
	 * @param rtuProtocol
	 * @param sateProtocol
	 * @param channels
	 * @param mainChannel
	 * @param comId
	 * @param dataTo
	 * @param onlineAlways
	 * @return
	 */
	public boolean editMeter(
			String oldId,
			String id,
			String phone, 
			String satelliteId,
			String rtuProtocol,
			String sateProtocol,
			String channels,
			String mainChannel,
			String comId,
			String dataTo , 
			String onlineAlways
			) {
		if(oldId == null 
				|| id == null 
				|| phone == null 
				|| rtuProtocol == null
				|| channels == null 
				|| mainChannel == null 
				|| dataTo == null
				|| onlineAlways == null 
				){
			this.error = "修改测控终端数据不全。" ;
			return false ;
		}
		MeterHelp mh = new MeterHelp() ;
		if(!oldId.equals(id) && mh.hasId(id)){
			this.error = "修改后的测控终端ID(" + id + ")已经在配置文件存在，不能重复配置!" ;
			return false ;
		}
		if(mh.hasPhone(phone , oldId)){
			this.error = "修改后的测控终端手机号phone(" + phone + ")已经在配置文件存在，不能重复配置!" ;
			return false ;
		}

		if(satelliteId != null && !satelliteId.equals("") && mh.hasSatelliteId(satelliteId , oldId)){
			this.error = "此测控终端卫星用户ID(" + satelliteId + ")已经在配置文件存在，不能重复配置!" ;
			return false ;
		}
		if(satelliteId == null || satelliteId.equals("")){
			if(channels.indexOf(AmConstant.channel_serialPort) >= 0){
				this.error = "此测控终端未填写卫星用户ID，但RTU通信通道选择了串口(卫星)，这是不允许的!" ;
				return false ;
			}
		}
		if(channels.indexOf(mainChannel) < 0){
			this.error = "此测控终端选择的主通信通道不在所选择的通信通道中，这是不允许的!" ;
			return false ;
		}

		if(satelliteId != null && !satelliteId.equals("")){
			if(comId == null || comId.equals("")){
				this.error = "此测控终端填写了卫星用户ID，但未填写卫星终端串口，这是不允许的!" ;
				return false ;
			}
		}
		
		if(comId != null && !comId.equals("") && !isExistComId(comId)){
			this.error = "此测控终端ID(" + id + ")的" + comId + "在serialPortServer.xml中未配置！" ;
			return false ;
		}

		MeterVO vo = mh.editMeter(oldId,id,phone,satelliteId,rtuProtocol,sateProtocol,channels,mainChannel,comId,dataTo,onlineAlways) ;
		if(vo != null){
			boolean flag = true ;
			try{
				new XMLOrperator().editMeter(oldId,id,phone,satelliteId,rtuProtocol,sateProtocol,channels,mainChannel,comId,dataTo,onlineAlways, false) ;
			}catch(Exception e){
				this.error = "将修改的测控终端存储到配置文件中出错:" + e.getMessage() ;
				flag = false ;
				return flag ;
			}finally{
				//保存测控终端状态 
				if(flag){
					if(!oldId.equals(id)){
						//保存测控终端状态 
						MeterStatusManager.instance().editMeter(oldId , id) ;
					}
				}
			}
		}else{
			this.error = "将添加的测控终端添加到内存中出错" ;
			return false ;
		}
		return true ;
	}
	/**
	 * 删除测控终端
	 * @param id
	 */
	public boolean deleteMeter(String id) {
		return this.doDeleteMeter(id , true);
	}
	/**
	 * 删除测控终端
	 * @param id
	 */
	private boolean doDeleteMeter(String id , boolean updateMeterStatus) {
		if(id == null){
			this.error = "删除测控终端必须提供ID。" ;
			return false ;
		}

		MeterHelp mh = new MeterHelp() ;
		if(!mh.hasId(id)){
			this.error = "此控终端ID(" + id + ")在配置文件不存在，不能执行删除操作!" ;
			return false ;
		}
		mh.deleteMeter(id) ;
		boolean flag = true ;
		try{
			new XMLOrperator().deleteMeter(id, false) ;
		}catch(Exception e){
			flag = false ;
			this.error = "从配置文件中删除测控终端出错:" + e.getMessage() ;
			return flag ;
		}finally{
			if(flag){
				//保存测控终端状态 
				MeterStatusManager.instance().removeMeter(id) ;
			}
		}
		return true ;
	}
	
	/**
	 * 是否存在测控终端ID这个值的配置
	 * @param id
	 * @return
	 */
	public boolean hasMeterWithId(String Id){
		return new MeterHelp().hasId(Id) ;
	}
	
	public MeterVO getMeter(String rtuId){
		return new MeterHelp().getMeter(rtuId) ;
	}
	/**
	 * 是否存在串中ID
	 * @param comId
	 * @return
	 */
	private boolean isExistComId(String comId){
		HashMap<String, SerialPortVO> map = ConfigCenter.instance().getSerialPortMap() ;
		if (map.containsKey(comId)) {
			return true ;
		}
		return false ;
	}


}
