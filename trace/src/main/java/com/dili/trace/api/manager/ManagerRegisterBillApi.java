package com.dili.trace.api.manager;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.BusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.RegisterBillApi;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.output.RegisterBillOutput;
import com.dili.trace.api.output.VerifyBillInputDto;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.enums.BillVerifyStatusEnum;
import com.dili.trace.glossary.ColorEnum;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;
import com.dili.trace.util.BasePageUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;

@RestController
@RequestMapping(value = "/api/manager/manageRegisterBill")
@Api(value = "/api/manager/manageRegisterBill", description = "登记单相关接口")
public class ManagerRegisterBillApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterBillApi.class);
	@Autowired
	private RegisterBillService registerBillService;
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	UserService userService;

	@ApiOperation(value = "获得指定用户登记单信息")
	@RequestMapping(value = "/listVerifyableBill.api", method = RequestMethod.POST)
	public BaseOutput<BasePage<RegisterBill>> listVerifyableBill(@RequestBody RegisterBill input) {
		if(input==null||input.getUserId()==null) {
			return BaseOutput.failure("参数错误");
		}
		try {
			OperatorUser operatorUser=sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			RegisterBillDto query = new RegisterBillDto();
			query.setSort("modified");
			query.setOrder("desc");
			query.setUserId(input.getUserId());
			BasePage<RegisterBill> page = this.registerBillService.listPageByExample(query);

			List<RegisterBillOutput> list = StreamEx.of(page.getDatas()).map(rb -> {
				RegisterBillOutput dto = new RegisterBillOutput();
				dto.setVerifyStatus(rb.getVerifyStatus());
				dto.setVerifyStatusDesc(BillVerifyStatusEnum.fromCode(rb.getVerifyStatus())
						.map(BillVerifyStatusEnum::getName).orElse(""));
				dto.setBillId(rb.getId());
				dto.setProductName(rb.getProductName());
				dto.setColor(ColorEnum.GREEN.getCode());
				return dto;
			}).toList();

			return BaseOutput.success().setData(BasePageUtil.convert(list, page));
		} catch (BusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("操作失败：服务端出错");
		}
	}

	@ApiOperation(value = "查验登记单")
	@RequestMapping(value = "/doVerify.api", method = RequestMethod.POST)
	public BaseOutput<Long> doVerify(@RequestBody VerifyBillInputDto inputDto) {
		LOGGER.info("通过ID查验登记单:{}", inputDto);
		try {
			if (inputDto == null || inputDto.getVerifyStatus() == null||inputDto.getBillId()==null) {
				return BaseOutput.failure("参数错误");
			}
			OperatorUser operatorUser=sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER);
			RegisterBill input = new RegisterBill();
			input.setId(inputDto.getBillId());
			input.setVerifyStatus(inputDto.getVerifyStatus());
			Long id = this.registerBillService.doVerify(input);
			return BaseOutput.success().setData(id);
		} catch (BusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("操作失败：服务端出错");
		}

	}
}
