package com.dili.trace.api.client;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.domain.TradeOrder;
import com.dili.trace.service.TradeOrderService;

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

@SuppressWarnings("deprecation")
@Api(value = "/api/client/clientTradeOrderApi")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/client/clientTradeOrderApi")
public class ClientTradeOrderApi {
    private static final Logger logger = LoggerFactory.getLogger(ClientTradeOrderApi.class);

    @Autowired
    LoginSessionContext sessionContext;
    @Autowired
    TradeOrderService tradeOrderService;


    @ApiOperation(value = "获取订单列表")
    @ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
    @RequestMapping(value = "/listPage.api", method = RequestMethod.POST)
    public BaseOutput<BasePage<TradeOrder>> listPage(@RequestBody TradeOrder inputDto) {
        try {
            Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            logger.info("订单列表 操作用户:{}", userId);
            inputDto.setBuyerId(userId);
            if (StringUtils.isBlank(inputDto.getOrder())) {
                inputDto.setOrder("desc");
                inputDto.setSort("created");
            }
            BasePage<TradeOrder> page = tradeOrderService.listPageByExample(inputDto);
            return BaseOutput.success().setData(page);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询数据出错");
        }

    }
    
    @ApiOperation(value = "获取订单列表")
    @ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
    @RequestMapping(value = "/viewTradeOrder.api", method = RequestMethod.POST)
    public BaseOutput<BasePage<TradeOrder>> viewTradeOrder(@RequestBody TradeOrder inputDto) {
        try {
            Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            logger.info("订单列表 操作用户:{}", userId);
            inputDto.setBuyerId(userId);
            if (StringUtils.isBlank(inputDto.getOrder())) {
                inputDto.setOrder("desc");
                inputDto.setSort("created");
            }
            BasePage<TradeOrder> page = tradeOrderService.listPageByExample(inputDto);
            return BaseOutput.success().setData(page);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询数据出错");
        }

    }
}