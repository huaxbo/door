package com.automic.door.web.app;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.am.cs12.commu.protocol.amRtu206.Code206;
import com.am.cs12.commu.protocol.amRtu206.cdF1.Param206_cdF1;
import com.am.cs12.commu.protocol.amRtu206.cdF2.Param206_cdF2;
import com.am.cs12.commu.protocol.amRtu206.cdF3.Param206_cdF3;
import com.automic.door.util.cmder.CmdRlt;
import com.automic.door.util.cmder.CmdSender;
import com.automic.door.util.cmder.RltCache;
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
    
    /**
     * 查询DTUID
     * @param dtuId DTUID
     * @return
     */
    @RequestMapping("/" + act_prefix + "/getDtuId" + MvcCfg.action_suffix)
    public DoorVO getDtuId(String dtuId){
    	CmdRlt cmder = CmdRlt.singleInstance();
    	String cmdId = cmder.generateCmdId();//command id
    	DoorVO vo = new DoorVO();
    	vo.setDtuId(dtuId);
    	//查询设备否在线
    	if(!CmdSender.isOnline(dtuId)){
    		vo.setSucc(ConstantGlo.NO);
    		vo.setError("设备尚未上线，命令发送失败！");
    		
    		return vo;
    	}
    	//命令发送
    	HashMap<String,Object> params = new HashMap<String,Object>(0);
    	CmdSender.sendCmd(dtuId, cmdId, Code206.cd_50, params);
    	//命令结果获取
    	Object rlt = cmder.getCmdRltWait(cmdId, null);
    	if(rlt == null){
    		vo.setSucc(ConstantGlo.NO);
    		vo.setError("命令回执超时，请稍后重试...");
    	}else{
    		vo.setSucc(ConstantGlo.YES);
            vo.setRltDtuId(dtuId);
    	}
        
        return vo;
    }
    
	/**
     * 门控操作
     * @param dtuId
     * @return
     */
    @RequestMapping("/" + act_prefix + "/state" + MvcCfg.action_suffix)
    public DoorVO state(String dtuId,String code,Integer flag){
    	CmdRlt cmder = CmdRlt.singleInstance();
    	String cmdId = cmder.generateCmdId();//command id
    	DoorVO vo = new DoorVO();
    	vo.setDtuId(dtuId);
    	//查询设备否在线
    	if(!CmdSender.isOnline(dtuId)){
    		vo.setSucc(ConstantGlo.NO);
    		vo.setError("设备尚未上线，命令发送失败！");
    		log.warn("设备[" + dtuId + "]尚未上线，命令发送失败！");
    		
    		return vo;
    	}
    	if(flag == 0){
        	//查看缓存数据
        	vo = RltCache.getData(dtuId);
        	
        	StringBuilder lg = new StringBuilder("查询设备[");
        	lg.append(dtuId);
        	lg.append("]最新有效状态数据:");
        	if(vo.getRltState() != null){
            	lg.append(vo.getRltState().toString());
        	}else{
        		lg.append("null");
        	}        	
    		log.info(lg);
    		
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
    	
    	CmdSender.sendCmd(dtuId, cmdId, code, params);
    	/*//命令结果获取
    	Object rlt = cmder.getCmdRltWait(cmdId, null);
    	if(rlt == null){
    		vo.setSucc(ConstantGlo.NO);
    		vo.setError("命令回执超时，请稍后重试...");
    		log.warn("命令回执超时，请稍后重试...");
    	}else{
    		Data d = (Data)rlt;
    		
    		vo.setSucc(ConstantGlo.YES);
    		vo.setRltState(d.getSubData());
    		log.info("命令回执=" + d.getSubData());
    	}
        
        return vo;*/
    	
    	return null;//不等待结果回执，交由app轮训查询
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
