package com.am.cs12.commu.protocol.amRtu206.cd01;

import com.am.cs12.commu.protocol.amRtu206.Code206;
import com.am.cs12.commu.protocol.amRtu206.common.*;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;

public class Write_01 {
	private static int len = Constant.Bits_Head 
							+ Constant.Bits_Control
							+ Constant.Bits_RTU_ID 
							+ Constant.Bits_Code 
							//+ Constant.Bits_Password 
							//+ Constant.Bits_Time 
							+ Constant.Bits_CRC
							+ Constant.Bits_Tail
							+ Constant.Bits_Code;//数据域长度
	/**
	 * 确认帧
	 * @param rtuId
	 * @param returnCode 所确认的功能码
	 * @param returnContent 确认附加内容
	 * @param keyPassword
	 * @return
	 * @throws Exception
	 */
	public byte[] remoteData(String rtuId, String returnCode, byte[] returnContent, byte DIVS, Integer[] keyPassword) throws Exception {
		
		int length = len + (returnContent == null ? 0 : returnContent.length) ;
		
		byte[] b = new byte[length];
		
		// 数据头
		b = new HeadProtocol().createHead(b, length);
		
		// 控制域
		byte DIV = Constant.DIV_no ;
		ControlProtocol cp = new ControlProtocol() ;
		b = cp.createToRTUControl(b, DIV, Constant.FCB_Default, DIV, Constant.ControlFunCode_0);
		
		// RTU ID
		int fromSite = Constant.Site_RTUID ;
		if(cp.hasDIVS){
			fromSite += 1 ;
		}
		
		RtuIdProtocol rp = new RtuIdProtocol() ;
		b = rp.createRtuId(b, rtuId, Constant.RTU_ID_type_RTU_206 , fromSite);
		
		//功能码
		CodeProtocol codep = new CodeProtocol() ;
		fromSite = Constant.Site_Code ;
		if(cp.hasDIVS){
			fromSite += 1 ;
		}
		b = codep.createCode(b, Code206.cd_02, fromSite) ;
		
		//数据域为被确认的功能码
		fromSite = Constant.Site_Data ;
		if(cp.hasDIVS){
			fromSite += 1 ;
		}
		b = codep.createCode(b, returnCode, fromSite) ;

		///////////////
		//内容
		if(returnContent != null){
			fromSite++ ;
			for(int i = 0 ; i < returnContent.length ; i++ ){
				b[fromSite++] = returnContent[i] ;
			}
		}

		// 数据尾(包括CRC)
		b = new TailProtocol().createTail(b);
		
		return b;
	}

}