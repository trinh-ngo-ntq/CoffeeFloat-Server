package com.login.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7996954097982347895L;

	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(final String message) {
		super(message);
	}

	public ResourceNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(final Throwable cause) {
		super(cause);
	}

}
