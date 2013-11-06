/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.action;

import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

public class BaseAction {

	public HttpHeaders setLocationHeaders(String location) {
		HttpHeaders headers = new HttpHeaders();

		if (StringUtils.isNotBlank(location)) {
			URI uri = UriComponentsBuilder.fromPath(location).build().toUri();
			headers.setLocation(uri);
		}
		return headers;
	}

}
