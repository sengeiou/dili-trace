package com.dili.trace.dto;

public class RegisterBillStaticsDto {
	
	//有产地证明、有检测报告、初检合格、初检不合格、复检合格、复检不合格、主动送检
	
//	/有产地证明
	private Integer hasOriginCertifiyNum=0;
	//有检测报告
 	private Integer hasDetectReportNum=0;
	
 	//初检合格
   	private Integer passNum=0;
   	//初检不合格
   	private Integer nopassNum=0;
   	//复检合格
   	private Integer checkNum=0;
   	//复检不合格
   	private Integer recheckNum=0;
   	
   	//采样检测
    private Integer sampleCheckNum=0;
    //主动送检
    private Integer autoCheckNum=0;
	public Integer getHasOriginCertifiyNum() {
		return hasOriginCertifiyNum;
	}
	public void setHasOriginCertifiyNum(Integer hasOriginCertifiyNum) {
		this.hasOriginCertifiyNum = hasOriginCertifiyNum;
	}
	public Integer getHasDetectReportNum() {
		return hasDetectReportNum;
	}
	public void setHasDetectReportNum(Integer hasDetectReportNum) {
		this.hasDetectReportNum = hasDetectReportNum;
	}
	public Integer getPassNum() {
		return passNum;
	}
	public void setPassNum(Integer passNum) {
		this.passNum = passNum;
	}
	public Integer getNopassNum() {
		return nopassNum;
	}
	public void setNopassNum(Integer nopassNum) {
		this.nopassNum = nopassNum;
	}
	public Integer getCheckNum() {
		return checkNum;
	}
	public void setCheckNum(Integer checkNum) {
		this.checkNum = checkNum;
	}
	public Integer getRecheckNum() {
		return recheckNum;
	}
	public void setRecheckNum(Integer recheckNum) {
		this.recheckNum = recheckNum;
	}
	public Integer getSampleCheckNum() {
		return sampleCheckNum;
	}
	public void setSampleCheckNum(Integer sampleCheckNum) {
		this.sampleCheckNum = sampleCheckNum;
	}
	public Integer getAutoCheckNum() {
		return autoCheckNum;
	}
	public void setAutoCheckNum(Integer autoCheckNum) {
		this.autoCheckNum = autoCheckNum;
	}
    
	

}
