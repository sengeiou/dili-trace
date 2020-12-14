package com.dili.trace.enums;

import java.util.stream.Stream;

/**
 * 客户类型
 */
public enum ClientTypeEnum {
    /**
     * 无
     */
    DRIVER(1, "司机"),
    /**
     * 卖家
     */
    SELLER(2, "卖家"),
    /**
     * 买家
     */
    BUYER(3, "买家"),
    /**
     * 管理员
     */
    MANAGER(4, "管理员"),
    /**
     * 其他
     */
    OTHERS(999, "其他"),;

    private Integer code;
    private String desc;

    ClientTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static ClientTypeEnum fromCode(Integer code) {
        return Stream.of(ClientTypeEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
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
