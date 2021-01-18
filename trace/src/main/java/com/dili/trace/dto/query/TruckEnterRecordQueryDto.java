package com.dili.trace.dto.query;

import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;
import com.dili.trace.domain.TruckEnterRecord;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class TruckEnterRecordQueryDto extends TruckEnterRecord {
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
     * 车牌模糊查询
     */
    @Column(name = "`code`")
    @Like(Like.RIGHT)
    private String likeCode;


    /**
     * 车牌模糊查询
     */
    @Column(name = "`truck_plate`")
    @Like(Like.RIGHT)
    private String likeTruckPlate;

    public String getLikeCode() {
        return likeCode;
    }

    public void setLikeCode(String likeCode) {
        this.likeCode = likeCode;
    }

    /**
     * 司机名字模糊查询
     */
    @Column(name = "`driver_name`")
    @Like(Like.RIGHT)
    private String likeDriverName;

    /**
     * 手机号码模糊查询
     */
    @Column(name = "`driver_phone`")
    @Like(Like.RIGHT)
    private String likeDriverPhone;


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

    public String getLikeTruckPlate() {
        return likeTruckPlate;
    }

    public void setLikeTruckPlate(String likeTruckPlate) {
        this.likeTruckPlate = likeTruckPlate;
    }

    public String getLikeDriverPhone() {
        return likeDriverPhone;
    }

    public void setLikeDriverPhone(String likeDriverPhone) {
        this.likeDriverPhone = likeDriverPhone;
    }
}
