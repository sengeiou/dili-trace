package com.dili.trace.api.client;

import java.util.List;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.input.UserPlateQueryDto;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.service.UserPlateService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(value = "/api/client/clientUserPlateApi")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/client/clientUserPlateApi")
public class ClientUserPlateApi {
    private static final Logger logger = LoggerFactory.getLogger(ClientUserPlateApi.class);
    @Resource
    private LoginSessionContext sessionContext;
    @Resource
    private UserPlateService userPlateService;

    @SuppressWarnings({ "unchecked" })
    @RequestMapping(value = "/listPage.api", method = { RequestMethod.POST })
    public BaseOutput<List<UserPlate>> listPage(@RequestBody UserPlateQueryDto condition) {
        try {
            Long userId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            condition.setUserId(userId);
            if(StringUtils.isNotBlank(condition.getLikePlate())){
                condition.setLikePlate(condition.getLikePlate().trim().toUpperCase());
            }
            List<UserPlate> data = this.userPlateService.listByExample(condition);

            return BaseOutput.success().setData(data);
        } catch (TraceBusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询出错");
        }

    }

}