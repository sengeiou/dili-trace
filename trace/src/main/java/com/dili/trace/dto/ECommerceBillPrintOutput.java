package com.dili.trace.dto;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.dili.trace.domain.RegisterBill;
import org.apache.commons.lang3.StringUtils;

public class ECommerceBillPrintOutput {

	// 编号
	private String code;
	// 商品名称
	private String productName;
	// 产地
	private String originName;
	// 检测时间
	private String latestDetectTime;
	// 检测机构
	private String latestDetectOperator;
	// 二维码
	private String base64Qrcode;
	//客服电话
	private String customerServiceTel;

	public static ECommerceBillPrintOutput build(RegisterBill bill, String base64Qrcode) {

		ECommerceBillPrintOutput out = new ECommerceBillPrintOutput();
		out.setCode(bill.getCode());
		out.setProductName(bill.getProductName());
		out.setOriginName(bill.getOriginName());
		Date latestDetectTime = bill.getCreated();
		out.setLatestDetectTime(latestDetectTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
				.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));

		out.setLatestDetectOperator(StringUtils.trimToEmpty(bill.getLatestDetectOperator()));
		out.setBase64Qrcode(base64Qrcode);
		out.setLatestDetectOperator("山东格林检测股份有限公司");
		out.setCustomerServiceTel("0536-2230893");
		return out;

	}

	public String getCustomerServiceTel() {
		return customerServiceTel;
	}

	public void setCustomerServiceTel(String customerServiceTel) {
		this.customerServiceTel = customerServiceTel;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getLatestDetectTime() {
		return latestDetectTime;
	}

	public void setLatestDetectTime(String latestDetectTime) {
		this.latestDetectTime = latestDetectTime;
	}

	public String getLatestDetectOperator() {
		return latestDetectOperator;
	}

	public void setLatestDetectOperator(String latestDetectOperator) {
		this.latestDetectOperator = latestDetectOperator;
	}

	public String getBase64Qrcode() {
		return base64Qrcode;
	}

	public void setBase64Qrcode(String base64Qrcode) {
		this.base64Qrcode = base64Qrcode;
	}

}