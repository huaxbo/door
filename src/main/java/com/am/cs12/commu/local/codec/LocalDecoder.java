package com.am.cs12.commu.local.codec ;

import java.io.*;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;  
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.am.cs12.command.*;


public class LocalDecoder extends CumulativeProtocolDecoder {

    private static final String DECODER_STATE_KEY = LocalDecoder.class.getName() + ".STATE";

    public static final int MAX_SIZE = 2147483647 ; 

    private class DecoderState {
        Command com;
    }

    /**
     * 对网络传输来的数据进行解码
     */
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws IOException, Exception{
        //从会话中得到已经解码的数据
    	DecoderState decoderState = (DecoderState) session.getAttribute(DECODER_STATE_KEY);
        if (decoderState == null) {
            decoderState = new DecoderState();
            session.setAttribute(DECODER_STATE_KEY, decoderState);
        }
        if (decoderState.com == null) {
            //试着读取命令区域数据
        	//MAX_IMAGE_SIZE 目的是防止大数据量攻击
            if (in.prefixedDataAvailable(4, MAX_SIZE)) {
                decoderState.com = readCommand(in);
            } else {
                //命令区域无足够的数据
                return false;
            }
        }
        if (decoderState.com != null) {
            //试着读取数据(文件)区域数据
        	//MAX_IMAGE_SIZE 目的是防止大数据量攻击
            if (in.prefixedDataAvailable(4, MAX_SIZE)) {
            	byte[] bs = getDataBytes(in);
            	MinaData minaCom = new MinaData() ;
            	minaCom.setCom(decoderState.com) ;
            	minaCom.setAttachment(bs) ;
                out.write(minaCom);
                
                decoderState.com = null;
                return true;
            } else {
                //数据(文件)区域无足够的数据
                return false;
            }
        }
        return false;
    }

    /**
     * 读取Command命令
     * @param in
     * @return
     * @throws IOException
     */
    private Command readCommand(IoBuffer iob) throws IOException , Exception{
        int length = iob.getInt();
        byte[] bytes = new byte[length];
        iob.get(bytes);
        Command	com = new Command().toObject(bytes);
        return com;
    }
   
    /**
     * 得到数据域数据的字节数组
     * @param in
     * @return
     * @throws IOException
     */
    private byte[] getDataBytes(IoBuffer iob) throws IOException {
        int length = iob.getInt();
        if(length == 0){
        	return null ;
        }
        byte[] bytes = new byte[length];
        iob.get(bytes);
        return bytes ;
    }
    
}
