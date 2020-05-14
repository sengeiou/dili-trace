package com.dili.trace.glossary;

public enum QrItemTypeEnum {
    /**
     * 个人信息
     */
    USER(10, "个人信息"),
    /**
     * 上游信息
     */
    UPSTREAM(20, "上游信息"),
    /**
     * 登记单信息
     */
    BILL(30, "登记单信息"),
    /**
     * 其他
     */
    OTHER(100, "其他"),;

    private Integer code;
    private String desc;

    QrItemTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
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
