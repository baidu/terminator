/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class Config {

	public static final String DEFAULT_WIN_STORAGE_DIR = "C:\\temp";

	public static final String DEFAULT_LINUX_STORAGE_DIR = "/tmp";

	private static String localIp;

	private static String operationSystem;

	private String baseStorageDir;

	public String getBaseStorageDir() {
		return baseStorageDir;
	}

	public void setBaseStorageDir(String baseStorageDir) {
		this.baseStorageDir = baseStorageDir;
	}

	public static String getLocalIp() {
		if (localIp == null) {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				localIp = addr.getHostAddress().toString();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return localIp;
	}

	public static String getOperationSystem() {
		if (operationSystem == null) {
			Properties props = System.getProperties();
			operationSystem = props.getProperty("os.name");
		}
		return operationSystem;
	}

}
