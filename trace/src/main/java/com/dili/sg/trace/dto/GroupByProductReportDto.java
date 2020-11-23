package com.dili.sg.trace.dto;

import java.math.BigDecimal;

import com.dili.ss.domain.BaseDomain;

public class GroupByProductReportDto extends BaseDomain{
	
	public GroupByProductReportDto() {
		super();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* 商品名称 */
	private String productName;
	/* 商品ID */
	private Long productId;
	/* 车牌	 */
	private String plate;
	
	/* 姓名 */
	private String name;
	
	private String originName;
	
	/* 运输重量 */
	private BigDecimal totalWeight=BigDecimal.ZERO;
	private BigDecimal momWeight;
	private BigDecimal yoyWeight;

	/* 进场次数 */
	private Long cnt=0L;
	private BigDecimal momCnt;
	private BigDecimal yoyCnt;

	/* 采样客户数量 */
	private Long totalCheckingOrRechecking=0L;
	private BigDecimal momCheckingOrRechecking;
	private BigDecimal yoyCheckingOrRechecking;

	/* 检测数量 */
	private Long totalCheckedOrRechecked=0L;
	private BigDecimal momCheckedOrRechecked;
	private BigDecimal yoyCheckedOrRechecked;

	/* 复检数量 */
	private Long totalRechecked=0L;
	private BigDecimal momRechecked;
	private BigDecimal yoyRechecked;
	
	/*已经初检成功数量*/
	
	private Long totalSuccessfulInitalChecked=0L;
	/*已经复检成功数量*/
	private Long totalSuccessfulRechecked=0L;
	/* 初检合格率 */
	private BigDecimal successfulInitalCheckedRate;

	/* 复检合格率 */
	private BigDecimal successfulRecheckedRate;

	/* 产地证明 */
	private Long totalHascertifiy=0L;
	/* 产地占进场占比 */
	private BigDecimal hascertifiyRate;

	private BigDecimal momHascertifiy;
	private BigDecimal yoyHascertifiy;

	/* 检测报告 */
	private Long totalHasdetectreport=0L;/* 有检测报告数量 */

	/* 检测报告占进场占比 */
	private BigDecimal hasdetectreportRate;
	private BigDecimal momHasdetectreport;
	private BigDecimal yoyHasdetectreport;
	/* 检测占进场占比 */
	private BigDecimal checkingOrRecheckingRate;
	
	
	
	
	private String label;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public String getOriginName() {
		return originName;
	}
	public void setOriginName(String originName) {
		this.originName = originName;
	}
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getTotalSuccessfulInitalChecked() {
		return totalSuccessfulInitalChecked;
	}
	public void setTotalSuccessfulInitalChecked(Long totalSuccessfulInitalChecked) {
		this.totalSuccessfulInitalChecked = totalSuccessfulInitalChecked;
	}
	public Long getTotalSuccessfulRechecked() {
		return totalSuccessfulRechecked;
	}
	public void setTotalSuccessfulRechecked(Long totalSuccessfulRechecked) {
		this.totalSuccessfulRechecked = totalSuccessfulRechecked;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	public BigDecimal getMomWeight() {
		return momWeight;
	}
	public void setMomWeight(BigDecimal momWeight) {
		this.momWeight = momWeight;
	}
	public BigDecimal getYoyWeight() {
		return yoyWeight;
	}
	public void setYoyWeight(BigDecimal yoyWeight) {
		this.yoyWeight = yoyWeight;
	}
	public Long getCnt() {
		return cnt;
	}
	public void setCnt(Long cnt) {
		this.cnt = cnt;
	}
	public BigDecimal getMomCnt() {
		return momCnt;
	}
	public void setMomCnt(BigDecimal momCnt) {
		this.momCnt = momCnt;
	}
	public BigDecimal getYoyCnt() {
		return yoyCnt;
	}
	public void setYoyCnt(BigDecimal yoyCnt) {
		this.yoyCnt = yoyCnt;
	}
	public Long getTotalCheckingOrRechecking() {
		return totalCheckingOrRechecking;
	}
	public void setTotalCheckingOrRechecking(Long totalCheckingOrRechecking) {
		this.totalCheckingOrRechecking = totalCheckingOrRechecking;
	}
	public BigDecimal getMomCheckingOrRechecking() {
		return momCheckingOrRechecking;
	}
	public void setMomCheckingOrRechecking(BigDecimal momCheckingOrRechecking) {
		this.momCheckingOrRechecking = momCheckingOrRechecking;
	}
	public BigDecimal getYoyCheckingOrRechecking() {
		return yoyCheckingOrRechecking;
	}
	public void setYoyCheckingOrRechecking(BigDecimal yoyCheckingOrRechecking) {
		this.yoyCheckingOrRechecking = yoyCheckingOrRechecking;
	}
	public Long getTotalCheckedOrRechecked() {
		return totalCheckedOrRechecked;
	}
	public void setTotalCheckedOrRechecked(Long totalCheckedOrRechecked) {
		this.totalCheckedOrRechecked = totalCheckedOrRechecked;
	}
	public BigDecimal getMomCheckedOrRechecked() {
		return momCheckedOrRechecked;
	}
	public void setMomCheckedOrRechecked(BigDecimal momCheckedOrRechecked) {
		this.momCheckedOrRechecked = momCheckedOrRechecked;
	}
	public BigDecimal getYoyCheckedOrRechecked() {
		return yoyCheckedOrRechecked;
	}
	public void setYoyCheckedOrRechecked(BigDecimal yoyCheckedOrRechecked) {
		this.yoyCheckedOrRechecked = yoyCheckedOrRechecked;
	}
	public Long getTotalRechecked() {
		return totalRechecked;
	}
	public void setTotalRechecked(Long totalRechecked) {
		this.totalRechecked = totalRechecked;
	}
	public BigDecimal getMomRechecked() {
		return momRechecked;
	}
	public void setMomRechecked(BigDecimal momRechecked) {
		this.momRechecked = momRechecked;
	}
	public BigDecimal getYoyRechecked() {
		return yoyRechecked;
	}
	public void setYoyRechecked(BigDecimal yoyRechecked) {
		this.yoyRechecked = yoyRechecked;
	}
	public BigDecimal getSuccessfulInitalCheckedRate() {
		return successfulInitalCheckedRate;
	}
	public void setSuccessfulInitalCheckedRate(BigDecimal successfulInitalCheckedRate) {
		this.successfulInitalCheckedRate = successfulInitalCheckedRate;
	}
	public BigDecimal getSuccessfulRecheckedRate() {
		return successfulRecheckedRate;
	}
	public void setSuccessfulRecheckedRate(BigDecimal successfulRecheckedRate) {
		this.successfulRecheckedRate = successfulRecheckedRate;
	}
	public Long getTotalHascertifiy() {
		return totalHascertifiy;
	}
	public void setTotalHascertifiy(Long totalHascertifiy) {
		this.totalHascertifiy = totalHascertifiy;
	}
	public BigDecimal getHascertifiyRate() {
		return hascertifiyRate;
	}
	public void setHascertifiyRate(BigDecimal hascertifiyRate) {
		this.hascertifiyRate = hascertifiyRate;
	}
	public BigDecimal getMomHascertifiy() {
		return momHascertifiy;
	}
	public void setMomHascertifiy(BigDecimal momHascertifiy) {
		this.momHascertifiy = momHascertifiy;
	}
	public BigDecimal getYoyHascertifiy() {
		return yoyHascertifiy;
	}
	public void setYoyHascertifiy(BigDecimal yoyHascertifiy) {
		this.yoyHascertifiy = yoyHascertifiy;
	}
	public Long getTotalHasdetectreport() {
		return totalHasdetectreport;
	}
	public void setTotalHasdetectreport(Long totalHasdetectreport) {
		this.totalHasdetectreport = totalHasdetectreport;
	}
	public BigDecimal getHasdetectreportRate() {
		return hasdetectreportRate;
	}
	public void setHasdetectreportRate(BigDecimal hasdetectreportRate) {
		this.hasdetectreportRate = hasdetectreportRate;
	}
	public BigDecimal getMomHasdetectreport() {
		return momHasdetectreport;
	}
	public void setMomHasdetectreport(BigDecimal momHasdetectreport) {
		this.momHasdetectreport = momHasdetectreport;
	}
	public BigDecimal getYoyHasdetectreport() {
		return yoyHasdetectreport;
	}
	public void setYoyHasdetectreport(BigDecimal yoyHasdetectreport) {
		this.yoyHasdetectreport = yoyHasdetectreport;
	}
	public BigDecimal getCheckingOrRecheckingRate() {
		return checkingOrRecheckingRate;
	}
	public void setCheckingOrRecheckingRate(BigDecimal checkingOrRecheckingRate) {
		this.checkingOrRecheckingRate = checkingOrRecheckingRate;
	}
	
}
