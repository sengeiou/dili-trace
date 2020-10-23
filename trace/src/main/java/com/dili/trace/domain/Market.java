package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

/**
 * 市场类
 *
 * @author Lily
 */
@Table(name = "`market`")
public class Market extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @ApiModelProperty(value = "市场名称")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "操作人ID")
    @Column(name = "`operator_id`")
    private Long operatorId;

    @ApiModelProperty(value = "操作人")
    @Column(name = "operator_name")
    private String operatorName;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;

    @ApiModelProperty(value = "修改时间")
    @Column(name = "`modified`")
    private Date modified;

    @ApiModelProperty(value = "appId")
    @Column(name = "`app_id`")
    private Long appId;

    @ApiModelProperty(value = "appSecret")
    @Column(name = "app_secret")
    private String appSecret;

    @ApiModelProperty(value = "contextUrl")
    @Column(name = "context_url")
    private String contextUrl;

    @ApiModelProperty(value = "对接平台市场ID")
    @Column(name = "`platform_market_id`")
    private Long platformMarketId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
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
}
