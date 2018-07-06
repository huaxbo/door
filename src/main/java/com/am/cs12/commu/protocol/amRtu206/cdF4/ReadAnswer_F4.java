package com.am.cs12.commu.protocol.amRtu206.cdF4;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.common.ControlProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.ProtocolAbstract;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;
import com.am.cs12.commu.protocol.util.UtilProtocol;

public class ReadAnswer_F4 extends ProtocolAbstract{

	private static Logger log = LogManager.getLogger(ReadAnswer_F4.class);
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
		
		log.info("分析<门状态报>应答: RTU ID=" + d.getId() + " 数据：" + d.getSubData().toString());
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

		Data206_cdF4 dd = new Data206_cdF4() ;
		d.setSubData(dd) ;
		
		parse(cp,b,dd);
		
		return d;
	}
	
	/**
	 * @param cp
	 * @param b
	 * @param dd
	 */
	public void parse(ControlProtocol cp, byte[] b,Data206_cdF4 dd){
		int n = Constant.Site_Data ;
		if(cp.hasDIVS){
			n += 1 ;
		}
		
		// 分析数据域
		UtilProtocol up = new UtilProtocol();
		if((b[n] & 0xFF) != 0xFF){
			Integer hcho = up.bytes2Int(b, n, 3,"DESC");
			dd.setHcho(new BigDecimal(hcho).divide(new BigDecimal(1000),
					3,BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		n += 3;
		
		dd.setDoorState((b[n] & 0xFF) == 0xFF ? null : (b[n] & 0xFF));
		n++;
		dd.setAngle((b[n] & 0xFF) == 0xFF ? null : (b[n] & 0xFF));
		n++;	
		dd.setLockMark((b[n] & 0xFF) == 0xFF ? null : (b[n] & 0xFF));
		n++;
		dd.setLockState((b[n] & 0xFF) == 0xFF ? null : (b[n] & 0xFF));
		n++;
		dd.setPowerMark((b[n] & 0xFF) == 0xFF ? null : (b[n] & 0xFF));
		n++;
		dd.setPowerState((b[n] & 0xFF) == 0xFF ? null : (b[n] & 0xFF));
		n++;
		dd.setWarnMark((b[n] & 0xFF) == 0xFF ? null : (b[n] & 0xFF));
		n++;
		dd.setWarnState((b[n] & 0xFF) == 0xFF ? null : (b[n] & 0xFF));
	}
}
