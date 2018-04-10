package com.nice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UsernameTakenException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UsernameTakenException(String message) {
		super(message);
	}

}
