package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.enums.MessageStateEnum;
import com.dili.trace.service.EventMessageService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@EnableRetry
public class EventMessageServiceImpl extends BaseServiceImpl<EventMessage,Long> implements EventMessageService {

    @Override
    public void readMessage(EventMessage message, MessageStateEnum readFlag) {
        message.setReadFlag(readFlag.getCode());
        getDao().updateByPrimaryKeySelective(message);
    }

    @Retryable(value = Exception.class,maxAttempts = 3, backoff = @Backoff(multiplier = 2,maxDelay = 2000l))
    @Async
    @Override
    public void addMessage(EventMessage eventMessage){
        try {
            insertSelective(eventMessage);
        }catch (Exception e){
            throw e;
        }

    }
}
