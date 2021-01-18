package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

/**
 * 上游与用户关系
 */
@Table(name = "`r_user_upstream`")
public class RUserUpstream extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    /**
     * 用户(商户)ID
     */
    @ApiModelProperty(value = "用户(商户)ID")
    @Column(name = "`user_id`")
    private Long userId;

    /**
     * 用户(商户)名
     */
    @ApiModelProperty(value = "用户(商户)名")
    @Column(name = "`user_name`")
    private String userName;


    /**
     * 上游信息ID
     */
    @ApiModelProperty(value = "上游信息ID")
    @Column(name = "`upstream_id`")
    private Long upstreamId;

    @ApiModelProperty(value = "操作人姓名")
    @Column(name = "`operator_name`")
    private String operatorName;

    @ApiModelProperty(value = "操作人ID")
    @Column(name = "`operator_id`")
    private Long operatorId;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;

    @ApiModelProperty(value = "更新时间")
    @Column(name = "`modified`")
    private Date modified;

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

    public Long getUpstreamId() {
        return upstreamId;
    }

    public void setUpstreamId(Long upstreamId) {
        this.upstreamId = upstreamId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
