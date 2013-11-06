/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.register.scanner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class AnnotationScanner<T> {

	private static final Log LOGGER = LogFactory
			.getLog(AnnotationScanner.class);

	protected String scannerPackage;

	protected Class<? extends Annotation> annotationType;

	public AnnotationScanner(String scannerPackage,
			Class<? extends Annotation> annotationType) {
		this.scannerPackage = scannerPackage;
		this.annotationType = annotationType;
	}

	@SuppressWarnings("unchecked")
	public List<Class<? extends T>> scanAnnotatedClass() {
		List<Class<? extends T>> classList = new ArrayList<Class<? extends T>>();

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
				false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType));
		Set<BeanDefinition> components = scanner
				.findCandidateComponents(scannerPackage);

		for (BeanDefinition bd : components) {
			String className = bd.getBeanClassName();
			try {
				Class<? extends T> clazz = (Class<? extends T>) Class
						.forName(className);
				classList.add(clazz);
			} catch (ClassNotFoundException e) {
				LOGGER.warn("can not find class" + className, e.getCause());
			}
		}
		return classList;
	}

}
