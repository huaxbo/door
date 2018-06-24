package com.am.cs12.commu.protocol.amRtu206.common;

import java.util.Date;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.util.Constant;
import com.am.cs12.commu.protocol.amRtu206.util.ConstantImage;
import com.am.cs12.commu.protocol.util.CRC16;
import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.util.DateTime;

public class CommonProtocol {

	private static UtilProtocol up = new UtilProtocol();
	public static final int SERIAL_ENQ = 0;//查询报文流水号
	
	/**
	 * 转换总包/包序为字节数组
	 * @param total
	 * @param index
	 * @return
	 */
	public static byte[] buildMuliPackages(int total,int index){
		//包总数及包序号
		total <<=12;
		total &= 0xFFF000;
		total += index;
		return new UtilProtocol().int2bytes(total);
	}
	/**
	 * 获取包总数、包序号
	 * @param b
	 * @return arr[0]:总包数、arr[1]：包序号
	 */
	public static int[] getMuliPackages(byte[] b){
		int[] arr = new int[2];
		int idx = 0;
		byte a1 = b[ConstantImage.Site_Report + idx++];
		byte a2 = b[ConstantImage.Site_Report + idx++];
		byte a3 = b[ConstantImage.Site_Report + idx++];
				
		//统计总包数
		int i1 = a1 & 0xFF;
		int i2_1 = (a2>>4) & 0x0F;
		i1 <<= 4;
		i1 += i2_1;
		//统计包序号
		int i2_2 = a2 & 0x0F;
		i2_2 <<= 8;
		int i3 = a3 & 0xFF;
		i3 += i2_2;
		
		arr[0] = i1;
		arr[1] = i3;
		
		return arr;
	}
	/**
	 * 获取多包报文结束符
	 * @param b
	 * @return
	 */
	public static byte getMuliPackagesReportTrail(byte[] b){
		if(b != null){
			return b[b.length-3];
		}
		return ConstantImage.Control_ENQ;
	}
	/**
	 * 获取当前包
	 * @param b
	 * @return
	 */
	public static byte getPackageNum(byte[] b){
		
		return b[Constant.Site_Control + 1];
	}
	/**
	 * 获取206协议图像数据报长度
	 * @param bts
	 * @return
	 */
	public static int getLenForPic(byte[] bts){
		int tlen = up.bytes2Int(new byte[]{bts[Constant.Site_Data+2],bts[Constant.Site_Data+1]},0,2);
		
		return tlen + 2 + 2+ Constant.Bits_Control + Constant.Bits_RTU_ID;//构建数据长度
	}
	/**
	 * 获取功能码
	 * @param b
	 * @param div
	 * @return
	 */
	public static byte getFunc(byte[] b,int div){
		
		return b[Constant.Site_Code + div];
	}
	/**
	 * 解析上报数据正文前部
	 * 流水号+发报时间
	 * @param b
	 * @param idx
	 * @param hasRemote
	 * @param d
	 * @return
	 */
	public static int anlyzedContentPrepare(byte[] b,int idx,boolean hasRemote,Data d){
		try{
			int serialNum = up.bytes2Int(b, ConstantImage.Site_Report + idx, 2);
			idx += 2;
			//d.setSerialNum(serialNum);
			// 发报时间
			String rt = up.BCD2String(b, ConstantImage.Site_Report + idx,
					ConstantImage.Site_Report + idx + 5);
			idx += 6;
			if(d != null){
				d.setClock(DateTime.yyMMddHHmmss2yyyy_MM_dd_HH_mm_ss(rt));
			}
			if(hasRemote){
				// 遥测站地址
				idx += 2;
				String rtuId = up.BCD2String(b, ConstantImage.Site_Report + idx,
						ConstantImage.Site_Report + idx + 4);
				idx += 5;
				if (rtuId != null && !rtuId.equals("")) {
					d.setId(rtuId);
				}
			}
		}catch(Exception e){
			
		}finally{}
		
		return idx;
	}
	/**
	 * 构建报文正文之前数据
	 * @param b
	 * @param idx
	 * @param center
	 * @param dtuId
	 * @param password
	 * @param funcCode
	 * @param reportHead
	 * @param contentLen
	 * @return
	 */
	public static int buildContentBefore(byte[] b,
			int idx,
			int center, 
			String dtuId, 
			int password,
			String funcCode,
			byte reportHead,
			int contentLen){
		try{
			// 帧起始符
			b[idx++] = ConstantImage.HEAD;
			b[idx++] = ConstantImage.HEAD;
			// 遥测站地址
			byte[] r = up.string2BCD(dtuId);
			idx = CommonProtocol.insert(b, r, idx, ConstantImage.Bits_Remote);
			// 中心站地址
			b[idx++] = (byte) center;
			// 密码
			byte[] p = up.int2bytes(password);
			idx = CommonProtocol.insert(b, p, idx, ConstantImage.Bits_Password);
			// 功能码
			idx = CommonProtocol.insert(b, up.hex2Bytes(funcCode), idx,
					ConstantImage.Bits_FuncCode);
			// 下行标识+报文正文长度
			int rlen = ((contentLen & 0x0FFF) | 0x8000) & 0x0FFFF;
			byte[] l = up.int2bytes(rlen);
			idx = CommonProtocol.insert(b, l, idx, ConstantImage.Bits_UdLen);
			// 报文起始符
			b[idx++] = reportHead;
		}catch(Exception e){
			
		}finally{}
		return idx;
	}
	/**
	 * 构建正文起始
	 * 流水号 + 发报时间
	 * @param b
	 * @param idx
	 * @param serialNum
	 * @return
	 */
	public static int buildContentPrepare(byte[] b,int idx,int serialNum){
		//流水号
		idx = CommonProtocol.insert(b,up.int2bytes(serialNum),idx,2);
		//构造发报时间
		byte[] tm = CommonProtocol.createReportTime();
		for(int j=0;j<tm.length;j++){
			b[idx++] = tm[j];
		}
		return idx;
	}
	/**
	 * 构建正文之后数据
	 * @param b
	 * @param idx
	 * @param reportTrail
	 * @return
	 */
	public static int buildContentAfter(byte[] b,int idx,byte reportTrail){
		b[idx++] = reportTrail;
		// 校验码
		byte[] crc = new CRC16().CRC2bytes(CommonProtocol.bytesToCRS(b));
		idx = CommonProtocol.insert(b, crc, idx, ConstantImage.Bits_CRC);
		return idx;
	}
	
	/**
	 * 解析数据要素值-BCD编码
	 * @param b
	 * @param idx
	 * @return {后续序列号，数据值，数据要素标识}
	 */
	public static Object[] getDataBCD(byte[] b,int idx){
		int idfier_0=0;
		int dLen=0;
		try{
			//标识符
		    idfier_0 = b[ConstantImage.Site_Report + idx++] & 0xFF;//信息标识符
		    if(idfier_0 - 0xFF == 0){//标识符为扩展
		    	idfier_0 = (idfier_0 << 8) | (b[ConstantImage.Site_Report + idx++] & 0xFF);
		    	idfier_0 &= 0xFFFF;
		    }
		    
			int idfier_1 = b[ConstantImage.Site_Report + idx++] & 0xFF;//数据定义
			
			Double dv=null;
			String strdv=null;
			//数据长度
			 dLen = (idfier_1&0x0F8)>>3;
			//数据定义自定义长度
			int dotLen = (idfier_1&0x07);
			byte flag = b[ConstantImage.Site_Report + idx];			
			if(idfier_0==ConstantImage.Identify_PT_FIVE){//1小时5分钟雨量
				dLen=12;
				strdv=fiveRainNumber(b, ConstantImage.Site_Report + idx, ConstantImage.Site_Report + idx + dLen - 1);
			}else if(idfier_0==ConstantImage.Identify_Z_FIVE){//1小时5分钟水位
				dLen=24;
				strdv=fiveZNumber(b, ConstantImage.Site_Report + idx, ConstantImage.Site_Report + idx + dLen - 1);
			}else{
				if(flag == (byte)(ConstantImage.Identify_Negative & 0xFF)){//负数
					idx++;
					dLen -= 1;
				}
				String v = up.BCD2String(b, ConstantImage.Site_Report + idx, ConstantImage.Site_Report + idx + dLen - 1);
				//dv = Double.parseDouble(v.substring(0,dLen*2-dotLen) + "." + v.substring(dLen*2-dotLen));
				dv=Double.parseDouble(returnData(v,dotLen));
				if(flag == (byte)(ConstantImage.Identify_Negative & 0xFF)){//负数
					dv = -dv;
				}
			}
			idx += dLen;
			
			return new Object[]{idx,dv!=null?dv:strdv,idfier_0};
		}catch(Exception e){
			//if(idfier_0 == ConstantImage.Identify_Z){//采集水位失败:aa aa aa ff处理
				idx += dLen;
				return new Object[]{idx,null,idfier_0};
			//}
		}finally{;}
		
		//return null;
	}
	public static String returnData(String data,int num){
		String temp="";
		if(data==null||data.equals("")){
			data="0";
		}
		if(data.length()<=num){
			num++;
			for(int i=0;i<num-data.length();i++){
				temp=temp+"0";
			}
			data=temp+data;
			num--;
		}
		data=data.substring(0,data.length()-num)+"."+data.substring(data.length()-num);
		return data;
	}
	/**
	 * 1小时内5分钟雨量，水位
	 * @param b
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	private static String fiveRainNumber(byte[] b, int startIndex, int endIndex){
		String temp="";
		if(b!=null){
			int t=0;
			for(int i=startIndex;i<endIndex+1;i++){
					t=(t << 8) ^ (b[i] & 0xFF);
					String m=t+"";
					temp+=(m.length()>1?m.substring(0, m.length()-1)+"."+m.substring(m.length()-1):"0."+m)+";";
					t=0;
			}
			temp=temp.substring(0, temp.length()-1);
		}
		return temp; 
	}
	/**
	 * 1小时内5分钟水位
	 * @param b
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	private static String fiveZNumber(byte[] b, int startIndex, int endIndex){
		String temp="";
		if(b!=null){
			int t=0;
			int c=0;
			for(int i=startIndex;i<endIndex+1;i++){
				c++;
				if(c%2==0){
					t=(t << 8) ^ (b[i] & 0xFF);
					String m=t+"";
					temp+=(m.length()>2?m.substring(0, m.length()-2)+"."+m.substring(m.length()-2):(m.length()==2?"0."+m:"0.0"+m))+";";
					c=0;
					t=0;
				}else{
					t=(t << 8) ^ (b[i] & 0xFF);
				}
			}
			temp=temp.substring(0, temp.length()-1);
		}
		return temp; 
	}
	
	/**
	 * @param b
	 * @param idx
	 * @return
	 */
	public static Object[] getDataBCD2(byte[] b,int idx,int idfier_1){
		try{
			if((b[ConstantImage.Site_Report + idx] & 0xFF) == 0xFF 
					&& (b[ConstantImage.Site_Report + idx+1] & 0xFF) == 0xFF){//无数据
				return null;
			}
			if((b[ConstantImage.Site_Report + idx] & 0xFF) == 0xFF){//负数
				idfier_1 += 8;
			}
			//标识符
			idfier_1 &= 0xFF;//数据定义
			//数据长度
			int dLen = (idfier_1&0x0F8)>>3;
			//数据定义自定义长度
			int dotLen = (idfier_1&0x07);
			byte flag = b[ConstantImage.Site_Report + idx];
			if(flag == (byte)(ConstantImage.Identify_Negative & 0xFF)){//负数
				idx++;
				dLen -= 1;
			}
			String v = up.BCD2String(b, ConstantImage.Site_Report + idx, ConstantImage.Site_Report + idx + dLen - 1);
			idx += dLen;
			
			Double dv = Double.parseDouble(v.substring(0,dLen*2-dotLen) + "." + v.substring(dLen*2-dotLen));
			if(flag == (byte)(ConstantImage.Identify_Negative & 0xFF)){//负数
				dv = -dv;
			}
			return new Object[]{idx,dv};
		}catch(Exception e){
			
		}finally{}
		
		return null;
	}
	/**
	 * 解析要素数据-hex编码
	 * @param b
	 * @param idx
	 * @return
	 */
	public static Object[] getDataHex(byte[] b,int idx){
		try{
			//标识符
			int idfier_0 = b[ConstantImage.Site_Report + idx++] & 0xFF;//信息标识符
			int idfier_1 = b[ConstantImage.Site_Report + idx++] & 0xFF;//数据定义
			//数据长度
			int dLen = (idfier_1&0x0F8)>>3;
			Integer dv = up.bytes2Int(b, ConstantImage.Site_Report + idx,dLen);
			idx += dLen;
			return new Object[]{idx,dv,idfier_0};
		}catch(Exception e){
			
		}finally{}
		
		return null;
	}
	/**
	 * 构建数据
	 * @param data 只能是Integer或者Double类型
	 * @return {数据定义字节码，数据值字节数组}
	 */
	public static Object[] buildData(Object data){
		if(data == null){
			return null;
		}
		try{
			Object[] oo = new Object[2];
			byte df = 0;//数据定义字节码
			byte[] db = null;//数据字节数组
			Double d = null;
			//整型数据处理
			if(data instanceof Integer){
				d = Double.valueOf((Integer)data);
			}else{
				d = (Double)data;
			}
			Double dd = Math.abs(d);
			String v = dd.toString();
			String[] vv = v.split("\\.");
			v = vv[0] + vv[1];
			byte[] b = up.string2BCD(v);
			df = (byte)((b.length * 8 + vv[1].length()) & 0xFF);
			if(d<0){
				df += 8;
			}
			db = new byte[df/8];
			int idx = 0;
			if(db.length > b.length){
				db[idx++] = (byte)ConstantImage.Identify_Negative;
			}
			for(int i=0;i<b.length;i++,idx++){
				db[idx] = b[i];
			}
			if(db != null){
				oo[0] = df;
				oo[1] = db;
				return oo;
			}
		}catch(Exception e){
			
		}finally{}
		
		return null;
	}
	/**
	 * @param data
	 * @param len：数据域总长度（字符数）
	 * @param dotLen:小数点后位数（字符数）
	 * @return
	 */
	public static Object[] buildData2(Object data,int len,int dotLen){
		if(data == null){
			return null;
		}
		try{
			len = (len/2 + len%2);//补全字节
			Object[] oo = new Object[2];
			byte df = 0;//数据定义字节码
			byte[] db = null;//数据字节数组
			Double d = null;
			//整型数据处理
			if(data instanceof Integer){
				d = Double.valueOf((Integer)data);
			}else{
				d = (Double)data;
			}
			Double dd = Math.abs(d);
			String v = dd.toString();
			String[] vv = v.split("\\.");
			v = vv[0];
			if(dotLen>0){
				v +=  vv[1];
				for(int i=0;i<dotLen-vv[1].length();i++){
					v+= "0";
				}
			}
			byte[] btmp = up.string2BCD(v);
			byte[] b = new byte[len];
			int k = 0;
			for(;k<len-btmp.length;k++){
				b[k] = 0;
			}
			for(int j = 0;j<btmp.length;j++,k++){
				b[k] = btmp[j];
			}
			df = (byte)((b.length * 8 + dotLen) & 0xFF);
			if(d<0){
				df += 8;
			}
			db = new byte[df/8];
			int idx = 0;
			if(db.length > b.length){
				db[idx++] = (byte)ConstantImage.Identify_Negative;
			}
			for(int i=0;i<b.length;i++,idx++){
				db[idx] = b[i];
			}
			if(db != null){
				oo[0] = df;
				oo[1] = db;
				return oo;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{}
		
		
		return null;
	}
	/**
	 * src[]插入b[]
	 * @param b
	 * @param src
	 * @param idx：b[]位置：从0起始
	 * @param len：插入b[]字节长度
	 * @return
	 */
	public static int insert(byte[] b,byte[] src,int idx,int len){
		if(b == null || src == null){
			return idx;
		}
		for(int i=0;i<len-src.length;i++){
			b[idx++] = 0;
		}
		for(int i=0;i<src.length;i++){
			b[idx++] = src[i];
		}
		return idx;
	}
	/**
	 * 获取报文正文长度
	 * @param bts
	 * @return
	 */
	public static int getReportLen(byte[] bts){
		if(bts != null && bts.length>=ConstantImage.Site_UdLen + ConstantImage.Bits_UdLen){
			byte[] btemp = new byte[ConstantImage.Bits_UdLen];
			for(int i = 0;i <btemp.length;i++){
				btemp[i] = bts[ConstantImage.Site_UdLen + i];
			}
			btemp[0]&=0x0F;
			return up.bytes2Int(btemp, 0, btemp.length) ;
		}
		return 0;
	}
	/**
	 * 获取中心站地址
	 * @param bts
	 * @return
	 */
	public static int getCenter(byte[] bts,int site){
		if(bts != null && bts.length>=site+ConstantImage.Bits_Center){
			return up.bytes2Int(bts, site,ConstantImage.Bits_Center);
		}
		return 1;
	}
	/**
	 * 获取测站地址
	 * @param bts
	 * @param site
	 * @return
	 */
	public static String getRemote(byte[] bts,int site){
		String r = null;
		if(bts != null && bts.length>=site+ConstantImage.Bits_Remote){
			try {
				r = up.BCD2String(bts, site, site + ConstantImage.Bits_Remote-1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
		}
		return r;
	}
	/**
	 * 获取密码
	 * @param bts
	 * @return
	 */
	public static int getPassword(byte[] bts){
		if(bts != null && bts.length>=ConstantImage.Site_Password + ConstantImage.Bits_Password){
			return up.bytes2Int(bts, ConstantImage.Site_Password, ConstantImage.Bits_Password);
		}
		return 0;
	}
	/**
	 * 获取功能码
	 * @param bts
	 * @return
	 */
	public static String getFuncCode(byte[] bts){
		if(bts != null && bts.length>=ConstantImage.Site_FuncCode + ConstantImage.Bits_FuncCode){
			byte[] btmp = new byte[ConstantImage.Bits_FuncCode];
			for(int i=0;i<btmp.length;i++){
				btmp[i] = bts[ConstantImage.Site_FuncCode+i];
			}
			try {
				return up.byte2Hex(btmp, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
		}
		return null;
	}
	/**
	 * 获取报文起始符
	 * @param bts
	 * @return
	 */
	public static byte getReportHead(byte[] bts){
		if(bts != null && bts.length>=ConstantImage.Site_ReportHead + ConstantImage.Bits_reportHead){
			return bts[ConstantImage.Site_ReportHead];
		}
		return 0;
	}
	/**
	 * @param bts
	 * @return
	 */
	public static byte getReportTrail(byte[] bts){
		int len = getReportLen(bts);
		if(bts != null && bts.length>=ConstantImage.Site_Report + len + ConstantImage.Bits_reportTrail){
			return bts[ConstantImage.Site_Report + len];
		}
		return 0;
	}
	/**
	 * @param bts
	 * @return
	 */
	public static int getCRC(byte[] bts){
		int len = getReportLen(bts);
		if(bts != null && bts.length>=ConstantImage.Site_Report + len + ConstantImage.Bits_reportTrail + ConstantImage.Bits_CRC){
			
			return up.bytes2Int(bts, ConstantImage.Site_Report + len + ConstantImage.Bits_reportTrail, ConstantImage.Bits_CRC);
		}
		return 0;
	}
	/**
	 * 计算CRC校验
	 * @param bts
	 * @return
	 */
	public static int bytesToCRS(byte[] bts){
		if(bts != null){
			int len = getReportLen(bts);
			if(bts.length>=ConstantImage.Site_ReportHead + len + ConstantImage.Bits_reportTrail){
				byte[] btmp = new byte[ConstantImage.Site_Report + len + ConstantImage.Bits_reportTrail];
				for(int i=0;i<btmp.length;i++){
					btmp[i] = bts[i];
				}
				return CRC16_forHydro.cal_crc16(btmp);
			}
		}
		return 0;
	}
	
	/**
	 * 获取流水号
	 * @param b
	 * @return
	 */
	public static byte[] getSerialNumUp(byte[] bts){
		int len = getReportLen(bts);
		if(bts != null && bts.length>=ConstantImage.Site_ReportHead + len){
			byte[] sn = new byte[2];
			for(int i=0;i<sn.length;i++){
				sn[i] = bts[ConstantImage.Site_Report + i];
			}
			return sn;
		}
		return null;
	}
	/**
	 * 获取流水号
	 * @param bts
	 * @return
	 */
	public static int getSerialNumUpForInt(byte[] bts){
		int len = getReportLen(bts);
		if(bts != null && bts.length>=ConstantImage.Site_ReportHead + len){
			byte[] sn = new byte[2];
			for(int i=0;i<sn.length;i++){
				sn[i] = bts[ConstantImage.Site_Report + i];
			}
			return up.bytes2Int(sn, 0, 2);
		}
		return 0;
	}
	/**
	 * 获取发报时间
	 * @return
	 */
	public static byte[] createReportTime(){
		String tm = DateTime.yyMMddhhmmss(new Date(System.currentTimeMillis()));
		try {
			return up.string2BCD(tm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{}
		return null;
	}
	/**
	 * 获取相同功能码不同要素标识符
	 * @param bts
	 * @return
	 */
	public static String getFuncCodeIdentify(byte[] bts,int idx){
		int len = getReportLen(bts);
		if(bts != null && bts.length>=ConstantImage.Site_ReportHead + len){
			byte idf = bts[ConstantImage.Site_Report + idx];
			try {
				return up.byte2Hex(new byte[]{idf}, false).toUpperCase();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
		}
		return "";
	}
	/**
	 * 是否为信道参数回复结果命令
	 * @param identifier
	 * @return
	 */
	public static boolean isCenterChannelResult(String identifier){
		if(identifier == null){
			return false;
		}
		try{
			identifier = identifier.trim();
			for(int i=0;i<ConstantImage.Identify_Code_Center_main.length;i++){
				if(up.hex2Int(identifier) == ConstantImage.Identify_Code_Center_main[i]){
					return true;
				}
			}
			for(int i=0;i<ConstantImage.Identify_Code_Center_stock.length;i++){
				if(up.hex2Int(identifier) == ConstantImage.Identify_Code_Center_stock[i]){
					return true;
				}
			}
		}catch(Exception e){
			
		}finally{}
		
		return false;
	}
	
	/**
	 * 构建数据定义
	 * @param dLen：数据总长度
	 * @param dotLen：小数点后位数
	 * @return
	 */
	public static byte buildDataDef(int dLen,int dotLen){
		dLen = dLen/2 + dLen%2;
		byte b = (byte)dLen;
		b <<= 3;
		dotLen &= 7;
		b |= dotLen;
		
		return b;
	}	
		
}
