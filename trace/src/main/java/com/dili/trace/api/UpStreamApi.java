package com.dili.trace.api;

import java.util.Arrays;

import javax.annotation.Resource;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.SessionContext;
import com.dili.common.exception.BusinessException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.domain.UpStream;
import com.dili.trace.domain.User;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    UpStreamService upStreamService;

    /**
     * 分页查询上游信息
     */
    @RequestMapping(value = "/listPagedUpStream.api", method = { RequestMethod.POST, RequestMethod.GET })
    public BaseOutput<BasePage<UpStream>> listPagedUpStream(@RequestBody UpStream query) {
        User user = userService.get(sessionContext.getAccountId());
        if (user == null) {
            return BaseOutput.failure("未登陆用户");
        }
        try {
            BasePage<UpStream> data = this.upStreamService.listPageUpStream(user.getId(),query);
            return BaseOutput.success().setData(data);
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
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
            this.upStreamService.deleteUpstream(user.getId(), input.getId());
            return BaseOutput.success();
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

    /**
     * 创建上游信息
     */
    @RequestMapping(value = "/doCreateUpStream.api", method = RequestMethod.POST)
    public BaseOutput<Long> doCreateUpStream(@RequestBody UpStreamDto input) {
        User user = userService.get(sessionContext.getAccountId());
        if (user == null) {
            return BaseOutput.failure("未登陆用户");
        }
        try {
            input.setUserIds(Arrays.asList(user.getId()));
            return this.upStreamService.addUpstream(input,new OperatorUser(user.getId(),user.getName()));
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

    /**
     * 修改上游信息
     */
    @RequestMapping(value = "/doModifyUpStream.api", method = RequestMethod.POST)
    public BaseOutput doModifyUpStream(@RequestBody UpStreamDto input) {
        User user = userService.get(sessionContext.getAccountId());
        if (user == null) {
            return BaseOutput.failure("未登陆用户");
        }
        try {
            return this.upStreamService.updateUpstream(input,new OperatorUser(user.getId(),user.getName()));
        } catch (BusinessException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

}
