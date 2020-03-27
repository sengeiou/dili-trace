package com.dili.trace.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:43
 * 1.初检合格 2.不合格 3.复检合格 4.复检不合格
 */
public enum BillDetectStateEnum {

	/**
	 * 初检合格
	 */
    PASS(1, "初检合格"),
    /**
     * 初检不合格
     */
    NO_PASS(2, "初检不合格"),
    /**
     * 复检合格
     */
    REVIEW_PASS(3, "复检合格"),
    /**
     * 复检不合格
     */
    REVIEW_NO_PASS(4, "复检不合格"),
    ;

    private String name;
    private Integer code ;

    BillDetectStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static BillDetectStateEnum getBillDetectStateEnum(Integer code) {
        for (BillDetectStateEnum anEnum : BillDetectStateEnum.values()) {
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
