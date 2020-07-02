package com.dili.trace.api.manager;

import java.util.List;

import javax.annotation.Resource;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.output.RegisterBillOutput;
import com.dili.trace.api.output.VerifyStatusCountOutputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.UpStream;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;

@RestController
@RequestMapping(value = "/api/manager/managerRegisterBill")
@Api(value = "/api/manager/managerRegisterBill", description = "登记单相关接口")
public class ManagerRegisterBillApi {
	private static final Logger logger = LoggerFactory.getLogger(ManagerRegisterBillApi.class);
	@Autowired
	private RegisterBillService registerBillService;
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	UserService userService;
	@Autowired
	UpStreamService upStreamService;
	@Autowired
	ImageCertService imageCertService;

	@ApiOperation(value = "获得登记单详情")
	@RequestMapping(value = "/viewRegisterBill.api", method = RequestMethod.POST)
	public BaseOutput<BasePage<RegisterBill>> viewRegisterBill(@RequestBody RegisterBillDto input) {
		if (input == null || input.getBillId() == null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			RegisterBill billItem = this.registerBillService.get(input.getBillId());
			if(billItem==null){
				return BaseOutput.failure("数据不存在");
			}

			RegisterBillOutput dto = RegisterBillOutput.build(billItem);
			
			String upStreamName=StreamEx.ofNullable(billItem.getUpStreamId()).nonNull()
			.map(upId->{return this.upStreamService.get(upId);})
			.nonNull().map(UpStream::getName).findFirst().orElse("");
			dto.setUpStreamName(upStreamName);
			dto.setImageCertList(this.imageCertService.findImageCertListByBillId(billItem.getId()));
			return BaseOutput.success().setData(dto);

		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("操作失败：服务端出错");
		}
	}
		/**
	 * 不同审核状态数据统计
	 */
	@RequestMapping(value = "/countByVerifyStatus.api", method = { RequestMethod.POST })
	public BaseOutput<List<VerifyStatusCountOutputDto>> countByVerifyStatus(@RequestBody RegisterBillDto query) {

		try {
			OperatorUser operatorUser = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			List<VerifyStatusCountOutputDto>list= this.registerBillService.countByVerifyStatus(query);
			return BaseOutput.success().setData(list);

		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("操作失败：服务端出错");
		}
	

	}

}
