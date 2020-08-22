package com.dili.trace.enums;

/**
 * @author asa.lee
 */

public enum MessageReceiverEnum {
    /**
     * 消息接收人类型-普通
     */
    MESSAGE_RECEIVER_TYPE_NORMAL(10, "普通"),

    /**
     * 消息接收人类型-管理员
     */
    MESSAGE_RECEIVER_TYPE_MANAGER(20, "管理员"),
    ;
    private String name;
    private Integer code;

    MessageReceiverEnum(Integer code, String name) {
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
