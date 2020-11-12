package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */

public enum SysConfigTypeEnum {



    /**
     * 运营报表限定天数
     */
    OPERATION_LIMIT_DAY("operation_report_limit_day", "运营报表限定天数"),
    /**
     * 展示大屏用户基数设定
     */
    STATISTIC_BASE_USER("statisticBaseUser", "展示大屏用户基数设定大类"),
    CATEGORY_TRADE("trade","交易单"),
    CATEGORY_BILL("bill","报备"),
    CALL_DATA_SWITCH("call_data_switch","远程调用数据开关大类"),
    CALL_DATA_HANG_GUO("source_hang_guo","远程调用数据开关_杭果"),
    ;

    private String name;
    private String code;

    SysConfigTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<UserFlagEnum> fromCode(String code) {
        return StreamEx.of(UserFlagEnum.values()).filterBy(UserFlagEnum::getCode, code).findFirst();
    }

    public boolean equalsToCode(String code) {
        return this.getCode().equals(code);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
