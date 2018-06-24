package com.am.cs12.commu.protocol.amRtu206.cd01;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.common.*;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class Confirm_01 extends ProtocolAbstract{
	private static Logger log = LogManager.getLogger(Confirm_01.class);
	
	/**
	 * 分析RTU 数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Data parseData(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception {
		Data d = this.doParse(b, cp, dataCode) ;
		d.setId(rtuId) ;
		
		log.info("分析<RTU确认数据>: RTU ID=" + d.getId() + " 所确认的功能码为:" + d.getCode());
		return d;
	}
	/**
	 * 分析RTU 数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private Data doParse(byte[] b ,ControlProtocol cp, String dataCode) throws Exception {
		Data d = new Data() ;
		int index = Constant.Site_Data ;
		if(cp.hasDIVS){
			index += 1 ;
		}
		//偷梁换柱，更换功能码为所确认的功能码
		d.setCode(new UtilProtocol().byte2Hex(new byte[]{b[index]}, false)) ;
		return d;
	}
}

