package com.am.common.socketPool;

import java.io.*;

public interface SocketJob {

	public void out(String serverId , byte[] data ,OutputStream out) throws IOException ;
}
