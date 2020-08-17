package com.dili.trace.service;

import com.dili.ss.base.BaseService;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.domain.MessageConfig;
import com.dili.trace.dto.MessageInputDto;

import java.util.Map;

public interface MessageService extends BaseService<MessageConfig,Long> {

    /**
     * 新增消息
     * @param messageInputDto
     */
    void addMessage(MessageInputDto messageInputDto);
}
