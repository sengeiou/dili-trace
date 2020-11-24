package com.dili.trace.dto;

import com.dili.common.exception.TraceBizException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

public class OperatorUser {
	private Long id;
	private String name;

	public OperatorUser(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	public static OperatorUser build(SessionContext sessionContext) {
		if(sessionContext==null) {
			throw new TraceBizException("请先登录");
		}
		try {
			UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
			if (userTicket == null || userTicket.getId() == null) {
				throw new TraceBizException("请先登录");
			}
		} catch (Exception e) {
			throw new TraceBizException("请先登录");
		}
		UserTicket userTicket=sessionContext.getUserTicket();
		return new OperatorUser(userTicket.getId(),userTicket.getRealName());
	}

	/**
	 * @return Long return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return String return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}