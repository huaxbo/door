package com.am.cs12.util;

import java.lang.reflect.Method;
import java.util.*;

import gnu.io.CommPortIdentifier;


public class FindCom {	

	static private Class<?> classCommPortIdentifier;
	static {
		try {
			classCommPortIdentifier = Class.forName("gnu.io.CommPortIdentifier");//"javax.comm.CommPortIdentifier");
		} catch (ClassNotFoundException e1) {
			throw new RuntimeException("出错，CommPortIdentifier类未发现!");
		}
	}

	/**
	 * 发现串口
	 * @return
	 */
	private static Enumeration<CommPortIdentifier> getPortIdentifiers() {
		if (classCommPortIdentifier == null) {
			throw new RuntimeException("出错，CommPortIdentifier类未发现!");
		}
		Enumeration<CommPortIdentifier> list;
		try {
			// get the enumeration of real objects
			Method method = classCommPortIdentifier.getMethod("getPortIdentifiers", (java.lang.Class[]) null);
			CommPortIdentifier type = null;
			list = invokeAndCastEnumeration(type, method, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// wrap the real objects
		Vector<CommPortIdentifier> vec = new Vector<CommPortIdentifier>();
		while (list.hasMoreElements()){
			vec.add(list.nextElement());
		}
		return vec.elements();
	}
	@SuppressWarnings( { "unchecked" })
	public static <T> Enumeration<T> invokeAndCastEnumeration(T returnType,
			Method m, Object obj, Object... args) {
		try {
			return (Enumeration<T>) m.invoke(obj, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 发现串口
	 * @return
	 */
	public List<String> find(){
		List<String> coms = new ArrayList<String>() ;
		Enumeration<CommPortIdentifier> portList = getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				coms.add(portId.getName());
			}
		}
		return coms ;
	}

	public static void main(String args[]) {
		FindCom o = new FindCom();
		List<String> list = o.find();
		if(list != null){
			Iterator<String> it = list.iterator() ;
			while(it.hasNext()){
				System.out.println(it.next()) ;
			}
		}
	}
}
