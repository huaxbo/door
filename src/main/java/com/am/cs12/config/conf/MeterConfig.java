package com.am.cs12.config.conf;

import java.util.*;

import org.jdom.Element;

import com.am.cs12.ServerConfig;
import com.am.cs12.config.*;
import com.am.util.*;
import com.am.cs12.util.*;

public class MeterConfig  extends Config {

	private TreeMap<String, MeterVO> meterMap ;
	
	public MeterConfig(){
		this.meterMap = new TreeMap<String, MeterVO>() ;
	}

	
	/**
	 * 分析测控器配置
	 * 
	 * @throws ACException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, MeterVO> parseOptions(String filePath) throws IllegalArgumentException {
		if(this.doc == null){
			super.createDom(filePath) ;
		}
		if(this.doc == null){
			throw new IllegalArgumentException(filePath + "语法有错误，不能创建xml文件的doc对象!");
		}
		Element root = this.doc.getRootElement();
		if (root == null) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能得到根元素!");
		}
		
		List meterList = root.getChildren(MeterConstant.meter_key);
		if (meterList == null) {
			return this.meterMap ;
		}
		Iterator meterIt = meterList.iterator();
		Element meter = null;
		
		String attValue = null ;
		while (meterIt.hasNext()) {
			meter = (Element)meterIt.next();
			MeterVO meterVo = new MeterVO() ;

			attValue = meter.getAttributeValue(MeterConstant.id_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.id_key + "属性必须配置！");
			}else{
				attValue = attValue.trim() ;
				if(!NumberUtil.isPlusIntNumber(attValue)){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.id_key + "属性配置了非正整数！");
				}
				if(this.isExistRtuId(attValue, meterMap)){
					throw new IllegalArgumentException(filePath + "配置有错误，RTU或透传RTU的ID(" + attValue + ")重复配置！");
				}
				meterVo.id = attValue ;
			}

			attValue = meter.getAttributeValue(MeterConstant.phone_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.phone_key + "属性必须配置！");
			}else{
				attValue = attValue.trim() ;
				if(!NumberUtil.isPlusIntNumber(attValue)){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.phone_key + "属性配置了非正整数！");
				}
				if(attValue.length() != 11){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.phone_key + "属性配置必须为11位数值！");
				}
				if(this.isExistPhone(attValue, meterMap)){
					throw new IllegalArgumentException(filePath + "配置有错误，RTU的phone(" + attValue + ")重复配置！");
				}
				meterVo.phone = attValue ;
			}
			
			if(ServerConfig.serialPortServerEnable && ServerConfig.satelliteEnable){
				attValue = meter.getAttributeValue(MeterConstant.satelliteId_key) ;
				if(attValue == null || attValue.trim().equals("")){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.satelliteId_key + "属性必须配置！");
				}else{
					attValue = attValue.trim() ;
					if(!NumberUtil.isPlusIntNumber(attValue)){
						throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.satelliteId_key + "属性配置了非正整数！");
					}
					if(attValue.length() != 6){
						throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.satelliteId_key + "属性配置必须为6位数值！");
					}
					if(this.isExistSatelliteId(attValue, meterMap)){
						throw new IllegalArgumentException(filePath + "配置有错误，RTU的卫星用户ID(" + attValue + ")重复配置！");
					}
					meterVo.satelliteId = attValue ;
				}
			}
			
			
			attValue = meter.getAttributeValue(MeterConstant.rtuProtocol_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.rtuProtocol_key + "属性未配置！");
			}else{
				if(!this.isExistRtuProtocol(attValue.trim())){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.rtuProtocol_key + "属性值在protocols.xml中未配置！");
				}
				meterVo.rtuProtocol = attValue.trim() ;
			}
			
			attValue = meter.getAttributeValue(MeterConstant.rtuKey_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.rtuKey_key + "属性必须配置正整数！");
			}else{
				if(!NumberUtil.isPlusIntNumber(attValue.trim())){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.rtuKey_key + "属性配置了非正整数！");
				}
				meterVo.rtuKey = Integer.parseInt(attValue.trim()) ;
			}
			attValue = meter.getAttributeValue(MeterConstant.rtuDefaultPassword_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.rtuDefaultPassword_key + "属性必须配置正整数！");
			}else{
				if(!NumberUtil.isPlusIntNumber(attValue.trim())){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.rtuDefaultPassword_key + "属性配置了非正整数！");
				}
				meterVo.rtuPass = Integer.parseInt(attValue.trim()) ;
			}
			
			
			if(ServerConfig.serialPortServerEnable && ServerConfig.satelliteEnable){
				attValue = meter.getAttributeValue(MeterConstant.sateProtocol_key) ;
				if(attValue == null || attValue.trim().equals("")){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.sateProtocol_key + "属性未配置！");
				}else{
					if(!this.isExistSateProtocol(attValue.trim())){
						throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.sateProtocol_key + "属性值在protocols.xml中未配置！");
					}
					meterVo.sateProtocol = attValue.trim() ;
				}
			}

			
			attValue = meter.getAttributeValue(MeterConstant.channels_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.channels_key + "属性必须配置！");
			}else{
				attValue = attValue.trim() ;
				String[] ss = this.splitReportTo(attValue) ;
				if(ss == null || ss.length == 0 ){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.channels_key + "属性必须配置！");
				}
				meterVo.channels = ss ;
			}
			
			attValue = meter.getAttributeValue(MeterConstant.mainChannel_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.mainChannel_key + "属性必须配置！");
			}else{
				attValue = attValue.trim() ;
				boolean finded = false ;
				String[] ss = meterVo.channels ;
				for(int i = 0 ; i < ss.length ; i++){
					if(ss[i].equals(attValue)){
						finded = true ;
						break ;
					}
				}
				if(!finded){
					throw new IllegalArgumentException(filePath + "配置有错误，选择的主通道不在所选择的所有数据通道中！");
				}
				meterVo.mainChannel = attValue ;
			}
			
			
			if(ServerConfig.serialPortServerEnable){
				attValue = meter.getAttributeValue(MeterConstant.comId_key) ;
				if(attValue == null || attValue.trim().equals("")){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.comId_key + "属性未配置！");
				}else{
					if(!this.isExistComId(attValue.trim())){
						throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.comId_key + "属性值在serialPortServer.xml中未配置！");
					}
					meterVo.comId = attValue.trim() ;
				}
			}

			/*attValue = meter.getAttributeValue(MeterConstant.dataTo_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.dataTo_key + "属性必须配置！");
			}else{
				attValue = attValue.trim() ;
				String[] ss = this.splitReportTo(attValue) ;
				if(ss == null || ss.length == 0 ){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.dataTo_key + "属性配置不正确！");
				}
				if(!this.isExistPublishServer(ss)){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.dataTo_key + "属性配置值在publishServers.xml中不存在！");
				}
				meterVo.dataTo = ss ;
			}*/
			
			
			attValue = meter.getAttributeValue(MeterConstant.onlineAlways_key) ;
			if(attValue == null || attValue.trim().equals("")){
				throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.onlineAlways_key + "属性必须配置！");
			}else{
				attValue = attValue.trim() ;
				if(attValue.equals("true") && attValue.equals("false")){
					throw new IllegalArgumentException(filePath + "配置有错误，meter元素的" + MeterConstant.onlineAlways_key + "属性必须是true或false！");
				}
				meterVo.onlineAlways = Boolean.parseBoolean(attValue) ;
			}
			
			
			
			meterMap.put(meterVo.id , meterVo) ;
		}
	    Map<String, MeterVO> meters = new TreeMap<String, MeterVO>(meterMap);
		
		return meters ;
	}
	/**
	 * 是否存在RTU协议
	 * @param p
	 * @return
	 */
	private boolean isExistRtuProtocol(String p){
		HashMap<String, RTUProtocolVO> map = ConfigCenter.instance().getRtuProtocolMap() ;
		if(map != null){
			if(map.containsKey(p)){
				return true ;
			}
		}
		return false ;
	}
	/**
	 * 是否存在卫星协议
	 * @param p
	 * @return
	 */
	private boolean isExistSateProtocol(String p){
		HashMap<String, SateProtocolVO> map = ConfigCenter.instance().getSateProtocolMap() ;
		if(map != null){
			if(map.containsKey(p)){
				return true ;
			}
		}
		return false ;
	}

	/**
	 * 检查RTU的手机号(phone)是否重复
	 * @param filePath
	 * @param vo
	 * @param RTUs
	 * @throws IllegalArgumentException
	 */
	private boolean isExistPhone(String phone , TreeMap<String, MeterVO> meterMap) {
		Set<Map.Entry<String , MeterVO>> meterEntrys = meterMap.entrySet() ;
		Iterator<Map.Entry<String , MeterVO>> meterIt = meterEntrys.iterator() ;
		Map.Entry<String , MeterVO> meterEntry = null ;
		MeterVO meter = null ;
		while(meterIt.hasNext()){
			meterEntry = meterIt.next() ;
			meter = meterEntry.getValue() ;
			if(meter.phone.equals(phone)){
				return true ;
			}
		}
		return false ;
	}
	/**
	 * 检查RTU的卫星用户ID是否重复
	 * @param filePath
	 * @param vo
	 * @param RTUs
	 * @throws IllegalArgumentException
	 */
	private boolean isExistSatelliteId(String satelliteId , TreeMap<String, MeterVO> meterMap) {
		Set<Map.Entry<String , MeterVO>> meterEntrys = meterMap.entrySet() ;
		Iterator<Map.Entry<String , MeterVO>> meterIt = meterEntrys.iterator() ;
		Map.Entry<String , MeterVO> meterEntry = null ;
		MeterVO meter = null ;
		while(meterIt.hasNext()){
			meterEntry = meterIt.next() ;
			meter = meterEntry.getValue() ;
			if(meter.satelliteId.equals(satelliteId)){
				return true ;
			}
		}
		return false ;
	}
	/**
	 * 检查是已经存在RTU ID
	 * @param rtuId
	 * @param meterMap
	 * @return
	 */
	private boolean isExistRtuId(String rtuId , TreeMap<String, MeterVO> meterMap) {
		if(meterMap.containsKey(rtuId)){
			return true ;
		}
		return false ;
	}
	
	/**
	 * 是否存在串中ID
	 * @param comId
	 * @return
	 */
	private boolean isExistComId(String comId){
		if(ServerConfig.serialPortServerEnable && ServerConfig.satelliteEnable){
			HashMap<String, SerialPortVO> map = ConfigCenter.instance().getSerialPortMap() ;
			if (map.containsKey(comId)) {
				return true ;
			}
			return false ;
		}else{
			return true ;
		}
	}
	/**
	 * 检查RTU的发布服务是否存在
	 * @param serverId
	 */
	private boolean isExistPublishServer(String[] serverId) {
		HashMap<String, PublishServerVO> map = ConfigCenter.instance().getPublishServerMap() ;
		boolean flag = true ;
		for(int i = 0 ; i < serverId.length ; i++){
			if (!map.containsKey(serverId[i])) {
				flag = false ;
			}
		}
		return flag ;
	}
	
	/**
	 * 把逗号分隔的字符串转转成字符串数组
	 * @param s
	 * @return
	 */
	private String[] splitReportTo(String s){
		String[] ss = StringUtil.splitStringByComma(s) ;
		if(ss == null){
			return null ;
		}
		int blankCount = 0 ;
		for(int i = 0 ; i < ss.length ; i++){
			if(ss[i].equals("")){
				blankCount++ ;
			}
		}
		if(blankCount == 0){
			return ss ;
		}else{
			int count = 0 ;
			String[] sss = new String[s.length() - blankCount] ; 
			for(int i = 0 ; i < ss.length ; i++){
				if(!ss[i].equals("")){
					sss[count++] = ss[i] ;
				}
			}
			return sss ;
		}
	}
}
