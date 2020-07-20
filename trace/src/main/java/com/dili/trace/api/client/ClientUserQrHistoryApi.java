package com.dili.trace.api.client;

import java.util.List;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.UserPlateQueryDto;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.domain.UserQrHistory;
import com.dili.trace.glossary.TFEnum;
import com.dili.trace.service.UserQrHistoryService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/client/clientUserQrHistoryApi")
public class ClientUserQrHistoryApi {
    private static final Logger logger = LoggerFactory.getLogger(ClientUserQrHistoryApi.class);
    @Resource
    private LoginSessionContext sessionContext;

    @Autowired
    UserQrHistoryService userQrHistoryService;

    @SuppressWarnings({ "unchecked" })
    @RequestMapping(value = "/listPage.api", method = { RequestMethod.POST })
    public BaseOutput<List<UserPlate>> listPage(@RequestBody UserQrHistory condition) {
        if (condition == null || condition.getUserId() == null) {
            return BaseOutput.failure("参数错误");
        }
        try {
            Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            logger.info("当前登录用户:{}",userId);
            // condition.setUserId(userId);
            condition.setSort("created");
            condition.setOrder("desc");
            condition.setIsValid(TFEnum.TRUE.getCode());
            BasePage<UserQrHistory> data = this.userQrHistoryService.listPageByExample(condition);
            return BaseOutput.success().setData(data);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询出错");
        }

    }
}