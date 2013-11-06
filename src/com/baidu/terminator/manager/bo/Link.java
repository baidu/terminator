/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.bo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.baidu.terminator.manager.common.Config;
import com.baidu.terminator.plugin.extractor.Extractor;
import com.baidu.terminator.plugin.signer.Signer;
import com.baidu.terminator.storage.Storage;

public class Link {

	private Integer id;

	private String name;

	private String localIp = Config.getLocalIp();

	private Integer localPort;

	private String remoteAddress;

	private Integer remotePort;

	private WorkMode workMode;

	private String protocolType;

	private StorageType storageType;

	private String signClass;

	private String extractClass;

	private Storage storage;

	private Signer requestSigner;

	private Extractor requestExtractor;

	private ServerStatus status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
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

	@JsonIgnore
	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	@JsonIgnore
	public Signer getRequestSigner() {
		return requestSigner;
	}

	public void setRequestSigner(Signer requestSigner) {
		this.requestSigner = requestSigner;
	}

	public ServerStatus getStatus() {
		return status;
	}

	public void setStatus(ServerStatus status) {
		this.status = status;
	}

	public String getExtractClass() {
		return extractClass;
	}

	public void setExtractClass(String extractClass) {
		this.extractClass = extractClass;
	}

	@JsonIgnore
	public Extractor getRequestExtractor() {
		return requestExtractor;
	}

	public void setRequestExtractor(Extractor requestExtractor) {
		this.requestExtractor = requestExtractor;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

}
