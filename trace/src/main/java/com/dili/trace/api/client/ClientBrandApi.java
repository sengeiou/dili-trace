package com.dili.trace.api.client;

import java.util.Map;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.BrandInputDto;
import com.dili.trace.api.output.BrandOutputDto;
import com.dili.trace.domain.Brand;
import com.dili.trace.service.BrandService;
import com.dili.trace.util.BasePageUtil;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * Created by wangguofeng
 */
@RestController
@RequestMapping(value = "/api/client/clientBrand")
@Api(value = "/api/client/clientBrand", description = "品牌相关接口")
@InterceptConfiguration
public class ClientBrandApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientBrandApi.class);
	@Resource
	private LoginSessionContext sessionContext;
	@Autowired
	BrandService brandService;

	@ApiOperation(value = "获取品牌列表")
	@ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
	@RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
	public BaseOutput<BasePage<BrandOutputDto>> listPage(@RequestBody BrandInputDto inputDto) {
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			logger.info("获取登记单列表 操作用户:{}", userId);
			inputDto.setUserId(userId);
			if (StringUtils.isBlank(inputDto.getOrder())) {
				inputDto.setOrder("desc");
				inputDto.setSort("created");
			}
			BasePage basePage = BasePageUtil.convert(brandService.listPageByExample(inputDto), BrandOutputDto::build);
			return BaseOutput.success().setData(basePage);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("查询数据出错");
		}

	}

}
