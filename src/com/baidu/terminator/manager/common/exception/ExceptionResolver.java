/*  
 * 	Copyright(C) 2010-2013 Baidu Group
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2 as
 *  published by the Free Software Foundation.
 *  
 */
package com.baidu.terminator.manager.common.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

public class ExceptionResolver extends AbstractHandlerExceptionResolver {

	private final static Log LOGGER = LogFactory
			.getLog(ExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		Object errors = null;

		if (ex instanceof MethodArgumentNotValidException) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			errors = getDecorateErrors(ex);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			errors = ex.getMessage();
			LOGGER.error(ex);
		}

		ObjectMapper mapper = new ObjectMapper();
		String errorJson = "Error occur when covert object to json!";
		try {
			errorJson = mapper.writeValueAsString(errors);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			outputStream.write(errorJson.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public Object getDecorateErrors(Exception ex) {
		HashMap<String, String> errors = new HashMap<String, String>();

		BindingResult bindingResult = ((MethodArgumentNotValidException) ex)
				.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		for (FieldError error : fieldErrors) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return errors;
	}

}
