package com.am.cs12.commu.protocol.amRtu206.cd00;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.common.ControlProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.ProtocolAbstract;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class Parse_00  extends ProtocolAbstract{
	private static Logger log = LogManager.getLogger(Parse_00.class);
	
	/**
	 * 分析RTU 数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Data parseData(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception {
		Data d = this.doParse(b, cp, dataCode) ;
		d.setId(rtuId) ;
		
		log.info("分析<RTU回还命令数据>: RTU ID=" + d.getId() + " 所回还的功能码为:" + d.getCode());
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

		Data206_cd00 subD = new Data206_cd00() ;
		d.setSubData(subD) ;
		
		int index = Constant.Site_Data ;
		if(cp.hasDIVS){
			index += 1 ;
		}
		//偷梁换柱，更换功能码为所回还的功能码
		d.setCode(new UtilProtocol().byte2Hex(new byte[]{(byte)b[index++]}, false)) ;		
		byte[] data = new byte[b.length - index - Constant.Bits_CRC - Constant.Bits_Tail ] ;
		for(int i = 0 ; i < data.length ; i++){
			data[i] = b[index++] ;
		}
		String content = new String(data) ;
		subD.setContent(content) ;
		
		return d;

	}
}