package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum ImageCertTypeEnum {


    /**
     * 检测报告
     */
    DETECT_REPORT(1, "检测报告"),
    /**
     * 产地证明
     */
    ORIGIN_CERTIFIY(2, "产地证明"),
    /**
     * 产检疫合格证
     */
    QUARANTINE_CERTIFICATE(3, "检疫合格证"),

    /**
     * 购货凭证
     */
    PURCHASE_VOUCHER(4, "购货凭证"),


    /**
     * 报关单
     */
    CUSTOMS_DECLARATION(5, "报关单"),

    /**
     * 采购协议
     */
    PURCHASE_AGREEMENT(6, "采购协议"),

    /**
     * 杭果用户证件照片
     */
    USER_PHOTO_HANGGUO(7, "杭果用户证件照片"),

    /**
     * 处理结果图片
     */
    Handle_Result(8, "处理结果图片"),


    /**
     * 其他
     */
    OTHERS(99, "其他"),


    ;

    private String name;
    private Integer code;

    ImageCertTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Optional<ImageCertTypeEnum> fromCode(Integer code) {
        return StreamEx.of(ImageCertTypeEnum.values()).filterBy(ImageCertTypeEnum::getCode, code).findFirst();
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
