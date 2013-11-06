/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.bo;

import java.util.List;

public class Log {

	private String logLocation;

	private long offset;

	private List<String> content;

	public String getLogLocation() {
		return logLocation;
	}

	public void setLogLocation(String logLocation) {
		this.logLocation = logLocation;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

}
