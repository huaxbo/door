package com.am.cs12.commu.protocol ;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.cs12.config.ConfigCenter;
import com.am.cs12.config.RTUProtocolVO;
import com.am.cs12.config.conf.ProtocolRTUConstant;

public class HandleOnLine {
	
	private static Logger log = LogManager.getLogger(HandleOnLine.class.getName()) ;
	private class OnLineObj{
		public Pattern p  ;
		//指在上线数据字节数组中，ID编码类型，分别有字符串(ASCII)、整数(INT)、BCD编码、427协议编码 、206协议编码  5种类型
		public String idType ;
		//指在上线字数据节数组中，ID编码起始位(包含)
		public int idFrom ;
		//指在上线字数据节数组中，ID编码终止位(包含)
		public int idEnd ;
	}
	private static HashMap<String , OnLineObj> matchs = null ;
	
	private static HandleOnLine instance ;
	
	/**
	 * 得到单实例
	 * @return
	 */
	public static HandleOnLine instance(){
		if(instance == null){
			instance = new HandleOnLine() ;
		}
		return instance ;
	}
	
	/**
	 * 处理上线数据
	 *
	 */
	private HandleOnLine(){
		if(HandleOnLine.matchs == null){
			HandleOnLine.matchs = new HashMap<String , OnLineObj>() ;
			HashMap<String, RTUProtocolVO> rtups = ConfigCenter.instance().getRtuProtocolMap() ;
			Set<Map.Entry<String,RTUProtocolVO>> set = rtups.entrySet() ;
			Iterator<Map.Entry<String,RTUProtocolVO>> it = set.iterator() ;
			Map.Entry<String,RTUProtocolVO> entry = null ;
			RTUProtocolVO vo = null ;
			while(it.hasNext()){
				OnLineObj oo = new OnLineObj() ;
				entry = it.next() ;
				vo = entry.getValue() ;
				oo.p = Pattern.compile(vo.onLine, Pattern.DOTALL) ;
				oo.idType = vo.idType ;
				oo.idFrom = vo.idFrom ;
				oo.idEnd = vo.idEnd ;
				HandleOnLine.matchs.put(vo.name , oo) ;
			}
		}
	}	

	/**
	 * 匹配上线数据
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public String match(byte[] bs) throws Exception {
		String data = new String(bs , "ISO-8859-1").trim();
		String id = null ;
		try {
			Set<Map.Entry<String, OnLineObj>> set = HandleOnLine.matchs.entrySet() ;
			Iterator<Map.Entry<String, OnLineObj>> it = set.iterator() ;
			Map.Entry<String,OnLineObj> entry = null ;
			OnLineObj op = null ;
			Matcher mer = null ;
			while(it.hasNext()){
				entry = it.next() ;
				op = entry.getValue() ;
				mer =op.p.matcher(data) ;
				boolean flag = mer.matches() ;
				if(flag){
					//是上线数据
					if(op.idType.equals(ProtocolRTUConstant.ASCII)){
						id = data.substring(op.idFrom, op.idEnd + 1).trim();
					}else if(op.idType.equals(ProtocolRTUConstant.INT)){
						id = String.valueOf(this.bytes2Int(bs, op.idFrom, op.idEnd));
					}else if(op.idType.equals(ProtocolRTUConstant.BCD)){
//						id = this.createGPRSID(this.BCD2String(bs , op.idFrom , op.idEnd ));
						id = this.createRtuIdForAm(bs , op.idFrom , op.idEnd);
					}else if(op.idType.equals(ProtocolRTUConstant.P206)){		
						id = this.createRtuIdFor206(bs , op.idFrom , op.idEnd);	
					}else{
						log.error("当前通信服务器不支持协议上线数据中测控器地址类型(" + op.idType +")！") ; ;
					}
					
					
					if (Level.INFO.isMoreSpecificThan(log.getLevel())) {
						log.info("从上线数据中分析出 ID:" + id + "。") ; ;
					}

					return id ;
				}
			}
			
		}catch (Exception e) {
			throw e;
		}
		return id ;
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
	 * 字节数组转整数
	 * @param bs
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
//	private String createRtuIdForHydro(byte[] bs, int startIndex, int endIndex){
//		int value = 0 ;
//		int temp = 0 ;
//		int index = 0 ;
//		for(int i = endIndex ; i >= startIndex ; i-- ){
//			temp = (bs[i]+1024)%1024 ;
//			for(int j = 0 ; j < index ; j++){
//				temp *= 1024 ;
//			}
//			value += temp ;
//			index++ ;
//			temp = 0 ;
//		}
//		return String.valueOf(value) ;
//	}
			
	
	/**
	 * BCD编码转成字符串型
	 * @param b
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws CommuException
	 */
	private String BCD2String(byte[] b, int startIndex, int endIndex) {
		String str = this.decodeBCD(b , startIndex , endIndex-startIndex+1) ;
		return this.cleanHead0FromStrNum(str);
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
			log.error("=====================================result="+result);
			throw new java.lang.IllegalArgumentException();
		}
		return result;
	}
	
	/**
	 * @param result
	 * @return
	 */
	private String createRtuIdForAm(byte[] b, int startIndex, int endIndex){
		int n = startIndex ;
		
		String cityNo = this.BCD2String(b , n , n + 2) ;
		String address = this.BCD2String(b , n + 3 , n + 6) ;
		while(cityNo.length() < 6){
			cityNo = "0" + cityNo ;
		}
		while(address.length() < 8){
			address = "0" + address ;
		}

		return cityNo + address ;
	}
	
	 /**
	   * 把数据前面的0去掉
	   * @param s String
	   * @return String
	   */
	private String cleanHead0FromStrNum(String s){
		int i = 0 ;
	    while(i < s.length() && s.charAt(i) == '0'){
	    	i++ ;
	    }
	    if(i>0){
	       s = s.substring(i) ;
	    }
	    return s ;
	  }

	/**
	 * 解析出206协议的ID
	 * @param result
	 * @return
	 */
	private String createRtuIdFor206(byte[] bs, int startIndex, int endIndex){
		UtilProtocol u = new UtilProtocol() ;
		int v = u.byte2PlusInt(bs[3]) ;
		int v2 = (v & 0x40) >> 6 ;
		if(v2 != 0){
			startIndex ++;
			endIndex ++;
		}
		String preId = this.BCD2String(bs , startIndex , startIndex + 2 ) ;
		while(preId.length() < 6){
			preId = "0" + preId ;
		}
		int index = startIndex + 3 ;
		byte[] b = new byte[endIndex - index + 1] ;
		for(int i = 0 ; i < b.length ; i++){
			b[i] = bs[index + i] ;
		}
		byte[] b2 = new byte[2] ;
		b2[0] = b[1];
		b2[1] = b[0];
		String afterId = String.valueOf(this.bytes2Int(b2, 0, b2.length-1)) ;
		while(afterId.length() < 4){
			afterId = "0" + afterId ;
		}
		return preId +  afterId;
	}
	/**
	 * 获取遥测站配置类型码（protocols.xml中onLine的 RTUIdType属性）
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public String matchType(byte[] bs) throws Exception {
		String data = new String(bs , "ISO-8859-1").trim();
		String idType = null ;
		try {
			Set<Map.Entry<String, OnLineObj>> set = HandleOnLine.matchs.entrySet() ;
			Iterator<Map.Entry<String, OnLineObj>> it = set.iterator() ;
			Map.Entry<String,OnLineObj> entry = null ;
			OnLineObj op = null ;
			Matcher mer = null ;
			while(it.hasNext()){
				entry = it.next() ;
				op = entry.getValue() ;
				mer =op.p.matcher(data) ;
				boolean flag = mer.matches() ;
				if(flag){
					return op.idType;
				}
			}
			
		}catch (Exception e) {
			throw e;
		}
		return idType ;
	}	
}

