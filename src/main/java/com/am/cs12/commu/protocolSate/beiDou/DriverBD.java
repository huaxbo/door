package com.am.cs12.commu.protocolSate.beiDou;

//import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.cs12.commu.protocolSate.*;
import com.am.cs12.commu.protocolSate.beiDou.task.GrantTimeManager;

public class DriverBD extends DriverSate{
	
	private static Logger log = LogManager.getLogger(DriverBD.class.getName()) ;
	
	protected String code ;//功能码
	
	protected DataSate data ;//上行数据
	

	protected String error ;//出错信息
	
	public DriverBD(String protocolName) {
		super(protocolName);
	}

	/* (non-Javadoc)
	 * @see com.am.cs12.commu.protocolSate.DriverSate#analyseData(byte[], java.lang.String)
	 */
	@Override
	public ActionSate analyseData(byte[] b , String dataHex) {
		try {
			DataProtocol dp = new DataProtocol() ;
			
			ActionSate action = ActionSate.nullAction ;
			//检查数据
			try{
				dp.chechData(b) ;
			}catch(Exception e){
				//可能是两条卫星数据合并在一起
				//卫星设备确认收到命令是11字节长
				if(b.length > 11){
					byte[] bb = new byte[b.length - 11] ;
					for(int i = 0 ; i < bb.length ; i++){
						bb[i] = b[11+i] ;
					}
					b = bb ;
				}
			}
			//得到数据中的功能码
			this.code = dp.getCode(b) ;
			if(this.code == null ||  this.code.trim().equals("")){
				this.error = "出错，未能得到卫星数据中的功能码，不能分析数据。" ;
				action = ActionSate.error ;
			}
			
//			if(this.code.equalsIgnoreCase(CodeBD.$BBXX)){
//				//终端(卫星基站)响应外设，表示接收外设传来的数据情况
//				this.data = dp.parse_BBXX(b) ;
//				action = ActionSate.commandConfirm ;
//			} else
//			if(this.code.equalsIgnoreCase(CodeBD.$DWXX)){
//				//终端(卫星基站)响应外设查询终端(卫星基站)状态
//				this.data = dp.parse_DWXX(b) ;
//				action = ActionSate.commandAnswer ;
//			} else
//			if(this.code.equalsIgnoreCase(CodeBD.$FKXX)){
//				//终端(卫星基站)将收到的卫星数据传给外设
//				this.data = dp.parse_FKXX(b) ;
//				action = ActionSate.receivedData ;
//			} else
//			if(this.code.equalsIgnoreCase(CodeBD.$ICXX)){
//				//卫星授时
//				this.data = dp.parse_ICXX(b) ;
//				action = ActionSate.grantTimeData ;
//			}else
//				if(this.code.equalsIgnoreCase(CodeBD.$SJXX)){
//					//终端(卫星基站)将收到的卫星数据传给外设
//					this.data = dp.parse_SJXX(b) ;
//					action = ActionSate.receivedData ;
//			}else
			if(this.code.equalsIgnoreCase(CodeBD.$TXXX)){
					//终端(卫星基站)将收到的卫星数据传给外设
					this.data = dp.parse_TXXX(b) ;
					action = ActionSate.receivedData ;
			}
//			else
//				if(this.code.equalsIgnoreCase(CodeBD.$ZJXX)){
//					//终端(卫星基站)将收到的卫星数据传给外设
//					this.data = dp.parse_ZJXX(b) ;
//					action = ActionSate.receivedData ;
//			}
				else{
				this.error = "卫星数据中功能码(" + this.code + ")不能识别，从而不能分析数据。" ;
				action = ActionSate.error ;
			}
			
			return action == null?ActionSate.noAction:action ;
		} catch (Exception e) {
			log.error(e.getMessage() , e);
			this.error = e.getMessage();
			return ActionSate.error;
		}
	}


	/**
	 * 启动卫星授时功能
	 */
	@Override
	public void startGrantTime() {
		GrantTimeManager.instance().startGrantTimeTask(this.protocolName) ;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public DataSate getData() {
		return data;
	}

	public void setData(DataSate data) {
		this.data = data;
	}


	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public ActionSate createCommand(CommandSate com) {
		// TODO Auto-generated method stub
		return null;
	}






}
