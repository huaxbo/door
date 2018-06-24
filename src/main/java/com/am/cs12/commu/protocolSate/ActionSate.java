package com.am.cs12.commu.protocolSate;



public class ActionSate {
	private int op;

	/**
	 * 没有动作
	 */
	public static final ActionSate nullAction = new ActionSate(0);
	/**
	 * 没有动作
	 */
	public static final ActionSate noAction = new ActionSate(1);

	/**
	 * 向卫星终端发送命令
	 */
	public static final ActionSate command = new ActionSate(2);

	/**
	 * 发给卫星终端的命令，卫星终端确认收到命令
	 */
	public static final ActionSate commandConfirm = new ActionSate(4);

	/**
	 * 发给卫星终端的命令，卫星终端应答
	 */
	public static final ActionSate commandAnswer = new ActionSate(8);

	/**
	 * 卫星终端收到的远程测控器的数据
	 */
	public static final ActionSate receivedData = new ActionSate(256);
	/**
	 * 卫星授时数据
	 */
	public static final ActionSate grantTimeData = new ActionSate(512);
	/**
	 * 未知的命令
	 */
	public static final ActionSate unknown = new ActionSate(2048);

	/**
	 * 出错
	 */
	public static final ActionSate error = new ActionSate(4096);

	private ActionSate(int op) {
		this.op = op;
	}

	//当前Action中，是否存在上面预定义的某一个action
	public boolean has(ActionSate action) {
		return (this.op & action.op) != 0;
	}

	//在一个action中加入另外一个action,
	public void add(ActionSate action) {
		this.op = (this.op | action.op);
	}

	/**
	 * 比较两个action是否相同
	 * @param action Action
	 * @return boolean
	 */
	public boolean equals(ActionSate action) {
		return this.op == action.op;
	}
	
	public String toString(){
		return this.op + "" ;
	}

}
