package com.dili.trace.api.client;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.output.BrandOutputDto;
import com.dili.trace.domain.TallyAreaNo;
import com.dili.trace.service.TallyAreaNoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 区位相关接口
 *
 * @author Lily
 */
@RestController
@RequestMapping(value = "/api/client/clientTallyAreaNo")
@Api(value = "/api/client/clientTallyAreaNo", description = "区位相关接口")
@InterceptConfiguration
public class ClientTallyAreaNoApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientTallyAreaNoApi.class);
	@Autowired
	private LoginSessionContext sessionContext;
	@Autowired
	TallyAreaNoService tallyAreaNoService;

	@ApiOperation(value = "获取区位列表")
	@ApiImplicitParam(paramType = "body", name = "TallyAreaNo", dataType = "TallyAreaNo", value = "获取区位列表")
	@RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
	public BaseOutput<List<TallyAreaNo>> listPage(@RequestBody TallyAreaNo inputDto) {
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			logger.info("获取区位列表 操作用户:{}", userId);
			if (StringUtils.isBlank(inputDto.getOrder())) {
				inputDto.setOrder("desc");
				inputDto.setSort("created");
			}
			List<TallyAreaNo> list = tallyAreaNoService.listByExample(inputDto);
			return BaseOutput.success().setData(list);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return BaseOutput.failure("查询数据出错");
		}

	}
}
