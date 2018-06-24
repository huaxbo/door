package com.am.util;


public final class UniqueId {

	/**
	 * 
	 * @return
	 */
	public static synchronized String create() {
		return System.nanoTime()+ "";
	}

	public static void main(String args[]){
		for(int i = 0 ; i < 100 ; i++){
			System.out.println(UniqueId.create()) ;
//			System.out.println(System.currentTimeMillis()) ;
		}
	}
}
