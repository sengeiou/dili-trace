package com.dili.trace.enums;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum MessageStateEnum {

    /**
     * 未读
     */
    UNREAD(0, "未读"),
    /**
     * 已读
     */
    READ(1, "已读"),

    /**
     * 消息业务类型-用户
     */
    BUSINESS_TYPE_USER(10, "用户"),

    /**
     * 消息业务类型-报备
     */
    BUSINESS_TYPE_BILL(20, "报备"),

    /**
     * 消息业务类型-报备(场内审核)
     */
    BUSINESS_TYPE_FIELD_BILL(21, "场内审核"),

    /**
     * 消息业务类型-交易
     */
    BUSINESS_TYPE_TRADE(30, "购买"),

    /**
     * 消息业务类型-交易
     */
    BUSINESS_TYPE_TRADE_SELL(40, "销售"),

    /**
     * 消息业务类型-进门登记
     */
    BUSINESS_TYPE_FORM_BILL(50, "进门登记"),

    ;

    private String name;
    private Integer code;

    MessageStateEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
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
