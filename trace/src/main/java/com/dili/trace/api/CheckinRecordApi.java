package com.dili.trace.api;

import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.SessionContext;
import com.dili.common.exception.BusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.dto.CheckInApiDetailOutput;
import com.dili.trace.api.dto.CheckInApiInput;
import com.dili.trace.api.dto.CheckInApiListOutput;
import com.dili.trace.domain.CheckinRecord;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.service.CheckinRecordService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;

import io.swagger.annotations.Api;

@Api(value = "/api/checkinRecordApi")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/checkinRecordApi")
public class CheckinRecordApi {
	private static final Logger logger = LoggerFactory.getLogger(UserApi.class);
	@Resource
	private UserService userService;
	@Resource
	private SessionContext sessionContext;
	@Autowired
	RegisterBillService registerBillService;
	@Autowired
	CheckinRecordService checkinRecordService;

	/**
	 * 分页查询需要被进场查询的信息
	 */
	@RequestMapping(value = "/listPagedAvailableCheckInData.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<BasePage<RegisterBill>> listPagedAvailableCheckInData(@RequestBody RegisterBillDto query) {
//        User user = userService.get(sessionContext.getAccountId());
//        if (user == null) {
//            return BaseOutput.failure("未登陆用户");
//        }
		logger.info(sessionContext.getSessionId());
		logger.info("{?}",sessionContext.getAccountId());
		logger.info("{?}",sessionContext.getMap());
		logger.info("{?}",sessionContext.getUserType());
		logger.info("{?}",sessionContext.getRelationId());
		BasePage<CheckInApiListOutput> page = this.checkinRecordService.listCheckInApiListOutputPage(query);

		return BaseOutput.success().setData(page);
	}

	/**
	 * 分页查询需要被进场查询的信息
	 */
	@RequestMapping(value = "/getCheckInDetail.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<CheckInApiDetailOutput> getCheckInDetail(@RequestBody RegisterBillDto query) {
//		User user = userService.get(sessionContext.getAccountId());
//		if (user == null) {
//			return BaseOutput.failure("未登陆用户");
//		}
		if (query == null || query.getId() == null) {
			return BaseOutput.failure("参数错误");
		}
		CheckInApiDetailOutput detail = this.checkinRecordService.getCheckInDetail(query.getId()).orElse(null);
		if (detail != null) {
			return BaseOutput.success().setData(detail);
		}
		return BaseOutput.failure("没有查找到详情");

	}

	/**
	 * 进场
	 */
	@RequestMapping(value = "/doCheckin.api", method = { RequestMethod.POST, RequestMethod.GET })
	public BaseOutput<Long> doCheckin(@RequestBody CheckInApiInput input) {
//		User user = userService.get(sessionContext.getAccountId());
//		if (user == null) {
//			return BaseOutput.failure("未登陆用户");
//		}
		try {
			CheckinRecord checkinRecord = this.checkinRecordService.doCheckin(new OperatorUser(0L, "test"), input);
			return BaseOutput.success().setData(checkinRecord.getId());
		} catch (BusinessException e) {

			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}
	}
}