package com.am.cs12.commu.remote_serialPort;

import java.util.*;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import java.io.*;

import org.apache.logging.log4j.Logger;

import com.am.util.NumberUtil;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import com.am.cs12.config.*;


public class SerialPortChannel  implements SerialPortEventListener  {
	
	private static Logger log = LogManager.getLogger(SerialPortChannel.class) ;
	
	private SerialPort serialPort;
	private SerialPortVO configVO ;
	private String appName ;
	private int getPortDelay ;
	
	/**
	 * 构造方法
	 * @param configVO 串口参数配置
	 * @param appName 就用程序名称
	 * @param getPortDelay 得到串口前及循环得到串口时的延迟时长(CommPortIdentifier原码中，每次最小延迟200毫秒)
	 */
	public SerialPortChannel(SerialPortVO configVO , String appName, int getPortDelay) {
		this.configVO = configVO ;
		this.appName = appName ;
		this.getPortDelay = getPortDelay ;
	}
	
	/**
	 * 向串口中写数据
	 * @param bytes
	 */
	public void write(byte[] bytes) {
		OutputStream os = null ;
		try {
			os = serialPort.getOutputStream();
			if(os == null){
				log.error("从串口(" + this.configVO.getName() + ")中得到输出流出错");
				return ;
			}
			if (Level.INFO.isMoreSpecificThan(log.getLevel())) {
				try {
					String hex = NumberUtil.byte2Hex(bytes);
					log.info("从串口(" + this.configVO.getName() + ")输出数据:\n" + hex);
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
			os.write(bytes);
		} catch (IOException e) {
			log.error("向串口(" + this.configVO.getName() + ")中写入数据发生异常!") ;
		}finally{
			if(os != null){
				try{
					os.close() ;
				}catch(Exception e){
					log.error("关闭串口(" + this.configVO.getName() + ")输出流出错!") ;
				}
			}
		}
	}
	
	
    
	
	/**
	 * 从串口中读数据
	 * @return
	 */
	@SuppressWarnings("finally")
	public byte[] read() {
		byte[] bytes = null;
		InputStream is = null ;
		try {
			is = serialPort.getInputStream();
			
			if(is == null) {
				log.error("从串口(" + this.configVO.getName() + ")中得到输入流出错!") ;
				return null ;
			}
			ArrayList<byte[]> list = new ArrayList<byte[]>();
			int count = 0 ;
			int total = 0 ;
			while (is.available() > 0) {
				byte[] b = new byte[is.available()] ;
				total += is.read(b);
				list.add(count++ , b) ;
				try {
					Thread.sleep(configVO.getWaitTime());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally{
					continue ;
				}
			}

			
			if (list.size() > 0) {
				// 说明读进来数据了
				bytes = new byte[total];
				Iterator<byte[]> it = list.iterator();
				byte[] b = null ;
				int num = 0 ;
				while (it.hasNext()) {
					b = it.next();
					System.arraycopy(b, 0, bytes, num, b.length);
					num += b.length;
				}
			}
			
			

			
			
			log.info("从串口(" + this.configVO.getName() + ")中经" + count + "次循环，读得"+total+"字节数据。");

			if (bytes != null && bytes.length > 0) {
				if (Level.INFO.isMoreSpecificThan(log.getLevel())) {
					try {
						String hex = NumberUtil.byte2Hex(bytes);
						log.info("从串口(" + this.configVO.getName() + ")中读得数据:" + hex);
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}
			new SerialPortHandle().receiveData(this.configVO.getId() , bytes);
			
			return bytes ;
		} catch (IOException e) {
			log.error("从串口(" + this.configVO.getName() + ")中读取数据发生异常!" , e) ;
		}finally{
			if(is != null){
				try{
					is.close() ;
				}catch(Exception e){
					log.error("关闭串口(" + this.configVO.getName() + ")输入流出错!") ;
				}
			}
		}
		return null;
	}

	/**
	 * 打开串口
	 * @param porter
	 */
	public boolean openPort(){
		if(this.configVO.getDebugForNoTerminal()){
			System.out.println("!!串口(" + this.configVO.getName() + ")通信服务(ID:" + appName + ")无终端设备模拟启动");
			return true ;
		}
		try {
			CommPortIdentifier porter = CommPortIdentifier.getPortIdentifier(this.configVO.getName());
			if (porter == null) {
				log.error("不能得到串口" + this.configVO.getName()) ;
				return false ;
			}
			if (porter.getPortType() != CommPortIdentifier.PORT_SERIAL) {
				log.error("得到的通信接口不是串口!") ;
				return false ;
			}
			this.serialPort = (SerialPort)porter.open(this.appName, this.getPortDelay);
			this.serialPort.setSerialPortParams(this.configVO.getBaudrate(), this.configVO.getDataBits(), this.configVO.getStopBits(), this.configVO.getParity());

			this.serialPort.addEventListener(this) ;
			
			// Set notifyOnDataAvailable to true to allow event driven input.
			this.serialPort.notifyOnDataAvailable(true);

			// Set notifyOnBreakInterrup to allow event driven break handling.
			this.serialPort.notifyOnBreakInterrupt(true);
			
			this.serialPort.notifyOnCTS(true);
			this.serialPort.notifyOnDSR(true);
			this.serialPort.notifyOnRingIndicator(true);
			this.serialPort.notifyOnCarrierDetect(true);
			this.serialPort.notifyOnOverrunError(true);
			this.serialPort.notifyOnParityError(true);
			this.serialPort.notifyOnFramingError(true);
			this.serialPort.notifyOnOutputEmpty(true);
			
		} catch (PortInUseException ex) {
			this.serialPort = null;
			ex.printStackTrace();
		} catch (NoSuchPortException ex) {
			this.serialPort = null;
			ex.printStackTrace();
		} catch (Exception ex){
			this.serialPort = null ;
			ex.printStackTrace();
		}
		if (this.serialPort == null) {
			return false ;
		}
		return true ;
	}
	
	/**
	 * 事件监听方法
	 */
	public void serialEvent(SerialPortEvent evt) {
		log.info(this.configVO.getName() + "有事件到来！");
		switch (evt.getEventType()) {
		case SerialPortEvent.CTS:
			log.info(this.configVO.getName() + "清除发送.");
			break;
		case SerialPortEvent.CD:
			log.info(this.configVO.getName() + "载波检测.");
			break;
		case SerialPortEvent.BI:
			log.error(this.configVO.getName() + "通讯中断.");
			break;
		case SerialPortEvent.DSR:
			log.info(this.configVO.getName() + "数据设备准备好.");
			break;
		case SerialPortEvent.FE:
			log.error(this.configVO.getName() + "帧错误.");
			break;
		case SerialPortEvent.OE:
			log.error(this.configVO.getName() + "溢位错误.");
			break;
		case SerialPortEvent.PE:
			log.error(this.configVO.getName() + "奇偶校验错.");
			break;
		case SerialPortEvent.RI:
			log.info(this.configVO.getName() + "振铃指示.");
			break;
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			log.info(this.configVO.getName() + "输出缓冲区已清空.");
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			log.info(this.configVO.getName() + "有数据到达.");
			this.read();
			break;
		}
	}
}
