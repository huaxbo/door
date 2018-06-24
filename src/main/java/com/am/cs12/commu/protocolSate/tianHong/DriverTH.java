package com.am.cs12.commu.protocolSate.tianHong;

//import java.util.HashMap;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

import com.am.cs12.commu.protocolSate.*;
import com.am.cs12.commu.protocolSate.tianHong.task.GrantTimeManager;

public class DriverTH extends DriverSate{
	
	private static Logger log = LogManager.getLogger(DriverTH.class.getName()) ;
	
	protected String code ;//功能码
	
	protected DataSate data ;//上行数据
	
	protected byte[] com ;//下行命令

	protected String error ;//出错信息
	
	public DriverTH(String protocolName) {
		super(protocolName);
	}

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
			
			if(this.code.equalsIgnoreCase(CodeTH.$CASS)){
				//终端(卫星基站)响应外设，表示接收外设传来的数据情况
				this.data = dp.parse_CASS(b) ;
				action = ActionSate.commandConfirm ;
			} else
			if(this.code.equalsIgnoreCase(CodeTH.$TSTA)){
				//终端(卫星基站)响应外设查询终端(卫星基站)状态
				this.data = dp.parse_TSTA(b) ;
				action = ActionSate.commandAnswer ;
			} else
			if(this.code.equalsIgnoreCase(CodeTH.$COUT)){
				//终端(卫星基站)将收到的卫星数据传给外设
				this.data = dp.parse_COUT(b) ;
				action = ActionSate.receivedData ;
			} else
			if(this.code.equalsIgnoreCase(CodeTH.$TINF)){
				//卫星授时
				this.data = dp.parse_TINF(b) ;
				action = ActionSate.grantTimeData ;
			}else{
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

	@Override
	public ActionSate createCommand(CommandSate comSate) {
		try {
//			HashMap<String, Object> params = comSate.getParams() ;
//			if(params == null || params.size() == 0){
//				this.error = "出错，命令中参数集合为空，不能构建任何命令！" ;
//				log.error(error) ;
//				return ActionSate.error ;
//			}
			this.code = comSate.getCode() ;
			if(this.code == null || this.code.trim().equals("")){
				this.error = "出错，命令中功能码为空，不能构建任何命令！" ;
				log.error(error) ;
				return ActionSate.error ;
			}
			
			ActionSate action = ActionSate.nullAction ;
			CommandProtocol cp = new CommandProtocol() ;
			
			if(this.code.equalsIgnoreCase(CodeTH.$QSTA)){
				//查询终端(卫星基站)状态
				this.com = cp.read_QSTA(comSate) ;
				action = ActionSate.command ;
			}else if(this.code.equalsIgnoreCase(CodeTH.$TTCA)){
				//外设请求终端(卫星基站)向卫星发送数据
				this.com = cp.write_TTCA(comSate) ;
				action = ActionSate.command ;
			}else if(this.code.equalsIgnoreCase(CodeTH.$TAPP)){
				//申请卫星授时
				this.com = cp.read_TAPP(comSate) ;
				action = ActionSate.command ;
			}else{
				this.error = "命令中功能码(" + this.code + ")不能识别，从而不能构造命令。" ;
				action = ActionSate.error ;
			}
			
			return action == null?ActionSate.noAction:action ;
		} catch (Exception e) {
			log.error(e.getMessage());
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

	public byte[] getCom() {
		return com;
	}

	public void setCom(byte[] com) {
		this.com = com;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
