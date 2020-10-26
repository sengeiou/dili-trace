package com.dili.trace.enums;

/**
 * 与天下粮仓对接的接口定义常量
 *
 * @author asa.lee
 */

public enum ReportInterfaceEnum {

    /**
     * 商品大类新增/修改
     */
    BIG_CATEGORY("category_bigClass", "商品大类新增/修改"),
    /**
     * 商品大类新增/修改
     */
    FRUITS_BIG_CATEGORY("fruits_category_bigClass", "杭果商品大类新增/修改"),
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
     * 报备报废上报
     */
    REGISTER_BILL_DELETE("register_bill_delete", "报备单报废"),

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

    /**
     * 杭果经营户
     */
    HANGGUO_USER("hangguo_user", "杭果经营户上报"),


    /**
     * 杭果商品
     */
    HANGGUO_GOODS("hangguo_goods", "杭果商品上报"),


    /**
     * 杭果检测数据上报
     */
    HANGGUO_INSPECTION("hangguo_inspection", "杭果检测数据上报"),


    /**
     * 杭果不合格处置数据上报
     */
    HANGGUO_DISPOSE("hangguo_dispose", "杭果不合格处置数据上报"),
    /**
     * 杭果交易数据上报
     */
    HANGGUO_TRADE("hangguo_trade", "杭果交易数据上报"),
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
