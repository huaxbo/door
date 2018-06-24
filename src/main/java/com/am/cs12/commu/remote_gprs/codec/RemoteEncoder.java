package com.am.cs12.commu.remote_gprs.codec ;

import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.io.*;


public class RemoteEncoder extends ProtocolEncoderAdapter {


    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws IOException, Exception{
        byte[] com = (byte[]) message;
        int capacity = (com==null?0:com.length) ;
        IoBuffer buffer = IoBuffer.allocate(capacity, false);
        buffer.put(com);
        buffer.flip();
        out.write(buffer);
    }

 }
