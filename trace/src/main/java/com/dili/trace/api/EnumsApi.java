package com.dili.trace.api;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ss.domain.BaseOutput;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.enums.ImageCertTypeEnum;
import com.dili.trace.enums.PreserveTypeEnum;

import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;

@RestController
@RequestMapping(value = "/api/enums")
public class EnumsApi {
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

}
