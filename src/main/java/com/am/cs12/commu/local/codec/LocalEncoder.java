package com.am.cs12.commu.local.codec ;

import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import java.io.*;

import com.am.cs12.command.*;

public class LocalEncoder extends ProtocolEncoderAdapter {

	/**
	 * 对数据进行编码，以备网络传输
	 */
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws IOException, Exception{
    	MinaData minaData = null ;
        if(message instanceof Command){
        	minaData = new MinaData() ;
        	minaData.setCom((Command)message) ;
        }else{
        	minaData = (MinaData) message;
        }
        byte[] bytes1 = getCommandBytes(minaData.getCom());
        byte[] bytes2 = minaData.getAttachment() ;
        int capacity = (bytes1==null?0:bytes1.length) + (bytes2==null?0:bytes2.length) + 8;
        IoBuffer buffer = IoBuffer.allocate(capacity, false);
        buffer.putInt(bytes1.length);
        buffer.put(bytes1);
        if(bytes2 == null){
            buffer.putInt(0);
        }else{
            buffer.putInt(bytes2.length);
            buffer.put(bytes2);
        }
        buffer.flip();
        out.write(buffer);
    }

    /**
     * 将命令转换成字节数组
     * @param com
     * @return
     * @throws IOException
     * @throws Exception
     */
    private byte[] getCommandBytes(Command com) throws IOException, Exception {
    	String xml = com.toXml() ;
    	byte[] bytes = xml.getBytes() ;
        return bytes ;
    }
}
