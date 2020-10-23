package com.dili.trace.enums;

import one.util.streamex.StreamEx;

import java.util.Optional;

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

	/**
	 * 经营户作废
	 */
	thirdUserDelete(50, "经营户作废"),

	/**
	 * 经营户新增/修改
	 */
	thirdUserSave(60, "经营户新增/修改"),

	/**
	 * 食安码
	 */
	userQrCode(70, "食安码新增/修改"),

    /**
     * 商品类目（一级）
     */
    categoryBigLevel(80, "商品大类新增/修改"),

    /**
     * 商品类目（二级）
     */
    categorySmallLevel(90, "商品小类新增/修改"),

	/**
	 * 商品
	 */
	goods(100, "商品新增/修改"),

    /**
	 * 报备新增/编辑
	 */
	registerBill(110, "报备新增/编辑"),

	/**
	 * 上游新增/修改
	 */
	upstream(120, "上游新增/修改"),

	/**
	 * 下游新增/修改
	 */
	downstream(130, "下游新增/修改"),

	/**
	 * 扫码交易
	 */
	scanCodeOrder(140, "扫码交易"),

	/**
	 * 配送交易
	 */
	deliveryOrder(150, "配送交易"),

	/**
	 * 进门
	 */
	inDoor(160, "进门"),

	/**
	 * 扫码交易作废
	 */
	deleteScanCodeOrder(170, "扫码交易作废"),

	/**
	 * 配送交易作废
	 */
	deleteDeliveryOrder(180, "配送交易作废"),
	/**
	 * 报备作废
	 */
	registerBillDelete(190, "报备作废"),

	/**
	 * 杭果经营户上报
	 */
	HangGuoUser(200, "杭果经营户上报"),
	/**
	 * 杭果商品信息上报
	 */
	HangGuoGoods(210, "杭果商品信息上报"),
	/**
	 * 杭果检测信息上报
	 */
	HangGuoInspection(220, "杭果检测信息上报"),
	/**
	 * 杭果不合格处置上报
	 */
	HangGuoUnqualifiedInspection(230, "杭果不合格处置上报"),
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
