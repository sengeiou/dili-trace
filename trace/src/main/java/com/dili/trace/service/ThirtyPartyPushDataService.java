package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.domain.ThirtyPartyPushData;
import com.dili.trace.enums.MessageStateEnum;
import org.springframework.scheduling.annotation.Async;

public interface ThirtyPartyPushDataService extends BaseService<ThirtyPartyPushData,Long> {

}
