package com.dili.trace.dto;

import java.util.HashMap;
import java.util.Map;

public class MessageInputDto {
    /**
     * 消息类型，对应MessageTypeEnum
     */
    private Integer messageType;

    /**
     * 业务类型，对应MessageStateEnum.BUSINESS_TYPE_xx
     */
    private Integer sourceBusinessType;

    /**
     * 业务单据id
     */
    private Long sourceBusinessId;

    /**
     * 接收者
     */
    private Long[] receiverIdArray;

    /**
     * 发送者
     */
    private Long creatorId;

    /**
     * 短信内容参数
     */
    private Map<String, Object> smsContentParam = new HashMap<>();

    /**
     * 站内消息标题
     */
    private Object[] eventMessageTitleParam;

    /**
     * 站内消息内容
     */
    private Object[] eventMessageContentParam;

    private Integer receiverType;

    public Integer getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(Integer receiverType) {
        this.receiverType = receiverType;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
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

    public Long[] getReceiverIdArray() {
        return receiverIdArray;
    }

    public void setReceiverIdArray(Long[] receiverIdArray) {
        this.receiverIdArray = receiverIdArray;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Map<String, Object> getSmsContentParam() {
        return smsContentParam;
    }

    public void setSmsContentParam(Map<String, Object> smsContentParam) {
        this.smsContentParam = smsContentParam;
    }

    public Object[] getEventMessageTitleParam() {
        return eventMessageTitleParam;
    }

    public void setEventMessageTitleParam(Object[] eventMessageTitleParam) {
        this.eventMessageTitleParam = eventMessageTitleParam;
    }

    public Object[] getEventMessageContentParam() {
        return eventMessageContentParam;
    }

    public void setEventMessageContentParam(Object[] eventMessageContentParam) {
        this.eventMessageContentParam = eventMessageContentParam;
    }
}
