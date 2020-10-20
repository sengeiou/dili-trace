package com.dili.trace.enums;

/**
 * @author Guzman
 * @version 1.0
 * @ClassName RegisgterHeadStatusEnum
 * @Description todo
 * @createTime 2020年10月20日 15:13:00
 */
public enum RegisgterHeadStatusEnum {

    ACTIVE(10,"已启用"),
    UNACTIVE(20,"已关闭"),
    DELETED(30,"已禁用"),
    ;

    private Integer code;
    private String desc;

    RegisgterHeadStatusEnum(Integer code, String desc) {
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
