package com.am.cs12.commu.protocol.amRtu206.cdF4;


public class Data206_cdF4 {
	
	private Integer angle;//门角度
	private Double hcho;//甲醛浓度，单位mg/m3
	private Integer dwarn1;//门报警-电池欠压
	private Integer dwarn2;//门报警-关门故障
	private Integer lockState;//锁状态
	private Integer lockOriginal;//锁原点
	private Integer lockWarn;//锁报警
	private Integer lockPower;//锁电源
	

	public String toString(){
		StringBuilder sder = new StringBuilder();
		sder.append("门角度=" + angle);
		sder.append(",甲醛浓度=" + hcho + "mg/m3");
		sder.append(",门报警-电池欠压=" + dwarn1);
		sder.append(",门报警-关门故障=" + dwarn2);
		sder.append(",锁状态=" + lockState);
		sder.append(",锁原点=" + lockOriginal);
		sder.append(",锁报警=" + lockWarn);
		sder.append(",锁电源=" + lockPower);
		
		return sder.toString();
	}


	public Integer getAngle() {
		return angle;
	}


	public void setAngle(Integer angle) {
		this.angle = angle;
	}


	public Double getHcho() {
		return hcho;
	}


	public void setHcho(Double hcho) {
		this.hcho = hcho;
	}


	public Integer getDwarn1() {
		return dwarn1;
	}


	public void setDwarn1(Integer dwarn1) {
		this.dwarn1 = dwarn1;
	}


	public Integer getDwarn2() {
		return dwarn2;
	}


	public void setDwarn2(Integer dwarn2) {
		this.dwarn2 = dwarn2;
	}


	public Integer getLockState() {
		return lockState;
	}


	public void setLockState(Integer lockState) {
		this.lockState = lockState;
	}


	public Integer getLockOriginal() {
		return lockOriginal;
	}


	public void setLockOriginal(Integer lockOriginal) {
		this.lockOriginal = lockOriginal;
	}


	public Integer getLockWarn() {
		return lockWarn;
	}


	public void setLockWarn(Integer lockWarn) {
		this.lockWarn = lockWarn;
	}


	public Integer getLockPower() {
		return lockPower;
	}


	public void setLockPower(Integer lockPower) {
		this.lockPower = lockPower;
	}

	
	
}
