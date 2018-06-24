package com.am.cs12.config;

import java.util.*;

import com.am.cs12.commu.protocol.DriverMeter;
import com.am.cs12.commu.protocol.amRtu206.Driver206;
import com.am.cs12.util.AmConstant;

public class MeterHelp {
	
	private String error ;
	
	public String getError() {
		return error;
	}
	
	/**
	 * 得到配置
	 * @param id
	 * @return
	 */
	public MeterVO getMeter(String id){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		return meters.get(id);
	}
	/**
	 * 得到测控终端的手机号
	 * @param id
	 * @return
	 */
	public String getMeterPhone(String id){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		MeterVO m = meters.get(id);
		if(m != null){
			return m.phone ;
		}
		return "" ;
	}
	/**
	 * 得到测控终端的卫星用户ID
	 * @param id
	 * @return
	 */
	public String getMeterSatelliteId(String id){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		MeterVO m = meters.get(id);
		if(m != null){
			return m.satelliteId ;
		}
		return "" ;
	}
	/**
	 * 得到测控终端的卫星协议名称
	 * @param id
	 * @return
	 */
	public String getMeterSatelliteProtocol(String id){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		MeterVO m = meters.get(id);
		if(m != null){
			return m.sateProtocol ;
		}
		return "" ;
	}
	/**
	 * 得到测控终端是否常在线
	 * @param id
	 * @return
	 */
	public Boolean getMeterOnlineAlways(String id){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		MeterVO m = meters.get(id);
		if(m != null){
			return m.onlineAlways ;
		}
		return false ;
	}
	/**
	 * 通过手机号得到测控终端ID
	 * @param phone
	 * @return
	 */
	public String getIdByPhone(String phone){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		Set<Map.Entry<String, MeterVO>> dset = meters.entrySet() ;
		Iterator<Map.Entry<String, MeterVO>> dit = dset.iterator() ;
		Map.Entry<String, MeterVO> dentry = null ;
		MeterVO meter = null ;
		while(dit.hasNext()){
			dentry = dit.next() ;
			meter = dentry.getValue() ;
			if(meter.phone.equals(phone)){
				return meter.id ;
			}
		}
		return null ;
	}
	
	/**
	 * 通过卫星ID得到测控终端ID
	 * @param satelliteId
	 * @return
	 */
	public String getIdBySatelliteId(String satelliteId){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		Set<Map.Entry<String, MeterVO>> dset = meters.entrySet() ;
		Iterator<Map.Entry<String, MeterVO>> dit = dset.iterator() ;
		Map.Entry<String, MeterVO> dentry = null ;
		MeterVO meter = null ;
		while(dit.hasNext()){
			dentry = dit.next() ;
			meter = dentry.getValue() ;
			if(meter.satelliteId.equals(satelliteId)){
				return meter.id ;
			}
		}
		return null ;
	}
	
	/**
	 * 查看配置中是否已经存在此 ID
	 * @param id
	 * @return
	 */
	public boolean hasId(String id){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		if(meters.containsKey(id)){
			return true ;
		}
		return false ;
	}
	/**
	 * 查看配置中是否已经存在此 ID
	 * @param id
	 * @param excludeRtuId
	 * @return
	 */
	public boolean hasId(String id , String excludeId){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		Set<Map.Entry<String, MeterVO>> mset = meters.entrySet() ;
		Iterator<Map.Entry<String, MeterVO>> mit = mset.iterator() ;
		Map.Entry<String, MeterVO> dentry = null ;
		MeterVO meter = null ;
		
		while(mit.hasNext()){
			dentry = mit.next() ;
			meter = dentry.getValue() ;
			if(meter.id.equals(excludeId)){
				continue ;
			}
			if(meter.id.equals(id)){
				return true ;
			}
		}
		return false ;
	}
	

	/**
	 * 查看配置中是否已经存在此 手机号phone
	 * @param phone
	 * @param excludeId
	 * @return
	 */
	public boolean hasPhone(String phone , String excludeId){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		Set<Map.Entry<String, MeterVO>> dset = meters.entrySet() ;
		Iterator<Map.Entry<String, MeterVO>> dit = dset.iterator() ;
		Map.Entry<String, MeterVO> dentry = null ;
		MeterVO meter = null ;
		while(dit.hasNext()){
			dentry = dit.next() ;
			meter = dentry.getValue() ;
			if(excludeId != null){
				if(meter.id.equals(excludeId)){
					continue ;
				}
			}
			if(meter.phone.equals(phone)){
				return true ;
			}
		}
		return false ;
	}
	
	/**
	 * 查看配置中是否已经存在此 卫星用户ID
	 * @param satelliteId
	 * @param excludeId
	 * @return
	 */
	public boolean hasSatelliteId(String satelliteId , String excludeId){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		Set<Map.Entry<String, MeterVO>> dset = meters.entrySet() ;
		Iterator<Map.Entry<String, MeterVO>> dit = dset.iterator() ;
		Map.Entry<String, MeterVO> dentry = null ;
		MeterVO meter = null ;
		while(dit.hasNext()){
			dentry = dit.next() ;
			meter = dentry.getValue() ;
			if(excludeId != null){
				if(meter.id.equals(excludeId)){
					continue ;
				}
			}
			if(meter.satelliteId.equals(satelliteId)){
				return true ;
			}
		}
		return false ;
	}
	
	/**
	 * 判断是否包含通道
	 * @param id
	 * @param channel
	 * @return
	 */
	public boolean containChannel(String id, String channel){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		MeterVO vo = meters.get(id) ;
		String[] channels = vo.channels ;
		for(int i = 0 ; i < channels.length ; i++){
			if(channels[i].equals(channel)){
				return true ;
			}
		}
		return false ;
	}
	
	
	
	/**
	 * 添加测控终端
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
	public MeterVO addMeter(
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
		MeterVO meterVo = new MeterVO() ;

		meterVo.id = id ;
		meterVo.phone = phone ;
		meterVo.satelliteId = satelliteId ;

		meterVo.rtuProtocol = rtuProtocol ;
		meterVo.rtuKey = AmConstant.rtuDefaultPasswordKey ;
		meterVo.rtuPass = AmConstant.rtuDefaultPassword ;
		meterVo.sateProtocol = sateProtocol ;
		
		meterVo.channels = channels.split(",") ;
		meterVo.mainChannel = mainChannel ;
		meterVo.comId = comId ;
		
		meterVo.dataTo = dataTo.split(",") ;
		meterVo.onlineAlways = Boolean.parseBoolean(onlineAlways) ;
		
		
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		meters.put(id, meterVo) ;
		
		return meterVo ;
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
	public MeterVO editMeter(
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
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;

		MeterVO meterVo = meters.get(oldId) ;
		if(meterVo == null ){
			error = "出错，未得到ID=" + oldId + "测控器配置！"  ;
			return null ;
		}

		meterVo.id = id ;
		meterVo.phone = phone ;
		meterVo.satelliteId = satelliteId ;
		
		meterVo.rtuProtocol = rtuProtocol ;
		meterVo.rtuKey = AmConstant.rtuDefaultPasswordKey ;
		meterVo.rtuPass = AmConstant.rtuDefaultPassword ;
		meterVo.sateProtocol = sateProtocol ;
		
		meterVo.channels = channels.split(",") ;
		meterVo.mainChannel = mainChannel ;
		meterVo.comId = comId ;
		
		meterVo.dataTo = dataTo.split(",") ;
		meterVo.onlineAlways = Boolean.parseBoolean(onlineAlways) ;
		
		
		if(!oldId.equals(id)){
			meters.remove(oldId) ;
			meters.put(id, meterVo) ;
		}
		
		return meterVo ;
	}
	/**
	 * 删除测控终端
	 * @param rtuId
	 */
	public void deleteMeter(String rtuId) {
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		meters.remove(rtuId) ;
	}

	/**
	 * 更新 ID
	 */
	public boolean updateMeterId(String id, String newId) {
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		if(meters == null || meters.size() == 0){
			error = "出错，未得到测控器配置集合！" ;
			return false ;
		}
		MeterVO meter = meters.get(id) ;
		if(meter == null ){
			error = "出错，未得到 ID=" + id + "测控器配置！"  ;
			return false ;
		}
		meter.id = newId ;
		meters.put(newId, meter) ;
		meters.remove(id) ;
		return true ;
	}
	
	/**
	 * 得到RTU协议配置对象
	 * @param id
	 * @return
	 */
	public RTUProtocolVO getRtuProtocolVO(String id ){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		if(meters == null || meters.size() == 0){
			error = "出错，未得到测控器配置集合！" ;
			return null ;
		}
		MeterVO meter = meters.get(id) ;
		if(meter == null ){
			error = "出错，未得到ID=" + id + "测控器配置！" ;
			return null ;
		}
		Map<String, RTUProtocolVO> rtups = cc.getRtuProtocolMap() ;
		if(rtups == null || rtups.size() == 0){
			error = "出错，未得到RTU协议配置集合！" ;
			return null ;
		}
		RTUProtocolVO rtuvo = rtups.get(meter.rtuProtocol) ;
		if(rtuvo == null ){
			error = "出错，未得到名称为" + meter.rtuProtocol + " RTU协议配置！" ;
			return null ;
		}
		return rtuvo ;
	}
	
	/**
	 * 得到RTU 驱动 
	 * @param id
	 * @param id
	 * @return
	 */
	public DriverMeter getRtuDriver(String id){
		/*RTUProtocolVO rtuvo = this.getRtuProtocolVO(id) ;
		DriverMeter driver = null ;
		if(rtuvo != null){
			driver = this.initRtuDriver(rtuvo.driverName, rtuvo.name) ;
			if(driver == null ){
				error = "出错，未得到名称为" + rtuvo.driverName + " RTU协议驱动实例！" ;
				return null ;
			}
		}
		return driver ;*/
		
		return initRtuDriver(Driver206.class.getName(),"RTU_206");
	}
	
	
	/**
	 * 得到RTU 密钥与密码
	 * @param id
	 * @return
	 */
	public Integer[] getKeyPassword(String id){
		ConfigCenter cc = ConfigCenter.instance() ;
		Map<String, MeterVO> meters = cc.getId_meterMap() ;
		if(meters == null || meters.size() == 0){
			error = "出错，未得到测控器配置集合！" ;
			return null ;
		}
		MeterVO meter = meters.get(id) ;
		if(meter == null ){
			error = "出错，未得到ID=" + id + "测控器配置！" ;
			return null ;
		}
		return new Integer[]{meter.rtuKey , meter.rtuPass} ;
	}
	/**
	 * 初始化协议驱动类
	 * @param filePath
	 * @param vo
	 * @param RTUs
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	private DriverMeter initRtuDriver(String clazz , String protocolName) throws IllegalArgumentException {
		try {
			Class c = Class.forName(clazz);
			if (c == null) {
				throw new IllegalArgumentException("不能实例化协议驱动类" + clazz + "！");
			}
			return (DriverMeter) c.getDeclaredConstructor(String.class).newInstance(protocolName);
		} catch (Exception e) {
			throw new IllegalArgumentException("不能实例化协议驱动类" + clazz + "！");
		}
	}

}
