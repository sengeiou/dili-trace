package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.enums.MessageStateEnum;
import org.springframework.scheduling.annotation.Async;

public interface EventMessageService extends BaseService<EventMessage,Long> {
    public void readMessage(EventMessage message, MessageStateEnum readFlag);
    @Async
    public void addMessage(EventMessage eventMessage);
}
