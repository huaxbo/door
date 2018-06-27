package com.automic.door.util.pusher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.automic.global.util.ConfigGloUtil;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

public class JgPusher {

	private static Logger log = LogManager.getLogger(JgPusher.class);

	/**
	 * @param mess
	 * return null表示发送失败
	 */
	public static String pushMess(String mess,String title) {
		JPushClient jpushClient = new JPushClient(
				ConfigGloUtil.getElementText("jg_secret"),
				ConfigGloUtil.getElementText("jg_key"), null,
				ClientConfig.getInstance());

		// For push, all you need do is to build PushPayload object.
		PushPayload payload = buildPushObject_all_all_alert(mess);
		try {
			PushResult result = jpushClient.sendPush(payload);
			
			log.info(result.getOriginalContent());
			
			return result.getOriginalContent();
			
		} catch (APIConnectionException e) {
			// Connection error, should retry later
			log.error("Connection error, should retry later", e);
			
			return null;
		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			log.error("Should review the error, and fix the request", e);
			log.info("HTTP Status: " + e.getStatus());
			log.info("Error Code: " + e.getErrorCode());
			log.info("Error Message: " + e.getErrorMessage());
			
			return null;
			
		}finally{}
	}

	 /**
	 * @param mess
	 * @return
	 */
	private static PushPayload buildPushObject_all_all_alert(String mess) {
	        return PushPayload.alertAll(mess);
	}
		
	
	public static void main(String[] args){
		pushMess("信息推送测试","门控信息");
	}
}
