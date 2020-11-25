package com.dili.trace.glossary;

import one.util.streamex.StreamEx;

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
    public static Optional<RegisterBilCreationSourceEnum>fromCode(Integer code){
        return StreamEx.of(RegisterBilCreationSourceEnum.values()).filterBy(RegisterBilCreationSourceEnum::getCode,code).findFirst();

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
