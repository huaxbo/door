package com.am.cs12.commu.protocol.amRtu206;


import java.util.HashMap;

import com.am.cs12.commu.protocol.amRtu206.cd00.Create_00;
import com.am.cs12.commu.protocol.amRtu206.cd01.Write_01;
import com.am.cs12.commu.protocol.amRtu206.cd10_50.Read_50;
import com.am.cs12.commu.protocol.amRtu206.cd10_50.Write_10;
import com.am.cs12.commu.protocol.amRtu206.cd11_51.Read_51;
import com.am.cs12.commu.protocol.amRtu206.cd11_51.Write_11;
import com.am.cs12.commu.protocol.amRtu206.cd12_52.Read_52;
import com.am.cs12.commu.protocol.amRtu206.cd12_52.Write_12;
import com.am.cs12.commu.protocol.amRtu206.cdF1.Write_F1;

public class CommandProtocol206 {

	/**
	 * 构造RTU 确认
	 * @param rtuId RTU ID
	 * @return
	 * @throws Exception
	 */
	public byte[] recircle(String rtuId, String recircleCode, byte[] recircleContent, Integer[] keyPassword)throws Exception{
		return new Create_00().recircleData(rtuId, recircleCode, recircleContent, keyPassword) ;
	}

	/**
	 * 构造RTU 确认
	 * @param rtuId RTU ID
	 * @return
	 * @throws Exception
	 */
	public byte[] confirm(String rtuId, String returnCode, byte[] returnContent, byte DIVS, Integer[] keyPassword)throws Exception{
		return new Write_01().remoteData(rtuId, returnCode, returnContent, DIVS, keyPassword) ;
	}
	
	/**
	 * 构造设置RTU ID命令
	 * @param rtuId RTU ID
	 * @param params 参数集合
	 * @param keyPassword 密钥与密码
	 * @return
	 * @throws Exception
	 */
	public byte[] write_10(String rtuId, HashMap<String , Object> params, Integer[] keyPassword) throws Exception{
		return new Write_10().remoteData(rtuId , params, keyPassword) ;
	}
	
	/**
	 * 构造设置RTU ID命令
	 * @param rtuId RTU ID
	 * @param params 参数集合
	 * @param keyPassword 密钥与密码
	 * @return
	 * @throws Exception
	 */
	public byte[] write_11(String rtuId, HashMap<String , Object> params, Integer[] keyPassword) throws Exception{
		return new Write_11().remoteData(rtuId , params, keyPassword) ;
	}
	/**
	 * 构造设置RTU ID命令
	 * @param rtuId RTU ID
	 * @param params 参数集合
	 * @param keyPassword 密钥与密码
	 * @return
	 * @throws Exception
	 */
	public byte[] write_12(String rtuId, HashMap<String , Object> params, Integer[] keyPassword) throws Exception{
		return new Write_12().remoteData(rtuId , params, keyPassword) ;
	}
		
	/**
	 * 构造查询RTU ID命令
	 * @param rtuId RTU ID
	 * @param params 参数集合
	 * @param keyPassword 密钥与密码
	 * @return
	 * @throws Exception
	 */
	public byte[] read_50(String rtuId, HashMap<String , Object> params, Integer[] keyPassword) throws Exception{
		return new Read_50().remoteData(rtuId , params, keyPassword) ;
	}
	
	/**
	 * 构造查询RTU ID命令
	 * @param rtuId RTU ID
	 * @param params 参数集合
	 * @param keyPassword 密钥与密码
	 * @return
	 * @throws Exception
	 */
	public byte[] read_51(String rtuId, HashMap<String , Object> params, Integer[] keyPassword) throws Exception{
		return new Read_51().remoteData(rtuId , params, keyPassword) ;
	}
	
	/**
	 * 构造查询RTU ID命令
	 * @param rtuId RTU ID
	 * @param params 参数集合
	 * @param keyPassword 密钥与密码
	 * @return
	 * @throws Exception
	 */
	public byte[] read_52(String rtuId, HashMap<String , Object> params, Integer[] keyPassword) throws Exception{
		return new Read_52().remoteData(rtuId , params, keyPassword) ;
	}
		
	/**
	 * 门控制
	 * @param rtuId
	 * @param params
	 * @param keyPassword
	 * @return
	 * @throws Exception
	 */
	public byte[] write_F1(String rtuId, HashMap<String , Object> params, Integer[] keyPassword) throws Exception{
		return new Write_F1().remoteData(rtuId , params, keyPassword) ;
	}
}
