package com.dili.sg.trace.service;


import java.util.concurrent.TimeUnit;

import com.dili.sg.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.sg.trace.rpc.MessageRpc;

/**
 * 短信接口
 * @author wangguofeng
 */
@Service
public class SMSService {
    private static final Logger logger = LoggerFactory.getLogger(SMSService.class);
    @Autowired(required = false)
    MessageRpc messageRpc;
    @Autowired
    private DefaultConfiguration defaultConfiguration;
    @Autowired
    protected RedisUtil redisUtil;
    /**
     * 注册验证码缓存前缀
     */
    public final static String REDIS_SYSTEM_VERCODE_PREIX = "USER:VERIFICATION_CODE:";

    /**
     * 重置密码验证码缓存前缀
     */
    public final static String REDIS_SYSTEM_RENEW_PASSWORD_PREIX = "USER:RENEW_PASSWORD_CODE:";


    /**
     * 验证码比较
     * @param phone
     * @param verCode
     * @return
     */
    public Boolean checkVerificationCode(String phone, String verCode) {
        String key = REDIS_SYSTEM_VERCODE_PREIX + phone;
        return this.checkSmsCode(key, verCode);
    }

    /**
     * 检查验证码
     * @param key
     * @param verCode
     * @return
     */
    private boolean checkSmsCode(String key, String verCode) {

        Object value = redisUtil.get(key);
        if (value == null) {
            throw new TraceBizException("验证码已过期");
        }
        String verificationCodeTemp = String.valueOf(value);
        if (verificationCodeTemp.equals(verCode)) {
            redisUtil.remove(key);
            return true;
        } else {
            throw new TraceBizException("验证码不正确");
        }

    }

    /**
     * 发送验证码
     * @param params
     * @param phone
     * @param verificationCode
     * @return
     */
    public BaseOutput sendVerificationCodeMsg(JSONObject params, String phone, String verificationCode) {
        if (defaultConfiguration.getCheckCodeExpire()
                - redisUtil.getRedisTemplate().getExpire(REDIS_SYSTEM_VERCODE_PREIX) < 60) {
            return BaseOutput.success();// 发送间隔60秒
        }
        BaseOutput msgOutput = messageRpc.sendVerificationCodeMsg(params);
        if (msgOutput.isSuccess()) {
            redisUtil.set(REDIS_SYSTEM_VERCODE_PREIX + phone, verificationCode,
                    defaultConfiguration.getCheckCodeExpire(), TimeUnit.SECONDS);
            logger.info("短信验证码发送成功：---------------手机号：【" + phone + "】，验证码：【" + verificationCode + "】--------------");
            return BaseOutput.success();
        } else {

            logger.error("发送失败,错误信息：" + msgOutput.getMessage());
            logger.info("短信验证码发送失败：---------------手机号：【" + phone + "】，验证码：【" + verificationCode + "】--------------");
            logger.info("继续将短信验证码{}缓存到REDIS方便测试", verificationCode);
            redisUtil.set(REDIS_SYSTEM_VERCODE_PREIX + phone, verificationCode,
                    defaultConfiguration.getCheckCodeExpire(), TimeUnit.SECONDS);
            return BaseOutput.success();
        }
    }


}