package com.giousa.heart;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class ServerHandler extends SimpleChannelHandler{

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		System.out.println("Server Message Receieved:"+e.getMessage());
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) e;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");
            System.out.println("state:"+event.getState()+"  "+simpleDateFormat.format(new Date()));
            
            //没有读和没有写
            if(event.getState() == IdleState.ALL_IDLE){
            	//关闭会话（踢玩家下线）
            	Channel channel = ctx.getChannel();
            	ChannelFuture write = channel.write("时间超时，你将要下线！");
            	write.addListener(new ChannelFutureListener() {
					
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						channel.close();
					}
				});
            	
            }
            
            //没有读
            if(event.getState() == IdleState.READER_IDLE){
            	
            }
            
            //没有写
            if(event.getState() == IdleState.WRITER_IDLE){
            	
            }
            
            
        } else {
            super.handleUpstream(ctx, e);
        }
	}
	
	

}
