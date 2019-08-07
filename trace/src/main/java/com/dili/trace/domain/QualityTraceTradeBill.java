package com.dili.trace.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`quality_trace_trade_bill`")
public interface QualityTraceTradeBill extends IBaseDomain {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`id`")
	@FieldDef(label = "id")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getId();

	void setId(Long id);

	@Column(name = "`bill_id`")
	@FieldDef(label = "流水号")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getBillId();

	void setBillId(Long billId);

	@Column(name = "`order_id`")
	@FieldDef(label = "订单号")
	@EditMode(editor = FieldEditor.Text, required = true)
	String getOrderId();

	void setOrderId(String orderId);

	@Column(name = "`order_status`")
	@FieldDef(label = "orderStatus")
	@EditMode(editor = FieldEditor.Number, required = true)
	Boolean getOrderStatus();

	void setOrderStatus(Boolean orderStatus);

	@Column(name = "`seller_account`")
	@FieldDef(label = "卖家账号")
	@EditMode(editor = FieldEditor.Text, required = true)
	String getSellerAccount();

	void setSellerAccount(String sellerAccount);

	@Column(name = "`seller_name`")
	@FieldDef(label = "卖家名称", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getSellerName();

	void setSellerName(String sellerName);

	@Column(name = "`sellerIDNo`")
	@FieldDef(label = "卖家身份证", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = true)
	public String getSellerIDNo();

	public void setSellerIDNo(String sellerIDNo);

	@Column(name = "`buyer_account`")
	@FieldDef(label = "买家账号")
	@EditMode(editor = FieldEditor.Text, required = true)
	String getBuyerAccount();

	void setBuyerAccount(String buyerAccount);

	@Column(name = "`buyer_name`")
	@FieldDef(label = "买家名称", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getBuyerName();

	void setBuyerName(String buyerName);

	@Column(name = "`buyerIDNo`")
	@FieldDef(label = "买家身份证", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = true)
	public String getBuyerIDNo();

	public void setBuyerIDNo(String buyerIDNo);

	@Column(name = "`order_create_date`")
	@FieldDef(label = "订单创建时间")
	@EditMode(editor = FieldEditor.Datetime, required = true)
	Date getOrderCreateDate();

	void setOrderCreateDate(Date orderCreateDate);

	@Column(name = "`order_pay_date`")
	@FieldDef(label = "订单支付时间")
	@EditMode(editor = FieldEditor.Datetime, required = true)
	Date getOrderPayDate();

	void setOrderPayDate(Date orderPayDate);

	@Column(name = "`pdResult`")
	@FieldDef(label = "残留值")
	@EditMode(editor = FieldEditor.Text, required = false)
	BigDecimal getPdresult();

	void setPdresult(BigDecimal pdresult);

	@Column(name = "`conclusion`")
	@FieldDef(label = "合格值  0-未知 1合格  2不合格 3作废")
	@EditMode(editor = FieldEditor.Number, required = false)
	Boolean getConclusion();

	void setConclusion(Boolean conclusion);

	@Column(name = "`check_result_EID`")
	@FieldDef(label = "检测结算单唯一标志,NULL表示无检测信息")
	@EditMode(editor = FieldEditor.Number, required = false)
	Long getCheckResultEid();

	void setCheckResultEid(Long checkResultEid);

	@Column(name = "`trade_flow_id`")
	@FieldDef(label = "交易号")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getTradeFlowId();

	void setTradeFlowId(Long tradeFlowId);

	@Column(name = "`total_money`")
	@FieldDef(label = "总金额", maxLength = 30)
	@EditMode(editor = FieldEditor.Text, required = true)
	Long getTotalMoney();

	void setTotalMoney(Long totalMoney);

	@Column(name = "`order_item_id`")
	@FieldDef(label = "订单项号")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getOrderItemId();

	void setOrderItemId(Long orderItemId);

	@Column(name = "`product_name`")
	@FieldDef(label = "商品名称", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getProductName();

	void setProductName(String productName);

	@Column(name = "`cate_name`")
	@FieldDef(label = "品类名称", maxLength = 50)
	@EditMode(editor = FieldEditor.Text, required = true)
	String getCateName();

	void setCateName(String cateName);

	@Column(name = "`price`")
	@FieldDef(label = "单价(分)")
	@EditMode(editor = FieldEditor.Number, required = true)
	Long getPrice();

	void setPrice(Long price);

//
//	@Column(name = "`amount`")
//	@FieldDef(label = "件数")
//	@EditMode(editor = FieldEditor.Number, required = true)
//	Long getAmount();
//
//	void setAmount(Long amount);
	@Column(name = "`piecequantity`")
	@FieldDef(label = "件数")
	@EditMode(editor = FieldEditor.Number, required = true)
	public Long getPiecequantity();

	public void setPiecequantity(Long piecequantity);

	@Column(name = "`pieceweight`")
	@FieldDef(label = "件重（公斤）")
	@EditMode(editor = FieldEditor.Number, required = true)
	public Long getPieceweight();

	public void setPieceweight(Long pieceweight);

	@Column(name = "`netWeight`")
	@FieldDef(label = "总净重（公斤）")
	@EditMode(editor = FieldEditor.Number, required = true)
	public Long getNetWeight();

	public void setNetWeight(Long netWeight);

	@Column(name = "`tradetypeid`")
	@FieldDef(label = "交易类型编码")
	@EditMode(editor = FieldEditor.Number, required = true)
	public String getTradetypeid();

	public void setTradetypeid(String tradetypeid);

	@Column(name = "`tradetypename`")
	@FieldDef(label = "交易类型名称")
	@EditMode(editor = FieldEditor.Number, required = true)

	public String getTradetypename();

	public void setTradetypename(String tradetypename);

	@Column(name = "`billActive`")
	@FieldDef(label = "状态 0：正常1：作废")
	@EditMode(editor = FieldEditor.Number, required = true)
	public Integer getBillActive();

	public void setBillActive(Integer billActive);

	@Column(name = "`sale_unit`")
	@FieldDef(label = "销售单位 1:斤 2：件")
	@EditMode(editor = FieldEditor.Number, required = true)
	Integer getSaleUnit();

	void setSaleUnit(Integer saleUnit);
}