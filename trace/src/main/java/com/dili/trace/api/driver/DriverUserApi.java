package com.dili.trace.api.driver;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.trace.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;

/**
 * @version 1.0
 * @ClassName DriverUserApi
 * @Description todo
 * @createTime 2020年12月01日 15:56:00
 */
@Api(value = "/api/driverUserApi")
@RestController
@RequestMapping(value = "/api/driverUserApi")
public class DriverUserApi {

    /**
     * 是否需要注册
     * @param user
     * @return
     */
    @ApiOperation(value = "是否需要注册", notes = "是否需要注册")
    @RequestMapping(value = "/needRegister", method = RequestMethod.POST)
    public BaseOutput needRegister(@RequestBody User user) {

        return BaseOutput.success().setData("need");
    }

    /**
     * 获取司机用户列表
     * @param user
     * @return
     */
    @ApiOperation(value = "获取司机用户列表", notes = "获取司机用户列表")
    @RequestMapping(value = "/getDriverUserList", method = RequestMethod.POST)
    public BaseOutput getDriverUserList(@RequestBody UserDriverDto user) {
        UserDriverDto driverDto = new UserDriverDto();
        driverDto.setDriverName("测试司机");
        driverDto.setDriverId(1L);
        return BaseOutput.success().setData(Collections.singletonList(driverDto));
    }


    /**
     * 获取司机列表
     * @return
     */
    @ApiOperation(value = "获取司机列表", notes = "获取司机列表")
    @RequestMapping(value = "/getDriverList", method = RequestMethod.POST)
    public BaseOutput getDriverList() {

        User user = DTOUtils.newDTO(User.class);
        user.setName("测试");
        user.setId(1L);

        return BaseOutput.success().setData(Collections.singletonList(user));
    }

    /**
     * 新增司机与卖家关联关系
     * @param userRef
     * @return
     */
    @ApiOperation(value = "新增司机与卖家关联关系", notes = "新增司机与卖家关联关系")
    @RequestMapping(value = "/updateDriverUserRef", method = RequestMethod.POST)
    public BaseOutput updateDriverUserRef(@RequestBody UserDriverDto userRef) {
        return BaseOutput.success();
    }




}
