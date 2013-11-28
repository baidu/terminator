/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.plugin.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.register.annotation.DefaultExtractor;
import com.baidu.terminator.server.common.util.ChannelUtil;

@DefaultExtractor(protocol = "HTTP", message = "\"METHOD\"代表HTTP method，\"URI\"代表HTTP 访问地址，\"BODY\"代表HTTP 内容。")
public class DefaultHttpExtractor implements Extractor {

	private Logger logger;

	public DefaultHttpExtractor(Link link) {
		logger = LinkLogger.getLogger(DefaultHttpExtractor.class,
				link.getId());
	}

	@Override
	public List<RequestElement> extract(Object request) {
		List<RequestElement> elements = new ArrayList<RequestElement>();

		HttpRequest httpRequest = (HttpRequest) request;

		String method = httpRequest.getMethod().getName();
		RequestElement methondElement = new RequestElement();
		methondElement.setKey("METHOD");
		methondElement.setValue(method);
		elements.add(methondElement);

		String uri = httpRequest.getUri();
		List<RequestElement> uri_elements = UriElementGetter.getUri(uri);
		for(int i =0;i<uri_elements.size();i++){
			elements.add(uri_elements.get(i));
		}

		List<Entry<String, String>> headers = httpRequest.getHeaders();
		for (Entry<String, String> entry : headers) {
			RequestElement headerElement = new RequestElement();
			headerElement.setKey(entry.getKey());
			headerElement.setValue(entry.getValue());
			elements.add(headerElement);
		}

		ChannelBuffer content = httpRequest.getContent();
		if (content.readable()) {
			String plainContent = ChannelUtil
					.readChannelBufferAsString(content);

			RequestElement bodyElement = new RequestElement();
			bodyElement.setKey("BODY");
			bodyElement.setValue(plainContent);
			elements.add(bodyElement);
		}

		logger.info("the elements of request are :" + elements);
		return elements;
	}

}
