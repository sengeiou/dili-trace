package com.dili.trace.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.trace.enums.BillTypeEnum;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.ImageCertTypeEnum;
import com.dili.trace.enums.WeightUnitEnum;
import com.dili.trace.glossary.BillDetectStateEnum;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.TFEnum;
import io.swagger.annotations.ApiModelProperty;
import one.util.streamex.StreamEx;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 由MyBatis Generator工具自动生成
 * <p>
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

    @ApiModelProperty(value = "用户iD")
    @Column(name = "`user_id`")
    private Long userId;
    @ApiModelProperty(value = "摊位号")
    @Column(name = "`tally_area_no`")
    private String tallyAreaNo;

    @ApiModelProperty(value = "车牌")
    @Column(name = "`plate`")
    private String plate;

    @ApiModelProperty(value = "1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中")
    @Column(name = "`state`")
    private Integer state;

    @ApiModelProperty(value = "1:采样检测 2:主动送检")
    @Column(name = "`sample_source`")
    private Integer sampleSource;

    @ApiModelProperty(value = "商品名称")
    @Column(name = "`product_name`")
    private String productName;

    @ApiModelProperty(value = "商品ID")
    @Column(name = "`product_id`")
    private Long productId;

    @ApiModelProperty(value = "产地ID")
    @Column(name = "`origin_id`")
    private Long originId;

    @ApiModelProperty(value = "产地名")
    @Column(name = "`origin_name`")
    private String originName;

    @ApiModelProperty(value = "重量KG")
    @Column(name = "`weight`")
    private BigDecimal weight;

    @ApiModelProperty(value = "重量单位")
    @Column(name = "`weight_unit`")
    private Integer weightUnit;

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

    @Column(name = "`operation_time`")
    private Date operationTime;

    @ApiModelProperty(value = "上游信息ID")
    @Column(name = "`upstream_id`")
    private Long upStreamId;

    @Transient
    private String upStreamName;

    @ApiModelProperty(value = "数据是否完整")
    @Column(name = "`complete`")
    private Integer complete;

    @ApiModelProperty(value = "查验状态值")
    @Column(name = "`verify_status`")
    private Integer verifyStatus;

    @ApiModelProperty(value = "保存类型")
    @Column(name = "`preserve_type`")
    private Integer preserveType;


    @ApiModelProperty(value = "查验类型")
    @Column(name = "`verify_type`")
    private Integer verifyType;

    @ApiModelProperty(value = "规格名称")
    @Column(name = "`spec_name`")
    private String specName;

    @ApiModelProperty(value = "报备类型")
    @Column(name = "`bill_type`")
    private Integer billType;
    @ApiModelProperty(value = "拼车类型")
    @Column(name = "`truck_type`")
    private Integer truckType;

    @ApiModelProperty(value = "品牌名称")
    @Column(name = "`brand_name`")
    private String brandName;

    @ApiModelProperty(value = "品牌ID")
    @Column(name = "`brand_id`")
    private Long brandId;

    @ApiModelProperty(value = "是否进门")
    @Column(name = "`is_checkin`")
    private Integer isCheckin;

    @ApiModelProperty(value = "原因")
    @Column(name = "`reason`")
    private String reason;

    @ApiModelProperty(value = "是否被删除")
    @Column(name = "`is_deleted`")
    private Integer isDeleted;

    @ApiModelProperty(value = "市场ID")
    @Column(name = "market_id")
    private Long marketId;

    @ApiModelProperty(value = "主台账编号")
    @Column(name = "register_head_code")
    private String registerHeadCode;

    @ApiModelProperty(value = "经营户卡号")
    @Column(name = "`third_party_code`")
    private String thirdPartyCode;

    @ApiModelProperty(value = "区号")
    @Column(name = "`area`")
    private String area;

    @ApiModelProperty(value = "计量类型。10-计件 20-计重。默认计件。")
    @Column(name = "`measure_type`")
    private Integer measureType;

    @ApiModelProperty(value = "件数")
    @Column(name = "`piece_num`")
    private BigDecimal pieceNum;

    @ApiModelProperty(value = "件重")
    @Column(name = "`piece_weight`")
    private BigDecimal pieceWeight;

    @ApiModelProperty(value = "备注")
    @Column(name = "`remark`")
    private String remark;

    @ApiModelProperty(value = "创建人")
    @Column(name = "`create_user`")
    private String createUser;

    @ApiModelProperty(value = "作废人")
    @Column(name = "`delete_user`")
    private String deleteUser;

    @ApiModelProperty(value = "作废时间")
    @Column(name = "`delete_time`")
    private Date deleteTime;

    @ApiModelProperty(value = "包装")
    @Column(name = "`packaging`")
    private String packaging;

    @ApiModelProperty(value = "订单类型 1.报备单 2.进门登记单")
    @Column(name = "`order_type`")
    private Integer orderType;

    @ApiModelProperty(value = "1.分销 2.全销")
    @Column(name = "`sales_type`")
    private Integer salesType;

    @Transient
    private List<ImageCert> imageCerts;

    @Transient
    private String tradeRequestCode;

    @Transient
    private List<TradePushLog> tradePushLogs;

    @Transient
    private BigDecimal headWeight;

    @Transient
    private BigDecimal remainWeight;

    @Transient
    private String orderTypeName;

    @Transient
    private String truckTypeName;

    public Integer getSalesType() {
        return salesType;
    }
    public Map<Integer,List<ImageCert>> getGroupedImageCertList(){
          return StreamEx.ofNullable(this.imageCerts).flatCollection(Function.identity())
                .mapToEntry(item-> item.getCertType(), Function.identity())
                .grouping();
    }

    public void setSalesType(Integer salesType) {
        this.salesType = salesType;
    }

    /**
     * 以下字段为市场合并字段
     */
    @ApiModelProperty(value = "1.理货区 2.交易区")
    @Column(name = "`register_source`")
    private Integer registerSource;

    @ApiModelProperty(value = "交易账号")
    @Column(name = "`trade_account`")
    private String tradeAccount;

    @ApiModelProperty(value = "印刷卡号")
    @Column(name = "`trade_printing_card`")
    private String tradePrintingCard;

    @Column(name = "`trade_type_id`")
    String tradeTypeId;

    @Column(name = "`trade_type_name`")
    private String tradeTypeName;

    @ApiModelProperty(value = "检测报告url")
    @Column(name = "`detect_report_url`")
    private String detectReportUrl;

    @ApiModelProperty(value = "产地证明图片")
    @Column(name = "`origin_certifiy_url`")
    private String originCertifiyUrl;

    @ApiModelProperty(value = "仪器编号")
    @Column(name = "`exe_machine_no`")
    String exeMachineNo;

    @ApiModelProperty(value = "处理结果图片URL")
    @Column(name = "`handle_result_url`")
    private String handleResultUrl;

    @ApiModelProperty(value = "处理结果备注")
    @Column(name = "`handle_result`")
    private String handleResult;

    @ApiModelProperty(value = "数据创建来源")
    @Column(name = "`creation_source`")
    Integer creationSource;

    @ApiModelProperty(value = "检测报告单ID")
    @Column(name = "`check_sheet_id`")
    private Long checkSheetId;

    @ApiModelProperty(value = "企业名称")
    @Column(name = "`corporate_name`")
    private String corporateName;

    @ApiModelProperty(value = "商品别名")
    @Column(name = "`product_alias_name`")
    private String productAliasName;

    public String getTruckTypeName() {
        return truckTypeName;
    }

    public void setTruckTypeName(String truckTypeName) {
        this.truckTypeName = truckTypeName;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public List<TradePushLog> getTradePushLogs() {
        return tradePushLogs;
    }

    public void setTradePushLogs(List<TradePushLog> tradePushLogs) {
        this.tradePushLogs = tradePushLogs;
    }

    public String getTradeRequestCode() {
        return tradeRequestCode;
    }

    public void setTradeRequestCode(String tradeRequestCode) {
        this.tradeRequestCode = tradeRequestCode;
    }

    public List<ImageCert> getImageCerts() {
        return imageCerts;
    }

    public void setImageCerts(List<ImageCert> imageCerts) {
        this.imageCerts = imageCerts;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    @Transient
    public String getFormattedWeight() {
        if (this.getWeight() == null) {
            return "0";
        }
        return this.getWeight().stripTrailingZeros().toPlainString();
        // return this.getWeight().
        // return BillVerifyStatusEnum.fromCode(this.getVerifyStatus()).map(BillVerifyStatusEnum::getName).orElse("");
    }

    @Transient
    public String getVerifyStatusName() {
        return BillVerifyStatusEnum.fromCode(this.getVerifyStatus()).map(BillVerifyStatusEnum::getName).orElse("");
    }

    @Transient
    public String getWeightUnitName() {
        return WeightUnitEnum.fromCode(this.getWeightUnit()).map(WeightUnitEnum::getName).orElse("");
    }

    @Transient
    public String getBillTypeName() {
        return BillTypeEnum.fromCode(this.getBillType()).map(BillTypeEnum::getName).orElse("");
    }

    @Transient
    public Long getBillId() {
        return this.getId();
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

    public Integer getSampleSource() {
        return sampleSource;
    }

    public void setSampleSource(Integer sampleSource) {
        this.sampleSource = sampleSource;
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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(Integer weightUnit) {
        this.weightUnit = weightUnit;
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

    /**
     * @return Integer return the preserveType
     */
    public Integer getPreserveType() {
        return preserveType;
    }

    /**
     * @param preserveType the preserveType to set
     */
    public void setPreserveType(Integer preserveType) {
        this.preserveType = preserveType;
    }

    /**
     * @return Integer return the verifyType
     */
    public Integer getVerifyType() {
        return verifyType;
    }

    /**
     * @param verifyType the verifyType to set
     */
    public void setVerifyType(Integer verifyType) {
        this.verifyType = verifyType;
    }

    /**
     * @return String return the specName
     */
    public String getSpecName() {
        return specName;
    }

    /**
     * @param specName the specName to set
     */
    public void setSpecName(String specName) {
        this.specName = specName;
    }

    /**
     * @return Integer return the billType
     */
    public Integer getBillType() {
        return billType;
    }

    /**
     * @param billType the billType to set
     */
    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    /**
     * @return Long return the brandId
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * @param brandId the brandId to set
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * @return String return the brandName
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * @param brandName the brandName to set
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * @return Integer return the truckType
     */
    public Integer getTruckType() {
        return truckType;
    }

    /**
     * @param truckType the truckType to set
     */
    public void setTruckType(Integer truckType) {
        this.truckType = truckType;
    }

    /**
     * @return String return the tallyAreaNo
     */
    public String getTallyAreaNo() {
        return tallyAreaNo;
    }

    /**
     * @param tallyAreaNo the tallyAreaNo to set
     */
    public void setTallyAreaNo(String tallyAreaNo) {
        this.tallyAreaNo = tallyAreaNo;
    }

    /**
     * @return String return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }


    /**
     * @return Integer return the isCheckin
     */
    public Integer getIsCheckin() {
        return isCheckin;
    }

    /**
     * @param isCheckin the isCheckin to set
     */
    public void setIsCheckin(Integer isCheckin) {
        this.isCheckin = isCheckin;
    }

    /**
     * @return Integer return the isDeleted
     */
    @Transient
    public String getIsDeletedName() {
        return TFEnum.fromCode(this.getIsDeleted()).map(TFEnum::getDesc).orElse("");
    }

    /**
     * @return Integer return the isDeleted
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }


    /**
     * @return Date return the operationTime
     */
    public Date getOperationTime() {
        return operationTime;
    }

    /**
     * @param operationTime the operationTime to set
     */
    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getUpStreamName() {
        return upStreamName;
    }

    public void setUpStreamName(String upStreamName) {
        this.upStreamName = upStreamName;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getRegisterHeadCode() {
        return registerHeadCode;
    }

    public void setRegisterHeadCode(String registerHeadCode) {
        this.registerHeadCode = registerHeadCode;
    }

    public String getThirdPartyCode() {
        return thirdPartyCode;
    }

    public void setThirdPartyCode(String thirdPartyCode) {
        this.thirdPartyCode = thirdPartyCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getMeasureType() {
        return measureType;
    }

    public void setMeasureType(Integer measureType) {
        this.measureType = measureType;
    }

    public BigDecimal getPieceNum() {
        return pieceNum;
    }

    public void setPieceNum(BigDecimal pieceNum) {
        this.pieceNum = pieceNum;
    }

    public BigDecimal getPieceWeight() {
        return pieceWeight;
    }

    public void setPieceWeight(BigDecimal pieceWeight) {
        this.pieceWeight = pieceWeight;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(String deleteUser) {
        this.deleteUser = deleteUser;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getHeadWeight() {
        return headWeight;
    }

    public void setHeadWeight(BigDecimal headWeight) {
        this.headWeight = headWeight;
    }

    public BigDecimal getRemainWeight() {
        return remainWeight;
    }

    public void setRemainWeight(BigDecimal remainWeight) {
        this.remainWeight = remainWeight;
    }

    public Integer getRegisterSource() {
        return registerSource;
    }

    public void setRegisterSource(Integer registerSource) {
        this.registerSource = registerSource;
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

    public String getDetectReportUrl() {
        return detectReportUrl;
    }

    public void setDetectReportUrl(String detectReportUrl) {
        this.detectReportUrl = detectReportUrl;
    }

    public String getOriginCertifiyUrl() {
        return originCertifiyUrl;
    }

    public void setOriginCertifiyUrl(String originCertifiyUrl) {
        this.originCertifiyUrl = originCertifiyUrl;
    }

    public String getExeMachineNo() {
        return exeMachineNo;
    }

    public void setExeMachineNo(String exeMachineNo) {
        this.exeMachineNo = exeMachineNo;
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

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getProductAliasName() {
        return productAliasName;
    }

    public void setProductAliasName(String productAliasName) {
        this.productAliasName = productAliasName;
    }

}