package com.am.cs12.commu.protocol.amRtu206.cdF1;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.common.ControlProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.ProtocolAbstract;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;

public class ReadAnswer_F1 extends ProtocolAbstract{

	private static Logger log = LogManager.getLogger(ReadAnswer_F1.class);
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

		Data206_cdF1 dd = new Data206_cdF1() ;
		d.setSubData(dd) ;
		
		int n = Constant.Site_Data ;
		if(cp.hasDIVS){
			n += 1 ;
		}
		
		// 分析数据域
		dd.setState(String.valueOf(b[n++]));
		
		return d;
	}
}
