package com.dili.trace.glossary;

import java.util.stream.Stream;

public enum UpStreamSourceEnum {
    /**
     * 下游
     */
    DOWN(10, "下游"),
    /**
     * 注册
     */
    REGISTER(20, "注册"),;

    private Integer code;
    private String name;

    UpStreamSourceEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static UpStreamSourceEnum fromCode(Integer code) {
        return Stream.of(UpStreamSourceEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }

    public boolean equalsCode(Integer code) {
        return this.getCode().equals(code);

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
