package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.DetectStatusEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.glossary.TFEnum;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 车子进门记录
 * <p>
 * This file was generated on 2019-07-26 09:20:34.
 */
@Table(name = "`truck_enter_record`")
public class TruckEnterRecord extends BaseDomain {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    private Long id;

    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @Column(name = "`code`")
    private String code;

    /**
     * 车牌
     */
    @ApiModelProperty(value = "车牌")
    @Column(name = "`truck_plate`")
    private String truckPlate;

    /**
     * 车型ID
     */
    @ApiModelProperty(value = "车型ID")
    @Column(name = "`truck_type_id`")
    private Long truckTypeId;
    /**
     * 车型名称
     */
    @ApiModelProperty(value = "车型名称")
    @Column(name = "`truck_type_name`")
    private String truckTypeName;


    /**
     * 司机ID
     */
    @ApiModelProperty(value = "司机ID")
    @Column(name = "`driver_id`")
    private Long driverId;
    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "司机姓名")
    @Column(name = "`driver_name`")
    private String driverName;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    @Column(name = "`corporate_name`")
    private String corporateName;

    /**
     * 司机姓名
     */
    @ApiModelProperty(value = "司机电话")
    @Column(name = "`driver_phone`")
    private String driverPhone;

    /**
     * 创建时间
     */
    @Column(name = "`created`")
    private Date created;

    /**
     * 修改时间
     */
    @Column(name = "`modified`")
    private Date modified;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @Column(name = "`operator_name`")
    private String operatorName;

    /**
     * 操作人ID
     */
    @ApiModelProperty(value = "操作人ID")
    @Column(name = "`operator_id`")
    private Long operatorId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getTruckPlate() {
        return truckPlate;
    }

    public void setTruckPlate(String truckPlate) {
        this.truckPlate = truckPlate;
    }

    public Long getTruckTypeId() {
        return truckTypeId;
    }

    public void setTruckTypeId(Long truckTypeId) {
        this.truckTypeId = truckTypeId;
    }

    public String getTruckTypeName() {
        return truckTypeName;
    }

    public void setTruckTypeName(String truckTypeName) {
        this.truckTypeName = truckTypeName;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }
}