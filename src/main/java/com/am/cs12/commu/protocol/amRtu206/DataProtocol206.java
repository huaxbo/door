package com.am.cs12.commu.protocol.amRtu206;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.cd00.Parse_00;
import com.am.cs12.commu.protocol.amRtu206.cd01.Confirm_01;
import com.am.cs12.commu.protocol.amRtu206.cd02.LinkTest_02;
import com.am.cs12.commu.protocol.amRtu206.cd10_50.ReadAnswer_50;
import com.am.cs12.commu.protocol.amRtu206.cd11_51.ReadAnswer_51;
import com.am.cs12.commu.protocol.amRtu206.cd12_52.ReadAnswer_52;
import com.am.cs12.commu.protocol.amRtu206.cdF1.ReadAnswer_F1;
import com.am.cs12.commu.protocol.amRtu206.cdF2.ReadAnswer_F2;
import com.am.cs12.commu.protocol.amRtu206.cdF3.ReadAnswer_F3;
import com.am.cs12.commu.protocol.amRtu206.cdF4.ReadAnswer_F4;
import com.am.cs12.commu.protocol.amRtu206.common.CodeProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.ControlProtocol;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;

public class DataProtocol206 {
	protected boolean needSynchronizeClock ;//是否需要同步测控器时钟


	public boolean isNeedSynchronizeClock() {
		return needSynchronizeClock;
	}
	/**
	 * 得到出数据中的功能码
	 * @param b
	 * @return
	 */
	public String getDataCode(byte[] b, ControlProtocol cp) throws Exception{
		//分析ID
		int fromSite = Constant.Site_Code ;
		if(cp.hasDIVS){
			fromSite += 1 ;
		}
		String code = new CodeProtocol().parseCode(b, fromSite) ;
		return code.toUpperCase() ;
	}
	
	/**
	 * 分析回还命令
	 * @param b
	 * @throws Exception 
	 */
	public Data parseRecircle_00(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception{
		Parse_00 rp = new Parse_00() ;
		Data d = rp.parseData(rtuId, b, cp, dataCode) ;
		return d ;
	}

	/**
	 * 分析RTU确认
	 * @param b
	 * @throws Exception 
	 */
	public Data parseConfirm_01(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception{
		Confirm_01 rp = new Confirm_01() ;
		Data d = rp.parseData(rtuId, b, cp, dataCode) ;
		return d ;
	}

	/**
	 * 分析RTU定时上报数据
	 * @param b
	 * @throws Exception 
	 */
	public Data parseReport_02(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception{
		LinkTest_02 rp = new LinkTest_02() ;
		Data d = rp.parseData(rtuId, b, cp, dataCode) ;
//		
//		ProtocolBaseVO pb = ProtocolBaseVO.instance() ;
//		if(pb.synchronizeClockEnable){
//			if(rp.getMeterClock_hour() != pb.synchronizeClockdeny){
//				if(rp.getClockDifference_minute_abs() > pb.synchronizeClockDifference){
//					this.needSynchronizeClock = true ;
//				}
//			}
//		}
		return d ;
	}
	
	/**
	 * 分析查询RTU ID
	 * @param b
	 * @throws Exception 
	 */
	public Data parseRead_50(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception{
		ReadAnswer_50 rp = new ReadAnswer_50() ;
		Data d = rp.parseData(rtuId, b, cp, dataCode) ;
		return d ;
	}
		
	/**
	 * 分析应答查询遥测终端、中继站时钟
	 * @param b
	 * @throws Exception 
	 */
	public Data parseRead_51(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception{
		ReadAnswer_51 rp = new ReadAnswer_51() ;
		Data d = rp.parseData(rtuId, b, cp, dataCode) ;
		return d ;
	}
	/**
	 * 分析应答查询遥测终端、中继站时钟
	 * @param b
	 * @throws Exception 
	 */
	public Data parseRead_52(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception{
		ReadAnswer_52 rp = new ReadAnswer_52() ;
		Data d = rp.parseData(rtuId, b, cp, dataCode) ;
		return d ;
	}
	
	/**
     * 门操作
     * @param rtuId
     * @param b
     * @param cp
     * @param dataCode
     * @return
     * @throws Exception
     */
	public Data parseRead_F1(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception{
		ReadAnswer_F1 rp = new ReadAnswer_F1() ;
		Data d = rp.parseData(rtuId, b, cp, dataCode) ;
		return d ;
	}
	/**
     * 门操作
     * @param rtuId
     * @param b
     * @param cp
     * @param dataCode
     * @return
     * @throws Exception
     */
	public Data parseRead_F2(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception{
		ReadAnswer_F2 rp = new ReadAnswer_F2() ;
		Data d = rp.parseData(rtuId, b, cp, dataCode) ;
		return d ;
	}
	/**
     * 门操作
     * @param rtuId
     * @param b
     * @param cp
     * @param dataCode
     * @return
     * @throws Exception
     */
	public Data parseRead_F3(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception{
		ReadAnswer_F3 rp = new ReadAnswer_F3() ;
		Data d = rp.parseData(rtuId, b, cp, dataCode) ;
		return d ;
	}
	
	
	/**
     * 门状态
     * @param rtuId
     * @param b
     * @param cp
     * @param dataCode
     * @return
     * @throws Exception
     */
	public Data parseRead_F4(String rtuId, byte[] b, ControlProtocol cp, String dataCode) throws Exception{
		ReadAnswer_F4 rp = new ReadAnswer_F4() ;
		Data d = rp.parseData(rtuId, b, cp, dataCode) ;
		return d ;
	}
}
