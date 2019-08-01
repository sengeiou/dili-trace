package com.dili.trace.etrade.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.QualityTraceTradeBill;
@Table(name = "VTradeBill")
public class VTradeBill extends BaseDomain{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//增量（唯一）
	@Column(name = "billID")
	private Long billID;//	Bigint				
	//交易单号
	@Column(name = "orderId")
	private String orderId;//	Varchar(20)				
	//卖家账号
	@Column(name = "sellerAccount")
	private String sellerAccount;//	varchar(20)				
	//卖家姓名
	@Column(name = "sellerName")
	private String sellerName;//	Varchar(20)					
	//卖家身份证
	@Column(name = "sellerIDNo")
	private String sellerIDNo;//	varchar(20)				
	//买家账号
	@Column(name = "buyerAccount")
	private String buyerAccount;//	varchar(20)				
	//买家姓名
	@Column(name = "buyerName")
	private String buyerName;//	varchar(50)				
	//买家身份证
	@Column(name = "buyerIDNo")
	private String buyerIDNo;//	varchar(20)				
	//开单时间
	@Column(name = "orderCreateDate")
	private Date orderCreateDate;//	datetime				
	//结算时间
	@Column(name = "orderPayDate")
	private Date orderPayDate;//	datetime				
	//总金额（分）
	@Column(name = "totalMoney")
	private BigDecimal totalMoney;//	Money				
	//商品编号
	@Column(name = "productId")
	private String productId;//	varchar(50)				
	//商品名称
	@Column(name = "productName")
	private String productName;//	varchar(50)				
	//目录编号
	@Column(name = "cateId")
	private String cateId;//	varchar(50)				
	//目录名称
	@Column(name = "cateName")
	private String cateName;//	varchar(50)				
	//单价（分）
	@Column(name = "price")
	private BigDecimal price;//	Money				
	//金额（分）
	@Column(name = "amount")
	private BigDecimal amount;//	Money				
	@Column(name = "saleUnit")
	//交易类型
	private Integer saleUnit;//	Tinyint	(1：件 2：重量)
	//省份
	@Column(name = "provinceName")
	private String provinceName;//	varchar(50)				
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSellerAccount() {
		return sellerAccount;
	}
	public void setSellerAccount(String sellerAccount) {
		this.sellerAccount = sellerAccount;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getSellerIDNo() {
		return sellerIDNo;
	}
	public void setSellerIDNo(String sellerIDNo) {
		this.sellerIDNo = sellerIDNo;
	}
	public String getBuyerAccount() {
		return buyerAccount;
	}
	public void setBuyerAccount(String buyerAccount) {
		this.buyerAccount = buyerAccount;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getBuyerIDNo() {
		return buyerIDNo;
	}
	public void setBuyerIDNo(String buyerIDNo) {
		this.buyerIDNo = buyerIDNo;
	}
	public Date getOrderCreateDate() {
		return orderCreateDate;
	}
	public void setOrderCreateDate(Date orderCreateDate) {
		this.orderCreateDate = orderCreateDate;
	}
	public Date getOrderPayDate() {
		return orderPayDate;
	}
	public void setOrderPayDate(Date orderPayDate) {
		this.orderPayDate = orderPayDate;
	}
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCateId() {
		return cateId;
	}
	public void setCateId(String cateId) {
		this.cateId = cateId;
	}
	public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getSaleUnit() {
		return saleUnit;
	}
	public void setSaleUnit(Integer saleUnit) {
		this.saleUnit = saleUnit;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public Long getBillID() {
		return billID;
	}
	public void setBillID(Long billID) {
		this.billID = billID;
	}
	public QualityTraceTradeBill buildQualityTraceTradeBill() {
		QualityTraceTradeBill bill=DTOUtils.newDTO(QualityTraceTradeBill.class);
		bill.setBillId(this.billID);
		bill.setOrderId(this.orderId);
		bill.setAmount(this.getAmount().longValue());
		bill.setBuyerAccount(this.buyerAccount);
		bill.setBuyerName(this.buyerName);
		bill.setBuyerIDNo(this.buyerIDNo);
		bill.setCateName(this.cateName);
//		bill.setCheckResultEid(checkResultEid);
//		bill.setConclusion(conclusion);
		bill.setOrderCreateDate(this.orderCreateDate);
//		bill.setOrderItemId("");
		bill.setOrderPayDate(this.orderPayDate);
//		bill.setOrderStatus("");
//		bill.setPdresult("");
		bill.setPrice(this.price.longValue());
		bill.setProductName(this.productName);
		bill.setSaleUnit(this.saleUnit);
		bill.setSellerAccount(this.sellerAccount);
		bill.setSellerName(this.sellerName);
		bill.setSellerIDNo(this.sellerIDNo);
		bill.setTotalMoney(this.totalMoney.longValue());
//		bill.setTradeFlowId("");
		
		
		return bill;
		
		
	}

}
