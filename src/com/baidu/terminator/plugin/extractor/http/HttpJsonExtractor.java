/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.plugin.extractor.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpRequest;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.common.log.LinkLogger;
import com.baidu.terminator.plugin.extractor.Extractor;
import com.baidu.terminator.plugin.extractor.RequestElement;
import com.baidu.terminator.plugin.extractor.UriElementGetter;
import com.baidu.terminator.plugin.signer.DefaultHttpSigner;
import com.baidu.terminator.register.annotation.CustomizedExtractor;
import com.baidu.terminator.server.common.util.ChannelUtil;
import com.baidu.terminator.server.common.util.JsonParser;

@CustomizedExtractor(protocol = "HTTP", message = "\"METHOD\"代表HTTP method，\"URI\"代表HTTP 访问地址，并将Http body中的Json解析为一个个键值对。")
public class HttpJsonExtractor implements Extractor {

	private Logger logger;

	public HttpJsonExtractor(Link link) {
		logger = LinkLogger.getLogger(DefaultHttpSigner.class, link.getId());
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
		for (int i = 0; i < uri_elements.size(); i++) {
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
			String stringContent = ChannelUtil
					.readChannelBufferAsString(content);
			JsonParser jp = new JsonParser();
			List<Map<String, Object>> resultJson = jp
					.parseJsonObjectToListMap(stringContent);
			for (int i = 0; i < resultJson.size(); i++) {
				Map<String, Object> temp = resultJson.get(i);
				Set<String> keyset = temp.keySet();
				Iterator<String> keys = keyset.iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					String value = temp.get(key).toString();
					RequestElement bodyElement = new RequestElement();
					bodyElement.setKey(key);
					bodyElement.setValue(value);
					elements.add(bodyElement);
				}
			}
		}

		logger.info("the elements of request is :" + elements);
		return elements;
	}

}
