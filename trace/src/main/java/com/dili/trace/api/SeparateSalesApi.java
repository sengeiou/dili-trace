package com.dili.trace.api;


import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.SessionContext;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.trace.api.dto.SeparateSalesApiListOutput;
import com.dili.trace.api.dto.SeparateSalesApiListQueryInput;
import com.dili.trace.domain.User;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SeparateSalesRecordService;
import com.dili.trace.service.UserService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/separateSales")
@Api(value = "/api/separateSales", description = "登记单相关接口")
@InterceptConfiguration
public class SeparateSalesApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeparateSalesApi.class);
    @Resource
    private SessionContext sessionContext;
    @Autowired
    UserService userService;
    @Autowired
    RegisterBillService registerBillService;

    @Autowired
    SeparateSalesRecordService separateSalesRecordService;

    @ApiOperation(value = "获取登记单列表")
    @ApiImplicitParam(paramType = "body", name = "RegisterBill", dataType = "RegisterBill", value = "获取登记单列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    // @InterceptConfiguration(loginRequired=false)
    public BaseOutput<EasyuiPageOutput> list(SeparateSalesApiListQueryInput input) throws Exception {
        LOGGER.info("获取登记单列表:" + JSON.toJSON(input).toString());
        User user = userService.get(sessionContext.getAccountId());
        if (user == null) {
            return BaseOutput.failure("未登陆用户");
        }
        LOGGER.info("获取登记单列表 操作用户:" + JSON.toJSONString(user));
        input.setUserId(user.getId());
        if (StringUtils.isBlank(input.getOrder())) {
            input.setOrder("desc");
            input.setSort("id");
        }
        EasyuiPageOutput easyuiPageOutput = this.separateSalesRecordService.listPageByQueryInput(input);
        return BaseOutput.success().setData(easyuiPageOutput);
    }

    @ApiOperation(value = "通过ID获取详细信息")
    @RequestMapping(value = "id/{id}", method = RequestMethod.GET)
    public BaseOutput<SeparateSalesApiListOutput> getSeparateSalesRecord(@PathVariable Long id) {
        LOGGER.info("获取登记单:" + id);
        User user = userService.get(sessionContext.getAccountId());
        if (user == null) {
            return BaseOutput.failure("未登陆用户");
        }
        SeparateSalesApiListQueryInput queryInput = new SeparateSalesApiListQueryInput();
        queryInput.setUserId(user.getId());
        queryInput.setId(id);

        SeparateSalesApiListOutput data = this.separateSalesRecordService.listByQueryInput(queryInput).stream()
                .findFirst().orElse(null);
        return BaseOutput.success().setData(data);
    }
}