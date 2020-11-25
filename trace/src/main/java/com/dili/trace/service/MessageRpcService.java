package com.dili.trace.service;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.rpc.MessageRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageRpcService {
    @Autowired(required = false)
    MessageRpc messageRpc;

    public BaseOutput sendVerificationCodeMsg(JSONObject jsonObject) {

        return this.messageRpc.sendVerificationCodeMsg(jsonObject);
    }
}
