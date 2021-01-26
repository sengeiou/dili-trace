package com.dili.common.annotation;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ECommerceBillMessageEvent {
    /**
     * 新增
     */
    ADD("add-btn"),
    /**
     * 审核
     */
    AUDIT("audit-btn"),
    /**
     * 撤销
     */
    DELETE("delete-btn"),
    /**
     * 打印不干胶
     */
    PRINT("print-btn"),
    /**
     * 打印分销报告
     */
    PRINT_SEPERATE("printSeperatePrintReport-btn"),
    /**
     * 查看详情
     */
    DETAIL("detail-btn"),

    /**
     * 导出
     */
    EXPORT("export"),;

    private ECommerceBillMessageEvent(String code) {
        this.code = code;
    }

    private String code;

    @JsonValue
    public String getCode() {
        return code;
    }
}
