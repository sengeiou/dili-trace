package com.dili.trace.enums;

import java.util.Optional;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 * @createTime 2018/11/8 18:43
 */
public enum TradeReturnStatusEnum {
	/**
	 * 正常
	 */
	NONE(0, "正常"),
	/**
	 * 退货中
	 */
	RETURNING(10, "退货中"), 
	/**
	 * 完成退货
	 */
	RETURNED(20, "完成退货"),
	
	/**
	 * 拒绝退货
	 */
	REFUSE(30, "拒绝退货"),

    ;

    private String name;
    private Integer code ;

    TradeReturnStatusEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static Optional<TradeReturnStatusEnum> fromCode(Integer code) {
        for (TradeReturnStatusEnum anEnum : TradeReturnStatusEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return Optional.of(anEnum);
            }
        }
        return Optional.empty();
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
