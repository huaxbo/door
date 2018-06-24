package com.am.cs12.commu.protocol ;


public class Action {
	private int op;

	/**
	 * 没有动作
	 */
	public static final Action nullAction = new Action(0);
	/**
	 * 没有动作
	 */
	public static final Action noAction = new Action(1);

	/**
	 * 向测控端回复确认
	 */
	public static final Action remoteConfirm = new Action(4);
	
	/**
	 * 向测控端发命令
	 */
	public static final Action remoteCommand = new Action(8);

	/**
	 * 命令结果
	 */
	public static final Action commandResult = new Action(16);

	/**
	 * 命令结果
	 */
	public static final Action commandWait = new Action(32);
	
	/**
	 * 主动上报数据
	 */
	public static final Action autoReport = new Action(64);


	/**
	 * 修改 ID
	 */
	public static final Action changeId = new Action(128);
	
	/**
	 * 同步测控器与通信服务器时钟，即对测控器较时
	 */
	public static final Action synchronizeClock = new Action(256);
	
	/**
	 * RTU网络注消 , 关闭网络连接
	 */
	public static final Action closeConnect = new Action(1024);


	/**
	 * 未知的命令
	 */
	public static final Action unknown = new Action(2048);

	/**
	 * 出错
	 */
	public static final Action error = new Action(4096);
	
	/**
	 * 卫星主动上报确认
	 */
	public static final Action stateReportConfirm = new Action(8192);

	private Action(int op) {
		this.op = op;
	}

	//当前Action中，是否存在上面预定义的某一个action
	public boolean has(Action action) {
		return (this.op & action.op) != 0 ;
	}

	//在一个action中加入另外一个action,
	public Action add(Action addAction , Action addedAction) {
		return new Action(addAction.op | addedAction.op);
	}

	/**
	 * 比较两个action是否相同
	 * @param action Action
	 * @return boolean
	 */
	public boolean equals(Action action) {
		return this.op == action.op;
	}
	
	public String toString(){
		return this.op + "" ;
	}
	
}

