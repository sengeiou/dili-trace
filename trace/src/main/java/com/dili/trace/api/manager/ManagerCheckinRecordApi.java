package com.dili.trace.api.manager;

import javax.annotation.Resource;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CheckinOutRecordQueryDto;
import com.dili.trace.domain.CheckinOutRecord;
import com.dili.trace.service.CheckinOutRecordService;

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
@RequestMapping(value = "/api/manager/managerCheckinRecordApi")
@Api(value = "/api/manager/managerCheckinRecordApi", description = "进门数据接口")
public class ManagerCheckinRecordApi {
	private static final Logger logger = LoggerFactory.getLogger(ManagerCheckinRecordApi.class);
	@Resource
	private LoginSessionContext sessionContext;

	@Autowired
	CheckinOutRecordService checkinOutRecordService;

	@ApiOperation(value = "获得当前管理员进门操作数据列表")
	@RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
	public BaseOutput<BasePage<CheckinOutRecord>> listPage(@RequestBody CheckinOutRecordQueryDto query) {
		try {
			Long operatorUserId = sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.SYS_MANAGER).getId();
			query.setOperatorId(operatorUserId);
			query.setSort("created");
			query.setOrder("desc");
			BasePage<CheckinOutRecord> page = this.checkinOutRecordService.listPageByExample(query);
			return BaseOutput.success().setData(page);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("服务端出错");
		}

	}
}
