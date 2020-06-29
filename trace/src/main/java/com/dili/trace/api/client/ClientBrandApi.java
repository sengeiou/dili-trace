package com.dili.trace.api.client;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.CreateRegisterBillInputDto;
import com.dili.trace.api.input.RegisterBillApiInputDto;
import com.dili.trace.api.output.RegisterBillOutput;
import com.dili.trace.domain.Brand;
import com.dili.trace.domain.ImageCert;
import com.dili.trace.domain.RegisterBill;
import com.dili.trace.domain.User;
import com.dili.trace.dto.CreateListBillParam;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.RegisterBillDto;
import com.dili.trace.dto.RegisterBillOutputDto;
import com.dili.trace.service.BrandService;
import com.dili.trace.service.ImageCertService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.UserService;
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
	public BaseOutput<BasePage<Brand>> listPage() {
		try {
			Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
			logger.info("获取登记单列表 操作用户:{}", userId);
			Brand query = new Brand();
			query.setUserId(userId);
			if (StringUtils.isBlank(query.getOrder())) {
				query.setOrder("desc");
				query.setSort("created");
			}
			BasePage basePage = BasePageUtil.convert(brandService.listPageByExample(query), bill -> {
				Map<Object, Object> map = new BeanMap(bill);
				// 将id换为billId字段
				map.put("brandId", map.remove("id"));
				return map;

			});
			return BaseOutput.success().setData(basePage);
		} catch (TraceBusinessException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			return BaseOutput.failure("查询数据出错");
		}

	}

}
