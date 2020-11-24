package com.dili.sg.common.entity;

import java.io.Serializable;

import com.dili.common.entity.SessionData;
import com.dili.sg.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.sg.trace.dto.OperatorUser;
import com.dili.common.exception.TraceBizException;

public class TraceSessionContext implements Serializable {
	private static final long serialVersionUID = -12145451L;
	private SessionData sessionData;

	public void setInvalidate(boolean invalidate) {
		if (this.sessionData != null) {
			this.getSessionData().setInvalidate(invalidate);
		}
	}

	public String getUserName() {

		return this.sessionData == null ? null : this.sessionData.getUserName();
	}

	public Long getAccountId() {

		return this.sessionData == null ? null : this.getSessionData().getUserId();
	}

	public OperatorUser getLoginUserOrException(LoginIdentityTypeEnum identityType) {
		if (this.getSessionData() != null && this.getSessionData().getIdentityType() != null
				&& this.getSessionData().getUserId() != null) {
			return new OperatorUser(this.getSessionData().getUserId(), this.getSessionData().getUserName());
		}
		throw new TraceBizException("你还未登录");

	}

	/**
	 * @return SessionData return the sessionData
	 */
	public SessionData getSessionData() {
		return sessionData;
	}

	/**
	 * @param sessionData the sessionData to set
	 */
	public void setSessionData(SessionData sessionData) {
		this.sessionData = sessionData;
	}
}
