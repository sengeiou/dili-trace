package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.enums.MessageStateEnum;
import org.springframework.scheduling.annotation.Async;

/**
 * 消息接口
 */
public interface EventMessageService extends BaseService<EventMessage, Long> {
    /**
     * 读取消息
     *
     * @param message
     * @param readFlag
     */
    public void readMessage(EventMessage message, MessageStateEnum readFlag);

    /**
     * 增加消息
     *
     * @param eventMessage
     */
    @Async
    public void addMessage(EventMessage eventMessage);

    /**
     * 查询未读消息数量
     *
     * @param userId
     * @param marketId
     * @return
     */

    public Integer countReadableEventMessage(Long receiverId, Long marketId);
}
