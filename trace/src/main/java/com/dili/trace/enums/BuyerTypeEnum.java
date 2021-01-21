package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum BuyerTypeEnum {

    /**
     * 常规买家
     */
    NORMAL_BUYER(1, "常规买家"),

    /**
     * 下游买家
     */
    DOWNSTREAM_BUYER(2, "下游买家"),

    /**
     * 其他买家
     */
    OTHERS(999, "其他买家"),

    ;;

    private String name;
    private Integer code;

    BuyerTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<BuyerTypeEnum> fromCode(Integer code) {
        return StreamEx.of(BuyerTypeEnum.values()).filterBy(BuyerTypeEnum::getCode, code).findFirst();
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
