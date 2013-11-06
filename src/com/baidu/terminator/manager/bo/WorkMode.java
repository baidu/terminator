/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */ 
package com.baidu.terminator.manager.bo;

/**
 * Describe work modes of Terminator
 * 
 * @author zhangjunjun
 * 
 */
public enum WorkMode {

	/**
	 * Terminator do nothing but just transfer the data
	 */
	TUNNEL,

	/**
	 * Terminator not only transfer data but also record the data
	 */
	RECORD,

	/**
	 * Terminator relay the data if the data have recorded
	 */
	REPLAY,

	/**
	 * Terminator as a stub service
	 */
	STUB,

}
