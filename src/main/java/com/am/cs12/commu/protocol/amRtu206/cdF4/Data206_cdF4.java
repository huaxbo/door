package com.am.cs12.commu.protocol.amRtu206.cdF4;


public class Data206_cdF4 {

	private Double hcho;//甲醛浓度，单位mg/m3
	private Integer doorState;//门状态
	private Integer angle;//门角度
	private Integer lockMark;//锁标记
	private Integer lockState;//锁状态
	private Integer powerMark;//电源标记
	private Integer powerState;//电源状态
	private Integer warnMark;//报警标记
	private Integer warnState;//报警状态
	
	//扩展属性
	private Integer[] lockStates;//锁状态数组
	private Integer[] warnStates;//报警状态数组
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuilder sder = new StringBuilder();
		sder.append("甲醛浓度=" + hcho + "mg/m3");
		sder.append(",门状态=" + doorState);
		sder.append(",门角度=" + angle);
		sder.append(",锁标记=" + lockMark);
		sder.append(",锁状态=" + lockState);
		if(lockStates != null){
			sder.append("(");
			sder.append(lockStates[0] == 0 ? "关锁" : "开锁");
			sder.append("/");
			sder.append(lockStates[1] == 0 ? "锁不在原点" : "锁在原点");
			sder.append("/");
			sder.append(lockStates[2] == 0 ? "锁无电" : "锁有点");
			sder.append(")");
		}
		sder.append(",电源标记=" + powerMark);
		sder.append(",电源状态=" + powerState);
		sder.append(",报警标记=" + warnMark);
		sder.append(",报警状态=" + warnState);
		if(warnStates != null){
			sder.append("(");
			sder.append(warnStates[0] == 0 ? "正常" : "欠压");
			sder.append("/");
			sder.append(warnStates[1] == 0 ? "正常" : "过流");
			sder.append("/");
			sder.append(warnStates[2] == 0 ? "门正常" : "门异常");
			sder.append(")");
		}
		
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


	public Integer getLockState() {
		return lockState;
	}


	public void setLockState(Integer lockState) {
		this.lockState = lockState;
		if(lockState == null){
			
			return ;
		}
		//锁状态数组
		lockStates = new Integer[8];
		for(int i = 0;i < 8;i++){
			lockStates[i] = (lockState >> i) & 0x1;
		}
	}


	public Integer getDoorState() {
		return doorState;
	}


	public void setDoorState(Integer doorState) {
		this.doorState = doorState;
	}


	public Integer getLockMark() {
		return lockMark;
	}


	public void setLockMark(Integer lockMark) {
		this.lockMark = lockMark;
	}


	public Integer getPowerMark() {
		return powerMark;
	}


	public void setPowerMark(Integer powerMark) {
		this.powerMark = powerMark;
	}


	public Integer getPowerState() {
		return powerState;
	}


	public void setPowerState(Integer powerState) {
		this.powerState = powerState;
	}


	public Integer getWarnMark() {
		return warnMark;
	}


	public void setWarnMark(Integer warnMark) {
		this.warnMark = warnMark;
	}


	public Integer getWarnState() {
		return warnState;
	}


	public void setWarnState(Integer warnState) {
		this.warnState = warnState;
		if(warnState == null){
			
			return ;
		}
		//报警状态数组
		warnStates = new Integer[8];
		for(int i = 0;i < 8;i++){
			warnStates[i] = (warnState >> i) & 0x1;
		}
		
	}


	public Integer[] getLockStates() {
		return lockStates;
	}


	public void setLockStates(Integer[] lockStates) {
		this.lockStates = lockStates;
	}


	public Integer[] getWarnStates() {
		return warnStates;
	}


	public void setWarnStates(Integer[] warnStates) {
		this.warnStates = warnStates;
	}
	
}
