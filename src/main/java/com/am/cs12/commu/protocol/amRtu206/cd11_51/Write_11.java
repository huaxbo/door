package com.am.cs12.commu.protocol.amRtu206.cd11_51;

import java.util.Arrays;
import java.util.HashMap;

import com.am.cs12.commu.protocol.amRtu206.Code206;
import com.am.cs12.commu.protocol.amRtu206.common.*;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;
import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.util.DateTime;

public class Write_11 {
	private static int len = Constant.Bits_Head 
									+ Constant.Bits_Control
									+ Constant.Bits_RTU_ID 
									+ Constant.Bits_Code 
									+ Constant.Bits_Password 
									+ Constant.Bits_Time 
									+ Constant.Bits_CRC
									+ Constant.Bits_Tail 
									+ 6 ;//数据域长度
	/**
	 * 构造设置RTU 命令
	 * @param rtuId RTU ID
	 * @param params 参数集合
	 * @param keyPassword 密钥与密码
	 * @return
	 * @throws Exception
	 */
	public byte[] remoteData(String rtuId, HashMap<String , Object> params, Integer[] keyPassword) throws Exception {
		/////////////////////////////
		//构造数据
		byte[] b = new byte[len];

		// 数据头
		b = new HeadProtocol().createHead(b, len);

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
		fromSite = Constant.Site_Code ;
		if(cp.hasDIVS){
			fromSite += 1 ;
		}
		b = new CodeProtocol().createCode(b, Code206.cd_11, fromSite) ;
		
		
		
		UtilProtocol u = new UtilProtocol() ;
		//数据域，时钟
		int n = Constant.Site_Data ;
		if(cp.hasDIVS){
			fromSite += 1 ;
		}
		byte secode = u.string2byte(DateTime.ss()) ;
		b[n++] = u.int2BCD(secode)[0] ;
		byte minute = u.string2byte(DateTime.mm()) ;
		b[n++] = u.int2BCD(minute)[0] ;
		byte hour = u.string2byte(DateTime.HH()) ;
		b[n++] = u.int2BCD(hour)[0] ;
		byte date = u.string2byte(DateTime.dd()) ;
		b[n++] = u.int2BCD(date)[0] ;
		byte month = u.string2byte(DateTime.MM()) ;
		b[n++] = u.int2BCD(month)[0] ;
		byte year = u.string2byte(DateTime.yy()) ;
		b[n++] = u.int2BCD(year)[0] ;
		
		// 附加域
		fromSite = b.length - (Constant.Bits_Password + Constant.Bits_Time + Constant.Bits_CRC + Constant.Bits_Tail) ;
		AuxProtocol ap = new AuxProtocol() ;
		b = ap.createPassword(b, fromSite, new int[]{keyPassword[0] , keyPassword[1]}) ;
		
		fromSite += 2 ;
		b = ap.create206Time(b, fromSite) ;

		if(cp.hasDIVS){
			byte[] ct = Arrays.copyOf(b, b.length+1);
			ct = new TailProtocol().createTail(ct);
			return ct;
		}
		// 数据尾(包括CRC)
		b = new TailProtocol().createTail(b);
		
		return b;
	}

}
