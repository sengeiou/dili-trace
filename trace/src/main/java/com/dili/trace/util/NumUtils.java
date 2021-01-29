package com.dili.trace.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * BigDecimal 工具类
 */
public class NumUtils {

    /**
     * 最大报备重量
     */
    public static final BigDecimal MAX_WEIGHT = new BigDecimal(99999999);

    /**
     * 最大商品单价
     */
    public static final BigDecimal MAX_UNIT_PRICE = new BigDecimal("9999.99");

    /**
     * 最大报备件数
     */
    public static final BigDecimal MAX_NUM = new BigDecimal(99999999);

    public static Optional<Long> toLong(String str) {
        if (StringUtils.isBlank(str)) {
            return Optional.empty();
        }
        try {
            Long value = Long.parseLong(str.trim());
            return Optional.ofNullable(value);
        } catch (Exception e) {
            return Optional.empty();

        }
    }

    public static boolean isIntegerValue(BigDecimal decimalVal) {
        return decimalVal.scale() <= 0 || decimalVal.stripTrailingZeros().scale() <= 0;
    }

    public static void main(String[] args) {
        BigDecimal weight = new BigDecimal("999999999");
        BigDecimal price = new BigDecimal("10");

        if (NumUtils.MAX_WEIGHT.compareTo(weight) < 0) {
            System.out.println("商品重量不能大于" + NumUtils.MAX_WEIGHT.toString());
        }

        if (NumUtils.isIntegerValue(weight)) {
            System.out.println("商品重量必须为整数" + NumUtils.MAX_WEIGHT.toString());
        }

        if (Objects.nonNull(price)
                && NumUtils.MAX_UNIT_PRICE.compareTo(price) <= 0) {
            System.out.println("商品单价不能大于" + NumUtils.MAX_UNIT_PRICE.toString());
        }
    }
}
