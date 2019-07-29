package com.dili.trace.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
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

    @Column(name = "`sales_customer_name`")
    @FieldDef(label="salesCustomerName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getSalesCustomerName();

    void setSalesCustomerName(String salesCustomerName);

    @Column(name = "`sales_city_id`")
    @FieldDef(label="salesCityId")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getSalesCityId();

    void setSalesCityId(Long salesCityId);

    @Column(name = "`sales_city_name`")
    @FieldDef(label="salesCityName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getSalesCityName();

    void setSalesCityName(String salesCityName);

    @Column(name = "`sales_weight`")
    @FieldDef(label="salesWeight")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSalesWeight();

    void setSalesWeight(Integer salesWeight);

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
}