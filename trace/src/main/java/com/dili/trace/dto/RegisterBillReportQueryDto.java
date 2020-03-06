package com.dili.trace.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.dili.ss.domain.BaseDomain;

public class RegisterBillReportQueryDto extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 登记时间开始
	@DateTimeFormat(iso = ISO.DATE,pattern = "yyyy-MM-dd")
	private LocalDate createdStart;
	// 登记时间结束
	@DateTimeFormat(iso = ISO.DATE,pattern = "yyyy-MM-dd")
	private LocalDate createdEnd;

	// 登记来源(交易区、理货区)
	private Integer registerSource;
	// 交易区编号
	private Long tradeTypeId;
	// 商品名称
	private String productName;
	
	
	private LocalDate momStart;
	private LocalDate momEnd;

	private LocalDate yoyStart;
	private LocalDate yoyEnd;

	



	public LocalDate getCreatedStart() {
		return createdStart;
	}

	public void setCreatedStart(LocalDate createdStart) {
		this.createdStart = createdStart;
	}

	public LocalDate getCreatedEnd() {
		return createdEnd;
	}

	public void setCreatedEnd(LocalDate createdEnd) {
		this.createdEnd = createdEnd;
	}

	public LocalDate getMomStart() {
		return momStart;
	}

	public void setMomStart(LocalDate momStart) {
		this.momStart = momStart;
	}

	public LocalDate getMomEnd() {
		return momEnd;
	}

	public void setMomEnd(LocalDate momEnd) {
		this.momEnd = momEnd;
	}

	public LocalDate getYoyStart() {
		return yoyStart;
	}

	public void setYoyStart(LocalDate yoyStart) {
		this.yoyStart = yoyStart;
	}

	public LocalDate getYoyEnd() {
		return yoyEnd;
	}

	public void setYoyEnd(LocalDate yoyEnd) {
		this.yoyEnd = yoyEnd;
	}

	public Integer getRegisterSource() {
		return registerSource;
	}

	public void setRegisterSource(Integer registerSource) {
		this.registerSource = registerSource;
	}

	public Long getTradeTypeId() {
		return tradeTypeId;
	}

	public void setTradeTypeId(Long tradeTypeId) {
		this.tradeTypeId = tradeTypeId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
