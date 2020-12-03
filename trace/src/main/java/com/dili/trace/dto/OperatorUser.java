package com.dili.trace.dto;

import com.dili.common.exception.TraceBizException;
import com.dili.trace.dto.idname.AbstraceIdName;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

public class OperatorUser extends AbstraceIdName {

	public OperatorUser(Long id, String name) {
		this.id = id;
		this.name = name;
	}


}