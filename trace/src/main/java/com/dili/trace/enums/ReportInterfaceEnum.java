package com.dili.trace.enums;

/**
 * @author asa.lee
 */

public enum ReportInterfaceEnum {

    /**
     * 商品大类新增/修改
     */
    BIG_CATEGORY("category_bigClass", "商品大类新增/修改"),

    /**
     * 商品二级类目新增/修改
     */
    CATEGORY_SMALL_CLASS("category_smallClass", "商品二级类目新增/修改"),

    /**
     * 商品新增/修改
     */
    CATEGORY_GOODS("category_goods", "商品新增/修改"),

    /**
     * 上游新增/修改
     */
    UPSTREAM_UP("upstream_up", "上游新增/修改"),

    /**
     * 下游新增/修改
     */
    UPSTREAM_DOWN("upstream_down", "下游新增/修改"),

    /**
     * 配送交易
     */
    TRADE_REQUEST_DELIVERY("trade_request_delivery", "配送交易"),

    /**
     * 配送交易作废
     */
    TRADE_REQUEST_DELIVERY_DELETE("trade_request_delivery_delete", "配送交易作废"),

    /**
     * 扫码交易
     */
    TRADE_REQUEST_SCAN("trade_request_scan", "扫码交易"),

    /**
     * 扫码交易作废
     */
    TRADE_REQUEST_SCAN_DELETE("trade_request_scan_delete", "扫码交易作废"),

    /**
     * 报备新增/编辑
     */
    REGISTER_BILL("register_bill", "报备新增/编辑"),

    /**
     * 进门
     */
    CHECK_INOUT_RECORD("checkinout_record", "进门"),

    /**
     * 食安码新增/修改
     */
    USER_QR_HISTORY("user_qr_history", "食安码新增/修改"),

    /**
     * 经营户新增/编辑
     */
    USER("user", "经营户新增/编辑"),

    /**
     * 经营户作废
     */
    USER_DELETE("user_delete", "经营户作废"),
    ;

    private String name;
    private String code;

    ReportInterfaceEnum(String code, String name) {
        this.code = code;
        this.name = name;
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
