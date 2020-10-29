package com.dili.trace.enums;

import java.util.stream.Stream;

/**
 * @author asa.lee
 */

public enum ThirdSourceTypeEnum {
    /**
     * 杭果商品
     */
    CATEGORY(1, "杭果商品"),
    /**
     * 杭果会员
     */
    MEMBER(2, "杭果会员"),
    /**
     * 杭果供应商
     */
    SUPPLIER(3, "杭果供应商"),
    /**
     * 杭果交易数据
     */
    TRADE(4, "杭果交易数据"),
    /**
     * 杭果会员证件照
     */
    MEMBER_CAR_PIC(5, "杭果会员证件照"),
    /**
     * 杭果供应商证件照
     */
    SUPPLIER_CAR_PIC(6, "杭果供应商证件照"),;

    private Integer code;
    private String desc;

    ThirdSourceTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static ThirdSourceTypeEnum fromCode(Integer code) {
        return Stream.of(ThirdSourceTypeEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }
    // public static String getNamefromCode(Integer code) {
    //     return Stream.ofNullable(CheckinStatusEnum.fromCode(code)).map(CheckinStatusEnum::getDesc).findFirst().orElse("");
    // }
    public boolean equalsToCode(Integer code) {
        return this.getCode().equals(code);

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
