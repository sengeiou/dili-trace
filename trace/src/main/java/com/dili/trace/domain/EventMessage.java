package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Table(name = "event_message")
public class EventMessage extends BaseDomain {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 消息头
     */
    @Column(name = "title")
    private String title;

    /**
     * 概述
     */
    @Column(name = "overview")
    private String overview;

    /**
     * 内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 来源业务id
     */
    @Column(name = "source_business_id")
    private Long sourceBusinessId;

    /**
     * 来源业务类型
     */
    @Column(name = "source_business_type")
    private Integer sourceBusinessType;

    /**
     * 创建人id
     */
    @Column(name = "creator_id")
    private Long creatorId;

    /**
     * 创建人
     */
    @Column(name = "creator")
    private String creator;

    /**
     * 接收人id
     */
    @Column(name = "receiver_id")
    private Long receiverId;
    /**
     * 接收人
     */
    @Column(name = "receiver")
    private String receiver;
    /**
     * 接受类型
     */
    @Column(name = "receiver_type")
    private Integer receiverType;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    /**
     * 已读标志位
     */
    @Column(name = "read_flag")
    private Integer readFlag;
    /**
     * 来源单据状态
     */
    @Transient
    private Integer sourceOrderStatus;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSourceOrderStatus() {
        return sourceOrderStatus;
    }

    public void setSourceOrderStatus(Integer sourceOrderStatus) {
        this.sourceOrderStatus = sourceOrderStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSourceBusinessId() {
        return sourceBusinessId;
    }

    public void setSourceBusinessId(Long sourceBusinessId) {
        this.sourceBusinessId = sourceBusinessId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Integer getSourceBusinessType() {
        return sourceBusinessType;
    }

    public void setSourceBusinessType(Integer sourceBusinessType) {
        this.sourceBusinessType = sourceBusinessType;
    }

    public Integer getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(Integer receiverType) {
        this.receiverType = receiverType;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Integer readFlag) {
        this.readFlag = readFlag;
    }
}
