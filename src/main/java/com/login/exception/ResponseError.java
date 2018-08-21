package com.login.exception;

import java.util.Date;

public class ResponseError {

	private String customCode;
	private Date timestamp;
	private String message;
	private String details;

	public ResponseError(String customCode, Date timestamp, String message, String details) {
		super();
		this.customCode = customCode;
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public String getCustomCode() {
		return customCode;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}
