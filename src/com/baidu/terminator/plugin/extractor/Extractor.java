/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.plugin.extractor;

import java.util.List;

public interface Extractor {

	/**
	 * how to extract request to request elements
	 * 
	 * @param request
	 * @return extract result
	 */
	public List<RequestElement> extract(Object request);

}
