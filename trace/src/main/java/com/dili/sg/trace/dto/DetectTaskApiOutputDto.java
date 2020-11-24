package com.dili.sg.trace.dto;

import com.dili.ss.domain.BaseDomain;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.dili.trace.domain.RegisterBill;
import org.apache.commons.lang3.StringUtils;

public class DetectTaskApiOutputDto extends BaseDomain {

    /**
     * 样品编号
     */
    private String code;
    /**
     * 获取任务的检测仪器编码
     */
    private String exeMachineNo;
    /**
     * 任务创建时间
     */
    private String created;
    /**
     * 交易卡号码（可能为空）
     */
    private String tradeAccount;
    /**
     * 被检单位
     */
    private String name;
    /**
     * 样品来源
     */
    private String orininName;
    /**
     * 车牌
     */
    private String plate;
    /**
     * 样品名称
     */
    private String productName;
    /**
     * 市场理货区号
     */
    private String tallyAreaNo;
    /**
     * 商品重量/KG
     */
    private Integer weight;

    private Long checkSheetId;

    private Integer detectState;

    private Integer state;

    private String latestPdResult;

    private String originName;

    private String latestDetectOperator;

    public static List<DetectTaskApiOutputDto> build(List<RegisterBill> billList) {
        return billList.stream().map(DetectTaskApiOutputDto::build).collect(Collectors.toList());
    }

    public static DetectTaskApiOutputDto build(RegisterBill bill) {

        DetectTaskApiOutputDto output = new DetectTaskApiOutputDto();
        output.setId(bill.getId());
        // 对code,samplecode进行互换(检测客户端程序那边他们懒得改,不要动这行代码)
        output.setCode(bill.getSampleCode());
        output.setCreated(bill.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        output.setExeMachineNo(bill.getExeMachineNo());
        output.setName(bill.getName());
        if(StringUtils.isBlank(output.getName())) {
        	output.setName(bill.getCorporateName());
        }
        if(StringUtils.isBlank(output.getName())) {
        	output.setName(bill.getCode());
        }
        output.setOrininName(bill.getOriginName());
        output.setPlate(bill.getPlate());
        output.setProductName(bill.getProductName());
        output.setTallyAreaNo(bill.getTallyAreaNo());
        output.setTradeAccount(bill.getTradeAccount());
        output.setWeight(bill.getWeight());
        return output;
    }

    /**
     * @return String return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return String return the exeMachineNo
     */
    public String getExeMachineNo() {
        return exeMachineNo;
    }

    /**
     * @param exeMachineNo the exeMachineNo to set
     */
    public void setExeMachineNo(String exeMachineNo) {
        this.exeMachineNo = exeMachineNo;
    }

    /**
     * @return String return the created
     */
    public String getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * @return String return the tradeAccount
     */
    public String getTradeAccount() {
        return tradeAccount;
    }

    /**
     * @param tradeAccount the tradeAccount to set
     */
    public void setTradeAccount(String tradeAccount) {
        this.tradeAccount = tradeAccount;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the orininName
     */
    public String getOrininName() {
        return orininName;
    }

    /**
     * @param orininName the orininName to set
     */
    public void setOrininName(String orininName) {
        this.orininName = orininName;
    }

    /**
     * @return String return the plate
     */
    public String getPlate() {
        return plate;
    }

    /**
     * @param plate the plate to set
     */
    public void setPlate(String plate) {
        this.plate = plate;
    }

    /**
     * @return String return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
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
     * @return Long return the weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * @return Long return the checkSheetId
     */
    public Long getCheckSheetId() {
        return checkSheetId;
    }

    /**
     * @param checkSheetId the checkSheetId to set
     */
    public void setCheckSheetId(Long checkSheetId) {
        this.checkSheetId = checkSheetId;
    }

    /**
     * @return Integer return the detectState
     */
    public Integer getDetectState() {
        return detectState;
    }

    /**
     * @param detectState the detectState to set
     */
    public void setDetectState(Integer detectState) {
        this.detectState = detectState;
    }

    /**
     * @return Integer return the state
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return String return the latestPdResult
     */
    public String getLatestPdResult() {
        return latestPdResult;
    }

    /**
     * @param latestPdResult the latestPdResult to set
     */
    public void setLatestPdResult(String latestPdResult) {
        this.latestPdResult = latestPdResult;
    }

    /**
     * @return String return the originName
     */
    public String getOriginName() {
        return originName;
    }

    /**
     * @param originName the originName to set
     */
    public void setOriginName(String originName) {
        this.originName = originName;
    }

    /**
     * @return String return the latestDetectOperator
     */
    public String getLatestDetectOperator() {
        return latestDetectOperator;
    }

    /**
     * @param latestDetectOperator the latestDetectOperator to set
     */
    public void setLatestDetectOperator(String latestDetectOperator) {
        this.latestDetectOperator = latestDetectOperator;
    }

}
