package com.am.cs12.commu.remote_gprs;

import java.lang.IllegalArgumentException ;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.core.session.IdleStatus ;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder ;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter ;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;

import com.am.cs12.ServerConfig;
import com.am.cs12.commu.remote_gprs.codec.RemoteCodecFactory;
import com.am.cs12.config.conf.*;
import com.am.cs12.config.*;

public class RemoteServer {
	/**
	 * 服务启动
	 * @return MinaContext 返回服务上下文，以备总服务持有
	 */
	public RemoteContext start()
			throws IllegalArgumentException {
		
		RemoteServerVO vo = ConfigCenter.instance().getRemoteServerVO() ;
		
		RemoteContext ctx = new RemoteContext();

		ctx.setObject(RemoteServerConstant.MINASERVERID, vo.id);

		/**
		 * 异步非阻塞网络通信接收器，负责接收联接请求
		 * 同时设置IoProcessor个数
		 * 这个地方用于执行真正的IO 操作，默认启用的线程个数是CPU 的核数+1，譬如：单CPU 双
		 * 核的电脑，默认的IoProcessor 线程会创建3 个。这也就是说一个IoAcceptor 或者
		 * IoConnector 默认会关联一个IoProcessor 池，这个池中有3 个IoProcessor。因为IO 操作
		 * 耗费资源，所以这里使用IoProcessor 池来完成数据的读写操作，有助于提高性能。这也就
		 * 是前面说的IoAccetor、IoConnector 使用一个Selector，而IoProcessor 使用自己单独的
		 * Selector 的原因。
		 * 那么为什么IoProcessor 池中的IoProcessor 数量只比CPU 的核数大1 呢？因为IO 读写操
		 * 作是耗费CPU 的操作，而每一核CPU 同时只能运行一个线程，因此IoProcessor 池中的
		 * IoProcessor 的数量并不是越多越好。
		 */
		NioSocketAcceptor acceptor = new NioSocketAcceptor(vo.processors);
		
		SocketSessionConfig seCon = acceptor.getSessionConfig() ;
		
		/* 设置读数据一次性申请堆缓存大小，
		 * 一般不需要调用这个方法，因为IoProcessor 会自动调
		 * 整缓冲的大小。你可以调用setMinReadBufferSize()、setMaxReadBufferSize()方法，这
		 * 样无论IoProcessor 无论如何自动调整，都会在你指定的区间。
		 */
		//seCon.setReadBufferSize(1024);
		//设置网络联接空闲时长
		seCon.setIdleTime(IdleStatus.BOTH_IDLE, vo.idle);

		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain() ;
		//编解码过滤器
		chain.addLast("protocol", new ProtocolCodecFilter(new RemoteCodecFactory()));
		if(ServerConfig.minaLogEnable){
			//日志过滤器,add by hua xinbo at 2013-04-19 14:17
			LoggingFilter lf = new LoggingFilter();
			lf.setSessionCreatedLogLevel(LogLevel.NONE);
			lf.setSessionIdleLogLevel(LogLevel.NONE);
			chain.addLast("logger", lf);	
		}		
		/*
		 * 一般ExecutorFilter 都是要放在ProtocolCodecFilter 过
		 * 滤器的后面，也就是不要让编解码运行在独立的线程上，而是要运行在IoProcessor 所在的
		 * 线程，因为编解码处理的数据都是由IoProcessor 读取和发送的，没必要开启新的线程，否
		 * 则性能反而会下降。一般使用ExecutorFilter 的典型场景是将业务逻辑（譬如：耗时的数
		 * 据库操作）放在单独的线程中运行，也就是说与IO 处理无关的操作可以考虑使用
		 * ExecutorFilter 来异步执行。
		 */
		chain.addLast("exceutor", new ExecutorFilter());

		//业务逻辑处理器，负责处理网络会话及输入输出数据
		RemoteIoHandler handler = new RemoteIoHandler();
		acceptor.setHandler(handler);


		boolean isException = false ;
		
		try {
			acceptor.bind(new InetSocketAddress(vo.port));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("远程网络通信服务(ID:" + vo.id + ")启动失败！");
			isException = true ;
		}finally{
			;
		}

		if(!isException){
			System.out.println("远程网络通信服务(ID:" + vo.id + ")已经启动 ，端口为" + vo.port);
			return ctx;
		}else{
			return null ;
		}

	}
}
