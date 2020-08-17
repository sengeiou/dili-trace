package com.dili.trace.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@SuppressWarnings("serial")
@Table(name = "`message_config`")
public class MessageConfig extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @JSONField(serialize =false)
    private Long id;

    @ApiModelProperty(value = "操作类型")
    @Column(name = "`operation`")
    private Integer operation;

    @ApiModelProperty(value = "是否发站内消息")
    @Column(name = "`message_flag`")
    private String messageFlag;

    @ApiModelProperty(value = "是否发短信消息")
    @Column(name = "`sms_flag`")
    private String smsFlag;

    @ApiModelProperty(value = "是否发微信消息")
    @Column(name = "`wechat_flag`")
    private String wechatFlag;

    @ApiModelProperty(value = "微信sceneCode")
    @Column(name = "`sms_scene_code`")
    private String smsSceneCode;

    @ApiModelProperty(value = "微信消息模板id")
    @Column(name = "`wechat_template_id`")
    private String wechatTemplateId;

    @ApiModelProperty(value = "站内信息标题")
    @Column(name = "`event_message_title`")
    private String eventMessageTitle;

    @ApiModelProperty(value = "站内信息内容")
    @Column(name = "`event_message_content`")
    private String eventMessageContent;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public String getMessageFlag() {
        return messageFlag;
    }

    public void setMessageFlag(String messageFlag) {
        this.messageFlag = messageFlag;
    }

    public String getSmsFlag() {
        return smsFlag;
    }

    public void setSmsFlag(String smsFlag) {
        this.smsFlag = smsFlag;
    }

    public String getWechatFlag() {
        return wechatFlag;
    }

    public void setWechatFlag(String wechatFlag) {
        this.wechatFlag = wechatFlag;
    }

    public String getSmsSceneCode() {
        return smsSceneCode;
    }

    public void setSmsSceneCode(String smsSceneCode) {
        this.smsSceneCode = smsSceneCode;
    }

    public String getWechatTemplateId() {
        return wechatTemplateId;
    }

    public void setWechatTemplateId(String wechatTemplateId) {
        this.wechatTemplateId = wechatTemplateId;
    }

    public String getEventMessageTitle() {
        return eventMessageTitle;
    }

    public void setEventMessageTitle(String eventMessageTitle) {
        this.eventMessageTitle = eventMessageTitle;
    }

    public String getEventMessageContent() {
        return eventMessageContent;
    }

    public void setEventMessageContent(String eventMessageContent) {
        this.eventMessageContent = eventMessageContent;
    }
}
