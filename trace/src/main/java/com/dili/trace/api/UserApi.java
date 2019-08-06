package com.dili.trace.api;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.PatternConstants;
import com.dili.common.entity.SessionContext;
import com.dili.common.exception.BusinessException;
import com.dili.common.util.MD5Util;
import com.dili.common.util.UUIDUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.domain.User;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 账号相关api
 */
@Api(value = "/api/userApi", description = "有关于帐号相关的接口")
@RestController
@RequestMapping(value = "/api/userApi")
public class UserApi {
    private static final Logger LOGGER= LoggerFactory.getLogger(UserApi.class);
    @Resource
    private UserService userService;
    @Resource
    private SessionContext sessionContext;

    @ApiOperation(value ="注册【接口已通】", notes = "注册")
    @RequestMapping(value = "/register.api", method = RequestMethod.POST)
    public BaseOutput<Long> register(@RequestBody User user){
        try{
            checkRegisterParams(user);
            user.setPassword(MD5Util.md5(user.getPassword()));
            user.setState(EnabledStateEnum.ENABLED.getCode());
            userService.register(user);
            return BaseOutput.success().setData(user.getId());
        }catch (BusinessException e){
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("register",e);
            return BaseOutput.failure();
        }
    }


    @ApiOperation(value ="登录【接口已通】", notes = "登录")
    @RequestMapping(value = "/login.api", method = RequestMethod.POST)
    public BaseOutput<Map<String,Object>> login(@RequestBody User user){
        try{
            if(StrUtil.isBlank(user.getPhone()) || !ReUtil.isMatch(PatternConstants.PHONE,user.getPhone())){
                return BaseOutput.failure("手机号为空或格式错误");
            }
            if(StrUtil.isBlank(user.getPassword())){
                return BaseOutput.failure("密码为空");
            }
            Map<String,Object> result=new HashMap<>();
            User po=userService.login(user.getPhone(), MD5Util.md5(user.getPassword()));
            sessionContext.setSessionId(UUIDUtil.get());
            sessionContext.setAccountId(po.getId());
            result.put("userId",po.getId());
            result.put("userName",po.getName());
            result.put("sessionId",sessionContext.getSessionId());
            return BaseOutput.success().setData(result);
        }catch (BusinessException e){
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("login",e);
            return BaseOutput.failure();
        }
    }

    @ApiOperation(value ="找回密码【接口已通】", notes = "找回密码")
    @RequestMapping(value = "/resetPassword.api", method = RequestMethod.POST)
    public BaseOutput<String> resetPassword(@RequestBody User user){
        try{
            if(StrUtil.isBlank(user.getPhone())){
                return BaseOutput.failure("手机号为空");
            }
            if(StrUtil.isBlank(user.getCheckCode())){
                return BaseOutput.failure("验证码为空");
            }
            if(StrUtil.isBlank(user.getPassword())){
                return BaseOutput.failure("密码为空");
            }
            if(StrUtil.isBlank(user.getAckPassword())){
                return BaseOutput.failure("确认密码为空");
            }
            if(!user.getPassword().equals(user.getAckPassword())){
                throw new BusinessException("密码与确认密码不同");
            }
            user.setPassword(MD5Util.md5(user.getPassword()));
            userService.resetPassword(user);
            return BaseOutput.success();
        }catch (BusinessException e){
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("resetPassword",e);
            return BaseOutput.failure();
        }
    }

    @ApiOperation(value ="用户获取个人信息【接口已通】", notes = "用户获取个人信息")
    @RequestMapping(value = "/get.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<Map<String,Object>> get(){
        try{
            Map<String,Object> result=new HashMap<>();
            User user=userService.get(sessionContext.getAccountId());
            if(user==null){
                return BaseOutput.failure("用户不存在");
            }
            result.put("id",user.getId());
            result.put("name",user.getName());
            return BaseOutput.success().setData(result);
        }catch (Exception e){
            LOGGER.error("get",e);
            return BaseOutput.failure();
        }
    }

    @ApiOperation(value ="修改密码【接口已通】", notes = "修改密码")
    @RequestMapping(value = "/changePassword.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<String> changePassword(@RequestBody User user){
        try{
            if(StrUtil.isBlank(user.getOldPassword())){
                return BaseOutput.failure("原密码为空");
            }
            if(StrUtil.isBlank(user.getPassword())){
                return BaseOutput.failure("密码为空");
            }
            if(StrUtil.isBlank(user.getAckPassword())){
                return BaseOutput.failure("确认密码为空");
            }
            if(!user.getPassword().equals(user.getAckPassword())){
                throw new BusinessException("密码与确认密码不同");
            }
            user.setPassword(MD5Util.md5(user.getPassword()));
            user.setOldPassword(MD5Util.md5(user.getOldPassword()));
            user.setId(sessionContext.getAccountId());
            userService.changePassword(user);
            return BaseOutput.success();
        }catch (BusinessException e){
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("changePassword",e);
            return BaseOutput.failure();
        }
    }

    @ApiOperation(value ="退出【接口已通】", notes = "退出")
    @RequestMapping(value = "/quit.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<String> quit(){
        try{
            sessionContext.setInvalidate(true);
            return BaseOutput.success();
        }catch (BusinessException e){
            return BaseOutput.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("quit",e);
            return BaseOutput.failure();
        }
    }

    @ApiOperation(value ="验证是否登录【接口已通】", notes = "验证是否登录")
    @RequestMapping(value = "/isLogin.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<String> isLogin(){
        try{
            return BaseOutput.success();
        }catch (Exception e){
            LOGGER.error("isLogin",e);
            return BaseOutput.failure();
        }
    }

    private void checkRegisterParams(User user){
        if(StrUtil.isBlank(user.getPhone()) || !ReUtil.isMatch(PatternConstants.PHONE,user.getPhone())){
            throw new BusinessException("手机号为空或格式错误");
        }
        if(StrUtil.isBlank(user.getCheckCode())){
            throw new BusinessException("验证码为空");
        }
        if(StrUtil.isBlank(user.getName()) || !ReUtil.isMatch(PatternConstants.C,user.getName())){
            throw new BusinessException("姓名为空或格式错误");
        }
        if(StrUtil.isBlank(user.getPassword())){
            throw new BusinessException("密码为空");
        }
        if(StrUtil.isBlank(user.getAckPassword())){
            throw new BusinessException("确认密码为空");
        }
        if(!user.getPassword().equals(user.getAckPassword())){
            throw new BusinessException("密码与确认密码不同");
        }
    }

}
