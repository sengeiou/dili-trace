package com.dili.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 默认配置信息
 */
@Component
@ConfigurationProperties(prefix = "default")
public class DefaultConfiguration {

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
