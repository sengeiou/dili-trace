package com.dili.trace.service;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SMSService {
    @Autowired
    private RedisUtil redisUtil;
    // 验证码缓存前缀
    public final static String REDIS_SYSTEM_VERCODE_PREIX = "USER:VERIFICATION_CODE:";

    // 重置密码验证码缓存前缀
    public final static String REDIS_SYSTEM_RENEW_PASSWORD_PREIX = "USER:RENEW_PASSWORD_CODE:";

    public  abstract BaseOutput sendVerificationCodeMsg(JSONObject params, String phone, String verificationCode);

    public Boolean checkVerificationCode(String phone, String verCode) {
        String verificationCodeTemp = String
                .valueOf(redisUtil.get(REDIS_SYSTEM_VERCODE_PREIX + phone));
        if (StringUtils.isBlank(verificationCodeTemp)) {
            throw new TraceBusinessException("验证码已过期");
        }
        if (verificationCodeTemp.equals(verCode)) {
            redisUtil.remove(REDIS_SYSTEM_VERCODE_PREIX + phone);
            return true;
        } else {
            throw new TraceBusinessException("验证码不正确");
        }
    }

    public abstract BaseOutput sendRenewPasswordSMSCodeMsg(JSONObject params, String phone, String verificationCode);

    public Boolean checkResetPasswordSmsCode(String phone, String verCode) {
        String verificationCodeTemp = String
                .valueOf(redisUtil.get(REDIS_SYSTEM_RENEW_PASSWORD_PREIX + phone));
        if (StringUtils.isBlank(verificationCodeTemp)) {
            throw new TraceBusinessException("验证码已过期");
        }
        if (verificationCodeTemp.equals(verCode)) {
            redisUtil.remove(REDIS_SYSTEM_RENEW_PASSWORD_PREIX + phone);
            return true;
        } else {
            throw new TraceBusinessException("验证码不正确");
        }
    }
}