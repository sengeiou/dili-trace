package com.dili.trace.enums;

import java.util.Optional;

public enum DetectTypeEnum {
    /**
     * 初检合格
     */
    NONE(0, ""),
    /**
     * 初检合格
     */
    INITIAL_CHECK(1, "初检"),
    /**
     * 复检
     */
    RECHECK(2, "复检"),
    /**
     * 抽检
     */
    SPOT_CHECK(3, "抽检"),
    ;


    private String name;
    private Integer code ;

    DetectTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static Optional<DetectTypeEnum> fromCode(Integer code){
        for (DetectTypeEnum anEnum : DetectTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return Optional.ofNullable(anEnum);
            }
        }
        return Optional.empty();
    }
    public Boolean equalsToCode(Integer code){
        return this.getCode().equals(code);
    }
    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
