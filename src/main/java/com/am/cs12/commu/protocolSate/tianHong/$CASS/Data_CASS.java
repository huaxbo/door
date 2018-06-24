package com.am.cs12.commu.protocolSate.tianHong.$CASS;

public class Data_CASS {

	protected Boolean receivedSuccess ;
	
	public String toString(){
		String s = "\n终端接收外设数据：\n" ;
		s += (this.receivedSuccess==null?"空值":(receivedSuccess?"成功校验":"校验失败"));
		return s ;
	}

	public boolean isReceivedSuccess() {
		return receivedSuccess;
	}

	public void setReceivedSuccess(boolean receivedSuccess) {
		this.receivedSuccess = receivedSuccess;
	}

}
