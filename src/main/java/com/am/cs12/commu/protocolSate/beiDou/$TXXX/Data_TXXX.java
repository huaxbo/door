package com.am.cs12.commu.protocolSate.beiDou.$TXXX;

import com.am.cs12.commu.protocol.util.UtilProtocol;

public class Data_TXXX {

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

