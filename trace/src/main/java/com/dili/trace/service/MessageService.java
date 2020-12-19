package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.MessageConfig;
import com.dili.trace.dto.MessageInputDto;

/**
 * 消息接口
 */
public interface MessageService extends BaseService<MessageConfig, Long> {

    /**
     * 新增消息
     * setCreatorId
     * setMessageType -- look enums
     * setReceiverIdArray 接收人数组
     * @param messageInputDto
     */
    void addMessage(MessageInputDto messageInputDto,Long marketId);
}
