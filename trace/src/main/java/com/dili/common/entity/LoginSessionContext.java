package com.dili.common.entity;

import java.io.Serializable;

import com.dili.common.exception.TraceBusinessException;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.dto.OperatorUser;

public class LoginSessionContext implements Serializable {

	private static final long serialVersionUID = -12145451L;
	private boolean invalidate;
	private boolean changed;
	private SessionData sessionData;
	
	public Integer getLoginType() {
		return this.sessionData==null?null:this.sessionData.getIdentityType();
	}



	public String getSessionId() {
		return this.sessionData==null?null:this.sessionData.getSessionId();
	}


	public void setInvalidate(boolean invalidate) {
		// this.map.clear();
		// this.changed = true;
		this.invalidate = invalidate;
	}

	public boolean getInvalidate() {
		return invalidate;
	}

	// public void setChanged(boolean changed) {
	// 	this.changed = changed;
	// }

	// public boolean getChanged() {
	// 	return changed;
	// }

	// public void setMillis(long millis) {
	// 	if (millis < 600000) {
	// 		this.changed = true;
	// 	}
	// 	this.millis = millis;
	// }

	// public long getMillis() {
	// 	return millis;
	// }

	// public void clear() {
	// 	this.map.clear();
	// 	this.changed = true;
	// }



	public String getUserName() {
		 
		return this.sessionData==null?null:this.sessionData.getUserName();
	}


	public Long getAccountId() {
		// // Object userId = this.map.get(SessionConstants.SESSION_ACCOUNT_ID);
		// if (userId == null) {
		// 	return null;
		// }
		return this.sessionData==null?null:this.getSessionData().getUserId();
	}



	public OperatorUser getLoginUserOrException(LoginIdentityTypeEnum identityType) {
		if (this.getLoginType()==null||this.getAccountId() != null) {
			return new OperatorUser(this.getAccountId(), this.getUserName());
		}
		throw new TraceBusinessException("你还未登录");

	}

//	public Optional<OperatorUser> getCurrentLoginUser(LoginIdentityTypeEnum identityType) {
//		if (this.getAccountId() != null || this.getUserName() != null) {
//			return Optional.of(new OperatorUser(this.getAccountId(), this.getUserName()));
//		}
//		return Optional.empty();
//
//	}

    /**
     * @return boolean return the invalidate
     */
    public boolean isInvalidate() {
        return invalidate;
    }

    /**
     * @return boolean return the changed
     */
    public boolean isChanged() {
        return changed;
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
