package com.am.cs12.commu.protocol.amRtu206.cd10_50;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.common.ControlProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.ProtocolAbstract;
import com.am.cs12.commu.protocol.amRtu206.common.RtuIdProtocol;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;

public class ReadAnswer_50  extends ProtocolAbstract{

	private static Logger log = LogManager.getLogger(ReadAnswer_50.class);
	
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

		log.info("分析<查询遥测终端或中继站地址>: RTU ID=" + d.getId() + " 数据：" + d.getSubData().toString());
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

		Data206_cd50 subD = new Data206_cd50() ;
		d.setSubData(subD) ;
		
		int index = Constant.Site_Data ;
		if(cp.hasDIVS){
			index += 1 ;
		}
		String rtuId = new RtuIdProtocol().parseRtuId(b, index , (index + Constant.Bits_RTU_ID - 1)) ;
		subD.setRtuId(rtuId);
		
		return d;
	}
}