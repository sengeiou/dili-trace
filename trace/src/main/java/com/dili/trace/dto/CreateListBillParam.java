package com.dili.trace.dto;

import java.util.List;

import com.dili.trace.api.input.CreateRegisterBillInputDto;

public class CreateListBillParam {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 市场id
     */
    private Long marketId;
    /**
     * 登记来源
     */
    private Integer registerSource;
    /**
     * 理货区编号
     */
    private String tallyAreaNo;
    /**
     * 交易类型id
     */
    private String tradeTypeId;
    /**
     * 交易类型描述
     */
    private String tradeTypeName;

    /**
     * 车牌
     */
    private String plate;

    /**
     * 交易账号
     */
    private String tradeAccount;

    /**
     * 交易印卡
     */
    private String tradePrintingCard;

    /**
     * 名称
     */
    private String name;

    /**
     * 身份证
     */
    private String idCardNo;

    /**
     * 地址
     */
    private String addr;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 检测报告url
     */
    private String detectReportUrl;

    private String corporateName;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private List<CreateRegisterBillInputDto> registerBills;

    public List<CreateRegisterBillInputDto> getRegisterBills() {
        return registerBills;
    }

    public void setRegisterBills(List<CreateRegisterBillInputDto> registerBills) {
        this.registerBills = registerBills;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
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

    public String getTradeTypeId() {
        return tradeTypeId;
    }

    public void setTradeTypeId(String tradeTypeId) {
        this.tradeTypeId = tradeTypeId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
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

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }
}
