package com.netty.server;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyServerHandler extends SimpleChannelInboundHandler<Object>implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7465474284894215790L;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("exception", cause);
		ctx.close();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("server接收到消息:" + msg);
		ctx.channel().writeAndFlush("这是server响应到客户端的消息，客户端原始请求消息为：" + msg);
	}
}
