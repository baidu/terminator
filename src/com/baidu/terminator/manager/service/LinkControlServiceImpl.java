/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.ServerStatus;
import com.baidu.terminator.manager.bo.WorkMode;
import com.baidu.terminator.manager.common.exception.LinkStatusException;
import com.baidu.terminator.plugin.extractor.Extractor;
import com.baidu.terminator.plugin.signer.Signer;
import com.baidu.terminator.plugin.signer.SignerInitializationException;
import com.baidu.terminator.register.ProtocolRegister;
import com.baidu.terminator.server.Server;
import com.baidu.terminator.server.common.ServerImpl;
import com.baidu.terminator.storage.Storage;
import com.baidu.terminator.storage.StorageFactory;

@Service("linkControlService")
public class LinkControlServiceImpl implements LinkControlService,
		InitializingBean, DisposableBean {

	@Resource(name = "linkService")
	private LinkService linkService;

	@Resource(name = "protocolRegister")
	private ProtocolRegister protocolRegister;

	/**
	 * Maintain all the terminator server
	 */
	private Map<Integer, Server> linkIdServerMap = new ConcurrentHashMap<Integer, Server>();

	private Map<Integer, Link> linkIdLinkMap = new ConcurrentHashMap<Integer, Link>();

	@Override
	public void startServer(int linkId) {
		Link link = linkService.getLinkById(linkId);
		if (link != null) {
			Server server = linkIdServerMap.get(linkId);
			if (server == null) {
				configLink(link);
				server = new ServerImpl(link);
				server.start();

				linkIdLinkMap.put(linkId, link);
				linkIdServerMap.put(linkId, server);
			}
			link.setStatus(ServerStatus.running);
			linkService.modifyLink(link);
		}
	}

	@Override
	public void stopServer(int linkId) {
		Link link = linkService.getLinkById(linkId);
		if (link != null) {
			Server server = linkIdServerMap.get(linkId);
			if (server != null) {
				server.stop();
				linkIdServerMap.remove(linkId);
			}
			linkIdLinkMap.remove(linkId);
			link.setStatus(ServerStatus.stopped);
			linkService.modifyLink(link);
		}
	}

	@Override
	public void changeWorkMode(int linkId, WorkMode mode)
			throws LinkStatusException {
		Server server = linkIdServerMap.get(linkId);
		if (server != null) {
			server.changeWorkMode(mode);

			Link link = linkService.getLinkById(linkId);
			link.setWorkMode(mode);
			linkService.modifyLink(link);
		} else {
			throw new LinkStatusException("Need_Link_Start_Up");
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<Link> allLink = linkService.getAllLink();
		for (Link link : allLink) {
			link.setStatus(ServerStatus.stopped);
			linkService.modifyLink(link);
		}
	}

	@Override
	public void destroy() throws Exception {
		for (Map.Entry<Integer, Server> entry : linkIdServerMap.entrySet()) {
			Server server = entry.getValue();
			server.stop();
		}
	}

	@Override
	public Link getLink(int linkId) {
		return linkIdLinkMap.get(linkId);
	}

	@SuppressWarnings("unchecked")
	private void configLink(Link link) {
		// configure request signer
		Class<? extends Signer> signerClass = (Class<Signer>) getPluginClass(
				link, WorkMode.RECORD);
		Class<? extends Extractor> extractorClass = (Class<Extractor>) getPluginClass(
				link, WorkMode.STUB);
		Signer signer = getPluginInstance(link, signerClass);
		Extractor extractor = getPluginInstance(link, extractorClass);
		link.setRequestSigner(signer);
		link.setRequestExtractor(extractor);

		// configure storage
		Storage storage = StorageFactory.getStorage(link);
		link.setStorage(storage);
	}

	@Override
	public Class<?> getPluginClass(Link link, WorkMode mode) {
		Class<?> clazz = null;

		String className = null;
		if (WorkMode.RECORD.equals(mode) || WorkMode.REPLAY.equals(mode)) {
			className = link.getSignClass();
		} else if (WorkMode.STUB.equals(mode)) {
			className = link.getExtractClass();
		}

		if (StringUtils.isBlank(className)) {
			if (WorkMode.RECORD.equals(mode) || WorkMode.REPLAY.equals(mode)) {
				clazz = protocolRegister.getDefaultSignerMap(link
						.getProtocolType());
			} else if (WorkMode.STUB.equals(mode)) {
				clazz = protocolRegister.getDefaultExtractorMap(link
						.getProtocolType());
			}
		} else {
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				if (WorkMode.RECORD.equals(mode)
						|| WorkMode.REPLAY.equals(mode)) {
					throw new SignerInitializationException(
							"can not found sign class!", e.getCause());
				} else if (WorkMode.STUB.equals(mode)) {
					throw new SignerInitializationException(
							"can not found extract class", e.getCause());
				}
			}
		}

		return clazz;
	}

	private <T> T getPluginInstance(Link link, Class<T> clazz) {
		try {
			Constructor<T> constructor = clazz.getConstructor(Link.class);
			T plugin = constructor.newInstance(link);
			return plugin;
		} catch (SecurityException e) {
			throw new SignerInitializationException("InitializationFail",
					e.getCause());
		} catch (IllegalArgumentException e) {
			throw new SignerInitializationException("InitializationFail",
					e.getCause());
		} catch (NoSuchMethodException e) {
			throw new SignerInitializationException("InitializationFail",
					e.getCause());
		} catch (InstantiationException e) {
			throw new SignerInitializationException("InitializationFail",
					e.getCause());
		} catch (IllegalAccessException e) {
			throw new SignerInitializationException("InitializationFail",
					e.getCause());
		} catch (InvocationTargetException e) {
			throw new SignerInitializationException("InitializationFail",
					e.getCause());
		}
	}

}
