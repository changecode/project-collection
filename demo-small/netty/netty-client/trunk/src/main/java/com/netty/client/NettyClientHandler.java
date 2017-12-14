package com.netty.client;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<Object> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1563554977632863510L;

	private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("client接收到服务器返回的消息:" + msg);
	}
}
