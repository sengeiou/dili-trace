package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * @author asa.lee
 */

public enum ImageFileTypeEnum {
    /**
     * jpeg
     */
    JPEG_TYPE(1, "jpeg"),
    /**
     * png
     */
    PNG_TYPE(2, "png"),
    /**
     * jpg
     */
    JPG_TYPE(3, "jpg"),
    ;

    private String name;
    private Integer code;

    ImageFileTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<ImageFileTypeEnum> fromCode(Integer code) {
        return StreamEx.of(ImageFileTypeEnum.values()).filterBy(ImageFileTypeEnum::getCode, code).findFirst();
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
