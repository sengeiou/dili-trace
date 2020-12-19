package com.dili.trace.service;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.rpc.api.MessageRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息服务
 */
@Service
public class MessageRpcService {
    @Autowired(required = false)
    MessageRpc messageRpc;

    /**
     * 发送消息
     * @param jsonObject
     * @return
     */
    public BaseOutput sendVerificationCodeMsg(JSONObject jsonObject) {

        return this.messageRpc.sendVerificationCodeMsg(jsonObject);
    }
}
