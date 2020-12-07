package com.dili.trace.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;

/**
 * 市场类
 *
 * @author Lily
 */
public class Market {

    /**
     * 市场id
     */
    private Long id;

    /**
     * 市场编码
     */
    private String code;

    /**
     * 市场名
     */
    private String name;

    @ApiModelProperty(value = "appId")
    @Column(name = "`app_id`")
    private Long appId;

    @ApiModelProperty(value = "appSecret")
    private String appSecret;

    @ApiModelProperty(value = "contextUrl")
    private String contextUrl;

    @ApiModelProperty(value = "对接平台市场ID")
    private Long platformMarketId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getContextUrl() {
        return contextUrl;
    }

    public void setContextUrl(String contextUrl) {
        this.contextUrl = contextUrl;
    }

    public Long getPlatformMarketId() {
        return platformMarketId;
    }

    public void setPlatformMarketId(Long platformMarketId) {
        this.platformMarketId = platformMarketId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
