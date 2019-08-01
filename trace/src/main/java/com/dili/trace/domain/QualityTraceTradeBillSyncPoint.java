package com.dili.trace.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2019-07-26 09:20:35.
 */
@Table(name = "`quality_trace_trade_bill_syncpoint`")
public interface QualityTraceTradeBillSyncPoint extends IBaseDomain {
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

}