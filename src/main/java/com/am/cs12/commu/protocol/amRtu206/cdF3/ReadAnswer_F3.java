package com.am.cs12.commu.protocol.amRtu206.cdF3;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.cdF4.ReadAnswer_F4;
import com.am.cs12.commu.protocol.amRtu206.common.ControlProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.ProtocolAbstract;

public class ReadAnswer_F3 extends ProtocolAbstract{

	private static Logger log = LogManager.getLogger(ReadAnswer_F3.class);
	/**
	 * 分析RTU 数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Data parseData(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception {
		Data d = this.doParse(cp, b) ;
		d.setId(rtuId) ;
		d.setCode(dataCode) ;
		
		log.info("分析<门控制指令>应答: RTU ID=" + d.getId() + " 数据：" + d.getSubData().toString());
		return d;
	}
	/**
	 * 分析RTU 数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private Data doParse(ControlProtocol cp, byte[] b) throws Exception {
		Data d = new Data() ;

		Data206_cdF3 dd = new Data206_cdF3() ;
		d.setSubData(dd) ;
		
		new ReadAnswer_F4().parse(cp, b, dd);
		
		return d;
	}
}
