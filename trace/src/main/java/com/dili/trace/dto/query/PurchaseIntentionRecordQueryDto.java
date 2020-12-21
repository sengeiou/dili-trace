package com.dili.trace.dto.query;

import com.dili.ss.domain.annotation.Like;
import com.dili.trace.domain.PurchaseIntentionRecord;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.time.LocalDate;

public class PurchaseIntentionRecordQueryDto extends PurchaseIntentionRecord {
    // 登记时间开始
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    private LocalDate createdStart;
    // 登记时间结束
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    private LocalDate createdEnd;

    /**
     * 买家姓名查询
     */
    @Column(name = "`buyer_name`")
    @Like(Like.RIGHT)
    private String likeBuyerName;

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

    public LocalDate getCreatedStart() {
        return createdStart;
    }

    public void setCreatedStart(LocalDate createdStart) {
        this.createdStart = createdStart;
    }

    public LocalDate getCreatedEnd() {
        return createdEnd;
    }

    public void setCreatedEnd(LocalDate createdEnd) {
        this.createdEnd = createdEnd;
    }

    public String getLikeBuyerName() {
        return likeBuyerName;
    }

    public void setLikeBuyerName(String likeBuyerName) {
        this.likeBuyerName = likeBuyerName;
    }
}
