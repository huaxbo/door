package com.am.cs12.config;

import gnu.io.SerialPort;


public class SerialPortVO {
	
	public String toString() {
		return new StringBuilder()
		.append("SerialPort[")
		.append("debugForNoTerminal:").append(debugForNoTerminal)
		.append("id:").append(id)
		.append(",name:").append(name)
		.append(",baudrate:").append(baudrate)
		.append(",dataBits:").append(dataBits)
		.append(",stopBits:").append(stopBits)
		.append(",parity:").append(parity)
		.append(",waitTime:").append(waitTime)
		.append(",protocolType:").append(protocolType)
		.append(",protocolName:").append(protocolName)
		.append(",sendInterval:").append(sendInterval)
		.append("]")
		.toString();
	}

	private boolean debugForNoTerminal ;//调试阶段，未接串口终端，以备业务系统感觉到发数据成功:设置为true，正式运行阶段，设置为false ;
	private String id;//ID号，其他部门以此来引用所对应的串口
	private String name;//端口名称
	private int baudrate;//波特率
	private int dataBits;//数据位数
	private int stopBits;//停止位数
	private int parity;//校验(none：不校验；odd：奇校验；even：偶校验)
	private long waitTime;//当串口有数据到来时，有个循环从串读数据读数据，每次循环相间隔的时长(毫秒)(取值0-500)
	private String protocolType;//端口对应的数据协议类型(当前只支持卫星协议(sate))
	private String protocolName;//端口对应的数据协议名称
	private int sendInterval ;//在串口上每次发送命令时间间隔(单位：毫秒，取值范围100-600000)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public boolean getDebugForNoTerminal() {
		return debugForNoTerminal;
	}

	public void setDebugForNoTerminal(boolean debugForNoTerminal) {
		this.debugForNoTerminal = debugForNoTerminal;
	}

	public int getBaudrate() {
		return baudrate;
	}

	public void setBaudrate(String baudrate) {
		try {
			this.baudrate = Integer.parseInt(baudrate);
		} catch (NumberFormatException ex) {
		}
		if (this.baudrate <= 0) {
			this.baudrate = 9600;
		}
	}
	public int getDataBits() {
		return dataBits;
	}

	public void setDataBits(String dataBits) {
		try {
			this.dataBits = Integer.parseInt(dataBits);
		} catch (Exception ex) {
		}
		switch (this.dataBits) {
		case SerialPort.DATABITS_5:
		case SerialPort.DATABITS_6:
		case SerialPort.DATABITS_7:
		case SerialPort.DATABITS_8:
			return;
		default:
			this.dataBits = SerialPort.DATABITS_8;
			return;
		}
	}

	public int getParity() {
		return parity;
	}

	public void setParity(String parity) {
		if ("none".equalsIgnoreCase(parity)) {
			this.parity = SerialPort.PARITY_NONE;
			return;
		}
		if ("odd".equalsIgnoreCase(parity)) {
			this.parity = SerialPort.PARITY_ODD;
			return;
		}
		if ("even".equalsIgnoreCase(parity)) {
			this.parity = SerialPort.PARITY_EVEN;
			return;
		}
		try {
			this.parity = Integer.parseInt(parity);
		} catch (Exception ex) {
		}
		switch (this.parity) {
		case SerialPort.PARITY_EVEN:
		case SerialPort.PARITY_MARK:
		case SerialPort.PARITY_NONE:
		case SerialPort.PARITY_ODD:
		case SerialPort.PARITY_SPACE:
			return;
		default:
			this.parity = SerialPort.PARITY_NONE;
			return;
		}
	}

	public int getStopBits() {
		return stopBits;
	}

	public void setStopBits(String stopBits) {
		try {
			this.stopBits = Integer.parseInt(stopBits);
		} catch (Exception ex) {
		}
		switch (this.stopBits) {
		case SerialPort.STOPBITS_1:
		case SerialPort.STOPBITS_1_5:
		case SerialPort.STOPBITS_2:
			return;
		default:
			this.stopBits = SerialPort.STOPBITS_1;
			return;
		}
	}

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public int getSendInterval() {
		return sendInterval;
	}

	public void setSendInterval(int sendInterval) {
		this.sendInterval = sendInterval;
	}

}
