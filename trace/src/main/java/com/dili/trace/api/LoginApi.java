package com.dili.trace.api;

import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.SessionData;
import com.dili.common.exception.TraceBizException;
import com.dili.ss.domain.BaseOutput;
import com.dili.trace.api.components.LoginComponent;
import com.dili.trace.api.input.LoginInputDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 账号相关api
 */
@Api(value = "/api/loginApi", description = "有关于帐号相关的接口")
@RestController
@RequestMapping(value = "/api/loginApi")
public class LoginApi {
    private static final Logger logger = LoggerFactory.getLogger(LoginApi.class);

    @Autowired
    LoginSessionContext loginSessionContext;
    @Autowired
    private LoginComponent loginComponent;

    /**
     * 小程序登录接口
     *
     * @param loginInput
     * @return
     */
    @ApiOperation(value = "登录", notes = "登录")
    @RequestMapping(value = "/login.api", method = RequestMethod.POST)
    public BaseOutput<SessionData> login(@RequestBody LoginInputDto loginInput) {
        try {
            SessionData data = this.loginComponent.login(loginInput);
            return BaseOutput.success().setData(data);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure();
        }
    }

    /**
     * 验证是否登录【接口已通】
     *
     * @return
     */
    @ApiOperation(value = "验证是否登录【接口已通】", notes = "验证是否登录")
    @RequestMapping(value = "/isLogin.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<String> isLogin() {
        try {
            if (this.loginSessionContext.getAccountId() == null) {
                return BaseOutput.success().setData(false);
            }
            return BaseOutput.success().setData(true);
        } catch (Exception e) {
            logger.error("isLogin", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 退出【接口已通】
     *
     * @return
     */
    @ApiOperation(value = "退出【接口已通】", notes = "退出")
    @RequestMapping(value = "/quit.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<String> quit() {
        try {
            loginSessionContext.setInvalidate(true);
            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("quit", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 退出【接口已通】
     *
     * @param wxInfo
     * @return
     */
    @ApiOperation(value = "wx授权登录", notes = "wx授权登录")
    @RequestMapping(value = "/wxLogin.api", method = RequestMethod.POST)
    public BaseOutput<SessionData> wxLogin(@RequestBody Map<String, String> wxInfo) {
        try {
            String openid = wxInfo.get("openid");
            String id = wxInfo.get("identityType");
            if (StringUtils.isBlank(openid)) {
                return BaseOutput.failure("openid 为空");
            }
            if (StringUtils.isBlank(id)) {
                return BaseOutput.failure("用户identityType 为空");
            }
            SessionData data = this.loginComponent.wxLogin(openid, Integer.valueOf(id));
            return BaseOutput.success().setData(data);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure();
        }
    }
}
