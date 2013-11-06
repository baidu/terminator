/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */ 
package com.baidu.terminator.plugin.signer;

public class SignerInitializationException extends RuntimeException {

	private static final long serialVersionUID = -8767214104379561442L;

	public SignerInitializationException() {
		super();
	}

	public SignerInitializationException(String message) {
		super(message);
	}

	public SignerInitializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SignerInitializationException(Throwable cause) {
		super(cause);
	}

}
