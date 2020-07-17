package com.dili.trace.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.ExecutionConstants;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.trace.rpc.MessageRpc;
import com.dili.trace.service.SMSService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({ "dev", "test" })
public class SMSServiceDefaultImpl implements SMSService {
    private static final Logger logger=LoggerFactory.getLogger(SMSServiceDefaultImpl.class);
    @Autowired
    MessageRpc messageRpc;
    @Resource
    private DefaultConfiguration defaultConfiguration;
    @Resource
    private RedisUtil redisUtil;
    @Override
    public BaseOutput sendVerificationCodeMsg( JSONObject params,String phone,String verificationCode) {

        BaseOutput msgOutput = messageRpc.sendVerificationCodeMsg(params);
        if (msgOutput.isSuccess()) {
            redisUtil.set(ExecutionConstants.REDIS_SYSTEM_VERCODE_PREIX + phone, verificationCode,
                    defaultConfiguration.getCheckCodeExpire(), TimeUnit.SECONDS);
            logger.info("短信验证码发送成功：---------------手机号：【" + phone + "】，验证码：【" + verificationCode + "】--------------");
            return BaseOutput.success();
        } else {

            logger.error("发送失败,错误信息：" + msgOutput.getMessage());
            logger.info("短信验证码发送失败：---------------手机号：【" + phone + "】，验证码：【" + verificationCode + "】--------------");
            logger.info("继续将短信验证码{}缓存到REDIS方便测试",verificationCode);
            redisUtil.set(ExecutionConstants.REDIS_SYSTEM_VERCODE_PREIX + phone, verificationCode,
            defaultConfiguration.getCheckCodeExpire(), TimeUnit.SECONDS);
            return BaseOutput.success();
        }
    }
}