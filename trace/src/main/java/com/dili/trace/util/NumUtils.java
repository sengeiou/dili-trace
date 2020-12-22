package com.dili.trace.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class NumUtils {
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
}
