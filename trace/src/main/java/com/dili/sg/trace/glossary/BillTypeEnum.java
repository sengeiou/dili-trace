package com.dili.trace.glossary;

import java.util.Optional;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 * @createTime 2020/06/03 15:43
 */
public enum BillTypeEnum {
	/**
	 * 登记单
	 */
	REGISTER_BILL(1, "登记单"),
	/**
	 * 委托单
	 */
	COMMISSION_BILL(2, "委托单"),
	/**
	 * 商务单
	 */
	E_COMMERCE_BILL(3, "商务单"),;

	private String name;
	private Integer code;

	BillTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<BillTypeEnum> getBillTypeEnum(Integer code) {
		for (BillTypeEnum anEnum : BillTypeEnum.values()) {
			if (anEnum.getCode().equals(code)) {
				return Optional.of(anEnum);
			}
		}
		return Optional.empty();
	}

	public boolean equalsToCode(Integer code) {
		return BillTypeEnum.getBillTypeEnum(code).map(item -> this == item).orElse(false);
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
