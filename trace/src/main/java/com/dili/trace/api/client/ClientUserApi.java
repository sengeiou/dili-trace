package com.dili.trace.api.client;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.trace.domain.UpStream;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UpStreamDto;
import com.dili.trace.service.UpStreamService;
import com.dili.trace.service.UserService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 用戶相关api
 *
 * @author asa.lee
 */
@Api(value = "/api/client/userApi")
@RestController
@AppAccess(role = Role.Client)
@RequestMapping(value = "/api/client/userApi")
public class ClientUserApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientUserApi.class);
//    @Autowired
//    SyncRpcService syncRpcService;
//
//    /**
//     * 同步信息
//     */
//    @RequestMapping(value = "/syncUserInfo.api", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public BaseOutput syncUserInfo(Long id) {
//        if (null == id) {
//            return BaseOutput.failure("syncUserInfo id is null");
//        }
//        try {
//            syncRpcService.syncRpcUserByUserId(id);
//            return BaseOutput.success();
//        } catch (TraceBizException e) {
//            return BaseOutput.failure(e.getMessage());
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            return BaseOutput.failure("服务端出错");
//        }
//    }


}
