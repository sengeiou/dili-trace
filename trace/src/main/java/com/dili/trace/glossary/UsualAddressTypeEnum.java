package com.dili.trace.glossary;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/11/8 18:43
 */
public enum UsualAddressTypeEnum {

    /**
     * 登记单产地
     */
    REGISTER("register", "登记单产地"),
    /**
     * 销地城市
     */
    USER("user", "销地城市"),
    ;

    private String desc;
    private String type ;

    UsualAddressTypeEnum(String type, String desc){
        this.type = type;
        this.desc = desc;
    }
    public boolean equalsTo(String type) {
    	return this.getType().equals(StringUtils.trim(type));
    }
    public static UsualAddressTypeEnum getUsualAddressType(String type) {
        for (UsualAddressTypeEnum anEnum : UsualAddressTypeEnum.values()) {
            if (anEnum.getType().equals(type)) {
                return anEnum;
            }
        }
        return null;
    }
    public static Optional<UsualAddressTypeEnum>fromType(String type){

        return Optional.ofNullable(UsualAddressTypeEnum.getUsualAddressType(type));
    }

    public String getType() {
		return type;
	}

    public String getDesc() {
        return desc;
    }
}
