package com.dili.trace.service;

import com.alibaba.fastjson.JSONObject;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class SMSService {
    @Autowired
    protected RedisUtil redisUtil;
    /**注册验证码缓存前缀 */ 
    public final static String REDIS_SYSTEM_VERCODE_PREIX = "USER:VERIFICATION_CODE:";

    /**重置密码验证码缓存前缀 */ 
    public final static String REDIS_SYSTEM_RENEW_PASSWORD_PREIX = "USER:RENEW_PASSWORD_CODE:";

    public abstract BaseOutput sendVerificationCodeMsg(JSONObject params, String phone, String verificationCode);

    public Boolean checkVerificationCode(String phone, String verCode) {
        String key = REDIS_SYSTEM_VERCODE_PREIX + phone;
        return this.checkSmsCode(key, verCode);
    }

    public abstract BaseOutput sendRenewPasswordSMSCodeMsg(JSONObject params, String phone, String verificationCode);

    public Boolean checkResetPasswordSmsCode(String phone, String verCode) {
        String key = REDIS_SYSTEM_RENEW_PASSWORD_PREIX + phone;
        return this.checkSmsCode(key, verCode);

    }

    private boolean checkSmsCode(String key, String verCode) {

        Object value = redisUtil.get(key);
        if (value == null) {
            throw new TraceBusinessException("验证码已过期");
        }
        String verificationCodeTemp = String.valueOf(value);
        if (verificationCodeTemp.equals(verCode)) {
            redisUtil.remove(key);
            return true;
        } else {
            throw new TraceBusinessException("验证码不正确");
        }

    }
}