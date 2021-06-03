package com.dili.trace.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("serial")
@Table(name = "`sms_message`")
public class SmsMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JsonIgnore
    private Long id;

    @ApiModelProperty(value = "业务类型")
    @Column(name = "`source_business_type`")
    private Integer sourceBusinessType;

    @ApiModelProperty(value = "业务单据id")
    @Column(name = "`source_business_id`")
    private Long sourceBusinessId;

    @ApiModelProperty(value = "接收手机号码")
    @Column(name = "`receive_phone`")
    private String receivePhone;

    @ApiModelProperty(value = "发送原因")
    @Column(name = "`send_reason`")
    private Integer sendReason;

    @ApiModelProperty(value = "结果码")
    @Column(name = "`result_code`")
    private String resultCode;

    @ApiModelProperty(value = "结果信息")
    @Column(name = "`result_info`")
    private String resultInfo;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "`created`")
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSourceBusinessType() {
        return sourceBusinessType;
    }

    public void setSourceBusinessType(Integer sourceBusinessType) {
        this.sourceBusinessType = sourceBusinessType;
    }

    public Long getSourceBusinessId() {
        return sourceBusinessId;
    }

    public void setSourceBusinessId(Long sourceBusinessId) {
        this.sourceBusinessId = sourceBusinessId;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public Integer getSendReason() {
        return sendReason;
    }

    public void setSendReason(Integer sendReason) {
        this.sendReason = sendReason;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
