package com.dili.trace.enums;

import java.util.Optional;

import one.util.streamex.StreamEx;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @author wangguofeng
 */
public enum ReportDtoTypeEnum {

	/**
	 * 市场经营户数据统计
	 */
	marketCount(10, "市场经营户数据统计"),
	/**
	 * 报备检测数据统计
	 */
	reportCount(20, "报备检测数据统计"),
	/**
	 * 品种产地排名统计
	 */
	regionCount(30, "品种产地排名统计"),

	/**
	 * 三色码状态数据统计
	 */
	codeCount(40, "三色码状态数据统计"),

	;

	private String name;
	private Integer code;

	ReportDtoTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static Optional<ReportDtoTypeEnum> fromCode(Integer code) {
		return StreamEx.of(ReportDtoTypeEnum.values()).filterBy(ReportDtoTypeEnum::getCode, code).findFirst();
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