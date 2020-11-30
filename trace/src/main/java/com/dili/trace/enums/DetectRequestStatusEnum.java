package com.dili.trace.enums;

import java.util.Optional;


public enum DetectRequestStatusEnum {
    /**
     * 新建
     */
    NEW(0, "新建"),
    /**
     * 检测中
     */
    DETECTING(10, "检测中"),
    /**
     * 复检
     */
    FINISHED(20, "检测结束"),
    ;


    private String name;
    private Integer code ;

    DetectRequestStatusEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static Optional<DetectRequestStatusEnum> fromCode(Integer code){
        for (DetectRequestStatusEnum anEnum : DetectRequestStatusEnum.values()) {
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
