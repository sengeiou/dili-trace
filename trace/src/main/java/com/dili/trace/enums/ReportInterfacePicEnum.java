package com.dili.trace.enums;

/**
 * @author asa.lee
 */

public enum ReportInterfacePicEnum {

    /**
     * 生产许可证
     */
    PRODUCTION_LICENSE(10, "生产许可证"),

    /**
     * 经营许可证
     */
    OPERATING_LICENSE(20, "经营许可证"),

    /**
     * 营业执照
     */
    BUSINESS_LICENSE(30, "营业执照"),

    /**
     * 身份证正面
     */
    ID_CARD_FRONT(40, "身份证正面"),

    /**
     * 身份证反面
     */
    ID_CARD_REVERSE(50, "身份证反面"),

    /**
     * 证件照片
     */
    CERTIFICATE(60, "证件照片"),
    ;

    private String name;
    private Integer code;

    ReportInterfacePicEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
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
