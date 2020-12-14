package com.dili.trace.dto;

import com.dili.ss.domain.annotation.Like;
import com.dili.trace.domain.TruckEnterRecord;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDate;


public class TruckEnterRecordQueryDto extends TruckEnterRecord {
    // 登记时间开始
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    private LocalDate createdStart;
    // 登记时间结束
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    private LocalDate createdEnd;

    @Column(name = "`truck_plate`")
    @Like(Like.RIGHT)
    private String likeTruckPlate;

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
