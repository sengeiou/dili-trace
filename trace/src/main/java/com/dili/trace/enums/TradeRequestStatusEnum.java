package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum TradeRequestStatusEnum {

    /**
     * 无
     */
    NONE(0, "无"),

    /**
     * 完成
     */
    FINISHED(10, "完成"),
    /**
     * 拒绝
     */
    CANCELLED(20, "拒绝"),
    ;;


    private String name;
    private Integer code;

    TradeRequestStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<TradeRequestStatusEnum> fromCode(Integer code) {
        return StreamEx.of(TradeRequestStatusEnum.values()).filterBy(TradeRequestStatusEnum::getCode, code).findFirst();
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
