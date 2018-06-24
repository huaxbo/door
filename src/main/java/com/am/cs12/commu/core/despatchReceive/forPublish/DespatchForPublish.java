package com.am.cs12.commu.core.despatchReceive.forPublish;

import java.util.Map;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.publish.PublishCenter;
import com.am.cs12.config.MeterVO;
import com.am.cs12.config.PublishServerVO;
import com.am.cs12.config.ConfigCenter;

public class DespatchForPublish {

	private static Logger log = LogManager.getLogger(DespatchForPublish.class.getName()) ;
	
	private static PublishCenter pcenter ;
	private static ConfigCenter ccenter ;
	
	/**
	 * 发送数据
	 * @param id
	 * @param dp
	 */
	public void publishResult(String id , Data d, String dataCode){
		if(dataCode == null || dataCode.trim().equals("")){
			log.error("出错！所要发布的数据(ID:" + id + ")无功能码，所以不能发布。") ;
			return ;
		}
		try {
			log.info("\n<<<<<<<<数据发布者收到ID：" + id + "数据：\n" + d.toXml()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(pcenter == null){
			pcenter = PublishCenter.instance() ;
		}
		if(ccenter == null){
			ccenter = ConfigCenter.instance() ;
		}
		Map<String, MeterVO> meters = ccenter.getId_meterMap() ;
		MeterVO meter = meters.get(id) ;
		if(meter != null){
			String[] dataTo = meter.dataTo ;
			if(dataTo != null){
				Map<String, PublishServerVO> servers = pcenter.getPservers() ;
				PublishServerVO pserver = null ;
				for(int i = 0 ; i < dataTo.length ; i++){
					pserver = servers.get(dataTo[i]) ;
					if(pserver.acceptCodes == null || pserver.acceptCodes.contains(dataCode)){
						try {
							pserver.server.putData(d.getCommandId() , d) ;
						} catch (Exception e) {
							log.error("调用数据发布服务器发布数据出错，" + e.getMessage() , e) ;
						}finally{}
					}
				}
			}
		}else{
			//说明测控器RTU ID 更改过
			log.error("严重错误，测控器RTU ID已经更改，但配置文件未更新，所以找不到上报数据对应配置。") ;
		}
	}
	
	
	
	
}
