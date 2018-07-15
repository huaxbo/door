package com.automic.door.util.cmder;

import java.util.Hashtable;

import com.automic.door.web.app.DoorVO;
import com.automic.global.util.ConstantGlo;

public class RltCache {

	private static int effect = 20;//时间有效范围，单位：s
	private static Hashtable<String,DoorVO> caches = new Hashtable<String,DoorVO>(0);
	
	/**
	 * 
	 */
	private RltCache(){}
	
	
	/**
	 * 更新数据
	 * @param dtuId
	 * @param vo
	 */
	public synchronized static void updateData(String dtuId,Object sub){
		DoorVO vo = caches.get(dtuId);
		if(vo == null){
			vo = new DoorVO();
			vo.setDtuId(dtuId);
			vo.setSucc(ConstantGlo.YES);
			vo.setRltState(sub);
			caches.put(dtuId, vo);
		}else{
			vo.updateStamp();
			vo.setRltState(sub);
		}
	}
	
	/**
	 * 获取数据
	 * @param dtuId
	 * @return
	 */
	public synchronized static DoorVO getData(String dtuId){
		DoorVO vo =  caches.get(dtuId);
		long curr = System.currentTimeMillis();
		if(vo == null 
				|| (curr - vo.getStamp() > effect * 1000)){
			vo = new DoorVO();
			vo.setDtuId(dtuId);
			vo.setSucc(ConstantGlo.NO);
    		vo.setError("设备无最新有效数据！");
		}
		
		return vo;
	}
}
