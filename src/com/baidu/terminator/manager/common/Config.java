/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.common;

import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
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
				for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();) { 
				    NetworkInterface item = e.nextElement(); 
				    if (item.isLoopback() != true && item.isUp() == true && item.getMTU() > 0) {//不是循环网卡、网卡启用、传输单元大于0
				    	for (InterfaceAddress address : item.getInterfaceAddresses()) { 
					        if (address.getAddress() instanceof Inet4Address) { //IP4地址
					            Inet4Address inet4Address = (Inet4Address) address.getAddress();
					            if (!inet4Address.isLinkLocalAddress()) {//不为连接地址
					            	return inet4Address.getHostAddress();
								}
					        } 
					    } 
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} 
			
//			try {
//				InetAddress addr = InetAddress.getLocalHost();
//				localIp = addr.getHostAddress().toString();
//			} catch (UnknownHostException e) {
//				e.printStackTrace();
//			}
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
