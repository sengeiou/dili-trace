package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * Description:
 *
 * @author Lily.Huang
 * @date 2020/11/10
 */
public enum CheckReportEnum {
    /**
     * 未处理
     */
    NONE_REPORT(-1, "未处理"),
    /**
     * 待上报
     */
    READY_REPORT(1, "待上报"),
    /**
     * 已上报
     */
    HAVE_REPORT(2,"已上报")
    ;

    private String name;
    private Integer code;

    CheckReportEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<CheckReportEnum> fromCode(Integer code) {
        return StreamEx.of(CheckReportEnum.values()).filterBy(CheckReportEnum::getCode, code).findFirst();
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
