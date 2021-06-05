package com.dili.trace.enums;

import java.util.Optional;

import com.dili.common.exception.TraceBizException;
import com.fasterxml.jackson.annotation.JsonValue;
import one.util.streamex.StreamEx;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum BillVerifyStatusEnum {

    /**
     * 待审核
     */
    WAIT_AUDIT(0, "待审核"),
    /**
     * 已退回
     */
    RETURNED(10, "已退回"),
    /**
     * 已通过
     */
    PASSED(20, "已通过"),

    /**
     * 不通过
     */
    NO_PASSED(30, "不通过"),

    /**
     * 作废
     */
    DELETED(40, "作废"),
    ;

    private String name;
    private Integer code;

    BillVerifyStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<BillVerifyStatusEnum> fromCode(Integer code) {
        return StreamEx.of(BillVerifyStatusEnum.values()).filterBy(BillVerifyStatusEnum::getCode, code).findFirst();
    }

    public static BillVerifyStatusEnum fromCodeOrEx(Integer code) {
        return StreamEx.of(BillVerifyStatusEnum.values()).filterBy(BillVerifyStatusEnum::getCode, code).findFirst().orElseThrow(() -> {
            return new TraceBizException("审核状态错误");
        });
    }

    public static String name(Integer code) {
        return BillVerifyStatusEnum.fromCode(code).map(BillVerifyStatusEnum::getName).orElse("");
    }

    public boolean equalsToCode(Integer code) {
        return this.getCode().equals(code);
    }

    @JsonValue
    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
