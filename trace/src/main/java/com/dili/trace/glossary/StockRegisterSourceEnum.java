package com.dili.trace.glossary;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

/**
 * 库存来源
 * @author Alvin.Li
 */
public enum StockRegisterSourceEnum {
    /**
     * 登记
     */
    REG(1, "进门登记"),
    /**
     * 补录
     */
    RECORD(2, "补录"),
    /**
     * 调拨
     */
    TRANSFER(3, "调拨"),;

    private String name;
    private Integer code;

    StockRegisterSourceEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static StockRegisterSourceEnum getEnabledState(Integer code) {
        for (StockRegisterSourceEnum anEnum : StockRegisterSourceEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static Optional<StockRegisterSourceEnum> getRegisterSourceEnum(Integer code) {
        for (StockRegisterSourceEnum anEnum : StockRegisterSourceEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return Optional.of(anEnum);
            }
        }
        return Optional.empty();
    }

    public boolean equalsToCode(Integer code) {
        return StockRegisterSourceEnum.getRegisterSourceEnum(code).map(item -> this == item).orElse(false);
    }

    @JsonValue
    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}