package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.MessageConfig;
import com.dili.trace.dto.MessageInputDto;

public interface MessageService extends BaseService<MessageConfig, Long> {

    /**
     * 新增消息
     * setCreatorId
     * setMessageType -- look enums
     * setReceiverIdArray 接收人数组
     * @param messageInputDto
     */
    void addMessage(MessageInputDto messageInputDto);
}
