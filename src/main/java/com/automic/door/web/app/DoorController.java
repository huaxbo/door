package com.automic.door.web.app;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.am.cs12.commu.core.remoteCommand.forGprsSerial.CommandTypeForGprsSerial;
import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.Code206;
import com.am.cs12.commu.protocol.amRtu206.cdF1.Param206_cdF1;
import com.am.cs12.commu.protocol.amRtu206.cdF2.Param206_cdF2;
import com.am.cs12.commu.protocol.amRtu206.cdF3.Param206_cdF3;
import com.am.util.DateTime;
import com.automic.door.util.cmder.CmdRlt;
import com.automic.door.util.cmder.CmdRltCache;
import com.automic.door.util.cmder.CmdSender;
import com.automic.door.util.pusher.JgPusher;
import com.automic.global.util.ConstantGlo;
import com.automic.global.util.mvc.MvcCfg;

@RestController
public class DoorController {

	private Logger log = LogManager.getLogger(DoorController.class);
	 /**
     * url前缀--自定义
     */
    private static final String act_prefix = "door";
    public static int counter = 0;
    /**
     * 门在线状态
     * @param dtuId
     * @return 1在线、0断线
     */
    @RequestMapping("/" + act_prefix + "/online" + MvcCfg.action_suffix)
    public Integer online(String dtuId){
    	
    	return CmdSender.isOnline(dtuId) ? 1 : 0;
    }
    
	
    /**
     * 门控操作
     * @param dtuId
     * @param code 功能码 F1/F2/F3
     * @param flag 指令类别：0查询、1开、2关、3停
     * @param tp 查询类别：1操作查询、0常规查询
     * @return
     */
    @RequestMapping("/" + act_prefix + "/state" + MvcCfg.action_suffix)
    public DoorVO state(String dtuId,String code,Integer flag,Integer tp){
    	DoorVO vo = new DoorVO();
    	vo.setDtuId(dtuId);
    	
    	//时间有效校验
    	String ymd = DateTime.yyyy_MM_dd();
    	if(ymd.compareTo("2018-09-30") > 0){
    		vo.setError("授权过期！");
    		
    		return vo;
    	}
    	//缓存结果
    	if(flag == 0 && tp == 0){
        	Data cd = CmdRltCache.singleInstance().getRlt(dtuId);
        	if(cd != null){
        		vo.setSucc(ConstantGlo.YES);
        		vo.setRltState(cd.getSubData());
        		log.info("设备[" + dtuId + "]命令[" + flag + "]缓存结果回执=" + cd.getSubData());
            	
        		return vo;
        	}else{
        		log.info("设备[" + dtuId + "]命令[" + flag + "]无缓存指令，实时发送指令！");
        	}
    	}
    	
    	//指令下发
    	CmdRlt cmder = CmdRlt.singleInstance();
    	String cmdId = cmder.generateCmdId();//command id

    	log.info("APP请求设备[" + dtuId + "]实时指令：" + code + "/" + flag);
    	
    	//查询设备否在线
    	if(!CmdSender.isOnline(dtuId)){
    		vo.setSucc(ConstantGlo.NO);
    		vo.setError("设备[" + dtuId + "]尚未上线，命令发送失败！");
    		log.warn("设备[" + dtuId + "]尚未上线，命令发送失败！");
    		
    		return vo;
    	}
    	//命令发送
    	HashMap<String,Object> params = new HashMap<String,Object>(0);
    	if(code.equals(Code206.cd_F1)){
        	Param206_cdF1 param = new Param206_cdF1();
        	param.setState(flag);
        	params.put(Param206_cdF1.KEY, param);
    	}
    	if(code.equals(Code206.cd_F2)){
        	Param206_cdF2 param = new Param206_cdF2();
        	param.setState(flag);
        	params.put(Param206_cdF2.KEY, param);
    	}if(code.equals(Code206.cd_F3)){
        	Param206_cdF3 param = new Param206_cdF3();
        	param.setState(flag);
        	params.put(Param206_cdF3.KEY, param);
    	}

    	CmdSender.sendCmd(dtuId, cmdId, code, params,getCmdPriority(flag));
    	//命令结果获取
    	Object rlt = cmder.getCmdRltWait(cmdId, null);
    	if(rlt == null){
    		vo.setSucc(ConstantGlo.NO);
    		vo.setError("设备[" + dtuId + "]命令[" + flag + "]回执超时，请稍后重试...");
    		log.warn("设备[" + dtuId + "]命令[" + flag + "]回执超时，请稍后重试...");
    	}else{
    		Data d = (Data)rlt;
    		
    		vo.setSucc(ConstantGlo.YES);
    		vo.setRltState(d.getSubData());
    		log.info("设备[" + dtuId + "]命令[" + flag + "]回执=" + d.getSubData());
    	}

        return vo;
    }
    
    private CommandTypeForGprsSerial.CommandSendPriority getCmdPriority(int flag){
    	
    	return flag >0 ? CommandTypeForGprsSerial.CommandSendPriority.yes : CommandTypeForGprsSerial.CommandSendPriority.no;
    }
    /**
     * 广告推送
     * @param dtuId
     * @return
     */
    @RequestMapping("/" + act_prefix + "/pushAdver" + MvcCfg.action_suffix)
    public DoorVO pushAdver(String mess){
    	DoorVO vo = new DoorVO();
    	if(mess == null || mess.trim().equals("")){

    		vo.setSucc(ConstantGlo.NO);
    		log.warn("推送信息失败！");
    		log.error("推送信息不能为空！");
    		
    		return vo;
    	}
    	log.info("推送信息=" + mess);
    	
    	String rlt = JgPusher.pushMess(mess, "信息通知");
    	if(rlt != null){
    		vo.setSucc(ConstantGlo.YES);
    		log.info("推送信息成功！");
    	}else{
    		vo.setSucc(ConstantGlo.NO);
    		log.warn("推送信息失败！");
    	}
    	
    	return vo;
    }
    
}
