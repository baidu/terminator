/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server.common.stub;

import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;

import com.baidu.terminator.manager.bo.StubCondition;
import com.baidu.terminator.manager.bo.StubData;
import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.plugin.extractor.RequestElement;
import com.baidu.terminator.plugin.matcher.AndRequestMatcher;
import com.baidu.terminator.plugin.matcher.RequestMatcher;
import com.baidu.terminator.server.NotHitHandler;
import com.baidu.terminator.server.RelayListener;
import com.baidu.terminator.server.ServerContext;
import com.baidu.terminator.server.common.record.BaseRecordServerHandler;

public abstract class BaseStubServerHandler<Request> extends
		BaseRecordServerHandler<Request> implements NotHitHandler {

	private Logger logger;

	private RequestMatcher matcher = new AndRequestMatcher();

	private boolean farward = true;

	public BaseStubServerHandler(ServerContext context) {
		super(context);
		logger = LinkLogger.getLogger(BaseStubServerHandler.class, context
				.getLink().getId());
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		if (farward) {
			super.channelOpen(ctx, e);
		} else {
			inboundChannel = e.getChannel();
			context.addChannel(inboundChannel);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		request = (Request) e.getMessage();
		List<RequestElement> elements = extractor.extract(request);

		Object response = null;
		List<StubData> stubData = storage.getStubData();
		for (StubData data : stubData) {
			List<StubCondition> conditions = data.getConditions();
			boolean isMatch = matcher.isMatch(conditions, elements);
			if (isMatch) {
				logger.info("the request match these conditions: \n"
						+ conditions);
				response = responseWhenHit(data.getResponse());
				break;
			}
		}

		if (response == null) {
			if (farward) {
				logger.info("the request does not hit the stub data, and it will be forward to real server!");
				writeObject(e, request);
			} else {
				logger.info("the request does not hit the stub data, it will retrun the default response!");
				response = responseWhenNotHit();
				inboundChannel.write(response);
			}
		} else {
			logger.info("the request hit the stub data, and response is: \n"
					+ response);
			inboundChannel.write(response);
		}

	}

	protected abstract Object responseWhenHit(String content);

	@Override
	public void onRelayResponse(Object response) {
		// do nothing
	}

	public abstract ChannelPipelineFactory getRelayChannelPipelineFactory(
			RelayListener relayListener);

}
