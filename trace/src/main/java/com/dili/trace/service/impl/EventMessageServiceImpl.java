package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.enums.MessageStateEnum;
import com.dili.trace.service.EventMessageService;
import org.springframework.stereotype.Service;

@Service
public class EventMessageServiceImpl extends BaseServiceImpl<EventMessage,Long> implements EventMessageService {

    @Override
    public void readMessage(EventMessage message, MessageStateEnum readFlag) {
        message.setReadFlag(readFlag.getCode());
        getDao().updateByPrimaryKeySelective(message);
    }
}
