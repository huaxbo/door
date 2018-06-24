package com.am.cs12.commu.publish;

public interface PublishServerContext {
	/**
	 * 得到在服务上下文中注册的对象实例
	 * 
	 * return 所查找的对象
	 */
	public Object getObject(String key)  throws IllegalArgumentException ;

	/**
	 * 在子服务上下文中注册对象实例
	 * 
	 * throws ACException 如果重复放入对象，将抛出异常
	 */
	public void setObject(String key, Object object) throws IllegalArgumentException ;

	/**
	 * 替换上下文中对象实例
	 */
	public void replaceObject(String key, Object object)  throws IllegalArgumentException  ;

}
