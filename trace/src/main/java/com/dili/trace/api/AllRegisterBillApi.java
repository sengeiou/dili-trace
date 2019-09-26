package com.dili.trace.api;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.SessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.QualityTraceTradeBill;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.SeparateSalesRecord;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.QualityTraceTradeBillOutDto;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.dto.SeparateSalesRecordDTO;
import com.dili.trace.glossary.RegisterBillStateEnum;
import com.dili.trace.glossary.RegisterSourceEnum;
import com.dili.trace.glossary.SalesTypeEnum;
import com.dili.trace.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;

/**
 * Created by laikui on 2019/7/26.
 */
@RestController
@RequestMapping(value = "/api/allRegisterBillApi")
@Api(value = "/api/allRegisterBillApi", description = "登记单相关接口")
public class AllRegisterBillApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(AllRegisterBillApi.class);
	@Autowired
	private RegisterBillService registerBillService;

	@ApiOperation(value = "通过登记单ID获取登记单详细信息")
	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	public BaseOutput<RegisterBillOutputDto> getRegisterBill(@PathVariable Long id) {
		LOGGER.info("获取登记单:" + id);

		RegisterBill registerBill = registerBillService.get(id);
		if (registerBill == null) {
			LOGGER.error("获取登记单失败id:" + id);
			return BaseOutput.failure();
		}
		RegisterBillOutputDto bill = registerBillService.conversionDetailOutput(registerBill);

		return BaseOutput.success().setData(bill);
	}

	@ApiOperation(value = "查询登记单", httpMethod = "GET", notes = "productName=?")
	@RequestMapping(value = "/listAllRegisterBill", method = RequestMethod.POST)
	public BaseOutput<List<RegisterBill>> listAllRegisterBill(RegisterBillDto input) {
		if(input.getPage()==null) {
			input.setPage(1);
		}
		if(input.getRows()==null) {
			input.setRows(10);
		}
		input.setSort("created");
		input.setOrder("desc");
		LOGGER.info("查询登记单:{}", input);
		RegisterBillDto condition = DTOUtils.newDTO(RegisterBillDto.class);
		condition.setState(input.getState());
		condition.setDetectState(input.getDetectState());
		condition.setPage(input.getPage());
		condition.setRows(input.getRows());
		condition.setSort(input.getSort());
		condition.setOrder(input.getOrder());
		BasePage<RegisterBill> list = this.registerBillService.listPageByExample(condition);

		return BaseOutput.success().setData(list);

	}
}
