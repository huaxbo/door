package com.automic.door.web.app;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.am.cs12.commu.protocol.Data;
import com.am.cs12.commu.protocol.amRtu206.Code206;
import com.am.cs12.commu.protocol.amRtu206.cdF1.Data206_cdF1;
import com.am.cs12.commu.protocol.amRtu206.cdF1.Param206_cdF1;
import com.automic.door.util.cmder.CmdRlt;
import com.automic.door.util.cmder.CmdSender;
import com.automic.global.util.ConstantGlo;
import com.automic.global.util.mvc.MvcCfg;

@RestController
public class DoorController {

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
    		
    		return vo;
    	}
    	//命令发送
    	HashMap<String,Object> params = new HashMap<String,Object>(0);
    	params.put(Param206_cdF1.KEY, flag);
    	CmdSender.sendCmd(dtuId, cmdId, code, params);
    	//命令结果获取
    	Object rlt = cmder.getCmdRltWait(cmdId, null);
    	if(rlt == null){
    		vo.setSucc(ConstantGlo.NO);
    		vo.setError("命令回执超时，请稍后重试...");
    	}else{
    		Data d = (Data)rlt;
    		Data206_cdF1 sd = (Data206_cdF1)d.getSubData();
    		vo.setSucc(ConstantGlo.YES);
    		vo.setRltState(sd.getState());
    	}
        
        return vo;
    }
}
