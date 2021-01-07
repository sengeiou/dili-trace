package com.dili.trace.dto.query;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.PurchaseIntentionRecord;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.time.LocalDateTime;

public class PurchaseIntentionRecordQueryDto extends PurchaseIntentionRecord {

    /**
     * 登记时间开始
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private LocalDateTime createdStart;

    /**
     * 登记时间结束
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private LocalDateTime createdEnd;

    /**
     * 进门时间开始
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private LocalDateTime checkinCreatedStart;

    /**
     * 进门时间结束
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private LocalDateTime checkinCreatedEnd;

    /**
     * 买家姓名查询
     */
    @Column(name = "`buyer_name`")
    @Like(Like.RIGHT)
    private String likeBuyerName;

    /**
     * 报备编号查询
     */
    @Column(name = "`code`")
    @Like(Like.RIGHT)
    private String likeCode;

    /**
     * 车牌号查询
     */
    @Column(name = "`plate`")
    @Like(Like.RIGHT)
    private String likePlate;

    /**
     * 姓名或电话模糊查询
     */
    @Transient
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public LocalDateTime getCreatedStart() {
        return createdStart;
    }

    public void setCreatedStart(LocalDateTime createdStart) {
        this.createdStart = createdStart;
    }

    public LocalDateTime getCreatedEnd() {
        return createdEnd;
    }

    public void setCreatedEnd(LocalDateTime createdEnd) {
        this.createdEnd = createdEnd;
    }

    public String getLikeBuyerName() {
        return likeBuyerName;
    }

    public void setLikeBuyerName(String likeBuyerName) {
        this.likeBuyerName = likeBuyerName;
    }

    public LocalDateTime getCheckinCreatedStart() {
        return checkinCreatedStart;
    }

    public void setCheckinCreatedStart(LocalDateTime checkinCreatedStart) {
        this.checkinCreatedStart = checkinCreatedStart;
    }

    public LocalDateTime getCheckinCreatedEnd() {
        return checkinCreatedEnd;
    }

    public void setCheckinCreatedEnd(LocalDateTime checkinCreatedEnd) {
        this.checkinCreatedEnd = checkinCreatedEnd;
    }

    public String getLikeCode() {
        return likeCode;
    }

    public void setLikeCode(String likeCode) {
        this.likeCode = likeCode;
    }

    public String getLikePlate() {
        return likePlate;
    }

    public void setLikePlate(String likePlate) {
        this.likePlate = likePlate;
    }
}
