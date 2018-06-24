package com.am.cs12.commu.core.remoteCommand.forGprsSerial;

import java.util.*;

import org.apache.mina.core.session.IoSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.common.threadPool.ACThreadJob;
import com.am.cs12.config.ProtocolBaseVO;
import com.am.cs12.config.ConfigCenter;
import com.am.cs12.commu.core.log.AmLog;
import com.am.cs12.commu.core.remoteGprs.RemoteSessionManager;
import com.am.cs12.commu.core.remoteStatus.MeterStatusManager;
import com.am.cs12.commu.core.CoreServer;
import com.am.cs12.commu.core.despatchReceive.forLocal.DespatchFromCachCommand;
import com.am.cs12.commu.core.remoteSerialPort.SerialPortReceiverSender;
import com.am.cs12.commu.protocol.util.UtilProtocol;
import com.am.cs12.config.*;
import com.am.cs12.util.AmConstant;

public class DealCachCommandForGprsSerail {
	
	private static Logger log = LogManager.getLogger(DealCachCommandForGprsSerail.class.getName()) ;
	
	//命令缓存队列上   正在进行命令处理的工作
	//Object[0]=ACThreadJob,Object[1]=触发者，Object[2]=通道
	private static HashMap<String , Object[]> id_threadJob = new HashMap<String , Object[]>() ;
	
	private static DealCachCommandForGprsSerail instance ;
	
	private static ProtocolBaseVO pbvo ;

	private DealCachCommandForGprsSerail(){
		pbvo = ConfigCenter.instance().getProtocolBaseVO() ;
	}
	/**
	 * 得到单实例
	 * @return
	 */
	public static DealCachCommandForGprsSerail instance(){
		if(instance == null){
			instance = new DealCachCommandForGprsSerail() ;
		}
		return instance ;
	}
	
	/**
	 * 加入对测控终端确认的命令
	 * 确认的命令可即时发送出去
	 * @param id
	 * @param data
	 */
	public void setConfirm(
			String id, 
			String code, 
			byte[] confirmData,
			String channel)throws Exception{
		if(id == null){
			throw new Exception("设置待发送命令时，必须提供测控终端 ID！") ;
		}
		if(code == null){
			throw new Exception("设置待发送命令时，必须提供命令功能码！") ;
		}
		if(confirmData == null){
			throw new Exception("设置待发送命令时，必须提供命令数据！") ;
		}
		RemoteSessionManager rsm = RemoteSessionManager.instance() ;
		CommandQueueForGprsSerial queue = rsm.getCachCommandQueueForGprsSerial(id) ;
		synchronized(queue){
			CommandNodeForGprsSerial node = new CommandNodeForGprsSerial(
					CommandTypeForGprsSerial.CommandType.confirmCom ,
					CommandTypeForGprsSerial.CommandSendPriority.yes ,
					CommandTypeForGprsSerial.CommandSendTimes.single ,
					CommandTypeForGprsSerial.CommandSendByNoOnLine.noOnLine_delete,
					null,
					id, 
					code, 
					confirmData) ;
			if(node.commandSendPriority.equals(CommandTypeForGprsSerial.CommandSendPriority.yes)){
				queue.addHead(node) ;
			}else{
				queue.addTail(node) ;
			}
			queue.notifyAll() ;
		}
		rsm.setSendCommandMomentForGprs(id, new Long(0)) ;
		Long[] lastMoment = rsm.getSendCommandMomentForGprs(id) ;
		synchronized(lastMoment){
			lastMoment.notifyAll() ;
		}
		this.remoteEventTrigger(id, channel) ;
	}
	/**
	 * 当命令成功，收到命令结果时，要从命令缓存中匹配命令
	 * @param dutId
	 * @param code
	 * @param channel 上报数据通道
	 * @return 返回命令ID
	 */
	public String successCommand(String id , String code , String channel){
		String commandId = null ;
		RemoteSessionManager rsm = RemoteSessionManager.instance() ;
		CommandQueueForGprsSerial queue = rsm.getCachCommandQueueForGprsSerial(id) ;
		synchronized(queue){
			int comNum = queue.size() ;
			if(comNum > 0){
				for(int i = 0 ; i < comNum ; i++){
					CommandNodeForGprsSerial node = queue.get(i) ;
					commandId = node.matchCommand(id, code, true) ;
					if(commandId != null){
						break ;
					}
				}
			}
			queue.notifyAll() ;
		}
		rsm.setSendCommandMomentForGprs(id, new Long(0)) ;
		Long[] lastMoment = rsm.getSendCommandMomentForGprs(id) ;
		synchronized(lastMoment){
			lastMoment.notifyAll() ;
		}
		
		if(commandId == null){
			//命令结果回来比较迟，命令在缓存已经因超时被清除
			//但也表示命令成功了，所以更改测控终端状态
			MeterStatusManager.instance().commandFail2Success(id) ;
		}
		
		this.remoteEventTrigger(id, channel) ;
		return commandId ;
	}
	
	/**
	 * 延长生命令周期，例如召测图像数据，需要多次上报才会把数据收全，所以要延长命令生命周期
	 * @param id
	 * @param code
	 * @param channel
	 * @return
	 */
	public String lengthenCommandLife(String id , String code , String channel){
		String commandId = null ;
		RemoteSessionManager rsm = RemoteSessionManager.instance() ;
		CommandQueueForGprsSerial queue = rsm.getCachCommandQueueForGprsSerial(id) ;
		synchronized(queue){
			int comNum = queue.size() ;
			if(comNum > 0){
				for(int i = 0 ; i < comNum ; i++){
					CommandNodeForGprsSerial node = queue.get(i) ;
					//不能算是命令已经成功，要等待命令后续结果
					commandId = node.matchCommand(id, code , false) ;
					if(commandId != null){
						node.lengthenLife() ;
						break ;
					}
				}
			}
			queue.notifyAll() ;
		}
//		rsm.setSendCommandMomentForGprs(id, new Long(0)) ;
//		Long[] lastMoment = rsm.getSendCommandMomentForGprs(id) ;
//		synchronized(lastMoment){
//			lastMoment.notifyAll() ;
//		}
//		
//		this.remoteEventTrigger(id, channel) ;
		
		return commandId ;
	}
	
	/**
	 * 测控终端有上行数据，但处理此上行数据后，不需要通信服务器对测控器确认回复，也不需要取得命令ID等操作，这样的上行数据如主动上报数据等，
	 * 这种情况下，表明测控终端与通信服务器通信畅通，通信服务器可以下发此测控终端的缓存中的命令。
	 * @param id
	 * @param channel 上报数据通道
	 */
	public void notifyCanSendCommand(String id , String channel){
		RemoteSessionManager rsm = RemoteSessionManager.instance() ;
		CommandQueueForGprsSerial queue = rsm.getCachCommandQueueForGprsSerial(id) ;
		synchronized(queue){
			queue.notifyAll() ;
		}
		rsm.setSendCommandMomentForGprs(id, new Long(0)) ;
		Long[] lastMoment = rsm.getSendCommandMomentForGprs(id) ;
		synchronized(lastMoment){
			lastMoment.notifyAll() ;
		}
		this.remoteEventTrigger(id, channel) ;
	}
	
	
	
	/**
	 * 加入等待发送的命令
	 * @param id
	 * @param data
	 * @param channel 上报数据通道
	 */
	public void setCommand(
			CommandTypeForGprsSerial.CommandType commandType ,
			CommandTypeForGprsSerial.CommandSendPriority commandSendPriority ,
			CommandTypeForGprsSerial.CommandSendTimes commandSendTimes ,
			CommandTypeForGprsSerial.CommandSendByNoOnLine commandSendByNoOnLine,
			String commandId ,
			String id, 
			String code, 
			Object commandData)throws Exception{
		if(commandType == null 
				|| commandSendPriority == null 
				|| commandSendTimes == null 
				|| commandSendByNoOnLine == null ){
			throw new Exception("命令的类型、优先级、发送次数、发送与在线关系、发送后是否取消网络连接等项目必须设置！") ;
		}
		if(id == null){
			throw new Exception("设置待发送命令时，必须提供测控终端 ID！") ;
		}
		if(code == null){
			throw new Exception("设置待发送命令时，必须提供命令功能码！") ;
		}
		if(commandData == null){
			throw new Exception("设置待发送命令时，必须提供命令数据！") ;
		}
		CommandQueueForGprsSerial queue = RemoteSessionManager.instance().getCachCommandQueueForGprsSerial(id) ;
		synchronized(queue){
			CommandNodeForGprsSerial node = new CommandNodeForGprsSerial(
					commandType ,
					commandSendPriority ,
					commandSendTimes ,
					commandSendByNoOnLine,
					commandId,
					id, 
					code, 
					commandData) ;
			if(node.commandSendPriority.equals(CommandTypeForGprsSerial.CommandSendPriority.yes)){
				queue.addHead(node) ;
			}else{
				queue.addTail(node) ;
			}
			queue.notifyAll() ;
		}
		this.localEventTrigger(id) ;
	}
	
	/**
	 * 缓存命令队列定时工作任务启动处理命令过程
	 * @param id
	 */
	public void timerTaskStartSendCommand(String id){
		this.localEventTrigger(id) ;
	}
	
	/**
	 * 远程测控终端上报数据触发处理缓存命令
	 * 测控终端主动上报数据、测控终端命令回数等
	 */
	private void remoteEventTrigger(String id , String channel){
		if(id == null){
			log.error("严重错误，未提供测控终端 ID，从而不能处理其缓存命令!") ;
			return ;
		}
		this.dealCachCommandByRemoteTrigger(id, channel, AmConstant.RemoteTrigger) ;
	}
	/**
	 * 本地事件(业务系统下发命令，缓存命令观察都定时任务)触发处理缓存命令
	 */
	private void localEventTrigger(String id){
		if(id == null){
			log.error("严重错误，未提供测控终端 ID，从而不能处理其缓存命令!") ;
			return ;
		}
		this.dealCachCommandByLocalTrigger(id , AmConstant.LocalTrigger) ;
	}
	
	/**
	 * 由远程测控终端事件触发，处理缓存中的命令，
	 * @param id
	 * @param trigger 触发者 
	 * @param channel GPRS通道或卫星通道 
	 */
	private void dealCachCommandByRemoteTrigger(String id , String channel , String trigger){
		try {
			Object[] os = id_threadJob.get(id) ;
			
			if(os != null){
				//已经存在命令处理者在处理缓存命令
				//此情况正常，因为触发处理命令的事件(确认应答、下发的命令，有命令结果上报并匹配命令、有通知可以处理命令等)发生时间不确定，
				//可能在事件发生时，已经存在命令处理者正在处理命令，
				if(os[1].equals(AmConstant.LocalTrigger)){
					//已经存在的命令处理者为本地触发，立即停止它
					((ACThreadJob)os[0]).stop() ;
					if(channel.equals(AmConstant.channel_serialPort)){
						//卫星通道已经接通，并被选择来处理命令
						this.selectSatelliteChannel(id , trigger) ;
					}else if(channel.equals(AmConstant.channel_gprs)){
						//GPRS通道已经被选择
						RemoteSessionManager rsm = RemoteSessionManager.instance() ;
						//有可处理的命令
						final IoSession session = rsm.getSession(id) ;
						if(session == null){
							//得到的会话为空session == null
							MeterHelp mh = new MeterHelp() ;
							if(mh.containChannel(id , AmConstant.channel_serialPort)){
								log.info("处理发送命令时，虽然上次通信信道是" + AmConstant.channel_gprs +"，但其网络会话为空，可能该测控终端掉线，所以选择备用卫星信道!") ;
								this.selectSatelliteChannel(id, trigger) ;
							}else{
								this.selectGprsChannel(id , trigger) ;
							}
						}else{
							this.selectGprsChannel(id , trigger) ;
						}
					}else{
						//不存在此情况
					}
				}else{
					//已经存在的命令处理者也为远程触发
					/*
					此处有两种处理方式：
					1、保留已经存在的命令处理者，以使其把命令处理完；
					2、判断已经存在的命令处理者的通道与当前通道是否相同
					   (1)、相同，保留已经存在的命令处理者，以使其把命令处理完；
					   (2)、不同，停止已经存在的命令处理者，生成新的命令处理者，用新的通道处理及发送命令，但这种问情况几乎是不存在的，所以不于实现
					*/
				}
			}else{
				//当前无处理者
				if(channel.equals(AmConstant.channel_serialPort)){
					//卫星通道已经接通，并被选择来处理命令
					this.selectSatelliteChannel(id , trigger) ;
				}else if(channel.equals(AmConstant.channel_gprs)){
					//GPRS通道已经被选择
					RemoteSessionManager rsm = RemoteSessionManager.instance() ;
					//有可处理的命令
					final IoSession session = rsm.getSession(id) ;
					if(session == null){
						//得到的会话为空session == null
						MeterHelp mh = new MeterHelp() ;
						if(mh.containChannel(id , AmConstant.channel_serialPort)){
							log.info("处理发送命令时，虽然上次通信信道是" + AmConstant.channel_gprs +"，但其网络会话为空，可能该测控终端掉线，所以选择备用卫星信道!") ;
							this.selectSatelliteChannel(id, trigger) ;
						}else{
							this.selectGprsChannel(id , trigger) ;
						}
					}else{
						this.selectGprsChannel(id , trigger) ;
					}
				}else{
					//不存在此情况
				}
		    }
		}catch(Exception e){
			log.error("异常，处理缓存命令的线程执行期间发生异常。" , e) ;
		}finally{
		}
	}
	
	/**
	 * 由本地事件触发，处理缓存中的命令，
	 * @param id
	 * @param trigger 触发者 
	 * @param channel GPRS通道或卫星通道 
	 */
	private void dealCachCommandByLocalTrigger(String id , String trigger){
		try {
			Object[] os = id_threadJob.get(id) ;
			
			if(os != null){
				//已经存在命令处理者在处理缓存命令
				//此情况正常，因为触发处理命令的事件(确认应答、下发的命令，有命令结果上报并匹配命令、有通知可以处理命令等)发生时间不确定，
				//可能在事件发生时，已经存在命令处理者正在处理命令，
				//此种情况，此处不作为。
			}else{
				//当前无处理者
				/*MeterHelp mh = new MeterHelp() ;
				MeterVO meter = mh.getMeter(id) ;
				if(!meter.onlineAlways){
					//根据配置，测控终端不常在线
					//不作处理
//					log.info("处理发送命令时，测控终端为不常在线，本次不处理命令，等待远程事件触发时再处理命令!") ;
				}else{
					//常在线
					if(meter.mainChannel.equals(AmConstant.channel_serialPort)){
						this.selectSatelliteChannel(id, trigger) ;
					}else if(meter.mainChannel.equals(AmConstant.channel_gprs)){
						RemoteSessionManager rsm = RemoteSessionManager.instance() ;
						final IoSession session = rsm.getSession(id) ;
						if(session == null){
							//得到的会话为空session == null
							if(mh.containChannel(id , AmConstant.channel_serialPort)){
								log.info("处理发送命令时，虽然主信道是" + AmConstant.channel_gprs +"，但其网络会话为空，可能该测控终端未上线，所以选择备用卫星信道!") ;
								this.selectSatelliteChannel(id, trigger) ;
							}else{
								this.selectGprsChannel(id , trigger) ;
							}
						}else{
							this.selectGprsChannel(id , trigger) ;
						}
					}else{
						//不存在此情况
					}
				}*/
				
				this.selectGprsChannel(id , trigger) ;
		    }
		}catch(Exception e){
			log.error("异常，处理缓存命令的线程执行期间发生异常。" , e) ;
		}finally{
		}
	}
	/**
	 * 选择卫星通道处理命令
	 * @param id
	 */
	private void selectSatelliteChannel(final String id , final String trigger) throws Exception{
		final CommandQueueForGprsSerial queue = RemoteSessionManager.instance().getCachCommandQueueForGprsSerial(id) ;
		if(queue != null && queue.sizeOfCanDeal() > 0) {
			//有可处理的命令
			//创建处理者，并进行缓存命令处理
			CoreServer.cachCommandPool.SetThreadJob(new ACThreadJob() {
			private Boolean needDeal  ;
			public void stop(){
				this.out(queue) ;
			}
			public void execute() {
				needDeal = true ;
				//标识队列上有处理者
				id_threadJob.put(id, new Object[]{this, trigger, AmConstant.channel_gprs}) ;
				this.deal(queue) ;
			}
			/**
			 * 处理缓存队列中的命令
			 */
			private void deal(CommandQueueForGprsSerial queue){
				synchronized(queue){
					while(needDeal){
						//每次处理，每个CommandNode必须有结果：清除队列或标识不处理
						//得到缓存中可以处理的命令数
						int comNum = queue.sizeOfCanDeal() ;
						if(comNum > 0){
							log.info("命令处理者在处理缓存命令前，在缓存队列中还有" + comNum + "条可处理命令。") ;
						}else{
							log.info("命令处理者发现命令缓存队列中无可处理命令，结束本次命令处理工作。") ;
						}
						if(comNum > 0){
							CommandNodeForGprsSerial node = queue.getCanDeal(0) ;
							if(node == null){
								log.error("严重错误，命令队列中有可处理的命令，但从队列中得到第一个可处理命令为空！") ;
								//退出
								this.out(queue) ;
							}else{
								if(node.commandType.equals(CommandTypeForGprsSerial.CommandType.confirmCom)){
									//确认应答命令，立即发送
									queue.remove(node) ;
									this.sendConfirmCommand(node) ;
									log.info("发送确认应答命令，并把其从命令缓存队列中清除。") ;
									//////////////////////////////////////////////////////////////////////////////////////
									//每次发送命令后，如果有后续可处理的命令，都要等待一个时间间隔，以使不同命令之间有一个间隔时长
									int count = queue.sizeOfCanDeal() ;
									if(count > 0){
										//队列中还有可处理命令，进入等待
										try {
											log.info("发送确认应答后，命令缓存中还有" + count + "条可处理的命令，进行发送命令间的等待。") ;
											queue.wait(pbvo.confirmCommandInterval * 1000) ;
											//注意!在等待期间可能有新的命令加入到队列中。 
										} catch (InterruptedException e) {
										}finally{}
									}
								}else{
									//非确认应答命令，
									if(node.isSuccessed()){
										//已经成功的命令，即已经收到命令结果
										//首先保存测控终端状态
										MeterStatusManager.instance().commandSuccess(id) ;
										queue.remove(node) ;
										log.info("当前命令是成功命令(已收到结果)(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")，清除出缓存队列。") ;
										/*
										 以下注释部分实现方式的考虑：
										           一个命令可能有多个命令结果(如召测24小时数据)，即命令应答分多个数据包上报，所以命令不能立即清除，要尽量等命令结果全部到来，直到命令超时被清除出命令队列，
										          但是这种实现方式会带来问题，即如果业务系统出来多个相同命令(即命令功能码相同)，基于当前协议设计(上报数据中无命令ID)，从上报的应答数据中不能区分结果数据是属于哪个命
										          令的，所以会把各个命令的结果 都匹配给第一个命令，从而带来混乱，所以不能应用此种实现方式。
										           											  
										//首先保存测控终端状态
										if(!node.hasSetSuccessStatus){
											MeterStatusManager.instance().commandSuccess(id) ;
											node.hasSetSuccessStatus = true ;
										}
										if(node.isValidLife()){
											//命令生命期在有效范围内
											//标识本次不处理此命令，因为一个命令结果可能分包上报，所以仍不清除队列，以等后续结果，直到命令超时被清除
											node.canDeal = false ;
											log.info("当前命令是成功命令(已收到结果)(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")，不处理，保留在命令队列中，等待后续结果(数据分包上报)，直到命令超时被清除出队列。") ;
										}else{
											//命令生命期超出有效范围
											//把命令清除出队列
											queue.remove(node) ;
											log.info("当前命令是成功命令(已收到结果)(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")，但已超时，清除出命令队列。") ;
										}
										*/
									}else{
										//非确认应答命令,并且是非成功命令
										if(node.commandType.equals(CommandTypeForGprsSerial.CommandType.objCom)){
											//非确认应答命令,也非成功命令，而且是原生命令
											//处理原生命令,在此方法退出前，此原生命令构造成字节数组命令，并已经加入到队列中
											this.dealObjCommand(node) ;
											queue.remove(node) ;
											log.info("发送原生命令去构建字节命令，把原生命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")清除出缓存队列。") ;
											//退出dealObjCommand方法后，原生命令已经构造成字节数组命令，并已经加入到本线程正在处理的队列中
										}//end 原生命令
										else if(node.commandType.equals(CommandTypeForGprsSerial.CommandType.byteCom)){
											//非确认应答命令,也非成功命令，而且是字节数组命令
											//假定卫星通信下，测控器总在线
											if(!node.isValidLifeWithSendTimes()){
												//命令达到或超过了允许重发的最大次数，并命令超时，此命令将被清除
												//首先保存测控终端状态
												MeterStatusManager.instance().commandFail(id) ;
												queue.remove(node) ;
												log.info("卫星通信下，认为测控器总在线，把超时命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")清除出缓存队列。") ;
											}else if(!node.isValidTimes()){
												//命令达到或超过了允许重发的最大次数，但命令未超时，
												//如果是单次发送命令，发送次已经是1
												//不做处理，进行等待(最长等待超时时长)，以便最后尝试接收测控器的回复
												node.canDeal = false ;
												log.info("卫星通信下，认为测控器总在线，命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")达到最大发送次数，但未超时，本次不处理。") ;
											}else{
												//有效命令
												//发送命令
												node.addSendTimes() ;
												node.canDeal = false ;
												
												log.info("卫星通信下，认为测控器总在线，发送命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")。") ;
												this.sendCommand(node) ;
												////////////////////////////////////////////
												//每次发送命令后，如果有后续可处理的命令，都要等待一个时间间隔，以使不同命令之间有一个间隔时长
												int count = queue.sizeOfCanDeal() ;
												if(count > 0){
													//队列中还有可处理命令
													try {
														log.info("发送命令后，命令缓存中还有" + count + "条可处理的命令，进行发送命令间的等待。") ;
														queue.wait(pbvo.sendCommandInterval * 1000) ;
														//注意!在等待期间可能有新的命令加入到队列中。 
													}  catch (InterruptedException e) {
													}finally{}
												}
											}//end 网络会话在线
										}//end 字节数组命令
										else{
											//不能识别的命令类型
											queue.remove(node) ;
											log.info("严重错误，命令缓存队列的命令类型不能识别，并把其清除出队列。") ;
										}
									}//end 非确认应答命令,并且也非成功命令
								}//end 非确认应答命令，
							}//end node不为空
						}//end comNum > 0
						else{
							//无可处理的命令
							this.out(queue) ;
						}
					}//end while循环
				}//end synchronized(queue)
			}
			/**
			 * 退出处理
			 */
			private void out(CommandQueueForGprsSerial queue){
				needDeal = false ;
				queue.canDealAll() ;
				id_threadJob.remove(id) ;
			}
			
			/**
			 * 发送确认应答命令
			 * @param node
			 * @param rsm
			 */
			private void sendConfirmCommand(CommandNodeForGprsSerial node){
				new SerialPortReceiverSender().sendMeterConfirmCommand(node.id, (byte[])node.commandData) ;
				MeterStatusManager.instance().sendMeterSatellite(id) ;
				try {
					log.info("成功向测控器发送了命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")。\n>>>>>>>>发向测控终端(ID:" + node.id + ")命令:" + new UtilProtocol().byte2Hex((byte[])node.commandData , true).toUpperCase()) ;
				} catch (Exception e) {
				}
			}
			
			/**
			 * 发送命令
			 * @param node
			 */
			private void sendCommand(CommandNodeForGprsSerial node){
				new SerialPortReceiverSender().sendMeterCommand(node.id, (byte[])node.commandData) ;
				//保存测控终端状态
				MeterStatusManager.instance().sendMeterSatellite(id) ;
				try {
					log.info("成功向测控器发送了命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")。\n>>>>>>>>发向测控终端(ID:" + node.id + ")命令:" + new UtilProtocol().byte2Hex((byte[])node.commandData , true).toUpperCase()) ;
				} catch (Exception e) {
				}
			}
			
			/**
			 * 处理原生命令
			 */
			private void dealObjCommand(CommandNodeForGprsSerial node){
				new DespatchFromCachCommand().receiveCachCommand(node.commandData) ;
			}
			
		}) ;
	}
	}
	/**
	 * 选择GPRS通道处理命令
	 * @param id
	 */
	private void selectGprsChannel(final String id , final String trigger) throws Exception{
		final RemoteSessionManager rsm = RemoteSessionManager.instance() ;
		final CommandQueueForGprsSerial queue = rsm.getCachCommandQueueForGprsSerial(id) ;
		if(queue != null && queue.sizeOfCanDeal() > 0) {
			//有可处理的命令
			final IoSession session = rsm.getSession(id) ;
			if(session == null){
				//得到的会话为空session == null
				//处理不处理，等待上线
//				log.info("处理命令时，存在发向测控终端( ID：" + id + ")的命令，但其网络会话为空，可能该测控终端未上线!") ;
			}else{
				//创建处理者，并进行缓存命令处理
				CoreServer.cachCommandPool.SetThreadJob(new ACThreadJob() {
				private Boolean needDeal  ;
				public void stop(){
					this.out(queue) ;
				}
				public void execute() {
					needDeal = true ;
					//标识队列上有处理者
					id_threadJob.put(id, new Object[]{this, trigger, AmConstant.channel_gprs}) ;
					
					/*
					 * 在上次下发命令(非确认应答)后，如果命令结果没有返回，
					 * 这时如果有新的命令到来， 而且距上次下发命令间隔较短，
					 * 则需要等待一定时长（命令间的等待时长）
					 * 如果在等待期间，有上行数据，可能是上次命令结果、主动上报数据、心跳或其他数据，都认为可以
					 * 继续新的命令处理及下发了，所以结束等待。
					 * 
					 */
					Long[] lastMoment = rsm.getSendCommandMomentForGprs(id) ;
					if(lastMoment != null && lastMoment.length > 0 && lastMoment[0].longValue() > 0){
						long gap = System.currentTimeMillis() - lastMoment[0].longValue() ;
						if(pbvo.sendCommandInterval * 1000 > gap){
							try {
								long waitTime = pbvo.sendCommandInterval * 1000 - gap ;
								log.info("距上次下发命令时间较短，本次处理缓存命令的任务需要进行命令间的等待，等待时长为" + waitTime + "毫秒") ;
								synchronized(lastMoment){
									lastMoment.wait(waitTime) ;
								}
								log.info("本次处理缓存命令任务结束命令间的等待。") ;
							} catch (InterruptedException e) {
							}finally{}
						}
					}
					this.deal(session, queue) ;
				}
				/**
				 * 处理缓存队列中的命令
				 */
				private void deal(IoSession session , CommandQueueForGprsSerial queue){
					synchronized(queue){
						while(needDeal){
							//每次处理，每个CommandNode必须有结果：清除队列或标识不处理
							//得到缓存中可以处理的命令数
							int comNum = queue.sizeOfCanDeal() ;
							if(comNum > 0){
								log.info("命令处理者在处理缓存命令前，在缓存队列中还有" + comNum + "条可处理命令。") ;
							}else{
								log.info("命令处理者发现命令缓存队列中无可处理命令，结束本次命令处理工作。") ;
							}
							if(comNum > 0){
								CommandNodeForGprsSerial node = queue.getCanDeal(0) ;
								if(node == null){
									log.error("严重错误，命令队列中有可处理的命令，但从队列中得到第一个可处理命令为空！") ;
									//退出
									this.out(queue) ;
								}else{
									if(node.commandType.equals(CommandTypeForGprsSerial.CommandType.confirmCom)){
										//确认应答命令，立即发送
										queue.remove(node) ;
										if(session != null && session.isConnected()){
											this.sendCommand(node , session, rsm, false) ;
										}
										log.info("发送确认应答命令，并把其从命令缓存队列中清除。") ;
										//////////////////////////////////////////////////////////////////////////////////////
										//每次发送命令后，如果有后续可处理的命令，都要等待一个时间间隔，以使不同命令之间有一个间隔时长
										int count = queue.sizeOfCanDeal() ;
										if(count > 0){
											//队列中还有可处理命令，进入等待
											try {
												log.info("发送确认应答后，命令缓存中还有" + count + "条可处理的命令，进行发送命令间的等待。") ;
												queue.wait(pbvo.confirmCommandInterval * 1000) ;
												//注意!在等待期间可能有新的命令加入到队列中。 
											} catch (InterruptedException e) {
											}finally{}
										}
									}else{
										//非确认应答命令，
										if(node.isSuccessed()){
											//已经成功的命令，即已经收到命令结果
											//首先保存测控终端状态
											MeterStatusManager.instance().commandSuccess(id) ;
											queue.remove(node) ;
											log.info("当前命令是成功命令(已收到结果)(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")，清除出缓存队列。") ;
											/*
											 以下注释部分实现方式的考虑：
											           一个命令可能有多个命令结果(如召测24小时数据)，即命令应答分多个数据包上报，所以命令不能立即清除，要尽量等命令结果全部到来，直到命令超时被清除出命令队列，
											          但是这种实现方式会带来问题，即如果业务系统出来多个相同命令(即命令功能码相同)，基于当前协议设计(上报数据中无命令ID)，从上报的应答数据中不能区分结果数据是属于哪个命
											          令的，所以会把各个命令的结果 都匹配给第一个命令，从而带来混乱，所以不能应用此种实现方式。
											           											  
											//首先保存测控终端状态
											if(!node.hasSetSuccessStatus){
												MeterStatusManager.instance().commandSuccess(id) ;
												node.hasSetSuccessStatus = true ;
											}
											if(node.isValidLife()){
												//命令生命期在有效范围内
												//标识本次不处理此命令，因为一个命令结果可能分包上报，所以仍不清除队列，以等后续结果，直到命令超时被清除
												node.canDeal = false ;
												log.info("当前命令是成功命令(已收到结果)(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")，不处理，保留在命令队列中，等待后续结果(数据分包上报)，直到命令超时被清除出队列。") ;
											}else{
												//命令生命期超出有效范围
												//把命令清除出队列
												queue.remove(node) ;
												log.info("当前命令是成功命令(已收到结果)(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")，但已超时，清除出命令队列。") ;
											}
											*/
										}else{
											//非确认应答命令,并且也非成功命令
											if(node.commandType.equals(CommandTypeForGprsSerial.CommandType.objCom)){
												//非确认应答命令,也非成功命令，而且是原生命令
												if(session != null && session.isConnected()){
													//当前测控器在线
													//处理原生命令,在此方法退出前，此原生命令构造成字节数组命令，并已经加入到队列中
													this.dealObjCommand(node) ;
													queue.remove(node) ;
													log.info("发送原生命令去构建字节命令，把原生命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")清除出缓存队列。") ;
													//退出dealObjCommand方法后，原生命令已经构造成字节数组命令，并已经加入到本线程正在处理的队列中
												}else{
													//当前测控器不在线
													//设置本命令在本线程中，本次不处理
													node.canDeal = false ;
												}
											}//end 原生命令
											else if(node.commandType.equals(CommandTypeForGprsSerial.CommandType.byteCom)){
												//非确认应答命令,也非成功命令，而且是字节数组命令
												if(!session.isConnected()){
													//当前测控器不在线
													if(node.commandSendByNoOnLine.equals(CommandTypeForGprsSerial.CommandSendByNoOnLine.noOnLine_delete)){
														//不在线即删除命令，不等待上线
														queue.remove(node) ;
														log.info("测控器不在线，把不在线即删除命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")清除出缓存队列。") ;
													}else{
														//不在线仍保留命令，
														if(!node.isValidLifeWithSendTimes()){
															//命令达到或超过了允许重发的最大次数，并且命令超时，此命令将被清除
															//首先保存测控终端状态
															MeterStatusManager.instance().commandFail(id) ;
															queue.remove(node) ;
															log.info("测控器不在线，把超时命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")清除出缓存队列。") ;
														}else if(!node.isValidTimes()){
															//命令达到或超过了允许重发的最大次数，但命令未超时，
															//不做处理，进行等待(最长等待超时时长)，以便最后尝试接收测控器的回复
															node.canDeal = false ;
															log.info("测控器不在线，命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")达到最大发送次数，但未超时，本次不处理。") ;
														}else{
															//有效命令
															//但不在线，本次不处理
															node.canDeal = false ;
															log.info("测控器不在线，命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")有效，本次不处理。") ;
														}
													}
												}else{
													//当前测控器在线
													if(!node.isValidLifeWithSendTimes()){
														//命令达到或超过了允许重发的最大次数，并命令超时，此命令将被清除
														//首先保存测控终端状态
														MeterStatusManager.instance().commandFail(id) ;
														queue.remove(node) ;
														log.info("测控器在线，把超时命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")清除出缓存队列。") ;
													}else if(!node.isValidTimes()){
														//命令达到或超过了允许重发的最大次数，但命令未超时，
														//如果是单次发送命令，发送次已经是1
														//不做处理，进行等待(最长等待超时时长)，以便最后尝试接收测控器的回复
														node.canDeal = false ;
														log.info("测控器在线，命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")达到最大发送次数，但未超时，本次不处理。") ;
													}else{
														//有效命令
														//发送命令
														node.addSendTimes() ;
														node.canDeal = false ;
														
														log.info("测控器在线，发送命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")。") ;
														this.sendCommand(node, session, rsm, true) ;
														////////////////////////////////////////////
														//每次发送命令后，如果有后续可处理的命令，都要等待一个时间间隔，以使不同命令之间有一个间隔时长
														int count = queue.sizeOfCanDeal() ;
														if(count > 0){
															//队列中还有可处理命令
															try {
																log.info("发送命令后，命令缓存中还有" + count + "条可处理的命令，进行发送命令间的等待。") ;
																queue.wait(pbvo.sendCommandInterval * 1000) ;
																//注意!在等待期间可能有新的命令加入到队列中。 
															}  catch (InterruptedException e) {
															}finally{}
														}
													}
												}//end 网络会话在线
											}//end 字节数组命令
											else{
												//不能识别的命令类型
												queue.remove(node) ;
												log.info("严重错误，命令缓存队列的命令类型不能识别，并把其清除出队列。") ;
											}
										}//end 非确认应答命令,并且也非成功命令
									}//end 非确认应答命令，
								}//end node不为空
							}//end comNum > 0
							else{
								//无可处理的命令
								this.out(queue) ;
							}
						}//end while循环
					}//end synchronized(queue)
				}
				/**
				 * 退出处理
				 */
				private void out(CommandQueueForGprsSerial queue){
					needDeal = false ;
					queue.canDealAll() ;
					id_threadJob.remove(id) ;
				}
				/**
				 * 发送命令
				 * @param node
				 */
				private void sendCommand(CommandNodeForGprsSerial node , IoSession se, RemoteSessionManager rsm , boolean setSendMoment){
					boolean isException = false ;
					try{
						se.write(node.commandData) ;
						if(node.commandData instanceof byte[]){
							String dataHex = null ;
							try {
								dataHex = new UtilProtocol().byte2Hex((byte[])node.commandData , true) ;
							} catch (Exception e) {
								e.printStackTrace();
								log.error("将数据转换为十六进制时出错！" ) ;
							}
							AmLog.log(id, "下发命令数据:" + dataHex) ;
						}
					}catch(Exception e){
						isException = true ;
						log.error("出错，向测控器( ID:" + node.id + ")发送命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")时发生异常。" , e) ;
					}finally{
						if(!isException){
							if(setSendMoment){
								rsm.setSendCommandMomentForGprs(node.id, new Long(System.currentTimeMillis())) ;
							}
							//保存测控终端状态
							MeterStatusManager.instance().sendMeterData(id, ((byte[])node.commandData).length) ;
							try {
								log.info("成功向测控器发送了命令(id=" + node.id + "/命令ID=" + node.commandId + "/功能码=" + node.code + "/已经发送次数:" + node.sendTimes + ")。\n>>>>>>>>发向测控终端(ID:" + node.id + ")命令:" + new UtilProtocol().byte2Hex((byte[])node.commandData , true).toUpperCase()) ;
							} catch (Exception e) {
							}
						}
					}
				}
				
				/**
				 * 处理原生命令
				 */
				private void dealObjCommand(CommandNodeForGprsSerial node){
					new DespatchFromCachCommand().receiveCachCommand(node.commandData) ;
				}
				
			}) ;
		}
	}

	}
		
	
}
