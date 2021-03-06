package com.dili.trace.api.client;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.UserStore;
import com.dili.trace.rpc.service.CustomerRpcService;
import com.dili.trace.service.UserStoreService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 用户店铺相关接口
 * @author asa.lee
 */
@Api(value = "/api/client/clientUserStoreApi")
@RestController
@AppAccess(role = Role.Client,url = "",subRoles = {CustomerEnum.CharacterType.经营户, CustomerEnum.CharacterType.买家})
@RequestMapping(value = "/api/client/clientUserStoreApi")
public class ClientUserStoreApi {

    private static final Logger logger = LoggerFactory.getLogger(ClientUserStoreApi.class);
    @Autowired
    UserStoreService userStoreService;
    @Autowired
    private LoginSessionContext sessionContext;
    @Autowired
    CustomerRpcService customerRpcService;

    /**
     * 查询当前用户的用户店铺名称
     */
    @RequestMapping(value = "/getUserStore.api", method = {RequestMethod.POST})
    public BaseOutput<UserStore> getUserStore(@RequestBody UserStore userStore) {
        try {
            Long userId = userStore.getUserId();
            if (null == userId) {
                return BaseOutput.failure("userid为空");
            }
            userStore.setSort("modified");
            userStore.setOrder("desc");
            List<UserStore> storeList =userStoreService.listByExample(userStore);
            UserStore store=new UserStore();
            if(storeList.isEmpty()) {
                CustomerExtendDto customer = customerRpcService.findCustomerByIdOrEx(userId,
                        this.sessionContext.getSessionData().getMarketId());
                store.setStoreName(customer.getName());
                return BaseOutput.success().setData(store);
            }
            store=storeList.get(0);
            return BaseOutput.success("success").setData(store);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询出错");
        }

    }

    /**
     * 修改当前用户的用户店铺名称
     */
    @RequestMapping(value = "/updateUserStore.api", method = {RequestMethod.POST})
    public BaseOutput<UserStore> updateUserStore(@RequestBody UserStore input) {
        try {

            SessionData sessionData=this.sessionContext.getSessionData();

            input.setUserId(sessionData.getUserId());
            input.setUserName(sessionData.getUserName());
            input.setMarketId(sessionData.getMarketId());
            input.setMarketName(sessionData.getMarketName());
            logger.debug("input={}",input);
            this.userStoreService.insertOrUpdateStore(input);
            return BaseOutput.success("success");
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }

    }

}
