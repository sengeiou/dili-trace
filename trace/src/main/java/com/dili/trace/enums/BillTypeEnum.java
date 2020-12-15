package com.dili.trace.enums;

import java.util.Optional;

import one.util.streamex.StreamEx;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum BillTypeEnum {

	/**
	 * 正常进场
	 */
	NONE(10, "正常进场"),


	/**
	 * 报备单
	 */
	REGISTER_BILL(1, "报备单"),

	/**
	 * 检测单
	 */
	CHECK_ORDER(2, "检测单"),

	/**
	 * 检测不合格处置单
	 */
	CHECK_DISPOSE(3, "检测不合格处置单"),

	/**
	 * 进门主台账单
	 */
	MASTER_BILL(4, "进门主台账单"),

	/**
	 * 进门登记单
	 */
	REGISTER_FORM_BILL(5, "进门登记单"),

	/**
	 * 委托单
	 */
	COMMISSION_BILL(6, "场外委托"),
	/**
	 * 商务单
	 */
	E_COMMERCE_BILL(7, "商务单"),




	/**
	 * 补单
	 */
	SUPPLEMENT(20, "补单"),
	/**
	 * 外冷分批进场
	 */
	PARTIAL(30, "外冷分批进场"),
	;

	;

	private String name;
	private Integer code;

	BillTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<BillTypeEnum> fromCode(Integer code) {
		return StreamEx.of(BillTypeEnum.values()).filterBy(BillTypeEnum::getCode, code).findFirst();
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
