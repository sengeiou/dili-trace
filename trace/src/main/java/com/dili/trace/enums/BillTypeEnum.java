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
	 * 补单
	 */
	SUPPLEMENT(20, "补单"),

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
		return BillVerifyStatusEnum.NONE.equalsToCode(code) || BillVerifyStatusEnum.RETURNED.equalsToCode(code);
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
