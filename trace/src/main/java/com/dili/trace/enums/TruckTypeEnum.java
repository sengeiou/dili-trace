package com.dili.trace.enums;

import java.util.Optional;

import one.util.streamex.StreamEx;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum TruckTypeEnum {

	/**
	 * 整车
	 */
	FULL(10, "整车"),
	/**
	 * 拼车
	 */
	POOL(20, "拼车"),

	;

	private String name;
	private Integer code;

	TruckTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<BillVerifyStatusEnum> fromCode(Integer code) {
		return StreamEx.of(BillVerifyStatusEnum.values()).filterBy(BillVerifyStatusEnum::getCode, code).findFirst();
	}

	public boolean equalsToCode(Integer code) {
		return this.getCode().equals(code);
	}

	public static boolean canDoVerify(Integer code) {
		return BillVerifyStatusEnum.NONE.equalsToCode(code) || BillVerifyStatusEnum.RETURNED.equalsToCode(code);
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
