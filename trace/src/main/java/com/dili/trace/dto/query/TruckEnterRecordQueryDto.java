package com.dili.trace.dto.query;

import com.dili.ss.domain.annotation.Like;
import com.dili.trace.domain.TruckEnterRecord;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.time.LocalDate;


public class TruckEnterRecordQueryDto extends TruckEnterRecord {
    // 登记时间开始
    /**
     * 开始时间
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    private LocalDate createdStart;
    // 登记时间结束
    /**
     * 结束时间
     */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    private LocalDate createdEnd;

    /**
     * 车牌模糊查询
     */
    @Column(name = "`truck_plate`")
    @Like(Like.RIGHT)
    private String likeTruckPlate;

    /**
     * 司机名字模糊查询
     */
    @Column(name = "`driver_name`")
    @Like(Like.RIGHT)
    private String likeDriverName;


    /**
     * 电话或姓名模糊查询
     */
    @Transient
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLikeDriverName() {
        return likeDriverName;
    }

    public void setLikeDriverName(String likeDriverName) {
        this.likeDriverName = likeDriverName;
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

    public String getLikeTruckPlate() {
        return likeTruckPlate;
    }

    public void setLikeTruckPlate(String likeTruckPlate) {
        this.likeTruckPlate = likeTruckPlate;
    }
}
