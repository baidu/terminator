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
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baidu.terminator.manager.service.LinkControlService;

public class SpringInit implements ServletContextListener {

	private static ApplicationContext context;

	public void contextInitialized(ServletContextEvent event) {
		context = WebApplicationContextUtils.getWebApplicationContext(event
				.getServletContext());

		LinkControlService linkControlService = (LinkControlService) context
				.getBean("linkControlService");
		linkControlService.recoveryLink();
	}

	public void contextDestroyed(ServletContextEvent event) {
	}

	public static void setApplicationContext(ApplicationContext testContext) {
		context = testContext;
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	public static Object getBean(String name) {
		return context.getBean(name);
	}

}
