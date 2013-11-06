/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.storage;

import java.util.List;

import com.baidu.terminator.manager.bo.StubData;
import com.baidu.terminator.plugin.signer.SignerListener;

public interface Storage extends SignerListener {

	public void putRecordData(String key, RequestResponseBundle bundles);

	public RequestResponseBundle getRecordData(String key);
	
	public void deleteRecordData(int version);

	public int putStubData(StubData stubData);

	public List<StubData> getStubData();

	public void deleteStubData(int id);

}
