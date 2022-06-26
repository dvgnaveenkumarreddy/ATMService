package com.atm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidDetailsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private Errors errors;

	public InvalidDetailsException() {
		super();
	}

	public InvalidDetailsException(String message) {
		super(message);
	}

	public InvalidDetailsException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDetailsException(String message, Errors errors) {
		super("Bad Request");
		this.errors = errors;
	}

	public Errors getErrors() {
		return errors;
	}

}
