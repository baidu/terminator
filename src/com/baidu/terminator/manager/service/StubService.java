/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.service;

import java.util.List;

import com.baidu.terminator.manager.bo.PluginInfo;
import com.baidu.terminator.manager.bo.StubData;

public interface StubService {

	public List<StubData> getStubData(int linkId);

	public int addStubData(int linkId, StubData data);

	public void deleteStubData(int linkId, int id);

	public PluginInfo getExtractor(int linkId);

}
