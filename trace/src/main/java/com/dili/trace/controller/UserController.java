package com.dili.trace.controller;

import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.BusinessException;
import com.dili.common.util.MD5Util;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.util.DateUtils;
import com.dili.trace.domain.User;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2019-07-26 09:20:35.
 */
@Api("/user")
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER= LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Resource
    DefaultConfiguration defaultConfiguration;

    @ApiOperation("跳转到User页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        Date now = new Date();
        modelMap.put("createdStart", DateUtils.format(now, "yyyy-MM-dd 00:00:00"));
        modelMap.put("createdEnd", DateUtils.format(now, "yyyy-MM-dd 23:59:59"));
        return "user/index";
    }

    @ApiOperation(value="分页查询User", notes = "分页查询User，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="User", paramType="form", value = "User的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(UserListDto user) throws Exception {
        return userService.listEasyuiPageByExample(user, true).toString();
    }

    @ApiOperation("新增User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "User", paramType = "form", value = "User的form信息", required = true, dataType = "string")
    })
    @RequestMapping(value = "/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    BaseOutput<Long> insert(UserListDto user) {
        try {
            user.setPassword(MD5Util.md5(defaultConfiguration.getPassword()));
            user.setState(EnabledStateEnum.ENABLED.getCode());
            userService.register(user, false);
            return BaseOutput.success("新增成功").setData(user.getId());
        } catch (BusinessException e) {
            LOGGER.error("register", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("register", e);
            return BaseOutput.failure();
        }
    }

    @ApiOperation("修改User")
    @ApiImplicitParams({
		@ApiImplicitParam(name="User", paramType="form", value = "User的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(UserListDto user) {
        try {
            userService.updateUser(user);
            return BaseOutput.success("修改成功");
        } catch (BusinessException e) {
            LOGGER.error("修改用户", e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("修改用户", e);
            return BaseOutput.failure();
        }

    }

    @ApiOperation("删除User")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "User的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        userService.delete(id);
        return BaseOutput.success("删除成功");
    }

    /**
     *
     * @param id
     * @param enable 是否启用
     * @return
     */
    @RequestMapping(value = "/doEnable.action", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput doEnable(Long id, Boolean enable) {
        return userService.updateEnable(id, enable);
    }

    @ApiOperation(value ="找回密码【接口已通】", notes = "找回密码")
    @RequestMapping(value = "/resetPassword.action", method = RequestMethod.POST)
    @ResponseBody
    public BaseOutput<Boolean> resetPassword(Long id){
        User user = DTOUtils.newDTO(User.class);
        user.setId(id);
        user.setPassword(MD5Util.md5(defaultConfiguration.getPassword()));
        userService.updateSelective(user);
        return BaseOutput.success().setData(true);
    }
}