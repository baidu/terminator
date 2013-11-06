/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.baidu.terminator.manager.bo.PluginInfo;
import com.baidu.terminator.plugin.extractor.Extractor;
import com.baidu.terminator.plugin.signer.Signer;
import com.baidu.terminator.register.annotation.CustomizedExtractor;
import com.baidu.terminator.register.annotation.CustomizedSigner;
import com.baidu.terminator.register.annotation.DefaultExtractor;
import com.baidu.terminator.register.annotation.DefaultSigner;
import com.baidu.terminator.register.annotation.PipelineFactory;
import com.baidu.terminator.register.scanner.AnnotationScanner;

@Service("protocolRegister")
public class ProtocolRegisterImpl implements ProtocolRegister, InitializingBean {

	private static final Log LOGGER = LogFactory
			.getLog(ProtocolRegisterImpl.class);

	public static final String DEFAULT_PIPELINEFACTORY_PACKAGE = "com.baidu.terminator.server";

	public static final String DEFAULT_PLUGIN_PACKAGE = "com.baidu.terminator.plugin";

	private Set<String> supportedProtocols = new LinkedHashSet<String>();

	private Map<String, Class<? extends ChannelPipelineFactory>> protocolChannelPipelineFactoryMap = new HashMap<String, Class<? extends ChannelPipelineFactory>>();

	private Map<String, Class<? extends Signer>> protocolRequestSignerMap = new HashMap<String, Class<? extends Signer>>();

	private List<PluginInfo> defaultSigners = new ArrayList<PluginInfo>();
	private List<PluginInfo> customizedSigners = new ArrayList<PluginInfo>();

	private Map<String, Class<? extends Extractor>> protocolRequestExtractorMap = new HashMap<String, Class<? extends Extractor>>();

	private List<PluginInfo> defaultExtractors = new ArrayList<PluginInfo>();
	private List<PluginInfo> customizedExtractors = new ArrayList<PluginInfo>();

	@Override
	public Set<String> getSupportedProtocols() {
		return supportedProtocols;
	}

	@Override
	public Class<? extends ChannelPipelineFactory> getChannelPipelineFactory(
			String protocal) {
		return protocolChannelPipelineFactoryMap.get(protocal);
	}

	@Override
	public Class<? extends Signer> getDefaultSignerMap(String protocal) {
		return protocolRequestSignerMap.get(protocal);
	}

	@Override
	public List<PluginInfo> getDefaultSigners() {
		return defaultSigners;
	}

	@Override
	public List<PluginInfo> getCustomizedSigners() {
		return customizedSigners;
	}

	@Override
	public Class<? extends Extractor> getDefaultExtractorMap(String protocal) {
		return protocolRequestExtractorMap.get(protocal);
	}

	@Override
	public List<PluginInfo> getDefaultExtractors() {
		return defaultExtractors;
	}

	@Override
	public List<PluginInfo> getCustomizedExtractors() {
		return customizedExtractors;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		scanPipelineFactory();

		scanDefaultSigner();
		scanCustomizedSigner();
		calculateSupportedProtocols();

		scanDefaultExtractor();
		scanCustomizedExtractor();
	}

	private void scanPipelineFactory() {
		AnnotationScanner<ChannelPipelineFactory> channelPipelineFactoryScanner = new AnnotationScanner<ChannelPipelineFactory>(
				DEFAULT_PIPELINEFACTORY_PACKAGE, PipelineFactory.class);
		List<Class<? extends ChannelPipelineFactory>> cpfClasses = channelPipelineFactoryScanner
				.scanAnnotatedClass();
		for (Class<? extends ChannelPipelineFactory> clazz : cpfClasses) {
			PipelineFactory pf = clazz.getAnnotation(PipelineFactory.class);
			String protocol = pf.protocol();
			protocolChannelPipelineFactoryMap.put(protocol, clazz);
		}
	}

	private void scanDefaultSigner() {
		AnnotationScanner<Signer> requestSignerScanner = new AnnotationScanner<Signer>(
				DEFAULT_PLUGIN_PACKAGE, DefaultSigner.class);
		List<Class<? extends Signer>> rsClasses = requestSignerScanner
				.scanAnnotatedClass();
		for (Class<? extends Signer> clazz : rsClasses) {
			DefaultSigner drs = clazz
					.getAnnotation(DefaultSigner.class);

			String protocol = drs.protocol();
			protocolRequestSignerMap.put(protocol, clazz);

			String message = drs.message();

			PluginInfo pi = new PluginInfo();
			pi.setName(clazz.getName());
			pi.setProtocol(protocol);
			pi.setMessage(message);
			defaultSigners.add(pi);
		}
	}

	private void scanCustomizedSigner() {
		AnnotationScanner<Signer> requestSignerScanner = new AnnotationScanner<Signer>(
				DEFAULT_PLUGIN_PACKAGE, CustomizedSigner.class);
		List<Class<? extends Signer>> rsClasses = requestSignerScanner
				.scanAnnotatedClass();
		for (Class<? extends Signer> clazz : rsClasses) {
			CustomizedSigner crs = clazz
					.getAnnotation(CustomizedSigner.class);
			String protocol = crs.protocol();
			String message = crs.message();

			PluginInfo cs = new PluginInfo();
			cs.setName(clazz.getName());
			cs.setProtocol(protocol);
			cs.setMessage(message);
			customizedSigners.add(cs);
		}
	}

	private void calculateSupportedProtocols() {
		Set<String> protocolsSupportedByChannelPipelineFactory = protocolChannelPipelineFactoryMap
				.keySet();
		Set<String> protocolsSupportedByRequestSigner = protocolRequestSignerMap
				.keySet();

		for (String protocol : protocolsSupportedByChannelPipelineFactory) {
			if (protocolsSupportedByRequestSigner.contains(protocol)) {
				supportedProtocols.add(protocol);
			} else {
				LOGGER.warn("protocol: " + protocol
						+ " skip， beacuse it has not default request signer!");
			}
		}
	}

	private void scanDefaultExtractor() {
		AnnotationScanner<Extractor> requestExtractorScanner = new AnnotationScanner<Extractor>(
				DEFAULT_PLUGIN_PACKAGE, DefaultExtractor.class);
		List<Class<? extends Extractor>> reClasses = requestExtractorScanner
				.scanAnnotatedClass();
		for (Class<? extends Extractor> clazz : reClasses) {
			DefaultExtractor dre = clazz
					.getAnnotation(DefaultExtractor.class);

			String protocol = dre.protocol();
			protocolRequestExtractorMap.put(protocol, clazz);

			String message = dre.message();

			PluginInfo pi = new PluginInfo();
			pi.setName(clazz.getName());
			pi.setProtocol(protocol);
			pi.setMessage(message);
			defaultExtractors.add(pi);
		}
	}

	private void scanCustomizedExtractor() {
		AnnotationScanner<Extractor> requestExtractorScanner = new AnnotationScanner<Extractor>(
				DEFAULT_PLUGIN_PACKAGE, CustomizedExtractor.class);
		List<Class<? extends Extractor>> rsClasses = requestExtractorScanner
				.scanAnnotatedClass();
		for (Class<? extends Extractor> clazz : rsClasses) {
			CustomizedExtractor crs = clazz
					.getAnnotation(CustomizedExtractor.class);
			String protocol = crs.protocol();
			String message = crs.message();

			PluginInfo cs = new PluginInfo();
			cs.setName(clazz.getName());
			cs.setProtocol(protocol);
			cs.setMessage(message);
			customizedExtractors.add(cs);
		}
	}

}
