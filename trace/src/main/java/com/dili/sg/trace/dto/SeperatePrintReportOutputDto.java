package com.dili.sg.trace.dto;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.dili.sg.trace.domain.SeperatePrintReport;
import com.dili.trace.domain.RegisterBill;

public class SeperatePrintReportOutputDto {

	private String productName;

	private String originName;


	public String userName;

	private String corporateName;

	private String operatorName;

	private String code;

	private String created;

	private Integer validPeriod;

	private String remark;
	private String approverBase64Sign;
	private String base64Qrcode;
	
	private String productAliasName;

	private BigDecimal salesWeight;

	private String salesPlate;

	public String salesUserName;;
	
    private String tallyAreaNo;

    private String latestPdResult;

    private String detectStateView;
    
    private String detectOperatorName;
    
	public static SeperatePrintReportOutputDto build(SeperatePrintReport seperatePrintReport, RegisterBill bill, String approverBase64Sign, String base64Qrcode) {
		SeperatePrintReportOutputDto dto = new SeperatePrintReportOutputDto();
		dto.setProductName(bill.getProductName());
		dto.setUserName(bill.getName());
		dto.setCorporateName(bill.getCorporateName());
		dto.setOriginName(bill.getOriginName());
		
		dto.setProductAliasName(seperatePrintReport.getProductAliasName());
		dto.setSalesWeight(seperatePrintReport.getSalesWeight());
		dto.setSalesPlate(seperatePrintReport.getSalesPlate());
		dto.setSalesUserName(seperatePrintReport.getSalesUserName());
		dto.setCode(seperatePrintReport.getCode());
		dto.setTallyAreaNo(seperatePrintReport.getTallyAreaNo());
		dto.setApproverBase64Sign(approverBase64Sign);
		dto.setBase64Qrcode(base64Qrcode);
		dto.setLatestPdResult(bill.getLatestPdResult());
		dto.setDetectStateView(bill.getDetectStateName());
		dto.setValidPeriod(seperatePrintReport.getValidPeriod());
		dto.setCreated(seperatePrintReport.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
		dto.setDetectOperatorName(bill.getLatestDetectOperator());
		return dto;

	}
	
	public String getDetectOperatorName() {
		return detectOperatorName;
	}

	public void setDetectOperatorName(String detectOperatorName) {
		this.detectOperatorName = detectOperatorName;
	}

	public String getLatestPdResult() {
		return latestPdResult;
	}

	public void setLatestPdResult(String latestPdResult) {
		this.latestPdResult = latestPdResult;
	}

	public String getDetectStateView() {
		return detectStateView;
	}

	public void setDetectStateView(String detectStateView) {
		this.detectStateView = detectStateView;
	}

	public String getTallyAreaNo() {
		return tallyAreaNo;
	}

	public void setTallyAreaNo(String tallyAreaNo) {
		this.tallyAreaNo = tallyAreaNo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}
	public String getCorporateName() {
		return corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductAliasName() {
		return productAliasName;
	}

	public void setProductAliasName(String productAliasName) {
		this.productAliasName = productAliasName;
	}

	public BigDecimal getSalesWeight() {
		return salesWeight;
	}

	public void setSalesWeight(BigDecimal salesWeight) {
		this.salesWeight = salesWeight;
	}

	public String getSalesPlate() {
		return salesPlate;
	}

	public void setSalesPlate(String salesPlate) {
		this.salesPlate = salesPlate;
	}

	

	public String getSalesUserName() {
		return salesUserName;
	}

	public void setSalesUserName(String salesUserName) {
		this.salesUserName = salesUserName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Integer getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(Integer validPeriod) {
		this.validPeriod = validPeriod;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getApproverBase64Sign() {
		return approverBase64Sign;
	}

	public void setApproverBase64Sign(String approverBase64Sign) {
		this.approverBase64Sign = approverBase64Sign;
	}

	public String getBase64Qrcode() {
		return base64Qrcode;
	}

	public void setBase64Qrcode(String base64Qrcode) {
		this.base64Qrcode = base64Qrcode;
	}

}
