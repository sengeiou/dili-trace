package com.dili.trace.service.impl;

import com.dili.ss.base.BaseServiceImpl;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.enums.MessageReceiverEnum;
import com.dili.trace.enums.MessageStateEnum;
import com.dili.trace.service.EventMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@EnableRetry
public class EventMessageServiceImpl extends BaseServiceImpl<EventMessage, Long> implements EventMessageService {


    @Override
    public void readMessage(EventMessage message, MessageStateEnum readFlag) {
        message.setReadFlag(readFlag.getCode());
        getDao().updateByPrimaryKeySelective(message);
    }

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(multiplier = 2, maxDelay = 2000l))
    @Async
    @Override
    public void addMessage(EventMessage eventMessage) {
        try {
            insertSelective(eventMessage);
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public Integer countReadableEventMessage(Long receiverId, Long marketId) {
        EventMessage msg = new EventMessage();
        msg.setReceiverId(receiverId);
        msg.setReadFlag(MessageStateEnum.UNREAD.getCode());
        msg.setReceiverType(MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_NORMAL.getCode());
       return this.getDao().selectCount(msg);
//        return this.getDao().selectCountByExample(msg);
    }
}
