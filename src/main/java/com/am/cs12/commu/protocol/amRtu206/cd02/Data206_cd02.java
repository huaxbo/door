package com.am.cs12.commu.protocol.amRtu206.cd02;

import com.am.cs12.commu.protocol.amRtu206.util.Constant;

public class Data206_cd02 {

	/**
	 * 用于GPRS、CDMA登录、退出登录、在线保持。数据域：1个字节，F0登录，F1退出登录，F2在线保持。
	 */
	private Integer flag ;
	
	public String toString(){
		String s = "\n" ;
		s += "链路测试：" + ":" + (this.flag==null?"":(this.flag.intValue() == (byte)Constant.Flag_f0_forCode2 ? "登录":(this.flag.intValue() == (byte)Constant.Flag_f1_forCode2 ? "退出登录":(this.flag.intValue() == (byte)Constant.Flag_f2_forCode2 ? "在线保持":"不能识别")))) ;
		return s ;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	
}
