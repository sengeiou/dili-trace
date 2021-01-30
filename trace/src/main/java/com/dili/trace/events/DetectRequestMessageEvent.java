package com.dili.trace.events;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DetectRequestMessageEvent {
    /**
     * 预约申请
     */
    booking("booking-btn"),
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
     * 人工检测
     */
    manual("manual-btn"),
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
    export("export"),
    /**
     * 预约检测
     */
    appointment("appointment-btn"),
    /**
     * 批量复检
     */
    batchReview("batchReview-btn"),
    /**
     * 创建打印报告
     */
    createSheet("createSheet-btn"),
    /**
     * 上传处理结果
     */
    uploadHandleResult("upload-handleresult-btn"),
    /**
     * 抽检
     */
    spotCheck("sample-btn"),
    /**
     *
     */
    unqualifiedHandle("unqualified-handle-btn"),
    ;

    private DetectRequestMessageEvent(String code) {
        this.code = code;
    }

    private String code;

    @JsonValue
    public String getCode() {
        return code;
    }
}
