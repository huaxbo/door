package com.am.cs12.commu.core.remoteSerialPort;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import com.am.cs12.commu.protocolSate.*;
import com.am.cs12.commu.protocolSate.tianHong.DriverTH;
import com.am.cs12.config.ConfigCenter;
import com.am.cs12.config.SateProtocolVO;
import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.cs12.commu.protocolSate.tianHong.$COUT.Data_COUT;
import com.am.cs12.commu.protocolSate.tianHong.$TINF.Data_TINF;
import com.am.util.DateTime;
import com.am.util.NumberUtil;

public class TianHongReceiver {

	private static Logger log = LogManager.getLogger(TianHongReceiver.class.getName()) ;

	/**
	 * 从卫星终端收到数据
	 * @param comId 串口ID
	 * @param protocolName 协议名称
	 * @param bs
	 */
	public void receiveData(String comId, String protocolName , byte[] data) {
		try{
			String hex = new UtilProtocol().byte2Hex(data , true) ;

			ConfigCenter cc = ConfigCenter.instance() ;
			HashMap<String, SateProtocolVO> pmap = cc.getSateProtocolMap() ;
			SateProtocolVO pvo = pmap.get(protocolName) ;
			if(pvo == null){
				log.error("严重错误，不能得到名称为" + protocolName + "的协议配置！") ;
			}
			
			DriverTH driver = (DriverTH)initProtocolDriver(pvo.driverName , protocolName) ;
			ActionSate action = driver.analyseData(data, hex) ;
			
			if(action.has(ActionSate.commandConfirm)){
				//发给卫星终端的命令，卫星终端确认收到命令
				log.info("收到卫星终端确认收到命令。") ;
			}else if(action.has(ActionSate.commandAnswer)){
				log.info("收到针对卫星终端命令的应答。") ;
			}else if(action.has(ActionSate.receivedData)){
				DataSate sateData = driver.getData() ;
				if(sateData == null){
					log.error("严重错误，从卫星协议驱动解析出的数据为空！") ;
					return ;
				}
				Data_COUT dout = (Data_COUT)sateData.getSubData() ;
				if(dout == null){
					log.error("严重错误，从卫星协议驱动解析出的数据为空！") ;
					return ;
				}
				
				log.info("收到从卫星通道发来的测控终端数据。") ;
				new SerialPortReceiverSender().receiverMeterDataBySatellite(sateData.getIdSate(), dout.getData()) ;
			}else if(action.has(ActionSate.grantTimeData)){
				//卫星授时
				DataSate sateData = driver.getData() ;
				Data_TINF tinf = (Data_TINF)sateData.getSubData() ;
				this.setLocalSystemTime(protocolName, tinf.getHour(), tinf.getMinute(), tinf.getSecond()) ;
			}else if(action.has(ActionSate.error)){
				log.error(driver.getError()) ;
			}else if(action.has(ActionSate.nullAction)){
				log.info("收到卫星终端数据，处理后无动作。") ;
			}else if(action.has(ActionSate.noAction)){
				log.info("收到卫星终端数据，处理后无动作。") ;
			}
			
		}catch(Exception e){
			log.error("通过串口接收卫星数据时出错，" + e.getMessage() , e) ;
		}finally{
			;
		}
	}
	
	/**
	 * 初始化协议驱动类
	 * @param filePath
	 * @param vo
	 * @param RTUs
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	private static DriverSate initProtocolDriver(String clazz , String protocolName) throws IllegalArgumentException {
		try {
			Class c = Class.forName(clazz);
			if (c == null) {
				throw new IllegalArgumentException("不能实例化天鸿卫星通信协议驱动类" + clazz + "！");
			}
			return (DriverSate) c.getDeclaredConstructor(String.class).newInstance(protocolName);
		} catch (Exception e) {
			throw new IllegalArgumentException("不能实例化天鸿卫星通信协议驱动类" + clazz + "！");
		}
	}
	/**
	 * 修改系统时钟
	 * @param hour
	 * @param minute
	 * @param second
	 */
	private void setLocalSystemTime(String protocolName , String hour, String minute , String second){
		if(hour == null || minute == null || second == null){
			log.error("卫星授时数据有空值，不能修改系统时钟！") ;
		}
		if(hour.equals("") || minute.equals("") || second.equals("")){
			log.error("卫星授时数据有空值，不能修改系统时钟！") ;
		}
		if(!NumberUtil.isPlusIntNumber(hour) || !NumberUtil.isPlusIntNumber(minute) || !NumberUtil.isPlusIntNumber(second)){
			log.error("卫星授时数据有误，不能修改系统时钟！") ;
		}
		
		ConfigCenter cc = ConfigCenter.instance() ;
		HashMap<String, SateProtocolVO> smap = cc.getSateProtocolMap() ;
		if(smap == null || smap.size() == 0){
			return ;
		}
		SateProtocolVO svo = (SateProtocolVO)smap.get(protocolName) ;
		if(svo == null){
			return ;
		}
		
		int inth = Integer.parseInt(hour) ;
		int intm = Integer.parseInt(minute) ;
		
		int nowh = Integer.parseInt(DateTime.HH()) ;
		int nowm = Integer.parseInt(DateTime.mm()) ;
		int deviate = svo.grantTimeByDeviate ;
		if(inth == nowh){
			if(Math.abs(intm - nowm) <= deviate){
				log.info("系统时钟与卫星时钟相差在允许范围内，不进行系统校时。") ;
				return ;
			}
		}
		
		
		String osName = System.getProperty("os.name");   
		String cmd = "";   
		try {
			if (osName.matches("^(?i)Windows.*$")) {
				// Window 系统   
				// 格式 HH:mm:ss
				cmd = "  cmd /c time " + hour + ":" + minute +  ":" + second ;
				Runtime.getRuntime().exec(cmd);

				// 格式：yyyy-MM-dd
//				cmd = " cmd /c date 2009-03-26";
//				Runtime.getRuntime().exec(cmd);

			} else {
				// Linux 系统
				// 格式：yyyyMMdd
//				cmd = "  date -s 20090326";
//				Runtime.getRuntime().exec(cmd);

				// 格式 HH:mm:ss
				cmd = "  date -s " + hour + ":" + minute +  ":" + second ;
				Runtime.getRuntime().exec(cmd);
			}
			log.info("根据卫星授时，进行了系统校时。") ;

		} catch (IOException e) {
			e.printStackTrace();

		}
	}
	

}
