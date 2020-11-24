package com.dili.sg.trace.api;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.SessionData;
import com.dili.common.util.MD5Util;
import com.dili.common.util.VerificationCodeUtil;
import com.dili.sg.common.entity.ExecutionConstants;
import com.dili.sg.common.entity.PatternConstants;
import com.dili.sg.common.entity.TraceSessionContext;
import com.dili.sg.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.sg.trace.domain.User;
import com.dili.trace.domain.UserPlate;
import com.dili.common.exception.TraceBizException;
import com.dili.sg.trace.glossary.UserTypeEnum;
import com.dili.sg.trace.rpc.MessageRpc;
import com.dili.sg.trace.service.SMSService;
import com.dili.trace.service.UserPlateService;
import com.dili.sg.trace.service.UserService;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.trace.api.components.LoginComponent;
import com.dili.trace.api.input.LoginInputDto;
import com.dili.trace.service.UserPlateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 账号相关api
 */
@Api(value = "/api/userApi", description = "有关于帐号相关的接口")
@RestController
@RequestMapping(value = "/api/userApi")
public class UserApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserApi.class);
	@Resource
	private UserService userService;
	@Resource
	private TraceSessionContext sessionContext;
	@Resource
	private DefaultConfiguration defaultConfiguration;
	@Resource
	private MessageRpc messageRpc;
	@Resource
	private RedisUtil redisUtil;
	@Resource
	UserPlateService userPlateService;

	@Autowired
	private LoginComponent loginComponent;
	@Autowired
	SMSService smsService;

	/**
	 * 注册【接口已通】
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "注册【接口已通】", notes = "注册")
	@RequestMapping(value = "/register.api", method = RequestMethod.POST)
	public BaseOutput<Long> register(@RequestBody User user) {
		try {
			checkRegisterParams(user);
			user.setCardNo(StringUtils.upperCase(user.getCardNo()));
			this.smsService.checkVerificationCode(user.getPhone(), user.getCheckCode());
			userService.register(user, UserTypeEnum.USUAL_USER,user.getPassword());
			return BaseOutput.success().setData(user.getId());
		} catch (TraceBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("register", e);
			return BaseOutput.failure(e.getMessage());
		}
	}

	/**
	 *
	 * 发送短信验证码
	 * 
	 * @param {code:"客户CODE",phone:''}
	 * @return
	 */
	@RequestMapping(value = "/sendVerificationCode.api", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public BaseOutput<Boolean> sendVerificationCode(@RequestBody JSONObject object) {
		String phone = object.getString("phone");
		if (StringUtils.isBlank(phone)) {
			LOGGER.error("参数为空" + JSON.toJSONString(object));
			return BaseOutput.failure("参数为空").setCode(ResultCode.PARAMS_ERROR);
		}

		if (defaultConfiguration.getCheckCodeExpire()
				- redisUtil.getRedisTemplate().getExpire(ExecutionConstants.REDIS_SYSTEM_VERCODE_PREIX) < 60) {
			return BaseOutput.success();// 发送间隔60秒
		}

		String verificationCode = VerificationCodeUtil.getRandNum(defaultConfiguration.getCheckCodeLength());
		// 发送短信验证码
		JSONObject params = new JSONObject();
		params.put("marketCode", ExecutionConstants.MARKET_CODE);
		params.put("systemCode", ExecutionConstants.SYSTEM_CODE);
		params.put("sceneCode", "registerAuthCode");
		params.put("cellphone", phone);
		Map<String, Object> content = new HashMap<>();
		content.put("code", verificationCode);
		params.put("parameters", content);

		BaseOutput msgOutput = messageRpc.sendVerificationCodeMsg(params);
		if (msgOutput.isSuccess()) {
			redisUtil.set(ExecutionConstants.REDIS_SYSTEM_VERCODE_PREIX + phone, verificationCode,
					defaultConfiguration.getCheckCodeExpire(), TimeUnit.SECONDS);
			LOGGER.info("短信验证码发送成功：---------------手机号：【" + phone + "】，验证码：【" + verificationCode + "】--------------");
		} else {
			LOGGER.error("发送失败,错误信息：" + msgOutput.getMessage());
			LOGGER.info("短信验证码发送失败：---------------手机号：【" + phone + "】，验证码：【" + verificationCode + "】--------------");
			return BaseOutput.failure(msgOutput.getMessage());
		}
		return BaseOutput.success();
	}

	/**
	 * 登录【接口已通】
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "登录【接口已通】", notes = "登录")
	@RequestMapping(value = "/login.api", method = RequestMethod.POST)
	public BaseOutput<SessionData> login(@RequestBody User user) {
		try {
			if (StrUtil.isBlank(user.getPhone()) || !ReUtil.isMatch(PatternConstants.PHONE, user.getPhone())) {
				return BaseOutput.failure("手机号为空或格式错误");
			}
			if (StrUtil.isBlank(user.getPassword())) {
				return BaseOutput.failure("密码为空");
			}

			LoginInputDto loginInput = new LoginInputDto();
			loginInput.setUsername(user.getPhone());
			loginInput.setPassword(user.getPassword());
			loginInput.setLoginIdentityType(LoginIdentityTypeEnum.USER.getCode());

			SessionData data = this.loginComponent.login(loginInput);
			return BaseOutput.success().setData(data);

		} catch (TraceBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("login", e);
			return BaseOutput.failure();
		}
	}

	/**
	 * 找回密码【接口已通】
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "找回密码【接口已通】", notes = "找回密码")
	@RequestMapping(value = "/resetPassword.api", method = RequestMethod.POST)
	public BaseOutput<String> resetPassword(@RequestBody User user) {
		try {
			if (StrUtil.isBlank(user.getPhone())) {
				return BaseOutput.failure("手机号为空");
			}
			if (StrUtil.isBlank(user.getPassword())) {
				return BaseOutput.failure("密码为空");
			}
			if (StrUtil.isBlank(user.getAckPassword())) {
				return BaseOutput.failure("确认密码为空");
			}
			if (!user.getPassword().equals(user.getAckPassword())) {
				throw new TraceBizException("密码与确认密码不同");
			}
			user.setPassword(MD5Util.md5(user.getPassword()));
			userService.resetPassword(user);
			return BaseOutput.success();
		} catch (TraceBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("resetPassword", e);
			return BaseOutput.failure();
		}
	}

	/**
	 * 用户获取个人信息【接口已通】
	 * @return
	 */
	@ApiOperation(value = "用户获取个人信息【接口已通】", notes = "用户获取个人信息")
	@RequestMapping(value = "/get.api", method = RequestMethod.POST)
	@InterceptConfiguration
	public BaseOutput<User> get() {
		try {
			User user = userService.get(sessionContext.getAccountId());
			if (user == null) {
				return BaseOutput.failure("用户不存在");
			}
			return BaseOutput.success().setData(user);
		} catch (Exception e) {
			LOGGER.error("get", e);
			return BaseOutput.failure();
		}
	}

	/**
	 * 查询车牌信息
	 * @return
	 */
	@ApiOperation(value = "查询车牌信息", notes = "用户获取个人信息")
	@RequestMapping(value = "/getUserPlate.api", method = RequestMethod.POST)
	@InterceptConfiguration
	public BaseOutput<List<UserPlate>> getUserPlate() {
		try {
			User user = userService.get(sessionContext.getAccountId());
			if (user == null) {
				return BaseOutput.failure("用户不存在");
			}
			List<UserPlate> userPlateList = this.userPlateService.findUserPlateByUserId(user.getId());
			return BaseOutput.success().setData(userPlateList);
		} catch (Exception e) {
			LOGGER.error("查询车牌信息错误", e);
			return BaseOutput.failure();
		}
	}

	/**
	 * 修改密码【接口已通】
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "修改密码【接口已通】", notes = "修改密码")
	@RequestMapping(value = "/changePassword.api", method = RequestMethod.POST)
	@InterceptConfiguration
	public BaseOutput<String> changePassword(@RequestBody User user) {
		try {
			if (StrUtil.isBlank(user.getOldPassword())) {
				return BaseOutput.failure("原密码为空");
			}
			if (StrUtil.isBlank(user.getPassword())) {
				return BaseOutput.failure("密码为空");
			}
			if (StrUtil.isBlank(user.getAckPassword())) {
				return BaseOutput.failure("确认密码为空");
			}
			if (!user.getPassword().equals(user.getAckPassword())) {
				throw new TraceBizException("密码与确认密码不同");
			}
			user.setPassword(MD5Util.md5(user.getPassword()));
			user.setOldPassword(MD5Util.md5(user.getOldPassword()));
			user.setId(sessionContext.getAccountId());
			userService.changePassword(user);
			return BaseOutput.success();
		} catch (TraceBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("changePassword", e);
			return BaseOutput.failure();
		}
	}

	/**
	 * 退出【接口已通】
	 * @return
	 */
	@ApiOperation(value = "退出【接口已通】", notes = "退出")
	@RequestMapping(value = "/quit.api", method = RequestMethod.POST)
	@InterceptConfiguration
	public BaseOutput<String> quit() {
		try {
			sessionContext.setInvalidate(true);
			return BaseOutput.success();
		} catch (TraceBizException e) {
			return BaseOutput.failure(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("quit", e);
			return BaseOutput.failure();
		}
	}

	/**
	 * 验证是否登录【接口已通】
	 * @return
	 */
	@ApiOperation(value = "验证是否登录【接口已通】", notes = "验证是否登录")
	@RequestMapping(value = "/isLogin.api", method = RequestMethod.POST)
	@InterceptConfiguration
	public BaseOutput<String> isLogin() {
		try {
			return BaseOutput.success();
		} catch (Exception e) {
			LOGGER.error("isLogin", e);
			return BaseOutput.failure();
		}
	}

	/**
	 * 检测参数
	 * @param user
	 */
	private void checkRegisterParams(User user) {
		if (StrUtil.isBlank(user.getPhone()) || !ReUtil.isMatch(PatternConstants.PHONE, user.getPhone())) {
			throw new TraceBizException("手机号为空或格式错误");
		}
		if (StrUtil.isBlank(user.getCardNo()) || !ReUtil.isMatch(PatternConstants.CARD_NO, user.getCardNo())) {
			throw new TraceBizException("身份证为空或格式错误");
		}
		if (StrUtil.isBlank(user.getCheckCode())) {
			throw new TraceBizException("验证码为空");
		}
		if (StrUtil.isBlank(user.getTallyAreaNos())
				|| !ReUtil.isMatch(PatternConstants.TALLY_AREA_NO, user.getTallyAreaNos())) {
			throw new TraceBizException("理货区号为空或格式错误");
		}
		if (Arrays.asList(user.getTallyAreaNos().split(",")).size() > 15) {
			throw new TraceBizException("用户最多添加15个理货区");
		}
		if (StrUtil.isBlank(user.getName()) || user.getName().length() < 2 || user.getName().length() > 20) {
			throw new TraceBizException("姓名为空或格式错误");
		}
		if (StrUtil.isBlank(user.getPassword())) {
			throw new TraceBizException("密码为空");
		}
		if (StrUtil.isBlank(user.getAckPassword())) {
			throw new TraceBizException("确认密码为空");
		}
		if (!user.getPassword().equals(user.getAckPassword())) {
			throw new TraceBizException("密码与确认密码不同");
		}
	}

}
