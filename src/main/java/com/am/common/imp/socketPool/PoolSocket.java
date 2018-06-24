package com.am.common.imp.socketPool;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PoolSocket{
	public Socket socket ;
	public OutputStream out ;
	public InputStream in ;
	public boolean newCreate ;
	public PoolSocket(Socket socket , 
			OutputStream out , 
			InputStream in ,
			boolean  newCreate){
		this.socket = socket ;
		this.out = out ;
		this.in = in ;
	}
}
