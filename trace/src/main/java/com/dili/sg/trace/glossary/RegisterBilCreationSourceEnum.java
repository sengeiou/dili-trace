package com.dili.sg.trace.glossary;

import java.util.Optional;

public enum RegisterBilCreationSourceEnum {
    PC(10,"电脑"),
    WX(20,"微信");

    private Integer code;
    private String desc;

    RegisterBilCreationSourceEnum(Integer code, String desc){
        this.code=code;
        this.desc=desc;
    }
    public static Optional<RegisterBilCreationSourceEnum> getRegisterBilCreationSourceEnum(Integer code) {
        for (RegisterBilCreationSourceEnum anEnum : RegisterBilCreationSourceEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return Optional.of(anEnum);
            }
        }
        return Optional.empty();
    }

    public boolean equalsToCode(Integer code) {
        return RegisterBilCreationSourceEnum.getRegisterBilCreationSourceEnum(code).map(item -> this == item).orElse(false);
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
