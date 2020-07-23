package com.dili.trace.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.dili.common.config.DefaultConfiguration;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.rpc.MessageRpc;
import com.dili.trace.service.SMSService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({ "prod" })
public class SMSServiceProdImpl extends SMSService {
    private static final Logger logger = LoggerFactory.getLogger(SMSServiceDefaultImpl.class);

    @Autowired
    MessageRpc messageRpc;
    @Autowired
    private DefaultConfiguration defaultConfiguration;

    @Override
    public BaseOutput sendVerificationCodeMsg(JSONObject params, String phone, String verificationCode) {
        if (defaultConfiguration.getCheckCodeExpire()
                - super.redisUtil.getRedisTemplate().getExpire(REDIS_SYSTEM_VERCODE_PREIX) < 60) {
            return BaseOutput.success();// 发送间隔60秒
        }
        BaseOutput msgOutput = messageRpc.sendVerificationCodeMsg(params);
        if (msgOutput.isSuccess()) {
            super.redisUtil.set(REDIS_SYSTEM_VERCODE_PREIX + phone, verificationCode,
                    defaultConfiguration.getCheckCodeExpire(), TimeUnit.SECONDS);
            logger.info("短信验证码发送成功：---------------手机号：【" + phone + "】，验证码：【" + verificationCode + "】--------------");
            return BaseOutput.success();
        } else {
            logger.error("发送失败,错误信息：" + msgOutput.getMessage());
            logger.info("短信验证码发送失败：---------------手机号：【" + phone + "】，验证码：【" + verificationCode + "】--------------");
            return BaseOutput.failure(msgOutput.getMessage());
        }
    }

    @Override
    public BaseOutput sendRenewPasswordSMSCodeMsg(JSONObject params, String phone, String verificationCode) {
        if (defaultConfiguration.getCheckCodeExpire()
            - super.redisUtil.getRedisTemplate().getExpire(REDIS_SYSTEM_VERCODE_PREIX) < 60) {
            return BaseOutput.success();// 发送间隔60秒
        }
        BaseOutput msgOutput = messageRpc.sendVerificationCodeMsg(params);
        if (msgOutput.isSuccess()) {
            super.redisUtil.set(REDIS_SYSTEM_RENEW_PASSWORD_PREIX + phone, verificationCode,
                    defaultConfiguration.getCheckCodeExpire(), TimeUnit.SECONDS);
            logger.info("短信验证码发送成功：---------------手机号：【" + phone + "】，验证码：【" + verificationCode + "】--------------");
            return BaseOutput.success();
        } else {
            logger.error("发送失败,错误信息：" + msgOutput.getMessage());
            logger.info("短信验证码发送失败：---------------手机号：【" + phone + "】，验证码：【" + verificationCode + "】--------------");
            return BaseOutput.failure(msgOutput.getMessage());
        }
    }

}