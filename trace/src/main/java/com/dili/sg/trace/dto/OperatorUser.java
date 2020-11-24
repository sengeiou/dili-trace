package com.dili.sg.trace.dto;

import com.dili.common.exception.TraceBizException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

public class OperatorUser {
	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OperatorUser(UserTicket userTicket) {
		this.id = userTicket.getId();
		this.name = userTicket.getRealName();
	}

	public OperatorUser(Long id, String name) {
		super();
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
	 
}
