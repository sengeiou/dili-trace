package com.dili.trace.api;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.RegisterBillApiInputDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.service.RegisterBillService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/registerBillApi")
@Api(value = "/api/registerBillApi", description = "登记单相关接口")
@InterceptConfiguration
public class RegisterBillApi {
	private static final Logger logger = LoggerFactory.getLogger(RegisterBillApi.class);
	@Autowired
	private RegisterBillService registerBillService;
	@Resource
	private LoginSessionContext sessionContext;

	@ApiOperation(value = "通过登记单ID获取登记单详细信息")
	@RequestMapping(value = "/viewTradeDetailBill.api", method = RequestMethod.POST)
	public BaseOutput<RegisterBillOutputDto> viewTradeDetailBill(@RequestBody RegisterBillApiInputDto inputDto) {
		if (inputDto == null || (inputDto.getBillId() == null && inputDto.getTradeDetailId() == null)) {
			return BaseOutput.failure("参数错误");
		}

		logger.info("获取登记单详细信息->billId:{},tradeDetailId:{}", inputDto.getBillId(), inputDto.getTradeDetailId());
		try {
			Long userId = this.sessionContext.getAccountId();
			if(userId==null){
				return BaseOutput.failure("你还未登录");
			}
			RegisterBillOutputDto outputdto = this.registerBillService.viewTradeDetailBill(inputDto.getBillId(),
					inputDto.getTradeDetailId());

			return BaseOutput.success().setData(outputdto);

		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}

}
