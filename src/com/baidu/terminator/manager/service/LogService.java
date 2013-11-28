/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.service;

import java.io.IOException;

import com.baidu.terminator.manager.bo.Log;
import com.baidu.terminator.manager.common.exception.LinkStatusException;

public interface LogService {

	public Log readLog(int linkId, long offset) throws IOException;

	public void deleteLog(int linkId) throws LinkStatusException;

}
