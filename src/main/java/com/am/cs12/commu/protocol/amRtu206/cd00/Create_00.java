package com.am.cs12.commu.protocol.amRtu206.cd00;

import com.am.cs12.commu.protocol.amRtu206.Code206;
import com.am.cs12.commu.protocol.amRtu206.common.CodeProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.ControlProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.HeadProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.RtuIdProtocol;
import com.am.cs12.commu.protocol.amRtu206.common.TailProtocol;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;

public class Create_00 {
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
		 * @param recircleCode 所确认的功能码
		 * @param recircleContent 确认附加内容
		 * @param keyPassword
		 * @return
		 * @throws Exception
		 */
		public byte[] recircleData(String rtuId, String recircleCode, byte[] recircleContent, Integer[] keyPassword) throws Exception {
			
			int length = len + (recircleContent == null ? 0 : recircleContent.length) ;
			
			byte[] b = new byte[length];
			
			// 数据头
			b = new HeadProtocol().createHead(b, length);
			
			// 控制域
			byte DIV = Constant.DIV_no ;
			ControlProtocol cp = new ControlProtocol() ;
			b = cp.createFromRTUControl(b, DIV, Constant.FCB_Default, DIV, Constant.ControlFunCode_10);
			
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
			b = codep.createCode(b, Code206.cd_00, fromSite) ;
			
			//数据域为被确认的功能码
			fromSite = Constant.Site_Data ;
			if(cp.hasDIVS){
				fromSite += 1 ;
			}
			b = codep.createCode(b, recircleCode, fromSite) ;
			
			///////////////
			//内容
			fromSite++ ;
			for(int i = 0 ; i < recircleContent.length ; i++ ){
				b[fromSite++] = recircleContent[i] ;
			}
			
//			// 附加域
//			fromSite = b.length - (Constant.Bits_Password + Constant.Bits_Time + Constant.Bits_CRC + Constant.Bits_Tail) ;
//			AuxProtocol ap = new AuxProtocol() ;
//			b = ap.createPassword(b, fromSite, new int[]{keyPassword[0] , keyPassword[1]}) ;
//			
//			fromSite += 2 ;
//			b = ap.createTime(b, fromSite) ;
			
			// 数据尾(包括CRC)
			b = new TailProtocol().createTail(b);
			
			return b;
		}

}
