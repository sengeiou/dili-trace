package com.dili.trace.api;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.service.DetectRecordService;
import com.dili.trace.service.RegisterBillService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
	@Autowired
	private DetectRecordService detectRecordService;

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
		condition.setStateList(input.getStateList());
		condition.setDetectStateList(input.getDetectStateList());
		BasePage<RegisterBill> list = this.registerBillService.listPageByExample(condition);

		return BaseOutput.success().setData(list);

	}
	
	 /**
     * 保存检查单
     * @param detectRecord
     * @return
     */
    @ApiOperation("查询部分检测记录")
    @RequestMapping(value = "/findTop2AndLatest",method = RequestMethod.POST)
    public BaseOutput<List<DetectRecord>> findTop2AndLatest(RegisterBill registerBill){
    	if(registerBill==null||StringUtils.isBlank(registerBill.getCode())) {
    		return BaseOutput.failure("参数错误");
    	}
    	
    	List<DetectRecord> list=this.detectRecordService.findTop2AndLatest(registerBill.getCode().trim());
    	return BaseOutput.success().setData(list);
    	
    }
}
