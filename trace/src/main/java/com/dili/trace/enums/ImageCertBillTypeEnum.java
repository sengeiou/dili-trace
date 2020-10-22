package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */

public enum ImageCertBillTypeEnum {
    /**
     * 报备单
     */
    BILL_TYPE(1, "报备单"),
    /**
     * 检测单
     */
    INSPECTION_TYPE(2, "检测单"),
    /**
     * 检测不合格处置单
     */
    DISPOSE_TYPE(3, "检测不合格处置单"),
    /**
     * 进门主台账单
     */
    MAIN_CHECKIN_ORDER_TYPE(4, "进门主台账单"),
    ;

    private String name;
    private Integer code;

    ImageCertBillTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<ImageCertBillTypeEnum> fromCode(Integer code) {
        return StreamEx.of(ImageCertBillTypeEnum.values()).filterBy(ImageCertBillTypeEnum::getCode, code).findFirst();
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
