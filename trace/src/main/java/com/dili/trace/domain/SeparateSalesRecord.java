package com.dili.trace.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`separate_sales_record`")
public interface SeparateSalesRecord extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);
    @ApiModelProperty(value = "分销用户ID")
    @Column(name = "`sales_user_id`")
    @FieldDef(label="salesUserId")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getSalesUserId();

    void setSalesUserId(Long salesUserId);
    @ApiModelProperty(value = "分销用户")
    @Column(name = "`sales_user_name`")
    @FieldDef(label="salesUserName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getSalesUserName();

    void setSalesUserName(String salesUserName);
    
    @ApiModelProperty(value = "交易单号")
    @Column(name = "`trade_no`")
    @FieldDef(label="trade_no", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getTradeNo();

    void setTradeNo(String salesUserName);
    
    @ApiModelProperty(value = "分销城市ID")
    @Column(name = "`sales_city_id`")
    @FieldDef(label="salesCityId")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getSalesCityId();

    void setSalesCityId(Long salesCityId);
    @ApiModelProperty(value = "分销城市")
    @Column(name = "`sales_city_name`")
    @FieldDef(label="salesCityName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getSalesCityName();

    void setSalesCityName(String salesCityName);
    @ApiModelProperty(value = "分销重量KG")
    @Column(name = "`sales_weight`")
    @FieldDef(label="salesWeight")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSalesWeight();

    void setSalesWeight(Integer salesWeight);
    
    
    @ApiModelProperty(value = "车牌号")
    @Column(name = "`sales_plate`")
    @FieldDef(label="sales_plate")
    @EditMode(editor = FieldEditor.Text, required = true)
    String getSalesPlate();

    void setSalesPlate(String salesPlate);
    
    
    @ApiModelProperty(value = "被分销的登记单")
    @Column(name = "`register_bill_code`")
    @FieldDef(label="registerBillCode")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getRegisterBillCode();

    void setRegisterBillCode(String registerBillCode);

    @Column(name = "`created`")
    @FieldDef(label="created")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getCreated();

    void setCreated(Date created);

    @Column(name = "`modified`")
    @FieldDef(label="modified")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getModified();

    void setModified(Date modified);


    @ApiModelProperty(value = "分销自")
    @Column(name = "`parent_id`")
    @FieldDef(label="parent_id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getParentId();

    void setParentId(Long parentId);

   
    @ApiModelProperty(value = "未分销重量")
    @Column(name = "`store_weight`")
    @FieldDef(label="store_weight")
    @EditMode(editor = FieldEditor.Number, required = true)
    BigDecimal getStoreWeight();

    void setStoreWeight(BigDecimal storeWeight);


    @ApiModelProperty(value = "最初登记单ID")
    @Column(name = "`bill_id`")
    @FieldDef(label="bill_id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getBillId();

    void setBillId(Long billId);


    @Column(name = "`sales_type`")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSalesType();

    void setSalesType(Integer salesType);


    

    @ApiModelProperty(value = "进场审核ID")
    @Column(name = "`checkin_record_id`")
    @FieldDef(label="checkin_record_id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getCheckinRecordId();

    void setCheckinRecordId(Long checkinRecordId);
    
    @ApiModelProperty(value = "出场审核ID")
    @Column(name = "`checkout_record_id`")
    @FieldDef(label="checkout_record_id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getCheckoutRecordId();

    void setCheckoutRecordId(Long checkoutRecordId);

}