package com.dili.trace.enums;

import java.util.Optional;

import com.dili.common.exception.TraceBizException;
import one.util.streamex.StreamEx;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum TradeOrderStatusEnum {

	/**
	 * 待完成
	 */
	NONE(0, "待完成"),
	/**
	 * 完成
	 */
	FINISHED(10, "完成"),
	/**
	 * 拒绝
	 */
	CANCELLED(20, "拒绝"),;

	private String name;
	private Integer code;

	TradeOrderStatusEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<TradeOrderStatusEnum> fromCode(Integer code) {
		return StreamEx.of(TradeOrderStatusEnum.values()).filterBy(TradeOrderStatusEnum::getCode, code).findFirst();
	}

	public static TradeOrderStatusEnum fromCodeOrEx(Integer code) {
		return StreamEx.of(TradeOrderStatusEnum.values()).filterBy(TradeOrderStatusEnum::getCode, code).findFirst().orElseThrow(() -> {
			return new TraceBizException("状态值错误");
		});
	}
	public static String toName(Integer code){
		return TradeOrderStatusEnum.fromCode(code).map(TradeOrderStatusEnum::getName).orElse("");
	}

	public boolean equalsToCode(Integer code) {
		return this.getCode().equals(code);
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
}
