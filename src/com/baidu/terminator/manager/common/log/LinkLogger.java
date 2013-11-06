/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.common.log;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.baidu.terminator.manager.common.Config;
import com.baidu.terminator.manager.common.SpringInit;
import com.baidu.terminator.storage.file.util.FileUtils;

/**
 * 
 * @author xingpei
 * @author zhangjunjuns
 * 
 */
public class LinkLogger {

	private static Map<Integer, LogManager> linkIdLogManagerMap = new ConcurrentHashMap<Integer, LogManager>();

	private static final Layout LAYOUT = new PatternLayout(
			"%-d{yyyy-MM-dd HH:mm:ss,SSS} [%c]-[%p] %m%n");

	private static final Log LOGGER = LogFactory.getLog(LinkLogger.class);

	private static final String DEFAULT_APPENDER_NAME = "linkLog";

	private static final String DEFAULT_LOG_FILE_NAME = "log";

	public static Logger getLogger(Class<?> clazz, int linkId) {
		LogManager logManager = linkIdLogManagerMap.get(linkId);
		if (logManager == null) {
			logManager = new LogManager();
			linkIdLogManagerMap.put(linkId, logManager);
		}
		Logger logger = logManager.getLogger(clazz.getName());

		try {
			Appender appender = logger.getAppender(DEFAULT_APPENDER_NAME);
			if (appender == null) {
				String logFileLocation = getLogFileLocation(linkId);
				FileUtils.createFile(logFileLocation);
				appender = new FileAppender(LAYOUT, logFileLocation);
				appender.setName(DEFAULT_APPENDER_NAME);
				logger.addAppender(appender);
			}
		} catch (IOException e) {
			LOGGER.error("create logger for link :" + linkId + " fail!", e);
		}
		return logger;
	}

	public static String getLogFileLocation(int linkId) {
		Config config = (Config) SpringInit.getBean("config");
		String baseLogDir = config.getBaseStorageDir();
		if (StringUtils.isBlank(baseLogDir)) {
			if (Config.getOperationSystem().startsWith("Windows")) {
				baseLogDir = Config.DEFAULT_WIN_STORAGE_DIR;
			} else {
				baseLogDir = Config.DEFAULT_LINUX_STORAGE_DIR;
			}
		}
		String baseDir = baseLogDir + File.separator + linkId;
		String logPath = baseDir + File.separator + DEFAULT_LOG_FILE_NAME;
		return logPath;
	}

	public static void shutdownLogManager(int linkId) {
		LogManager logManager = linkIdLogManagerMap.get(linkId);
		if (logManager != null) {
			logManager.shutdown();
			linkIdLogManagerMap.remove(linkId);
		}
	}

}
