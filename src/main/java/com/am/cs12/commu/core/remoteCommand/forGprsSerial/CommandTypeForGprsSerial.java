package com.am.cs12.commu.core.remoteCommand.forGprsSerial;

public final class CommandTypeForGprsSerial {
	/**
	 * 命令类型
	 * 是否已经生成字节数据或仍以Command对象形式存的命令
	 * 对于设置时钟命令，是以Command对象形式存的命令，因为当没控器不在线时，命令要缓存，只在测控器上线后，才发送此命令，此时构造命令并付值时间，此时方准确
	 * @author liu runyu
	 *
	 */
	public static class CommandType{
		public static final CommandType objCom = new CommandType((byte)0) ;//原生命令，未生成字节数据，仍是以Command对象存在的命令
		public static final CommandType byteCom = new CommandType((byte)1) ;//一般命令，已经生成了字节数据
		protected static final CommandType confirmCom = new CommandType((byte)2) ;//确认应答
		private byte code ;
		private CommandType(byte code){
			this.code = code ;
		}
		/**
		 * 重载方法
		 * @param commandType
		 * @return
		 */
		@Override
		public boolean equals(Object obj){
			if(obj instanceof CommandType){
				if(this.code == ((CommandType)obj).code){
					return true ;
				}
			}
			return false ;
		}
	}
	/**
	 * 命令优先级
	 * 优先的命令放在命令队列的最前面，当测控器在线时，优先发送命令
	 * 非优先的命令放在命令队列的最后面，
	 * 例如对测控上报数据（主动上报数据或命令结果数据）的确认命令是优先的命令
	 * 
	 * @author liu runyu
	 *
	 */
	public static class CommandSendPriority{
		public static final CommandSendPriority yes = new CommandSendPriority((byte)1) ;//优先
		public static final CommandSendPriority no = new CommandSendPriority((byte)0) ;//非优先
		private byte code ;
		private CommandSendPriority(byte code){
			this.code = code ;
		}
		/**
		 * 重载方法
		 * @param commandType
		 * @return
		 */
		@Override
		public boolean equals(Object obj){
			if(obj instanceof CommandSendPriority){
				if(this.code == ((CommandSendPriority)obj).code){
					return true ;
				}
			}
			return false ;
		}
	}
	
	/**
	 * 命令发送次数
	 * 下发的命令如果测控无应答，即命令不成功，间隔一定时间后，
	 * 有的命令需要重新发送，但有的命令不需要重新发送，
	 * 例如对测控上报数据（主动上报数据或命令结果数据）的确认命令是一次性发送的命令
	 * @author liu runyu
	 *
	 */
	public static class CommandSendTimes{
		public static final CommandSendTimes single = new CommandSendTimes((byte)1) ;//单次
		public static final CommandSendTimes repeat = new CommandSendTimes((byte)0) ;//可多次
		private byte code ;
		private CommandSendTimes(byte code){
			this.code = code ;
		}
		/**
		 * 重载方法
		 * @param commandType
		 * @return
		 */
		@Override
		public boolean equals(Object obj){
			if(obj instanceof CommandSendTimes){
				if(this.code == ((CommandSendTimes)obj).code){
					return true ;
				}
			}
			return false ;
		}
	}
	
	/**
	 * 命令发送与测控器是否网络在线的关系
	 * 有的命令当测控器不在线时，将不发送，并
	 * 有的命令当测控器不在线时，等待发送
	 * 例如对测控上报数据（主动上报数据或命令结果数据）的确认命令是非在线不发送的命令
	 * @author liu runyu
	 *
	 */
	public static class CommandSendByNoOnLine{
		public static final CommandSendByNoOnLine noOnLine_delete = new CommandSendByNoOnLine((byte)1) ;//不在线，命令失效
		public static final CommandSendByNoOnLine noOnLine_wait = new CommandSendByNoOnLine((byte)0) ;//不在线，等待
		private byte code ;
		private CommandSendByNoOnLine(byte code){
			this.code = code ;
		}
		/**
		 * 重载方法
		 * @param commandType
		 * @return
		 */
		@Override
		public boolean equals(Object obj){
			if(obj instanceof CommandSendByNoOnLine){
				if(this.code == ((CommandSendByNoOnLine)obj).code){
					return true ;
				}
			}
			return false ;
		}
	}
	

}
