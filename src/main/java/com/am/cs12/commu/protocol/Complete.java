package com.am.cs12.commu.protocol;

import java.util.HashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.cs12.config.ConfigCenter;
import com.am.cs12.config.RTUProtocolVO;
import com.am.cs12.config.conf.ProtocolRTUConstant;

public class Complete {
	
	private static Logger log = LogManager.getLogger(Complete.class.getName()) ;
	private class CompleteObj{
		public Pattern p  ;
		//指在上报数据字节数组中，数据长度编码类型，分别有字符串(ASCII)、整数(INT)、BCD编码
		public String lenType ;
		//指在上报字数据节数组中，数据长度编码起始位(包含)
		public int lenFrom ;
		//指在上报字数据节数组中，数据长度编码终止位(包含)
		public int lenEnd ;
	}
	private static HashMap<String , CompleteObj> matchs = null ;
	
	private static Complete instance ;
	
	/**
	 * 得到单实例
	 * @return
	 */
	public static Complete instance(){
		if(instance == null){
			instance = new Complete() ;
		}
		return instance ;
	}
	
	/**
	 * 处理上报数据
	 *
	 */
	private Complete(){
		if(Complete.matchs == null){
			Complete.matchs = new HashMap<String , CompleteObj>() ;
			HashMap<String, RTUProtocolVO> rtups = ConfigCenter.instance().getRtuProtocolMap() ;
			Set<Map.Entry<String,RTUProtocolVO>> set = rtups.entrySet() ;
			Iterator<Map.Entry<String,RTUProtocolVO>> it = set.iterator() ;
			Map.Entry<String,RTUProtocolVO> entry = null ;
			RTUProtocolVO vo = null ;
			while(it.hasNext()){
				CompleteObj oo = new CompleteObj() ;
				entry = it.next() ;
				vo = entry.getValue() ;
				oo.p = Pattern.compile(vo.onLine, Pattern.DOTALL) ;
				oo.lenType = vo.lenType ;
				oo.lenFrom = vo.lenFrom ;
				oo.lenEnd = vo.lenEnd ;
				Complete.matchs.put(vo.name , oo) ;
			}
		}
	}
	

	/**
	 * 进行上报数据头部匹配检查，
	 * 同时获取数据长度
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public Integer match(String protocolName , byte[] bs) throws Exception {
		String data = new String(bs , "ISO-8859-1").trim();
		Integer len = 0 ;
		try {
			CompleteObj op = Complete.matchs.get(protocolName) ;
			Matcher mer = op.p.matcher(data) ;
			boolean flag = mer.matches() ;
			if(flag){
				//是上报数据
				if(op.lenType.equals(ProtocolRTUConstant.ASCII)){
					len = Integer.parseInt(data.substring(op.lenFrom, op.lenEnd + 1).trim()) ;
				}else if(op.lenType.equals(ProtocolRTUConstant.INT)){					
					len = this.bytes2Int(bs, op.lenFrom, op.lenEnd) ;					
				}else if(op.lenType.equals(ProtocolRTUConstant.BCD)){
					len = Integer.parseInt(this.BCD2String(bs , op.lenFrom , op.lenEnd)) ;
				}else{
					log.error("当前通信服务器不支持协议上报数据中数据长度类型(" + op.lenType +")！") ; ;
				}
				
				if (Level.INFO.isMoreSpecificThan(log.getLevel())) {
					log.info("从上报数据中分析出 数据长度:" + len + "。") ; ;
				}
				return len ;
			}else{
				log.error("当进行断包及粘包检查前，进行数据协议头匹配未成功，头数据为:" + new UtilProtocol().byte2Hex(bs , true)  +" ") ; ;
			}
			
		}catch (Exception e) {
			throw e;
		}
		return len ;
	}
	
	/**
	 * 字节数组转整数
	 * @param bs
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	private int bytes2Int(byte[] bs, int startIndex, int endIndex){
		int value = 0 ;
		int temp = 0 ;
		int index = 0 ;
		for(int i = endIndex ; i >= startIndex ; i-- ){
			temp = (bs[i]+256)%256 ;
			for(int j = 0 ; j < index ; j++){
				temp *= 256 ;
			}
			value += temp ;
			index++ ;
			temp = 0 ;
		}
		return value ;
	}
	

	/**
	 * BCD编码转成字符串型
	 * @param b
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws CommuException
	 */
	private String BCD2String(byte[] b, int startIndex, int endIndex) {
		return this.decodeBCD(b , startIndex , endIndex-startIndex+1) ;
	}
	
	
	/**
	 * @param src
	 * @param startIndex
	 * @param length
	 * @return
	 */
	private String decodeBCD(byte[] src, int startIndex, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = startIndex; i < startIndex + length; i++) {
			int value = (src[i] + 256) % 256;
			sb.append((char) (value / 16 + '0')).append(
					(char) (value % 16 + '0'));
			value++;
		}
		String result = sb.toString();
		if (!result.matches("\\d*")) {
			throw new java.lang.IllegalArgumentException();
		}
		return result;
	}
}
