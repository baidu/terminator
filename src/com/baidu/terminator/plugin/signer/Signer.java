/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.plugin.signer;

import com.baidu.terminator.storage.RequestResponseBundle;

public interface Signer {

	/**
	 * how to sign request
	 * 
	 * @param request
	 * @return sign result
	 */
	public String sign(Object request);

	/**
	 * log response (why give bundle to function logResponse not only response,
	 * because in some cases how to decode response depends on the request.)
	 * 
	 * @param bundle
	 */
	public void logResponse(RequestResponseBundle bundle);

}
