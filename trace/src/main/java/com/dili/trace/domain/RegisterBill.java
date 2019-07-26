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
 * This file was generated on 2019-07-26 09:20:34.
 */
@Table(name = "`register_bill`")
public interface RegisterBill extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`code`")
    @FieldDef(label="编号")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getCode();

    void setCode(Long code);

    @Column(name = "`register_source`")
    @FieldDef(label="1.理货区 2.交易区")
    @EditMode(editor = FieldEditor.Text, required = true)
    Byte getRegisterSource();

    void setRegisterSource(Byte registerSource);

    @Column(name = "`tally_area_no`")
    @FieldDef(label="tallyAreaNo", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTallyAreaNo();

    void setTallyAreaNo(String tallyAreaNo);

    @Column(name = "`name`")
    @FieldDef(label="业户姓名", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @Column(name = "`id_card_no`")
    @FieldDef(label="身份证号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getIdCardNo();

    void setIdCardNo(String idCardNo);

    @Column(name = "`addr`")
    @FieldDef(label="地址", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddr();

    void setAddr(String addr);

    @Column(name = "`trade_no`")
    @FieldDef(label="tradeNo")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTradeNo();

    void setTradeNo(Long tradeNo);

    @Column(name = "`user_id`")
    @FieldDef(label="用户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getUserId();

    void setUserId(Long userId);

    @Column(name = "`plate`")
    @FieldDef(label="plate", maxLength = 15)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getPlate();

    void setPlate(String plate);

    @Column(name = "`state`")
    @FieldDef(label="1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中")
    @EditMode(editor = FieldEditor.Number, required = true)
    Boolean getState();

    void setState(Boolean state);

    @Column(name = "`sales_type`")
    @FieldDef(label="1.分销 2.全销")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getSalesType();

    void setSalesType(Boolean salesType);

    @Column(name = "`product_name`")
    @FieldDef(label="productName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getProductName();

    void setProductName(String productName);

    @Column(name = "`product_id`")
    @FieldDef(label="productId")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getProductId();

    void setProductId(Long productId);

    @Column(name = "`origin_id`")
    @FieldDef(label="originId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOriginId();

    void setOriginId(Long originId);

    @Column(name = "`orinin_name`")
    @FieldDef(label="orininName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOrininName();

    void setOrininName(String orininName);

    @Column(name = "`weight`")
    @FieldDef(label="weight")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getWeight();

    void setWeight(Integer weight);

    @Column(name = "`detect_state`")
    @FieldDef(label="1.合格 2.不合格 3.复检合格 4.复检不合格")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getDetectState();

    void setDetectState(Boolean detectState);

    @Column(name = "`latest_detect_record_id`")
    @FieldDef(label="latestDetectRecordId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getLatestDetectRecordId();

    void setLatestDetectRecordId(Long latestDetectRecordId);

    @Column(name = "`latest_detect_time`")
    @FieldDef(label="latestDetectTime")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getLatestDetectTime();

    void setLatestDetectTime(Date latestDetectTime);

    @Column(name = "`exe_machine_no`")
    @FieldDef(label="exeMachineNo")
    @EditMode(editor = FieldEditor.Text, required = false)
    Byte getExeMachineNo();

    void setExeMachineNo(Byte exeMachineNo);

    @Column(name = "`version`")
    @FieldDef(label="version")
    @EditMode(editor = FieldEditor.Text, required = false)
    Byte getVersion();

    void setVersion(Byte version);

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