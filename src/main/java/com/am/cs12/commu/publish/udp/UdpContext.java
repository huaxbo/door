package com.am.cs12.commu.publish.udp;

import java.util.HashMap;
import com.am.cs12.commu.publish.* ;

public class UdpContext implements PublishServerContext {

	/**
	 * 不用设为static属性，因为db服务可能复用
	 */
	private HashMap<String, Object> objects;

	public UdpContext() {
		objects = new HashMap<String, Object>();
	}

	/**
	 * 得到在子服务上下文中注册的对象实例
	 * 
	 * return 所查找的对象
	 */
	public Object getObject(String key) {
		return this.objects.get(key);
	}

	/**
	 * 在子服务上下文中注册对象实例
	 * 
	 * throws ACException 如果重复放入对象，将抛出异常
	 */
	public void setObject(String key, Object object) throws IllegalArgumentException {
		if (objects.containsKey(key)) {
			throw new IllegalArgumentException("在本地db服务上下文中注册对象实例失败，对象实例key:" + key
					+ "在上下文中已经有注册!");
		}
		objects.put(key, object);
	}

	/**
	 * 替换上下文中对象实例
	 */
	public void replaceObject(String key, Object object) {
		objects.put(key, object);
	}

}
