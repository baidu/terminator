/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringInit implements ServletContextListener {

	private static WebApplicationContext context;

	public void contextInitialized(ServletContextEvent event) {
		context = WebApplicationContextUtils.getWebApplicationContext(event
				.getServletContext());
	}

	public void contextDestroyed(ServletContextEvent event) {
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	public static Object getBean(String name) {
		return context.getBean(name);
	}

}
