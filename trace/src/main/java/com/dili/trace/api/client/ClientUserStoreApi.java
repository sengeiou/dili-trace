package com.dili.trace.api.client;

import com.dili.common.annotation.Access;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.UserStore;
import com.dili.trace.service.UserService;
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

import java.util.List;

/**
 * 用户店铺相关接口
 * @author asa.lee
 */
@Api(value = "/api/client/clientUserStoreApi")
@RestController
@Access(role = Role.Client,url = "")
@RequestMapping(value = "/api/client/clientUserStoreApi")
public class ClientUserStoreApi {

    private static final Logger logger = LoggerFactory.getLogger(ClientUserStoreApi.class);
    @Autowired
    UserStoreService userStoreService;
    @Autowired
    UserService userService;
    @Autowired
    private LoginSessionContext sessionContext;

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
            if(storeList.isEmpty()){
                store.setStoreName(userService.get(userId).getName());
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
    public BaseOutput<UserStore> updateUserStore(@RequestBody UserStore userStore) {
        try {
            Long userId = userStore.getUserId();
            if (null == userId) {
                return BaseOutput.failure("userid为空");
            }
            if (StringUtils.isBlank(userStore.getStoreName())) {
                return BaseOutput.failure("用户店铺名称不能为空");
            }
            userStore.setUserId(userId);
            UserStore queObj = new UserStore();
            queObj.setUserId(userId);
            //查询用户店铺列表
            List<UserStore> storeList =userStoreService.listByExample(queObj);
            //用户无店铺则新增
            if(storeList.isEmpty()){
                userStoreService.insert(userStore);
            }else{
                //验证店铺名是否已存在
                UserStore queStore = new UserStore();
                queStore.setStoreName(userStore.getStoreName());
                List<UserStore> oldList=userStoreService.listByExample(queStore);
                if(oldList.isEmpty()){
                    this.userStoreService.updateExactByExample(userStore, queObj);
                }else{
                    //旧店铺名与新店铺名一致
                    if(userStore.getStoreName().equals(storeList.get(0).getStoreName())){
                        this.userStoreService.updateExactByExample(userStore, queObj);
                        return BaseOutput.success("success");
                    }
                    return BaseOutput.failure("店铺名已存在");
                }
            }
            return BaseOutput.success("success");
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询出错");
        }

    }

}
