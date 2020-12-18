package com.dili.trace.api.client;

import com.dili.common.annotation.AppAccess;
import com.dili.common.annotation.Role;
import com.dili.customer.sdk.enums.CustomerEnum;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.RUserCategory;
import com.dili.trace.service.RUserCategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Autowired
    RUserCategoryService rUserCategoryService;

    /**
     * 查询常用品类
     *
     * @param rUserCategory
     * @return
     */
    @RequestMapping("/listUserCategory.api")
    public BaseOutput listUserCategory(@RequestBody RUserCategory rUserCategory) {
        if (rUserCategory.getUserId() == null) {
            return BaseOutput.failure("参数错误");
        }
        List<RUserCategory> data = this.rUserCategoryService.listByExample(rUserCategory);
        return BaseOutput.successData(data);

    }
}
