package com.atm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.atm.exception.InvalidDetailsException;
import com.atm.model.ExceptionResponse;

@ControllerAdvice
public class ErrorControllerAdvice {


	@ExceptionHandler(InvalidDetailsException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ExceptionResponse handleResourceNotFound(final InvalidDetailsException exception,
			final HttpServletRequest request) {
		ExceptionResponse error = new ExceptionResponse();
		if(exception.getMessage().isEmpty()) {
			error.setErrorMessage("Details entered are Invalid. Please check and Retry");
		} else {
			error.setErrorMessage(exception.getMessage());
		}
		error.setRequestedURI(request.getRequestURI());
		return error;
	}

}
