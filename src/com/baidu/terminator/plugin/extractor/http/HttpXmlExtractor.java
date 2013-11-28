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
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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

@CustomizedExtractor(protocol = "HTTP", message = "内容采用xml传输")
public class HttpXmlExtractor implements Extractor {

	private Logger logger;
	private List<Object> contentmap=new ArrayList<Object>();

	public HttpXmlExtractor(Link link) {
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
		for(int i =0;i<uri_elements.size();i++){
			elements.add(uri_elements.get(i));
		}
//		RequestElement uriElement = new RequestElement();
//		uriElement.setKey("URI");
//		uriElement.setValue(uri);
//		elements.add(uriElement);

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
			Document document = null;
			try {
				document = DocumentHelper.parseText(stringContent);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Element root = document.getRootElement();
			contentmap.clear();
			getElementList(root);
			for(int i =0;i<contentmap.size();i++){
				elements.add((RequestElement)contentmap.get(i));
			}
		}
		logger.info("the elements of request is :" + elements);
		return elements;
	}

	/**
	 * 递归遍历方法
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	public void getElementList(Element element) {
		List<Element> elements = element.elements();
		if (elements.size() == 0) {
			// 没有子元素
			String key = element.getName();
			String value = element.getTextTrim();
			RequestElement bodyElement = new RequestElement();
			bodyElement.setKey(key);
			bodyElement.setValue(value);
			contentmap.add(bodyElement);
		} else {
			// 有子元素
			for (Iterator<Element> it = elements.iterator(); it.hasNext();) {
				Element elem = (Element) it.next();
				// 递归遍历
				getElementList(elem);
			}
		}
	}
}
