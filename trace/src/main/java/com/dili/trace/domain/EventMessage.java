package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;

import javax.persistence.*;

@Table(name = "event_message")
public class EventMessage extends BaseDomain {
    /**
     * 接收者角色 普通用户10 管理员20， 默认是 10；
     */
    public static final Integer RECEIVER_TYPE = 10;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "overview")
    private String overview;

    @Column(name = "content")
    private String content;

    @Column(name = "source_business_id")
    private Long sourceBusinessId;

    @Column(name = "source_business_type")
    private Integer sourceBusinessType;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "creator")
    private String creator;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "receiver_type")
    private Integer receiverType;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "read_flag")
    private Integer readFlag;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
