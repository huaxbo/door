package com.automic.door.web.app;

public class DoorVO {

	private String dtuId;//设备id
	
	private String rltDtuId;//回执：设备id
	private String rltState;//回执：门状态,0:关、1：开、2：停
	
	private String succ;//成功提示，1:成功、0：失败
	private String error;//异常提示，提示信息
	
	public String getDtuId() {
		return dtuId;
	}

	public void setDtuId(String dtuId) {
		this.dtuId = dtuId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getSucc() {
		return succ;
	}

	public void setSucc(String succ) {
		this.succ = succ;
	}

	public String getRltDtuId() {
		return rltDtuId;
	}

	public void setRltDtuId(String rltDtuId) {
		this.rltDtuId = rltDtuId;
	}

	public String getRltState() {
		return rltState;
	}

	public void setRltState(String rltState) {
		this.rltState = rltState;
	}
	
	
	
}
