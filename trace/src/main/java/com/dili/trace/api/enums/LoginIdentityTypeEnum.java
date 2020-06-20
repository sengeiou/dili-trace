package com.dili.trace.api.enums;

import java.util.Optional;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 * @createTime 2020/06/03 15:43
 */
public enum LoginIdentityTypeEnum {
	
	/**
	 * 市场普通用户
	 */
	USER(1, "市场普通用户", null),
	/**
	 * 系统管理
	 */
	SYS_MANAGER(2, "系统管理", "registerBill/index.html#weichat-view"),;
	
	private String name;
	private Integer code;
	private String authUrl;

	LoginIdentityTypeEnum(Integer code, String name, String authUrl) {
		this.code = code;
		this.name = name;
		this.authUrl = authUrl;
	}

	public static Optional<LoginIdentityTypeEnum> fromCode(Integer code) {
		for (LoginIdentityTypeEnum anEnum : LoginIdentityTypeEnum.values()) {
			if (anEnum.getCode().equals(code)) {
				return Optional.of(anEnum);
			}
		}
		return Optional.empty();
	}

	public boolean equalsToCode(Integer code) {
		return LoginIdentityTypeEnum.fromCode(code).map(item -> this == item).orElse(false);
	}

	public Integer getCode() {
		return code;
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public String getName() {
		return name;
	}
}
