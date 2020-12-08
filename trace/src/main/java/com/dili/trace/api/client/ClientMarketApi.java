package com.dili.trace.api.client;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.Market;
import com.dili.trace.service.MarketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 市场相关接口
 *
 * @author Alvin.li
 */
@RestController
@RequestMapping(value = "/api/client/market")
@Api(value = "/api/client/market", description = "市场相关接口")
@AppAccess(role = Role.Client,url = "",subRoles = {CustomerEnum.CharacterType.经营户, CustomerEnum.CharacterType.买家})
public class ClientMarketApi {
	private static final Logger logger = LoggerFactory.getLogger(ClientMarketApi.class);

	@Autowired
	MarketService marketService;

	/**
	 * 查询市场列表
	 * @return
	 */
	@ApiOperation("查询市场列表")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list.api", method = {RequestMethod.GET, RequestMethod.POST})
	public BaseOutput<List<Market>> list() {
		try {
			Optional<List<Market>> markets = marketService.getMarkets();
			return BaseOutput.success().setData(markets.orElse(new ArrayList<>()));
		} catch (TraceBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			logger.error("查询市场列表出错", e);
			return BaseOutput.failure("查询市场列表出错");
		}
	}
}
