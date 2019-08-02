package com.dili.trace.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2019/8/2 18:43
 */
public enum ImageTypeEnum {

    ID_CARD(1, "身份证照"),
    BUSINESS_LICENSE(2, "营业执照"),
    ORIGIN_CERTIFICATE(3, "产地证明"),
    DETECT_REPORT(4, "检测报告"),
    ;

    private String name;
    private Integer code ;

    ImageTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static ImageTypeEnum getImageTypeEnum(Integer code) {
        for (ImageTypeEnum anEnum : ImageTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
