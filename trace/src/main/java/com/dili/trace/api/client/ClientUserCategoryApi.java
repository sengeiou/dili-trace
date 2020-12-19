package com.dili.trace.api.client;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.CategoryApi;
import com.dili.trace.domain.RUserCategory;
import com.dili.trace.service.RUserCategoryService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 经营户(卖家)常用品类接口
 * Created by wangguofeng
 */
@RestController
@RequestMapping(value = "/api/client/clientUserCategory")
@Api(value = "/api/client/clientRegisterBill", description = "登记单相关接口")
@AppAccess(role = Role.Client, url = "", subRoles = {CustomerEnum.CharacterType.经营户, CustomerEnum.CharacterType.买家})

public class ClientUserCategoryApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientUserCategoryApi.class);
    @Autowired
    RUserCategoryService rUserCategoryService;
    @Autowired
    private LoginSessionContext sessionContext;
    /**
     * 查询常用品类
     *
     * @param rUserCategory
     * @return
     */
    @RequestMapping("/listUserCategory.api")
    public BaseOutput listUserCategory(@RequestBody RUserCategory rUserCategory) {
//        if (rUserCategory.getUserId() == null) {
//            return BaseOutput.failure("参数错误");
//        }
        rUserCategory.setSort("create_time");
        rUserCategory.setOrder("desc");
        List<RUserCategory> data = this.rUserCategoryService.listByExample(rUserCategory);
        return BaseOutput.successData(data);

    }
    /**
     * 添加个人经营品类
     */
    @RequestMapping(value = "/addUserCategory.api", method = RequestMethod.POST)
    public BaseOutput addUserCategory(@RequestBody List<RUserCategory> input) {
        try {
            SessionData sessionData = this.sessionContext.getSessionData();
            Long id = sessionData.getUserId();
            return rUserCategoryService.dealBatchAdd(input, id);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }
    /**
     * 删除个人经营品类
     */
    @RequestMapping(value = "/delUserCategory.api", method = RequestMethod.POST)
    public BaseOutput delUserCategory(@RequestBody List<Long> input) {
        try {
            SessionData sessionData = this.sessionContext.getSessionData();
            int rows = rUserCategoryService.delete(input);
            return rows > 0 ? BaseOutput.success() : BaseOutput.failure();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return BaseOutput.failure("服务端出错");
        }
    }

}
