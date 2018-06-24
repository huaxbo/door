package com.am.cs12.commu.protocolSate.tianHong.$TSTA;

public class Data_TSTA {

	protected Integer channelOneEleLevel ;//通道一功率电平，当大于等于1时，即能发送数据
	protected Integer channelTwoEleLevel ;//通道二功率电平，当大于等于1时，即能发送数据
	
	protected Boolean powerStatus ; //为true时，表示电能源可以使终端正常工作，反之不能正常工作
	
	protected String thisSateId ;//自身ID
	
	protected Integer serverFrequency;//服务频度(单位为秒，即间隔多少秒钟，卫星系统给服务(发送数据)一次)

	public String toString(){
		String s = super.toString() ;
		s += "\n卫星终端状态：\n" ;
		s += "通道一功率电平:" + (this.channelOneEleLevel==null?"":channelOneEleLevel-48) + "\n" ;
		s += "通道二功率电平:" + (this.channelTwoEleLevel==null?"":channelOneEleLevel-48) + "\n" ;
		s += "供电满足终端正常工作:" + (this.powerStatus==null?"":powerStatus?"是":"否") + "\n" ;
		s += "终端服务频度:" + (this.serverFrequency==null?"":(serverFrequency + "秒"))  + "\n" ;
		s += "本地卫星终端ID:" + (this.thisSateId==null?"":thisSateId)  + "\n" ;
		return s ;
	}

	public Integer getChannelOneEleLevel() {
		return channelOneEleLevel;
	}

	public void setChannelOneEleLevel(Integer channelOneEleLevel) {
		this.channelOneEleLevel = channelOneEleLevel;
	}

	public Integer getChannelTwoEleLevel() {
		return channelTwoEleLevel;
	}

	public void setChannelTwoEleLevel(Integer channelTwoEleLevel) {
		this.channelTwoEleLevel = channelTwoEleLevel;
	}

	public Boolean getPowerStatus() {
		return powerStatus;
	}

	public void setPowerStatus(Boolean powerStatus) {
		this.powerStatus = powerStatus;
	}

	public String getThisSateId() {
		return thisSateId;
	}

	public void setThisSateId(String thisSateId) {
		this.thisSateId = thisSateId;
	}

	public Integer getServerFrequency() {
		return serverFrequency;
	}

	public void setServerFrequency(Integer serverFrequency) {
		this.serverFrequency = serverFrequency;
	}
	
	
}
