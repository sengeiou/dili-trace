package com.dili.trace.api.client;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.domain.UpStream;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.service.UpStreamService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * 上游信息相关api
 */
@Api(value = "/api/upstreamApi")
@RestController
@AppAccess(role = Role.Client)
@RequestMapping(value = "/api/upstreamApi")
public class ClientUpStreamApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientUpStreamApi.class);
    @Autowired
    private LoginSessionContext sessionContext;
    @Autowired
    UpStreamService upStreamService;

    /**
     * 分页查询上游信息
     */
    @RequestMapping(value = "/listPagedUpStream.api", method = { RequestMethod.POST, RequestMethod.GET })
    public BaseOutput<BasePage<UpStream>> listPagedUpStream(@RequestBody UpStreamDto query) {
        if (StringUtils.isBlank(query.getOrder())) {
            query.setOrder("desc");
            query.setSort("created");
        }
        query.setUserIds(Arrays.asList(this.sessionContext.getSessionData().getUserId()));
        try {
            BasePage<UpStream> data = this.upStreamService.listPageUpStream(query);
            return BaseOutput.success().setData(data);
        } catch (TraceBizException e) {
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

        try {
            this.upStreamService.deleteUpstream(sessionContext.getAccountId(), input.getId());
            return BaseOutput.success();
        } catch (TraceBizException e) {
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
        Long userId = sessionContext.getAccountId();
        if(input.getSourceUserId() != null){
            userId = input.getSourceUserId();
        }

        try {
            input.setUserIds(Arrays.asList(userId));
            input.setSourceUserId(userId);
            //市场id
            input.setMarketId(sessionContext.getSessionData().getMarketId());
            return this.upStreamService.addUpstream(input,new OperatorUser(userId,sessionContext.getUserName()),true);
        } catch (TraceBizException e) {
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
        Long userId=sessionContext.getAccountId();

        try {
            input.setUserIds(Arrays.asList(userId));
            return this.upStreamService.updateUpstream(input,new OperatorUser(userId,sessionContext.getUserName()));
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

}
