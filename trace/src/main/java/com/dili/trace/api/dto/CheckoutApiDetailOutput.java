package com.dili.trace.api.dto;

import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;

public class CheckoutApiDetailOutput {
	private Long id;//separatebillid
	private Integer state;

	private UpStream upStream;
	private User user;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public UpStream getUpStream() {
		return upStream;
	}
	public void setUpStream(UpStream upStream) {
		this.upStream = upStream;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

   

}