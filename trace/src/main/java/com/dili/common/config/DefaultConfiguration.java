package com.dili.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 默认配置信息
 */
@Component
@ConfigurationProperties(prefix = "default")
public class DefaultConfiguration {

    //session过期时间 单位秒
    private long sessionExpire;

    //默认手机验证码失效时间 单位秒
    private long checkCodeExpire;
    //手机验证码默认长度
    private int checkCodeLength;

    //货站默认密码
    private String password;
    private String enTag;

    /**
     *getEnTag
     */
    public String getEnTag() {
        return enTag;
    }

    /**
     *
     * @param enTag
     */
    public void setEnTag(String enTag) {
        this.enTag = enTag;
    }

    /**
     *getSessionExpire
     */

    public long getSessionExpire() {
        return sessionExpire;
    }

    /**
     *setSessionExpire
     */
    public void setSessionExpire(long sessionExpire) {
        this.sessionExpire = sessionExpire;
    }



    /**
     *getCheckCodeExpire
     */
    public long getCheckCodeExpire() {
        return checkCodeExpire;
    }

    /**
     *setCheckCodeExpire
     */
    public void setCheckCodeExpire(long checkCodeExpire) {
        this.checkCodeExpire = checkCodeExpire;
    }

    /**
     *getCheckCodeLength
     */
    public int getCheckCodeLength() {
        return checkCodeLength;
    }

    /**
     *setCheckCodeLength
     */
    public void setCheckCodeLength(int checkCodeLength) {
        this.checkCodeLength = checkCodeLength;
    }



    /**
     *getPassword
     */
    public String getPassword() {
        return password;
    }

    /**
     *setPassword
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
