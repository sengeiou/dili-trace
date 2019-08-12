package com.dili.trace.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;
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

    @ApiModelProperty(value = "编号")
    @Column(name = "`code`")
    @FieldDef(label="编号")
    @EditMode(editor = FieldEditor.Text, required = true)
    String getCode();

    void setCode(String code);

    @ApiModelProperty(value = "1.理货区 2.交易区")
    @Column(name = "`register_source`")
    @FieldDef(label="1.理货区 2.交易区")
    @EditMode(editor = FieldEditor.Text, required = true)
    Byte getRegisterSource();

    void setRegisterSource(Byte registerSource);

    @ApiModelProperty(value = "理货区号")
    @Column(name = "`tally_area_no`")
    @FieldDef(label="tallyAreaNo", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTallyAreaNo();

    void setTallyAreaNo(String tallyAreaNo);

    @ApiModelProperty(value = "业户姓名")
    @Column(name = "`name`")
    @FieldDef(label="业户姓名", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getName();

    void setName(String name);

    @ApiModelProperty(value = "身份证号")
    @Column(name = "`id_card_no`")
    @FieldDef(label="身份证号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getIdCardNo();

    void setIdCardNo(String idCardNo);

    @ApiModelProperty(value = "身份地址")
    @Column(name = "`addr`")
    @FieldDef(label="地址", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddr();

    void setAddr(String addr);

    @ApiModelProperty(value = "交易单号")
    @Column(name = "`trade_no`")
    @FieldDef(label="tradeNo")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTradeNo();

    void setTradeNo(String tradeNo);

    @ApiModelProperty(value = "交易账号")
    @Column(name = "`trade_account`")
    @FieldDef(label="tradeAccount")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTradeAccount();

    void setTradeAccount(String tradeAccount);

    @ApiModelProperty(value = "用户iD")
    @Column(name = "`user_id`")
    @FieldDef(label="用户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getUserId();

    void setUserId(Long userId);

    @ApiModelProperty(value = "车牌")
    @Column(name = "`plate`")
    @FieldDef(label="plate", maxLength = 15)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getPlate();

    void setPlate(String plate);

    @ApiModelProperty(value = "1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中")
    @Column(name = "`state`")
    @FieldDef(label="1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中")
    @EditMode(editor = FieldEditor.Number, required = true)
    Integer getState();

    void setState(Integer state);
    @Transient
    default String getStateName(){
        if(getState()==null){
            return "";
        }
        return RegisterBillStateEnum.getEnabledState(getState()).getName();
    }

    @ApiModelProperty(value = "1.分销 2.全销")
    @Column(name = "`sales_type`")
    @FieldDef(label="1.分销 2.全销")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSalesType();

    void setSalesType(Integer salesType);

    @ApiModelProperty(value = "1:采样检测 2:主动送检")
    @Column(name = "`sample_source`")
    @FieldDef(label="1:采样检测 2:主动送检")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getSampleSource();

    void setSampleSource(Integer sampleSource);

    @ApiModelProperty(value = "检测报告url")
    @Column(name = "`detect_report_url`")
    @FieldDef(label="检测报告url", maxLength = 50)
    @EditMode(editor = FieldEditor.Text)
    String getDetectReportUrl();

    void setDetectReportUrl(String detectReportUrl);
    @ApiModelProperty(value = "商品名称")
    @Column(name = "`product_name`")
    @FieldDef(label="productName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = true)
    String getProductName();

    void setProductName(String productName);

    @ApiModelProperty(value = "商品ID")
    @Column(name = "`product_id`")
    @FieldDef(label="productId")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getProductId();

    void setProductId(Long productId);
    @ApiModelProperty(value = "产地证明图片")
    @Column(name = "`origin_certifiy_url`")
    @FieldDef(label="产地证明图片", maxLength = 50)
    @EditMode(editor = FieldEditor.Text)
    String getOriginCertifiyUrl();

    void setOriginCertifiyUrl(String originCertifiyUrl);
    @ApiModelProperty(value = "产地ID")
    @Column(name = "`origin_id`")
    @FieldDef(label="originId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOriginId();

    void setOriginId(Long originId);
    @ApiModelProperty(value = "产地名")
    @Column(name = "`origin_name`")
    @FieldDef(label="originName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOriginName();

    void setOriginName(String originName);
    @ApiModelProperty(value = "重量KG")
    @Column(name = "`weight`")
    @FieldDef(label="weight")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getWeight();

    void setWeight(Integer weight);
    @ApiModelProperty(value = "1.合格 2.不合格 3.复检合格 4.复检不合格")
    @Column(name = "`detect_state`")
    @FieldDef(label="1.合格 2.不合格 3.复检合格 4.复检不合格")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getDetectState();

    void setDetectState(Integer detectState);
    @Transient
    default String getDetectStateName(){
        if(getDetectState()==null){
            return "";
        }
        return BillDetectStateEnum.getEnabledState(getDetectState()).getName();
    }

    @ApiModelProperty(value = "检测记录ID")
    @Column(name = "`latest_detect_record_id`")
    @FieldDef(label="latestDetectRecordId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getLatestDetectRecordId();

    void setLatestDetectRecordId(Long latestDetectRecordId);
    @ApiModelProperty(value = "检测记录时间")
    @Column(name = "`latest_detect_time`")
    @FieldDef(label="latestDetectTime")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    Date getLatestDetectTime();

    void setLatestDetectTime(Date latestDetectTime);
    @ApiModelProperty(value = "检测人员")
    @Column(name = "`latest_detect_operator`")
    @FieldDef(label="latestDetectOperator")
    @EditMode(editor = FieldEditor.Text, required = true)
    Date getLatestDetectOperator();

    void setLatestDetectOperator(String latestDetectOperator);
    @ApiModelProperty(value = "仪器编号")
    @Column(name = "`exe_machine_no`")
    @FieldDef(label="exeMachineNo")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getExeMachineNo();

    void setExeMachineNo(String exeMachineNo);
    @ApiModelProperty(value = "版本")
    @Column(name = "`version`")
    @FieldDef(label="version")
    @EditMode(editor = FieldEditor.Number)
    Integer getVersion();

    void setVersion(Integer version);

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
    @ApiModelProperty(value = "操作人")
    @Column(name = "`operator_name`")
    @FieldDef(label="operatorName")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOperatorName();

    void setOperatorName(String operatorName);
    @ApiModelProperty(value = "操作人ID")
    @Column(name = "`operator_id`")
    @FieldDef(label="operatorId")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getOperatorId();

    void setOperatorId(Long operatorId);


}