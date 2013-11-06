/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.server;

import com.baidu.terminator.manager.bo.Link;
import com.baidu.terminator.manager.bo.WorkMode;

public interface Server {

	/**
	 * Start the server.
	 */
	public void start();

	/**
	 * Stop the server.
	 */
	public void stop();

	/**
	 * Change work mode.
	 * 
	 * @param mode
	 *            work mode.
	 */
	public void changeWorkMode(WorkMode mode);

	public Link getLink();

}
