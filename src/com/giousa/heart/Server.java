package com.giousa.heart;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;

public class Server {

	public static void main(String[] args) {
		
		//服务类
		ServerBootstrap bootstrap = new ServerBootstrap();
		//创建线程池
		ExecutorService boss = Executors.newCachedThreadPool();
		ExecutorService worker = Executors.newCachedThreadPool();
		//设置NioSocket工厂
		bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));
		
		HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();
		
		//设置管道工厂
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("idle", new IdleStateHandler(hashedWheelTimer, 5, 5, 10));
				pipeline.addLast("decoder", new StringDecoder());
				pipeline.addLast("encode", new StringEncoder());
				pipeline.addLast("heart handler", new ServerHandler());
				return pipeline;
			}
		});
		
		bootstrap.bind(new InetSocketAddress(7788));
		System.out.println("Server Start");
	}

}
