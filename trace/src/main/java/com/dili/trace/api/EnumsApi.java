package com.dili.trace.api;

import java.util.List;
import java.util.Map.Entry;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.enums.*;

import com.dili.trace.glossary.UpStreamTypeEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import one.util.streamex.StreamEx;

/**
 * 枚举变量接口
 */
@RestController
@RequestMapping(value = "/api/enums")
@AppAccess(role = Role.ANY)
public class EnumsApi {
	/**
	 * 上游类型枚举查询
	 */
	@RequestMapping(value = "/listUpStreamTypeEnum.api", method = RequestMethod.POST)
	public BaseOutput<List<Entry<Integer, String>>> listUpStreamTypeEnum() {
		try {
			List<Entry<Integer, String>> list = StreamEx.of(UpStreamTypeEnum.values())
					.mapToEntry(UpStreamTypeEnum::getCode, UpStreamTypeEnum::getName).toList();
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
	}
	/**
	 * 客户类型枚举查询
	 */
	@RequestMapping(value = "/listClientTypeEnum.api", method = RequestMethod.POST)
	public BaseOutput<List<Entry<Integer, String>>> listClientTypeEnum() {
		try {
			List<Entry<Integer, String>> list = StreamEx.of(ClientTypeEnum.values())
					.mapToEntry(ClientTypeEnum::getCode, ClientTypeEnum::getDesc).toList();
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
	}
	/**
	 * 检测状态枚举查询
	 */
	@RequestMapping(value = "/listDetectStatusEnum.api", method = RequestMethod.POST)
	public BaseOutput<List<Entry<Integer, String>>> listDetectStatusEnum() {
		try {
			List<Entry<Integer, String>> list = StreamEx.of(DetectStatusEnum.values())
					.mapToEntry(DetectStatusEnum::getCode, DetectStatusEnum::getName).toList();
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
	}
	/**
	 * 检测类型枚举查询
	 */
	@RequestMapping(value = "/listDetectTypeEnum.api", method = RequestMethod.POST)
	public BaseOutput<List<Entry<Integer, String>>> listDetectTypeEnum() {
		try {
			List<Entry<Integer, String>> list = StreamEx.of(DetectTypeEnum.values())
					.mapToEntry(DetectTypeEnum::getCode, DetectTypeEnum::getName).toList();
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
	}
	/**
	 * 证明类型查询
	 */
	@RequestMapping(value = "/listImageCertType.api", method = RequestMethod.POST)
	public BaseOutput<List<Entry<Integer, String>>> listImageCertType() {
		try {
			List<Entry<Integer, String>> list = StreamEx.of(ImageCertTypeEnum.values())
					.mapToEntry(ImageCertTypeEnum::getCode, ImageCertTypeEnum::getName).toList();
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
	}


	/**
	 * 报备单审核状态查询
	 */
	@RequestMapping(value = "/listBillVerifyStatus.api", method = RequestMethod.POST)
	public BaseOutput<List<Entry<Integer, String>>> listBillVerifyStatus() {
		try {
			List<Entry<Integer, String>> list = StreamEx.of(BillVerifyStatusEnum.values())
					.mapToEntry(BillVerifyStatusEnum::getCode, BillVerifyStatusEnum::getName).toList();
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
	}
	/**
	 * 商品保存类型查询
	 */
	@RequestMapping(value = "/listPreserveType.api", method = RequestMethod.POST)
	public BaseOutput<List<Entry<Integer, String>>> listPreserveType() {
		try {
			List<Entry<Integer, String>> list = StreamEx.of(PreserveTypeEnum.values())
					.mapToEntry(PreserveTypeEnum::getCode, PreserveTypeEnum::getName).toList();
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
	}
	/**
	 * 登记单类型查询
	 */
	@RequestMapping(value = "/listBillType.api", method = RequestMethod.POST)
	public BaseOutput<List<Entry<Integer, String>>> listBillType() {
		try {
			List<Entry<Integer, String>> list = StreamEx.of(BillTypeEnum.values())
					.mapToEntry(BillTypeEnum::getCode, BillTypeEnum::getName).toList();
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
	}
	/**
	 * 登记类型查询
	 */
	@RequestMapping(value = "/listRegistType.api", method = RequestMethod.POST)
	public BaseOutput<List<Entry<Integer, String>>> listRegistType() {
		try {
			List<Entry<Integer, String>> list = StreamEx.of(RegistTypeEnum.values())
					.mapToEntry(RegistTypeEnum::getCode, RegistTypeEnum::getName).toList();
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
	}
	/**
	 * 报备单类型查询
	 */
	@RequestMapping(value = "/listTruckType.api", method = RequestMethod.POST)
	public BaseOutput<List<Entry<Integer, String>>> listTruckType() {
		try {
			List<Entry<Integer, String>> list = StreamEx.of(TruckTypeEnum.values())
					.mapToEntry(TruckTypeEnum::getCode, TruckTypeEnum::getName).toList();
			return BaseOutput.success().setData(list);
		} catch (Exception e) {
			return BaseOutput.failure(e.getMessage());
		}
	}

}
