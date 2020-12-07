package com.dili.trace.api.output;

import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.glossary.RegisterBillStateEnum;

public class CheckInApiDetailOutput {
	/**
	 * 报备单ID
	 */
	private Long id;//registerbill id

	/**
	 * 状态
	 */
	private Integer state;

	/**
	 * 检测状态
	 */
	private Integer detectState;

	/**
	 * 上游企业
	 */
	private UpStream upStream;

	/**
	 * 经营户
	 */
	private User user;

	public Integer getDetectState() {
		return detectState;
	}

	public void setDetectState(Integer detectState) {
		this.detectState = detectState;
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

	public String getStateName() {
		try {
			if (getState() == null) {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
		return RegisterBillStateEnum.getRegisterBillStateEnum(getState()).getName();
	}

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

}
