package com.am.cs12.commu.remote_gprs.codec ;

import java.io.*;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Vector;

import org.apache.logging.log4j.Logger;
 
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.core.buffer.* ;
import org.apache.mina.core.session.IoSession; 

import com.am.cs12.commu.core.remoteGprs.RemoteSessionManager;
import com.am.cs12.commu.protocol.*;
import com.am.cs12.commu.protocol.amRtu206.common.CommonProtocol;
import com.am.cs12.util.AmConstant;
import com.am.cs12.config.*;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class RemoteDecoder extends CumulativeProtocolDecoder {
	private static Logger log = LogManager.getLogger(RemoteDecoder.class) ;
	/*
	 * 解码
	 * (non-Javadoc)
	 * @see org.apache.mina.filter.codec.CumulativeProtocolDecoder#doDecode(org.apache.mina.core.session.IoSession, org.apache.mina.core.buffer.IoBuffer, org.apache.mina.filter.codec.ProtocolDecoderOutput)
	 */
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws IOException, Exception {
		//非水文规约
		RemoteSessionManager rsm = RemoteSessionManager.instance() ;
		if(!rsm.hasSession(session)){
			//会话管理器中不存在此会话，说明刚建立网络连接，根据通信规约，此数据应该是上线数据
			//上线数据较短，一般不会出现断包或粘包现象
			this.transData(in, null, out) ;
			return true;
		}else{
			//非上线数据，可能会出现断包或粘包现象
			ProtocolBaseVO pbvo = ConfigCenter.instance().getProtocolBaseVO() ;
			String id = rsm.getRtuId(session) ;
			if(id != null){
				RTUProtocolVO rtuPVO = new MeterHelp().getRtuProtocolVO(id) ;
				if(rtuPVO != null){
					Integer[] flag = prefixedDataAvailable(in, rtuPVO, pbvo.headMinLength, AmConstant.MAX_DATA_SIZE) ;
					if(flag[0] == -1){
						//断包了
						return false ;
					}else if(flag[0] == 0 || flag[0] == 1){
						//数据已经全部接收
						this.transData(in, flag[1], out) ;
						if(flag[0] == 1){
							//说明粘包了，还有数据，需要对这些数据再次执行doDecode方法.
				        	log.warn("发生粘包现象。") ;
//							return false ;
							this.doDecode(session, in, out) ;//加上递归，就得return false
							return false;
						}else{
							return true;
						}
					}else{
						//不存在此种情况
						return true ;
					}
				}else{
					log.error("严重错误，未能获得RTU ID为" + id + "测控器协议配置对象，从而不能进行断包或粘包检查，将数据直接放行。") ;
					this.transData(in, null, out) ;
					return true;
				}
			}else{
				log.error("严重错误，会话管理器顾在此测控器的联接会话，但未能从会话管理器中获得测控终端ID，从而不能进行断包或粘包检查，将数据直接放行。") ;
				this.transData(in, null, out) ;
				return true;
			}
		}
	}
	
	/**
	 * 向后传送数据
	 * @param in
	 * @param out
	 */
	private void transData(IoBuffer in, Integer length, ProtocolDecoderOutput out){
		byte[] data = new byte[length==null?in.limit():length];
		try{
			in.get(data);//get一个字节，相应position向后移动一个字节
		}catch(Exception e){
			e.printStackTrace();
		}finally{;}
		out.write(data);
	}
		
	/**
	 * 拼接多包数据
	 * @param vs
	 * @return
	 */
	@SuppressWarnings("unused")
	private byte[] joinDatas(Vector<byte[]> vs){
		byte[] bts = null;
		Iterator<byte[]> it = vs.iterator();
		int tLen = 0;
		while(it.hasNext()){
			byte[] tmp = it.next();
			tLen += CommonProtocol.getReportLen(tmp);
			//多包数据拼接操作.....
		}
		return bts;
	}
	 /**
     * {@inheritDoc}
	 * @throws Exception 
     */
    public Integer[] prefixedDataAvailable(IoBuffer in, RTUProtocolVO rtuPVO, int headMinLength, int maxDataLength) throws Exception {
    	int remain = in.remaining() ;
        if (remain < headMinLength) {
        	log.warn("数据头部发生断包现象。") ;
            return new Integer[]{-1,null};
        }

        int oldPosition = in.position() ;
//    	log.warn("oldPosition:" + oldPosition) ;
       
		byte[] preByte = new byte[headMinLength];
		in.get(preByte) ;
		
		in.position(oldPosition) ;
//    	log.warn("还原oldPosition后:" + in.position()) ;
		
		Complete cp = Complete.instance() ;
		Integer len = cp.match(rtuPVO.name, preByte) ;
		if(len == null){
			throw new BufferDataException("严重错误，在进行断包与粘包检查时，未能得到数据帧的长度。");
		}
		if(len == -10000){//南瑞数据校验
			
			return new Integer[]{0,remain};
		}
        if (len <= 0 || len > maxDataLength) {
            throw new BufferDataException("严重错误，在进行断包与粘包检查时，数据帧的长度(" + len + ")超出合法范围。");
        }
        
        int dataLen = len + (rtuPVO.extraLen == null?0:rtuPVO.extraLen.intValue()) ;
        if(remain == dataLen){
        	log.info("remain:" + remain + "  等于  dataLen:" + dataLen) ;
//        	in.limit(dataLen) ;
            return new Integer[]{0,dataLen};
        }else if(remain > dataLen){
        	log.warn("remain:" + remain + " 大于   dataLen:" + dataLen) ;
//        	in.limit(dataLen) ;
            return new Integer[]{1,dataLen};
        }else{
        	log.warn("发生断包现象。") ;
            return new Integer[]{-1,null};
        }
    }


}
