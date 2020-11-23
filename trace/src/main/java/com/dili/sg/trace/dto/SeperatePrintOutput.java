package com.dili.trace.dto;

import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeperatePrintReport;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public class SeperatePrintOutput {
	//企业名称
	private String corporateName;
	//编号
	private String code;

	//审核人
	private String approverBase64Sign;

	private String base64Qrcode;

	//送检人
	public String userName;

	//有效期
	private Integer validPeriod;

	//检测人
	private String detectOperatorName;

	//操作人姓名
	private String operatorName;

	//是否显示别名
	private Boolean showProductAlias = Boolean.FALSE;

	private List<SeperatePrintReportOutputDto> data;

	public Integer getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(Integer validPeriod) {
		this.validPeriod = validPeriod;
	}

	public static SeperatePrintOutput build(List<SeperatePrintReportOutputDto>data) {
		SeperatePrintReportOutputDto output=StreamEx.ofNullable(data).nonNull().flatCollection(Function.identity()).nonNull().findFirst().orElse(null);

		SeperatePrintOutput dto = new SeperatePrintOutput();
		dto.setData(data);
		dto.setApproverBase64Sign(output.getApproverBase64Sign());
		dto.setBase64Qrcode(output.getBase64Qrcode());
		dto.setCode(output.getCode());
		dto.setCorporateName(output.getCorporateName());
		dto.setUserName(output.getUserName());
		dto.setValidPeriod(output.getValidPeriod());

		dto.showProductAlias=StreamEx.ofNullable(data).nonNull().flatCollection(Function.identity()).nonNull().anyMatch(item-> StringUtils.isNotBlank(item.getProductAliasName()));
		return dto;

	}

	public String getCorporateName() {
		return corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDetectOperatorName() {
		return detectOperatorName;
	}

	public void setDetectOperatorName(String detectOperatorName) {
		this.detectOperatorName = detectOperatorName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Boolean getShowProductAlias() {
		return showProductAlias;
	}

	public void setShowProductAlias(Boolean showProductAlias) {
		this.showProductAlias = showProductAlias;
	}

	public List<SeperatePrintReportOutputDto> getData() {
		return data;
	}

	public void setData(List<SeperatePrintReportOutputDto> data) {
		this.data = data;
	}
}
