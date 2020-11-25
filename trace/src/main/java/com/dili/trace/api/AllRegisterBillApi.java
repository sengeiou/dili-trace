package com.dili.trace.api;

import java.util.List;

import com.dili.sg.trace.glossary.BillTypeEnum;
import com.dili.trace.service.BillService;
import com.dili.trace.service.SgRegisterBillService;
import com.dili.trace.domain.DetectRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.service.DetectRecordService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;

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
	private BillService billService;
	
	@Autowired
	private SgRegisterBillService registerBillService;
	@Autowired
	private DetectRecordService detectRecordService;

	/**
	 * 通过登记单ID获取登记单详细信息
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "通过登记单ID获取登记单详细信息")
	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	public BaseOutput<RegisterBillOutputDto> getRegisterBill(@PathVariable Long id) {
		LOGGER.info("获取登记单:" + id);

		RegisterBill registerBill = billService.get(id);
		if (registerBill == null) {
			LOGGER.error("获取登记单失败id:" + id);
			return BaseOutput.failure();
		}
		RegisterBillOutputDto bill = registerBillService.conversionDetailOutput(registerBill);

		return BaseOutput.success().setData(bill);
	}

	/**
	 * 查询登记单
	 * @param input
	 * @return
	 */
	@ApiOperation(value = "查询登记单", httpMethod = "GET", notes = "productName=?")
	@RequestMapping(value = "/listAllRegisterBill", method = RequestMethod.POST)
	public BaseOutput<List<RegisterBill>>
	listAllRegisterBill(@RequestBody RegisterBillDto input) {
		if(input.getPage()==null) {
			input.setPage(1);
		}
		if(input.getRows()==null) {
			input.setRows(10);
		}
		input.setSort("created");
		input.setOrder("desc");
		LOGGER.info("查询登记单:{}", input);
		RegisterBillDto condition = new RegisterBillDto();
		condition.setState(input.getState());
		condition.setDetectState(input.getDetectState());
		condition.setPage(input.getPage());
		condition.setRows(input.getRows());
		condition.setSort(input.getSort());
		condition.setOrder(input.getOrder());
		condition.setStateList(input.getStateList());
		condition.setDetectStateList(input.getDetectStateList());
		condition.setBillType(BillTypeEnum.REGISTER_BILL.getCode());
		BasePage<RegisterBill> list = this.billService.listPageByExample(condition);

		return BaseOutput.success().setData(list);

	}
	
	 /**
     * 保存检查单
     * @param registerBill
     * @return
     */
    @ApiOperation("查询部分检测记录")
    @RequestMapping(value = "/findTop2AndLatest",method = RequestMethod.POST)
    public BaseOutput<List<DetectRecord>> findTop2AndLatest(@RequestBody RegisterBill registerBill){
    	if(registerBill==null||StringUtils.isBlank(registerBill.getCode())) {
    		return BaseOutput.failure("参数错误");
    	}
    	
    	List<DetectRecord> list=this.detectRecordService.findTop2AndLatest(registerBill.getCode().trim());
    	return BaseOutput.success().setData(list);
    	
    }
}
