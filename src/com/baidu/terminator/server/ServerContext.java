/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */ 
package com.baidu.terminator.server;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import com.baidu.terminator.manager.bo.Link;

/**
 * ServerContext is to save runtime state, such as otherChannels which contains
 * all alive channels except server channel; And maintain some single instance
 * in one ServerBootstrap, such as serverConfig, timer and socketChannelFactory.
 * 
 * @author zhangjunjun
 * 
 */
public class ServerContext {

	private static final Log logger = LogFactory.getLog(ServerContext.class);

	private Link link;

	private Channel serverChannel;

	/**
	 * In order to change work mode dynamically, should maintain the handle
	 * channel and server channel separately. Because all of handle channel
	 * should be closed when work mode changes, or the alive channel will use
	 * previous handler.
	 */
	private final ChannelGroup otherChannels = new DefaultChannelGroup(
			"Terminator-Server");

	/**
	 * the Timer that is used to trigger the scheduled event, please see
	 * {@link com.baidu.terminator.server.http.HttpServerPipelineFactory} and
	 * {@link com.baidu.terminator.server.http.HttpRelayPipelineFactory}
	 */
	private final Timer timer = new HashedWheelTimer();

	private ServerSocketChannelFactory serverSocketChannelFactory;

	private ClientSocketChannelFactory relaySocketChannelFactory;

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public Channel getServerChannel() {
		return serverChannel;
	}

	public void setServerChannel(Channel serverChannel) {
		this.serverChannel = serverChannel;
	}

	public ServerSocketChannelFactory getServerSocketChannelFactory() {
		return serverSocketChannelFactory;
	}

	public void setServerSocketChannelFactory(
			ServerSocketChannelFactory serverSocketChannelFactory) {
		this.serverSocketChannelFactory = serverSocketChannelFactory;
	}

	public ClientSocketChannelFactory getRelaySocketChannelFactory() {
		return relaySocketChannelFactory;
	}

	public void setRelaySocketChannelFactory(
			ClientSocketChannelFactory relaySocketChannelFactory) {
		this.relaySocketChannelFactory = relaySocketChannelFactory;
	}

	public void addChannel(Channel channel) {
		this.otherChannels.add(channel);
	}

	public Timer getTimer() {
		return timer;
	}

	public void closeServerChannel() {
		ChannelFuture future = serverChannel.close();
		future.awaitUninterruptibly();
		if (!future.isSuccess()) {
			logger.warn("close channel fail: " + future.getCause());
		}
	}

	public void closeOtherChannels() {
		ChannelGroupFuture future = otherChannels.close();
		future.awaitUninterruptibly();
		if (!future.isCompleteSuccess()) {
			final Iterator<ChannelFuture> iter = future.iterator();
			while (iter.hasNext()) {
				final ChannelFuture cf = iter.next();
				if (!cf.isSuccess()) {
					logger.warn("close channel fail: " + cf.getCause());
				}
			}
		}
	}

	public void releaseResources() {
		closeServerChannel();
		closeOtherChannels();
		timer.stop();
		relaySocketChannelFactory.releaseExternalResources();
		serverSocketChannelFactory.releaseExternalResources();
	}

}
