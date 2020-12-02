package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author huangwennan
 */
public enum MessageTypeEnum {

	// 提10:商户注册交;11:商户注册审核通过;12:商户注册审核未通过;
	// 20:提交报备;21:报备审核通过;22:报备审核未通过;23:报备审核退回;
	// 30:进门审核;40:卖家下单;50:买家下单;51:卖家确认订单
	/**
	 * 10:商户注册交
	 */
	USERREGISTER(10, "商户注册提交"),
	/**
	 * 商户注册审核通过
	 */
	REGISTERPASS(11, "商户注册审核通过"),

	/**
	 * 商户注册审核未通过
	 */
	REGISTERFAILURE(12, "商户注册审核未通过"),

	/**
	 * 提交报备
	 */
	BILLSUBMIT(20, "提交报备"),

	/**
	 * 报备审核通过
	 */
	BILLPASS(21, "报备审核通过"),

	/**
	 * 报备审核未通过
	 */
	BILLFAILURE(22, "报备审核未通过"),

	/**
	 * 报备审核退回
	 */
	BILLRETURN(23, "报备审核退回"),

	/**
	 * 进门审核
	 */
	CHECKIN(30, "进门审核"),

	/**
	 * 卖家下单
	 */
	SALERORDER(40, "卖家下单"),

	/**
	 * 买家下单
	 */
	BUYERORDER(50, "买家下单"),

	/**
	 * 卖家确认订单
	 */
	CONFIRMORDER(51, "卖家确认订单")

	;

	private String name;
	private Integer code;

	MessageTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<MessageTypeEnum> fromCode(Integer code) {
		return StreamEx.of(MessageTypeEnum.values()).filterBy(MessageTypeEnum::getCode, code).findFirst();
	}

	public boolean equalsToCode(Integer code) {
		return this.getCode().equals(code);
	}

	public static boolean canDoVerify(Integer code) {
		return BillVerifyStatusEnum.WAIT_AUDIT.equalsToCode(code) || BillVerifyStatusEnum.RETURNED.equalsToCode(code);
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
