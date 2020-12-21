package com.dili.trace.service.impl;

import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.dili.common.config.DefaultConfiguration;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.service.MessageRpcService;
import com.dili.trace.service.SMSService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * 发送短信默认实现
 */
@Service
@Profile({ "dev", "test" })
public class SMSServiceDefaultImpl extends SMSService {
    private static final Logger logger = LoggerFactory.getLogger(SMSServiceDefaultImpl.class);
    @Autowired
    MessageRpcService messageRpcService;
    @Autowired
    private DefaultConfiguration defaultConfiguration;



}