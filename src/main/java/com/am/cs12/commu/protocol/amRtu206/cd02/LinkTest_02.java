package com.am.cs12.commu.protocol.amRtu206.cd02;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.common.*;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;
import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.automic.door.util.cmder.CmdPwdCache;

/**
 * 链路检测（AFN=02H）
 * 用于GPRS、CDMA登录、退出登录、在线保持。数据域：1个字节，F0登录，F1退出登录，F2在线保持。
 * @author Administrator
 */
public class LinkTest_02 extends ProtocolAbstract{

	private static Logger log = LogManager.getLogger(LinkTest_02.class);
	
	/**
	 * 分析RTU 数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Data parseData(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception {
		Data d = this.doParse(b, cp, dataCode,rtuId) ;
		d.setId(rtuId) ;
		d.setCode(dataCode) ;
		
		log.info("分析<RTU链路检测数据>: RTU ID=" + d.getId() + " 数据：" + d.getSubData().toString());
		return d;
	}
	/**
	 * 分析RTU 数据
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private Data doParse(byte[] b, ControlProtocol cp, String dataCode,String rtuId) throws Exception {
		Data d = new Data() ;

		Data206_cd02 subD = new Data206_cd02() ;
		d.setSubData(subD) ;
		
		int index = Constant.Site_Data ;
		if(cp.hasDIVS){
			index += 1 ;
		}
		subD.setFlag(new Integer((byte)b[index++])) ;
		subD.setPwd(new UtilProtocol().byte2Hex(new byte[]{
				b[index++],b[index]
		},false).toUpperCase());
		//更新设备操作密码
		CmdPwdCache.singleInstance().pushPwd(rtuId,subD.getPwd());
		
		return d;
	}

}
