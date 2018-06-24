package com.am.cs12.config.meter.monitor;

import java.util.Map;

import com.am.common.timerTask.* ;
import com.am.cs12.config.meter.XMLOrperator;
import com.am.cs12.config.* ;
import com.am.cs12.config.conf.MeterConfig;
import com.am.cs12.util.AmConstant;

/**
 * 这是通信服务器定时调度任务中的一个工作任务， 
 * 其工作是： 检查测控器终端配置文件是否更新，
 * 即是否增加删除或修改了远程测控器参数据配置，
 * 如查有更新，则把更新同步到系统运行时中，
 * 
 */
public class MonitorMeterConfig extends ACTaskJob{

	/**
	 * 这是通信服务器定时调度任中的一个工作任务， 
	 * 它的工作是： 检查测控器终端配置文件是否更新，
	 * 即是否增加删除或修改了远程测控器参数据配置，
	 * 如查有更新，则把更新同步到系统运行时中，
	 * 这个方法只针对手工增加、修改或删除的测控器配置，
	 * 通过系统进行的配置，系统自动进行了运行时更新，
	 * 同时自动更新保存的的配置文件的更新时间，
	 * 使这个方法不起作用，
	 */
	@Override
	public void execute() throws Exception {
//		System.out.println(System.currentTimeMillis()) ;
		XMLOrperator xml = new XMLOrperator() ;
		synchronized(XMLOrperator.synObj){
			long old = xml.getSavedMeterXMLFileLastUpdateTime() ;
			long now = xml.getMeterXMLFileLastUpdateTime() ;
			long n = now - old ;
			if(n > 2000 ){
				//配置文件发生了更改
				//保存在临时文件中的配置文件最后更新时间
				//与文件实际最后更新的时间，可能有两秒的误差，
				//一般误差在一秒以内,所认上面用n>2000
				xml.saveMetersXmlFileLastUpdateTime() ;
				if(XMLOrperator.updateMeterXmlFileToMemory){
					MeterConfig mc = new MeterConfig() ;
					String path = mc.createDom(AmConstant.meterConfigFilePath) ;
					Map<String, MeterVO> id_meterMap = mc.parseOptions(path) ;
					if(id_meterMap != null && id_meterMap.size() > 0){
						ConfigCenter.instance().setId_meterMap(id_meterMap) ;
					}
				}
				XMLOrperator.updateMeterXmlFileToMemory = true ;
			}
		}
	}
}