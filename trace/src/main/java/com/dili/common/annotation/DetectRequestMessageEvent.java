package com.dili.common.annotation;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DetectRequestMessageEvent {
    /**
     * 接单
     */
    assign("assign-btn"),
    /**
     * 采样检测
     */
    sampling("sampling-btn"),
    /**
     * 主动送检
     */
    auto("auto-btn"),
    /**
     * 复检
     */
    review("review-btn"),
    /**
     * 撤销检测请求
     */
    undo("undo-btn"),
    /**
     * 查看详情
     */
    detail("detail-btn"),
    /**
     * 导出
     */
    export("export");

    private DetectRequestMessageEvent(String code) {
        this.code = code;
    }

    private String code;

    @JsonValue
    public String getCode() {
        return code;
    }
}
