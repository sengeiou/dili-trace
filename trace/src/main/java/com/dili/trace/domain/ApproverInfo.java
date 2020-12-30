package com.dili.trace.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`approver_info`")
public class ApproverInfo extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JSONField(serialize = false)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    @Column(name = "`user_id`")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    @Column(name = "`user_name`")
    private String userName;

    @ApiModelProperty(value = "手机号")
    @Column(name = "`phone`")
    private String phone;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;

    @ApiModelProperty(value = "修改时间")
    @Column(name = "`modified`")
    private Date modified;

    @ApiModelProperty(value = "市场ID")
    @Column(name = "`market_id`")
    private Long marketId;

    @Transient
    private String signBase64;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getSignBase64() {
        return signBase64;
    }

    public void setSignBase64(String signBase64) {
        this.signBase64 = signBase64;
    }
}
