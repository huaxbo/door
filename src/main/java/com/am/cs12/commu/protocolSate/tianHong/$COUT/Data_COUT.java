package com.am.cs12.commu.protocolSate.tianHong.$COUT;

import com.am.cs12.commu.protocol.util.UtilProtocol;

public class Data_COUT {

	protected byte[] data ;
	
	public String toString(){
		if(data != null){
			try {
				return new UtilProtocol().byte2Hex(data , true) ;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "" ;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
