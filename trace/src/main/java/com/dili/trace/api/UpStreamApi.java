package com.dili.trace.api;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.SessionContext;
import com.dili.common.exception.BusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.rpc.MessageRpc;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * 上游信息相关api
 */
@Api(value = "/api/upstreamApi")
@RestController
@InterceptConfiguration
@RequestMapping(value = "/api/upstreamApi")
public class UpStreamApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserApi.class);
    @Resource
    private UserService userService;
    @Resource
    private SessionContext sessionContext;

    /**
     * 分页查询上游信息
     */
    @RequestMapping(value = "/listPagedUpStream.api", method = RequestMethod.POST)
    public BaseOutput<Long> listPagedUpStream(@RequestBody UpStream input) {
        User user = userService.get(sessionContext.getAccountId());
        if (user == null) {
            return BaseOutput.failure("未登陆用户");
        }
        try {
            return BaseOutput.success();
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("register", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 删除上游信息
     */
    @RequestMapping(value = "/deleteUpStream.api", method = RequestMethod.POST)
    public BaseOutput<Long> deleteUpStream(@RequestBody UpStream input) {
        User user = userService.get(sessionContext.getAccountId());
        if (user == null) {
            return BaseOutput.failure("未登陆用户");
        }
        try {
            return BaseOutput.success();
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("register", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 创建上游信息
     */
    @RequestMapping(value = "/doCreateUpStream.api", method = RequestMethod.POST)
    public BaseOutput<Long> doCreateUpStream(@RequestBody UpStream input) {
        User user = userService.get(sessionContext.getAccountId());
        if (user == null) {
            return BaseOutput.failure("未登陆用户");
        }
        try {
            return BaseOutput.success();
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("register", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 修改上游信息
     */
    @RequestMapping(value = "/doModifyUpStream.api", method = RequestMethod.POST)
    public BaseOutput<Long> doModifyUpStream(@RequestBody UpStream input) {
        User user = userService.get(sessionContext.getAccountId());
        if (user == null) {
            return BaseOutput.failure("未登陆用户");
        }
        try {
            return BaseOutput.success();
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("register", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

}
