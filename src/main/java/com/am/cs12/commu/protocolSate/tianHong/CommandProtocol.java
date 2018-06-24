package com.am.cs12.commu.protocolSate.tianHong;

import com.am.cs12.commu.protocolSate.tianHong.$QSTA.Read_QSTA ;
import com.am.cs12.commu.protocolSate.tianHong.$TTCA.Write_TTCA;
import com.am.cs12.commu.protocolSate.tianHong.$TAPP.Read_TAPP;
import com.am.cs12.commu.protocolSate.CommandSate;

public class CommandProtocol {
	
	/**
	 * 构造命令---查询卫星终端状态
	 * @param com
	 * @return
	 * @throws Exception
	 */
	public byte[] read_QSTA(CommandSate com) throws Exception {
		return new Read_QSTA().remoteData(com) ;
	}
	
	/**
	 * 构造命令---叫卫星终端发送数据
	 * @param com
	 * @return
	 * @throws Exception
	 */
	public byte[] write_TTCA(CommandSate com) throws Exception {
		return new Write_TTCA().remoteData(com) ;
	}

	
	/**
	 * 构造命令---申请卫星授时
	 * @param com
	 * @return
	 * @throws Exception
	 */
	public byte[] read_TAPP(CommandSate com) throws Exception {
		return new Read_TAPP().remoteData(com) ;
	}

}
