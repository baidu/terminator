/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */ 
package com.baidu.terminator.storage;

import java.io.Serializable;

public class RequestResponseBundle implements Serializable {

	private static final long serialVersionUID = 6941767927538834801L;

	private Object request;

	private Object response;

	public RequestResponseBundle() {
	}

	public RequestResponseBundle(Object request, Object response) {
		this.request = request;
		this.response = response;
	}

	public Object getRequest() {
		return request;
	}

	public void setRequest(Object request) {
		this.request = request;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

}
