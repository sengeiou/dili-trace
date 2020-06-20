package com.dili.trace.api.input;

public class LoginInputDto {
	private String username;
	private String password;
	private Integer loginIdentityType;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getLoginIdentityType() {
		return loginIdentityType;
	}

	public void setLoginIdentityType(Integer loginIdentityType) {
		this.loginIdentityType = loginIdentityType;
	}



}
