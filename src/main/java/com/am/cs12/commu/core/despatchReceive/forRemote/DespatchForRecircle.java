package com.am.cs12.commu.core.despatchReceive.forRemote;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class DespatchForRecircle extends DespatchForGprs{
	
	private static Logger log = LogManager.getLogger(DespatchForRecircle.class.getName()) ;
	/**
	 * 接收到远程数据
	 * @param rtuId RTU ID
	 * @param data 附加数据，可以为空
	 */
	public void receiveData(String rtuId, byte[] data){
		if(rtuId == null){
			log.error("出错，收到测控器的ID为空！") ;
			return ;
		}
		String dataHex = null ;
		try {
			dataHex = new UtilProtocol().byte2Hex(data , true) ;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("将数据转换为十六进制时出错！" ) ;
		}
		this.dealData(rtuId, data, dataHex) ;
	}

}
