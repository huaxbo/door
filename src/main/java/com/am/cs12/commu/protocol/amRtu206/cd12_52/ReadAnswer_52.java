package com.am.cs12.commu.protocol.amRtu206.cd12_52;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.common.ControlProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.ProtocolAbstract;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class ReadAnswer_52 extends ProtocolAbstract{

	private static Logger log = LogManager.getLogger(ReadAnswer_52.class);

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

		log.info("分析<读取RTU 工作模式>应答: RTU ID=" + d.getId() + " 数据：" + d.getSubData().toString());
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

		Data206_cd52 subD = new Data206_cd52() ;
		d.setSubData(subD) ;
		
		int index = Constant.Site_Data ;
		if(cp.hasDIVS){
			index += 1 ;
		}
		UtilProtocol u = new UtilProtocol() ;
		// 分析数据域
		Integer c = u.byte2PlusInt(b[index])  ;
		c = c.intValue() & 0x03 ;//&0x03，使字节前6 bit清为0
				
		subD.setModel(c) ;
		
		return d;
	}
	
}
