package com.dili.trace.glossary;

public enum RegisterBilCreationSourceEnum {
    PC(10,"电脑"),
    WX(20,"微信");

    private Integer code;
    private String desc;

    RegisterBilCreationSourceEnum(Integer code, String desc){
        this.code=code;
        this.desc=desc;
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
