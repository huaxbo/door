package com.am.cs12.commu.protocol.amRtu206.common;

import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.util.DateTime;

/**
 * 用户数据域中附加数据(密码、时标)
 * @author liu runyu
 *
 */
public class AuxProtocol {
	
	/**
	 * 分析RTU数据中的密钥及密码
	 * @param b
	 * @param site_password
	 * @return int[]{密钥，密码}
	 */
	public int[] parsePassword(byte[] b , int site_password) throws Exception{
		UtilProtocol u = new UtilProtocol() ;
		
		String key = u.BCD2String(b , site_password , 1) ;
		String pass = u.BCD2String(b , site_password + 1 , 1) ;
		
		if(key.length() > 1){
			key = key.substring(0,1) ;
			pass = key.substring(1,2) +  pass ;
		}else{
			pass = key +  pass ;
			key = "0" ;
		}
		return new int[]{Integer.parseInt(key) , Integer.parseInt(pass)} ;
	}
	
	/**
	 * 构造RTU命令中的密钥及密码
	 * @param b
	 * @param site_password
	 * @param p_key_value
	 * @return
	 * @throws Exception
	 */
	public byte[] createPassword(byte[] b , int site_password , int[] p_key_value) throws Exception{
		if(p_key_value == null || p_key_value.length != 2){
			throw new Exception("RTU密钥算法不能为空!") ;
		}
		if(p_key_value[0] > 9 || p_key_value[0] < 0){
			throw new Exception("RTU密钥算法(" + p_key_value[0] + ")不正确，其取值范围是0-9!") ;
		}
		if(p_key_value[1] > 999 || p_key_value[1] < 0){
			throw new Exception("RTU密钥(" + p_key_value[1] + ")不正确，其取值范围是0-999!") ;
		}
		UtilProtocol u = new UtilProtocol() ;
		
		String key = "" + p_key_value[0] ;
		String pass = "" + p_key_value[1] ;
		if(pass.length() == 2){
			pass = "0"+pass;
		}else if(pass.length() == 1){
			pass = "00"+pass;
		}
		if(pass.length() > 2){
			key = key + pass.substring(0,1) ;
			pass = pass.substring(1) ;
		}
		
		byte[] bkey = u.string2BCD(key) ;
		byte[] bpass = u.string2BCD(pass) ;
		
		b[site_password] = 1;//bkey[0] ;
		b[site_password + 1] = 2;//bpass[0] ;

		return b ;
	}
	/**
	 * 分析RTU数据中的时间标签
	 * @param b
	 * @param site_password
	 * @return Object[]{String:时间(格式例如 09 10:01:00) , int:delay}
	 */
	public Object[] parseTime(byte[] b , int site_time) throws Exception{
		UtilProtocol u = new UtilProtocol() ;
		int date = u.BCD2Int(b, site_time, site_time) ;
		int hour = u.BCD2Int(b, site_time + 1, site_time + 1) ;
		int minute = u.BCD2Int(b, site_time + 2, site_time + 2) ;
		int second = u.BCD2Int(b, site_time + 3, site_time + 3) ;
		int delay = u.BCD2Int(b, site_time + 4, site_time + 4) ;
		
		return new Object[]{"" + (date < 10?("0" + date):date)  
	 			  + " "
				  + (hour < 10?("0" + hour):hour) + ":"
				  + (minute < 10?("0" + minute):minute) + ":"
				  + (second < 10?("0" + second):second) , delay} ;
	}
	
	/**
	 * 构造RTU命令中的密钥及密码
	 * @param b
	 * @param site_password
	 * @param p_key_value
	 * @return
	 * @throws Exception
	 */
	public byte[] createTime(byte[] b , int site_time) throws Exception{
		UtilProtocol u = new UtilProtocol() ;
		b[site_time] = u.int2BCD(Integer.parseInt(DateTime.dd()))[0] ;
		b[site_time + 1] = u.int2BCD(Integer.parseInt(DateTime.HH()))[0] ;
		b[site_time + 2] = u.int2BCD(Integer.parseInt(DateTime.mm()))[0] ;
		b[site_time + 3] = u.int2BCD(Integer.parseInt(DateTime.ss()))[0] ;
		b[site_time + 4] = 0;//允许发送传输延时时间,0代表此字节值不生效,否则进行时间验证
		
		return b ;
	}
	/**
	 * 构造RTU命令中的密钥及密码
	 * @param b
	 * @param site_password
	 * @param p_key_value
	 * @return
	 * @throws Exception
	 */
	public byte[] create206Time(byte[] b , int site_time) throws Exception{
		UtilProtocol u = new UtilProtocol() ;
		b[site_time] = u.int2BCD(Integer.parseInt(DateTime.ss()))[0] ;
		b[site_time + 1] = u.int2BCD(Integer.parseInt(DateTime.mm()))[0] ;
		b[site_time + 2] = u.int2BCD(Integer.parseInt(DateTime.HH()))[0] ;
		b[site_time + 3] = u.int2BCD(Integer.parseInt(DateTime.dd()))[0] ;
		b[site_time + 4] = 0x0a;//允许发送传输延时时间,0代表此字节值不生效,否则进行时间验证
		
		return b ;
	}

}
