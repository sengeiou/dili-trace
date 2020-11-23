package com.dili.sg.trace.glossary;

import java.util.Optional;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 * @createTime 2020/06/03 15:43
 */
public enum OrderVersionEnum {
    /**
     * 新订单接口
     */
    VERSION(2, "新订单接口"),
    ;

    private String name;
    private Integer code;

    OrderVersionEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<OrderVersionEnum> getBillTypeEnum(Integer code) {
        for (OrderVersionEnum anEnum : OrderVersionEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return Optional.of(anEnum);
            }
        }
        return Optional.empty();
    }

    public boolean equalsToCode(Integer code) {
        return OrderVersionEnum.getBillTypeEnum(code).map(item -> this == item).orElse(false);
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
