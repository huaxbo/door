package com.am.cs12.commu.protocolSate.tianHong;


import com.am.cs12.commu.protocol.util.UtilProtocol;

public class CodeTH {
	//查询终端(卫星基站)状态
	public static final String $QSTA = "$QSTA" ; 
	//终端(卫星基站)响应外设，表示成功收到外设传来的数据
	public static final String $CASS = "$CASS" ; 
	//终端(卫星基站)响应外设查询终端(卫星基站)状态
	public static final String $TSTA = "$TSTA" ; 
	//外设请求终端(卫星基站)向卫星发送数据
	public static final String $TTCA = "$TTCA" ; 
	//终端(卫星基站)将收到的卫星数据传给外设
	public static final String $COUT = "$COUT" ; 

	//申请授时
	public static final String $TAPP = "$TAPP" ; 
	//授时应答
	public static final String $TINF = "$TINF" ; 
	
	public static void main(String[] args) throws Exception{
		byte[] b = $QSTA.getBytes() ;
		UtilProtocol u = new UtilProtocol() ;
		String hex = u.byte2Hex(b , true) ;
		
		System.out.println(hex) ;
		
		String s = new String(b) ;
		
		System.out.println(s) ;
	}
}
