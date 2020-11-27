package com.dili.trace.glossary;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:43
 */
public enum RegisterSourceEnum {
    /**
     * 其他
     */
    OTHERS(0, "其他"),
    /**
     * 理货区
     */
    TALLY_AREA(1, "理货区"),
    /**
     * 交易区
     */
    TRADE_AREA(2, "交易区"),;

    private String name;
    private Integer code;

    RegisterSourceEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RegisterSourceEnum getEnabledState(Integer code) {
        for (RegisterSourceEnum anEnum : RegisterSourceEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static Optional<RegisterSourceEnum> getRegisterSourceEnum(Integer code) {
        for (RegisterSourceEnum anEnum : RegisterSourceEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return Optional.of(anEnum);
            }
        }
        return Optional.empty();
    }

    public boolean equalsToCode(Integer code) {
        return RegisterSourceEnum.getRegisterSourceEnum(code).map(item -> this == item).orElse(false);
    }

    @JsonValue
    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}