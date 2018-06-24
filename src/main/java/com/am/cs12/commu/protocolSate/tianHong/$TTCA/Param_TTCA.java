package com.am.cs12.commu.protocolSate.tianHong.$TTCA;

import com.am.cs12.commu.protocol.util.UtilProtocol;

public class Param_TTCA {
	public static final String KEY = Param_TTCA.class.getName() ;

	protected String idSate;//远程测控终端所接卫星终端ID
	
	protected byte[] data ;//需要发送的数据
	
	public String toString() {
		String hex = "" ;
		try {
			if(this.data != null && this.data.length > 0){
				hex = new UtilProtocol().byte2Hex(this.data , true) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String s = super.toString() ;
		s += "\n向远程测控终端发送数据：\n" ;
		s += "远程卫星终端ID:" + ":" + (this.idSate==null?"":this.idSate) + "\n" ;
		s += "发向测控终端的数据:" + ":" + (this.data==null?"":hex) + "\n" ;
		return s ;
	}

	
	
	public String getIdSate() {
		return idSate;
	}

	public void setIdSate(String idSate) {
		this.idSate = idSate;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}


}
