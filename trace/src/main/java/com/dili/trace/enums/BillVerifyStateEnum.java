package com.dili.trace.enums;

import java.util.Optional;

import one.util.streamex.StreamEx;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:43 1.初检合格 2.不合格 3.复检合格 4.复检不合格
 */
public enum BillVerifyStateEnum {

	/**
	 * 无
	 */
	NONE(0, "无"),
	/**
	 * 部分合格
	 */
	PARTLY_PASSED(10, "部分合格"),
	/**
	 * 合格
	 */
	PASSED(20, "合格"),

	/**
	 * 不通过
	 */
	NO_PASSED(30, "不通过"),;

	private String name;
	private Integer code;

	BillVerifyStateEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<BillVerifyStateEnum> fromCode(Integer code) {
		return StreamEx.of(BillVerifyStateEnum.values()).filterBy(BillVerifyStateEnum::getCode, code).findFirst();
	}

	public boolean equalsToCode(Integer code) {
		return this.getCode().equals(code);
	}

	public static boolean doVerify(Integer code) {
		return BillVerifyStateEnum.NONE.equalsToCode(code) || BillVerifyStateEnum.PARTLY_PASSED.equalsToCode(code);
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
