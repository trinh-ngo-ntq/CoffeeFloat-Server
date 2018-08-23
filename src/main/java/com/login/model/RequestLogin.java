package com.login.model;

import com.fasterxml.jackson.annotation.JsonGetter;

public class RequestLogin {

	private String type;
	private String token;

	private String phoneNumber; // TrinhNX: Add optional phone number

	public RequestLogin() {
	}

	public RequestLogin(String type, String token) {
		this(type, token, null);
	}
	
	public RequestLogin(String type, String token, String phoneNumber) {
		this.type = type;
		this.token = token;
		this.phoneNumber = phoneNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@JsonGetter("phone_number")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
