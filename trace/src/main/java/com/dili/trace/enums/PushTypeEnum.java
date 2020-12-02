package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author lily
 */
public enum PushTypeEnum {

	/**
	 * 上架
	 */
	UP(1, "上架"),
	/**
	 * 下架
	 */
	DOWN(0, "下架"),

	;

	private String name;
	private Integer code;

	PushTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<PushTypeEnum> fromCode(Integer code) {
		return StreamEx.of(PushTypeEnum.values()).filterBy(PushTypeEnum::getCode, code).findFirst();
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
