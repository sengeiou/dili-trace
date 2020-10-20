package com.dili.trace.glossary.hanguo;

/**
 * @author asa.lee
 */

public enum HangGuoGoodsLevelEnum {
    GOODS_ONE(1, "商品类第一层级"),
    GOODS_TWO(2, "商品类第二层级"),
    GOODS_THREE(3, "商品类第三层级"),
    GOODS_FOUR(4, "商品类第四层级"),
    GOODS_FIVE(5, "商品类第五层级"),
    ;

    private String name;
    private Integer code;

    HangGuoGoodsLevelEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static HangGuoGoodsLevelEnum getEnabledState(Integer code) {
        for (HangGuoGoodsLevelEnum anEnum : HangGuoGoodsLevelEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
