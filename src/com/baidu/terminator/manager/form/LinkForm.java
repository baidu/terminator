/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.baidu.terminator.manager.bo.StorageType;
import com.baidu.terminator.manager.bo.WorkMode;
import com.baidu.terminator.manager.common.validation.IP;

public class LinkForm {

	@NotBlank(message = "name should be not blank!")
	private String name;

	@Range(min = 1, message = "local port should be greater than zero!")
	private int localPort;

	@IP
	private String remoteAddress;

	@Range(min = 1, message = "remote port should be greater than zero!")
	private int remotePort;

	@NotNull(message = "work mode should be not null!")
	private WorkMode workMode;

	@NotNull(message = "protocal should be not null!")
	private String protocolType;

	@NotNull(message = "storage type should be not null!")
	private StorageType storageType;

	private String signClass;

	private String extractClass;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLocalPort() {
		return localPort;
	}

	public void setLocalPort(Integer localPort) {
		this.localPort = localPort;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public Integer getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(Integer remotePort) {
		this.remotePort = remotePort;
	}

	public WorkMode getWorkMode() {
		return workMode;
	}

	public void setWorkMode(WorkMode workMode) {
		this.workMode = workMode;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	public void setStorageType(StorageType storageType) {
		this.storageType = storageType;
	}

	public String getSignClass() {
		return signClass;
	}

	public void setSignClass(String signClass) {
		this.signClass = signClass;
	}

	public String getExtractClass() {
		return extractClass;
	}

	public void setExtractClass(String extractClass) {
		this.extractClass = extractClass;
	}

}
