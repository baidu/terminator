/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server.common;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.ThreadNameDeterminer;
import org.jboss.netty.util.ThreadRenamingRunnable;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.WorkMode;
import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.server.Server;
import com.baidu.terminator.server.ServerContext;

/**
 * HTTP Server response for record/replay HTTP protocol data.
 * 
 * @author zhangjunjun
 * 
 */
public class ServerImpl implements Server {

	private Logger logger;

	private Link link;

	private ServerContext context;

	public ServerImpl(Link link) {
		this.link = link;
		this.logger = LinkLogger.getLogger(ServerImpl.class, link.getId());
		initContext();
	}

	@Override
	public void start() {
		logger.info("Link Server is starting up on port: " + link.getLocalPort()
				+ ", the work mode is " + link.getWorkMode());

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		// if not set this the thread name will be New IO worker 1,2...
		ThreadRenamingRunnable
				.setThreadNameDeterminer(ThreadNameDeterminer.CURRENT);
		serverBootstrap.setFactory(context.getServerSocketChannelFactory());
		serverBootstrap.setPipelineFactory(new ServerPipelineFactory(context));
		Channel channel = serverBootstrap.bind(new InetSocketAddress(link
				.getLocalPort()));
		context.setServerChannel(channel);
	}

	@Override
	public void stop() {
		logger.info("Terminator Server related to link " + link.getId()
				+ " is shutting down");
		context.releaseResources();
	}

	@Override
	public void changeWorkMode(WorkMode mode) {
		link.setWorkMode(mode);
		context.closeOtherChannels();
		logger.info("Link have changed successfully to mode: " + mode);
	}

	@Override
	public Link getLink() {
		return this.link;
	}

	/**
	 * In order to distinct the terminator server thread name from other
	 * threads'
	 * 
	 * @return thread pool
	 */
	private static Executor newServerThreadPool() {
		return Executors.newCachedThreadPool(new ThreadFactory() {

			private int num = 0;

			@Override
			public Thread newThread(final Runnable r) {
				final Thread t = new Thread(r, "Terminator-Server-Thread-"
						+ num++);
				return t;
			}
		});
	}

	/**
	 * In order to distinct the terminator relay thread name from other threads'
	 * 
	 * @return thread pool
	 */
	private static Executor newRelayThreadPool() {
		return Executors.newCachedThreadPool(new ThreadFactory() {

			private int num = 0;

			public Thread newThread(final Runnable r) {
				final Thread t = new Thread(r, "Terminator-Relay-Thread-"
						+ num++);
				return t;
			}
		});
	}

	private void initContext() {
		context = new ServerContext();
		context.setLink(link);

		NioServerSocketChannelFactory serverSocketChannelFactory = new NioServerSocketChannelFactory(
				newServerThreadPool(), newServerThreadPool());
		context.setServerSocketChannelFactory(serverSocketChannelFactory);

		NioClientSocketChannelFactory relaySocketChannelFactory = new NioClientSocketChannelFactory(
				newRelayThreadPool(), newRelayThreadPool());
		context.setRelaySocketChannelFactory(relaySocketChannelFactory);
	}

}
