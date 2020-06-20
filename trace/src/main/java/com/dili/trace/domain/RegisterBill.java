package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
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
public class RegisterBill extends BaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	private Long id;

	@ApiModelProperty(value = "编号")
	@Column(name = "`code`")
	private String code;

	@ApiModelProperty(value = "采样编号")
	@Column(name = "`sample_code`")
	private String sampleCode;

	@ApiModelProperty(value = "1.理货区 2.交易区")
	@Column(name = "`register_source`")
	private Integer registerSource;

	@ApiModelProperty(value = "理货区号")
	@Column(name = "`tally_area_no`")
	private String tallyAreaNo;

	@ApiModelProperty(value = "业户姓名")
	@Column(name = "`name`")
	private String name;

	@ApiModelProperty(value = "身份证号")
	@Column(name = "`id_card_no`")
	private String idCardNo;

	@ApiModelProperty(value = "身份地址")
	@Column(name = "`addr`")
	private String addr;

	@ApiModelProperty(value = "业户手机号")
	@Column(name = "`phone`")
	private String phone;

	@ApiModelProperty(value = "交易账号")
	@Column(name = "`trade_account`")
	private String tradeAccount;

	@ApiModelProperty(value = "印刷卡号")
	@Column(name = "`trade_printing_card`")
	private String tradePrintingCard;

	@ApiModelProperty(value = "用户iD")
	@Column(name = "`user_id`")
	private Long userId;

	@ApiModelProperty(value = "车牌")
	@Column(name = "`plate`")
	private String plate;

	@ApiModelProperty(value = "1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中")
	@Column(name = "`state`")
	private Integer state;

	@ApiModelProperty(value = "1.分销 2.全销")
	@Column(name = "`sales_type`")
	private Integer salesType;

	@ApiModelProperty(value = "1:采样检测 2:主动送检")
	@Column(name = "`sample_source`")
	private Integer sampleSource;

	@ApiModelProperty(value = "检测报告url")
	@Column(name = "`detect_report_url`")
	String detectReportUrl;

	@ApiModelProperty(value = "商品名称")
	@Column(name = "`product_name`")
	private String productName;

	@ApiModelProperty(value = "商品ID")
	@Column(name = "`product_id`")
	private Long productId;

	@ApiModelProperty(value = "产地证明图片")
	@Column(name = "`origin_certifiy_url`")
	private String originCertifiyUrl;

	@ApiModelProperty(value = "产地ID")
	@Column(name = "`origin_id`")
	private Long originId;

	@ApiModelProperty(value = "产地名")
	@Column(name = "`origin_name`")
	private String originName;

	@ApiModelProperty(value = "重量KG")
	@Column(name = "`weight`")
	private Integer weight;

	@ApiModelProperty(value = "1.合格 2.不合格 3.复检合格 4.复检不合格")
	@Column(name = "`detect_state`")
	private Integer detectState;

	@ApiModelProperty(value = "检测记录ID")
	@Column(name = "`latest_detect_record_id`")
	private Long latestDetectRecordId;

	@ApiModelProperty(value = "检测记录时间")
	@Column(name = "`latest_detect_time`")
	private Date latestDetectTime;

	@ApiModelProperty(value = "检测人员")
	@Column(name = "`latest_detect_operator`")
	private String latestDetectOperator;

	@ApiModelProperty(value = "检测值结果")
	@Column(name = "`latest_pd_result`")
	private String latestPdResult;

	@ApiModelProperty(value = "仪器编号")
	@Column(name = "`exe_machine_no`")
	private String exeMachineNo;

	@ApiModelProperty(value = "版本")
	@Column(name = "`version`")
	private Integer version;

	@Column(name = "`created`")
	private Date created;

	@Column(name = "`modified`")
	private Date modified;

	@ApiModelProperty(value = "操作人")
	@Column(name = "`operator_name`")
	private String operatorName;

	@ApiModelProperty(value = "操作人ID")
	@Column(name = "`operator_id`")
	private Long operatorId;

	@Column(name = "`trade_type_id`")
	private String tradeTypeId;

	@Column(name = "`trade_type_name`")
	private String tradeTypeName;

	@ApiModelProperty(value = "处理结果图片URL")
	@Column(name = "`handle_result_url`")
	private String handleResultUrl;

	@ApiModelProperty(value = "处理结果备注")
	@Column(name = "`handle_result`")
	private String handleResult;

	@ApiModelProperty(value = "数据创建来源")
	@Column(name = "`creation_source`")
	private Integer creationSource;

	@ApiModelProperty(value = "检测报告单ID")
	@Column(name = "`check_sheet_id`")
	private Long checkSheetId;

	@ApiModelProperty(value = "上游信息ID")
	@Column(name = "`upstream_id`")
	private Long upStreamId;

	@ApiModelProperty(value = "数据是否完整")
	@Column(name = "`complete`")
	private Integer complete;
	
	@ApiModelProperty(value = "查验状态值")
	@Column(name = "`verify_state`")
	private Integer verifyState;

	public Integer getVerifyState() {
		return verifyState;
	}

	public void setVerifyState(Integer verifyState) {
		this.verifyState = verifyState;
	}

	@Transient
	public String getDetectStateName() {
		try {
			if (getDetectState() == null) {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
		BillDetectStateEnum state = BillDetectStateEnum.getBillDetectStateEnum(getDetectState());
		return state.getName();

	}

	@Transient
	public String getStateName() {
		try {
			if (getState() == null) {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
		return RegisterBillStateEnum.getRegisterBillStateEnum(getState()).getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSampleCode() {
		return sampleCode;
	}

	public void setSampleCode(String sampleCode) {
		this.sampleCode = sampleCode;
	}

	public Integer getRegisterSource() {
		return registerSource;
	}

	public void setRegisterSource(Integer registerSource) {
		this.registerSource = registerSource;
	}

	public String getTallyAreaNo() {
		return tallyAreaNo;
	}

	public void setTallyAreaNo(String tallyAreaNo) {
		this.tallyAreaNo = tallyAreaNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTradeAccount() {
		return tradeAccount;
	}

	public void setTradeAccount(String tradeAccount) {
		this.tradeAccount = tradeAccount;
	}

	public String getTradePrintingCard() {
		return tradePrintingCard;
	}

	public void setTradePrintingCard(String tradePrintingCard) {
		this.tradePrintingCard = tradePrintingCard;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getSalesType() {
		return salesType;
	}

	public void setSalesType(Integer salesType) {
		this.salesType = salesType;
	}

	public Integer getSampleSource() {
		return sampleSource;
	}

	public void setSampleSource(Integer sampleSource) {
		this.sampleSource = sampleSource;
	}

	public String getDetectReportUrl() {
		return detectReportUrl;
	}

	public void setDetectReportUrl(String detectReportUrl) {
		this.detectReportUrl = detectReportUrl;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getOriginCertifiyUrl() {
		return originCertifiyUrl;
	}

	public void setOriginCertifiyUrl(String originCertifiyUrl) {
		this.originCertifiyUrl = originCertifiyUrl;
	}

	public Long getOriginId() {
		return originId;
	}

	public void setOriginId(Long originId) {
		this.originId = originId;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getDetectState() {
		return detectState;
	}

	public void setDetectState(Integer detectState) {
		this.detectState = detectState;
	}

	public Long getLatestDetectRecordId() {
		return latestDetectRecordId;
	}

	public void setLatestDetectRecordId(Long latestDetectRecordId) {
		this.latestDetectRecordId = latestDetectRecordId;
	}

	public Date getLatestDetectTime() {
		return latestDetectTime;
	}

	public void setLatestDetectTime(Date latestDetectTime) {
		this.latestDetectTime = latestDetectTime;
	}

	public String getLatestDetectOperator() {
		return latestDetectOperator;
	}

	public void setLatestDetectOperator(String latestDetectOperator) {
		this.latestDetectOperator = latestDetectOperator;
	}

	public String getLatestPdResult() {
		return latestPdResult;
	}

	public void setLatestPdResult(String latestPdResult) {
		this.latestPdResult = latestPdResult;
	}

	public String getExeMachineNo() {
		return exeMachineNo;
	}

	public void setExeMachineNo(String exeMachineNo) {
		this.exeMachineNo = exeMachineNo;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

	public String getTradeTypeId() {
		return tradeTypeId;
	}

	public void setTradeTypeId(String tradeTypeId) {
		this.tradeTypeId = tradeTypeId;
	}

	public String getTradeTypeName() {
		return tradeTypeName;
	}

	public void setTradeTypeName(String tradeTypeName) {
		this.tradeTypeName = tradeTypeName;
	}

	public String getHandleResultUrl() {
		return handleResultUrl;
	}

	public void setHandleResultUrl(String handleResultUrl) {
		this.handleResultUrl = handleResultUrl;
	}

	public String getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}

	public Integer getCreationSource() {
		return creationSource;
	}

	public void setCreationSource(Integer creationSource) {
		this.creationSource = creationSource;
	}

	public Long getCheckSheetId() {
		return checkSheetId;
	}

	public void setCheckSheetId(Long checkSheetId) {
		this.checkSheetId = checkSheetId;
	}

	public Long getUpStreamId() {
		return upStreamId;
	}

	public void setUpStreamId(Long upStreamId) {
		this.upStreamId = upStreamId;
	}

	public Integer getComplete() {
		return complete;
	}

	public void setComplete(Integer complete) {
		this.complete = complete;
	}

}