/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */ 
package com.baidu.terminator.server.http;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpServerCodec;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.Timer;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.WorkMode;
import com.baidu.terminator.register.annotation.PipelineFactory;
import com.baidu.terminator.server.ServerContext;

/**
 * Factory for creating pipelines for incoming requests to our listening socket.
 * 
 * @author zhangjunjun
 * 
 */
@PipelineFactory(protocol = "HTTP")
public class HttpServerPipelineFactory implements ChannelPipelineFactory {

	private ServerContext context;

	public HttpServerPipelineFactory(ServerContext context) {
		this.context = context;
	}

	public ChannelPipeline getPipeline() throws Exception {
		Link link = context.getLink();
		ChannelPipeline pipeline = Channels.pipeline();

		Timer timer = context.getTimer();

		pipeline.addLast("codec", new HttpServerCodec(8192, 8192 * 2, 8192 * 2));
		pipeline.addLast("idle", new IdleStateHandler(timer, 0, 0, 120));

		if (WorkMode.RECORD == link.getWorkMode()) {
			pipeline.addLast("handler", new HttpRecordServerHandler(context));
		}
		if (WorkMode.REPLAY == link.getWorkMode()) {
			pipeline.addLast("handler", new HttpReplayServerHandler(context));
		}
		if(WorkMode.STUB == link.getWorkMode()) {
			pipeline.addLast("handler", new HttpStubServerHandler(context));
		}

		return pipeline;
	}

}
