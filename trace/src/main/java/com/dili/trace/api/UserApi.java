package com.dili.trace.api;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.common.annotation.InterceptConfiguration;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.ExecutionConstants;
import com.dili.common.entity.LoginSessionContext;
import com.dili.common.entity.PatternConstants;
import com.dili.common.exception.TraceBizException;
import com.dili.common.util.MD5Util;
import com.dili.common.util.VerificationCodeUtil;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.ss.util.DateUtils;
import com.dili.trace.api.enums.LoginIdentityTypeEnum;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.api.output.UserQrOutput;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.rpc.MessageRpc;
import com.dili.trace.service.SMSService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserService;
import com.dili.trace.util.BasePageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 账号相关api
 */
@Api(value = "/api/userApi", description = "有关于帐号相关的接口")
@RestController
@RequestMapping(value = "/api/userApi")
public class UserApi {
    private static final Logger logger = LoggerFactory.getLogger(UserApi.class);
    @Autowired
    private UserService userService;
    @Autowired
    private LoginSessionContext sessionContext;
    @Autowired
    private DefaultConfiguration defaultConfiguration;


    @Autowired
    private MessageRpc messageRpc;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    UserPlateService userPlateService;
    @Autowired
    SMSService smsService;
    @Autowired
    ExecutionConstants executionConstants;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "注册", notes = "注册")
    @RequestMapping(value = "/register.api", method = RequestMethod.POST)
    public BaseOutput<Long> register(@RequestBody User user) {
        try {
            checkRegisterParams(user);
            user.setState(EnabledStateEnum.ENABLED.getCode());
            userService.register(user, true);
            return BaseOutput.success().setData(user.getId());
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("register", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 请求实名认证
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "请求实名认证", notes = "请求实名认证")
    @RequestMapping(value = "/realNameCertificationReq.api", method = RequestMethod.POST)
    public BaseOutput<Long> realNameCertificationReq(@RequestBody User user) {
        try {
            Long id = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            // User currentUser = userService.get(id);
            // if (ValidateStateEnum.PASSED.equalsToCode(currentUser.getValidateState()))
            // {// 已通过
            // return BaseOutput.success().setData(id).setMessage("已经通过审核，请勿反复提交");
            // }

            checkRealNameCertificationParams(user);
            user.setId(id);
            user.setCardNo(StringUtils.upperCase(user.getCardNo()));
            user.setLicense(StringUtils.upperCase(user.getLicense()));
            user.setValidateState(ValidateStateEnum.UNCERT.getCode());
            userService.updateUser(user);
            return BaseOutput.success().setData(user.getId());
        } catch (TraceBizException e) {
            // LOGGER.error("realNameCertificationReq",e);
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("realNameCertificationReq", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * 发送短信验证码
     *
     * @param {code:"客户CODE",phone:''}
     * @return
     */
    @RequestMapping(value = "/sendVerificationCode.api", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BaseOutput<Boolean> sendVerificationCode(@RequestBody JSONObject object) {
        String phone = object.getString("phone");
        if (StringUtils.isBlank(phone)) {
            logger.error("参数为空" + JSON.toJSONString(object));
            return BaseOutput.failure("参数为空").setCode(ResultCode.PARAMS_ERROR);
        }

        String verificationCode = VerificationCodeUtil.getRandNum(defaultConfiguration.getCheckCodeLength());
        // 发送短信验证码
        JSONObject params = new JSONObject();
        params.put("marketCode", executionConstants.getMarketCode());
        params.put("systemCode", ExecutionConstants.SYSTEM_CODE);
        params.put("sceneCode", "registerAuthCode");
        params.put("cellphone", phone);
        Map<String, Object> content = new HashMap<>();
        content.put("code", verificationCode);
        params.put("parameters", content);
        return this.smsService.sendVerificationCodeMsg(params, phone, verificationCode);
    }

    /**
     * 用户获取个人信息【接口已通】
     *
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
            // //初始状态获取个人信息不展示名称
            // if (user.getValidateState() == ValidateStateEnum.CERTREQ.getCode()){
            // user.setName("");
            // }
            return BaseOutput.success().setData(user);
        } catch (Exception e) {
            logger.error("get", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 查询车牌信息
     *
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
            logger.error("查询车牌信息错误", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 修改密码【接口已通】
     *
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
            logger.error("changePassword", e);
            return BaseOutput.failure();
        }
    }


    /**
     * 发送重置密码验证码【接口已通】
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "发送重置密码验证码【接口已通】", notes = "发送重置密码验证码")
    @RequestMapping(value = "/sendSmsCodeForRenewPassword.api", method = RequestMethod.POST)
    @InterceptConfiguration(loginRequired = false)
    public BaseOutput<String> sendSmsCodeForRenewPassword(@RequestBody User user) {
        try {
            if (StringUtils.isBlank(user.getPhone())) {
                return BaseOutput.failure("手机号码不能为空");
            }
            User query = DTOUtils.newDTO(User.class);
            query.setPhone(user.getPhone());
            query.setYn(YnEnum.YES.getCode());
            User userItem = StreamEx.of(this.userService.listByExample(query)).findFirst().orElseThrow(() -> {
                return new TraceBizException("手机号码不存在");
            });


            String verificationCode = VerificationCodeUtil.getRandNum(defaultConfiguration.getCheckCodeLength());
            // 发送短信验证码
            JSONObject params = new JSONObject();
            params.put("marketCode", executionConstants.getMarketCode());
            params.put("systemCode", ExecutionConstants.SYSTEM_CODE);
            params.put("sceneCode", "resetAuthcode");
            params.put("cellphone", userItem.getPhone());
            Map<String, Object> content = new HashMap<>();
            content.put("code", verificationCode);
            params.put("parameters", content);
            return this.smsService.sendRenewPasswordSMSCodeMsg(params, userItem.getPhone(), verificationCode);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("sendSmsCodeForResetPassword", e);
            return BaseOutput.failure();
        }
    }


    /**
     * 重置密码【接口已通】
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "重置密码【接口已通】", notes = "重置密码")
    @RequestMapping(value = "/renewPassword.api", method = RequestMethod.POST)
    @InterceptConfiguration(loginRequired = false)
    public BaseOutput<String> renewPassword(@RequestBody User user) {
        try {
            if (StringUtils.isBlank(user.getPhone())) {
                return BaseOutput.failure("手机号码不能为空");
            }
            if (StringUtils.isBlank(user.getCheckCode())) {
                return BaseOutput.failure("验证码不能为空");
            }
            if (StringUtils.isBlank(user.getPassword())) {
                return BaseOutput.failure("密码不能为空");
            }
            this.userService.renewPassword(user, user.getCheckCode());
            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("changePassword", e);
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
            sessionContext.setInvalidate(true);
            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("quit", e);
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
            return BaseOutput.success();
        } catch (Exception e) {
            logger.error("isLogin", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 检查注册输入信息
     *
     * @param user
     */
    private void checkRegisterParams(User user) {
        if (StrUtil.isBlank(user.getName()) || user.getName().length() < 2 || user.getName().length() > 20) {
            throw new TraceBizException("姓名为空或格式错误");
        }
        if (StrUtil.isBlank(user.getPhone()) || !ReUtil.isMatch(PatternConstants.PHONE, user.getPhone())) {
            throw new TraceBizException("手机号为空或格式错误");
        }
        if (StrUtil.isBlank(user.getCheckCode())) {
            throw new TraceBizException("验证码为空");
        }
        if (StrUtil.isBlank(user.getPassword())) {
            throw new TraceBizException("密码为空");
        }

    }

    /**
     * 检查身份证信息
     *
     * @param user
     */
    private void checkRealNameCertificationParams(User user) {
        if (UserTypeEnum.USER.getCode() == user.getUserType()) {// 个人
            if (StrUtil.isBlank(user.getCardNo()) || !ReUtil.isMatch(PatternConstants.CARD_NO, user.getCardNo())) {
                throw new TraceBizException("身份证为空或格式错误");
            }
            if (StrUtil.isBlank(user.getCardNoFrontUrl())) {
                throw new TraceBizException("身份证正面照片未上传");
            }
            if (StrUtil.isBlank(user.getCardNoBackUrl())) {
                throw new TraceBizException("身份证背面照片未上传");
            }
        } else {
            if (StrUtil.isBlank(user.getBusinessLicenseUrl())) {
                throw new TraceBizException("营业执照照片未上传");
            }
            if (StrUtil.isBlank(user.getLicense())) {
                throw new TraceBizException("统一信用编码不能为空");
            }
        }

        if (StrUtil.isBlank(user.getPhone()) || !ReUtil.isMatch(PatternConstants.PHONE, user.getPhone())) {
            throw new TraceBizException("手机号为空或格式错误");
        }

        // if(StrUtil.isBlank(user.getTallyAreaNos()) ||
        // !ReUtil.isMatch(PatternConstants.TALLY_AREA_NO,user.getTallyAreaNos())){
        // throw new TraceBusinessException("理货区号为空或格式错误");
        // }
        if (Arrays.asList(StringUtils.trimToEmpty(user.getTallyAreaNos()).split(",")).size() > 15) {
            throw new TraceBizException("用户最多添加15个摊位号");
        }
        if (StrUtil.isBlank(user.getName()) || user.getName().length() < 2 || user.getName().length() > 30) {
            throw new TraceBizException("姓名为空或格式错误");
        }

    }

    /**
     * 通过姓名关键字查询用户信息
     *
     * @param input
     * @return
     */
    @ApiOperation(value = "通过姓名关键字查询用户信息", notes = "通过姓关键字查询用户信息")
    @RequestMapping(value = "/findUserByLikeName.api", method = RequestMethod.POST)
    public BaseOutput<BasePage<User>> findUserByLikeName(UserListDto input) {
        try {
            if (input == null || StringUtils.isBlank(input.getLikeName())) {
                return BaseOutput.success().setData(BasePageUtil.empty(input.getPage(), input.getRows()));
            }
            BasePage<User> source = this.userService.listPageByExample(input);
            BasePage<User> page = BasePageUtil.convert(source, (u) -> {
                User user = DTOUtils.newDTO(User.class);
                user.setId(u.getId());
                user.setName(u.getName());
                user.setLegalPerson(u.getLegalPerson());
                user.setUserType(u.getUserType());
                return user;
            });
            return BaseOutput.success().setData(page);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询出错");
        }

    }

    /**
     * 通过姓名/手机号/经营户卡号关键字查询用户信息
     *
     * @param input
     * @return
     */
    @ApiOperation(value = "通过姓名/手机号/经营户卡号关键字查询用户信息", notes = "通过姓名/手机号/经营户卡号关键字查询用户信息")
    @RequestMapping(value = "/findUserByKeyword.api", method = RequestMethod.POST)
    public BaseOutput<BasePage<User>> findUserByKeyword(UserListDto input) {
        try {
            if (input == null || (StringUtils.isBlank(input.getThirdPartyCode())
                    && StringUtils.isBlank(input.getPhone()) && StringUtils.isBlank(input.getName()))) {
                return BaseOutput.success().setData(BasePageUtil.empty(input.getPage(), input.getRows()));
            }
            BasePage<User> source = this.userService.findUserByKeyword(input);
            BasePage<User> page = BasePageUtil.convert(source, (u) -> {
                User user = DTOUtils.newDTO(User.class);
                user.setId(u.getId());
                user.setName(u.getName());
                user.setLegalPerson(u.getLegalPerson());
                user.setThirdPartyCode(u.getThirdPartyCode());
                user.setPhone(u.getPhone());
                return user;
            });
            return BaseOutput.success().setData(page);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询出错");
        }

    }

    /**
     * 通过姓名关键字查询用户信息
     *
     * @param input
     * @return
     */
    @ApiOperation(value = "通过姓名关键字查询用户信息", notes = "通过姓名关键字查询用户信息")
    @RequestMapping(value = "/getUserQrCode.api", method = RequestMethod.POST)
    @InterceptConfiguration
    public BaseOutput<UserQrOutput> getUserQrCode(UserListDto input) {
        try {
            Long loginUserId = this.sessionContext.getLoginUserOrException(LoginIdentityTypeEnum.USER).getId();
            if (input == null || input.getUserId() == null) {
                return BaseOutput.failure("参数错误");
            }
            Long userId = input.getUserId();
            UserQrOutput qrOutput = this.userService.getUserQrCode(userId);
            return BaseOutput.success().setData(qrOutput);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return BaseOutput.failure("查询出错");
        }

    }

    /**
     * wx授权一键注册
     *
     * @param userInfo
     * @return
     */
    @ApiOperation(value = "wx授权一键注册", notes = "wx授权一键注册")
    @RequestMapping(value = "/wxRegister.api", method = RequestMethod.POST)
    public BaseOutput<Long> wxRegister(@RequestBody Map<String, String> userInfo) {
        logger.info("wxRegister param:" + userInfo.toString());
        try {
            String phone = userInfo.get("phone");
            String wxName = userInfo.get("wxName");
            String openid = userInfo.get("openid");
            if (StringUtils.isBlank(openid)) {
                return BaseOutput.failure("未获取到微信openid");
            }
            if (StringUtils.isBlank(phone)) {
                return BaseOutput.failure("未获取到手机号");
            }
            if (StringUtils.isBlank(wxName)) {
                return BaseOutput.failure("未获取到微信昵称");
            }
            String marketId = userInfo.get("marketId");
            String marketName = userInfo.get("marketName");
            User user = DTOUtils.newDTO(User.class);
            user.setOpenId(openid);
            user.setPhone(phone);
            user.setName(wxName);
            if (!StringUtils.isBlank(marketId)) {
                user.setMarketId(Long.valueOf(marketId));
            }
            user.setMarketName(marketName);
            String defaultPassword = userService.wxRegister(user);
            userInfo.put("password", defaultPassword);
            return BaseOutput.success().setData(userInfo);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("register", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * wx用户绑定
     *
     * @param wxInfo
     * @return
     */
    @ApiOperation(value = "wx用户绑定", notes = "微信用户绑定")
    @RequestMapping(value = "/userBindWeChat.api", method = RequestMethod.POST)
    public BaseOutput userBindWeChat(@RequestBody Map<String, String> wxInfo) {
        logger.info("wxRegister param:" + wxInfo.toString());
        try {
            String openid = wxInfo.get("openid");
            String user_id = wxInfo.get("user_id");
            if (StrUtil.isBlank(openid)) {
                throw new TraceBizException("微信用户绑定未获取到openid");
            }
            if (StrUtil.isBlank(user_id)) {
                throw new TraceBizException("微信用户绑定未获取到用户id");
            }
            userService.userBindWeChat(openid, Long.valueOf(user_id));
            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("微信用户绑定失败", e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    /**
     * wx用户绑定弹窗查询
     *
     * @param user_id
     * @return
     */
    @ApiOperation(value = "wx用户绑定弹窗查询", notes = "微信用户绑定")
    @GetMapping(value = "/getUserBindWeChat.api")
    public BaseOutput getUserBindWeChat(@RequestParam String user_id) {
        logger.info("微信用户绑定查询，user_id:" + user_id);
        try {
            User user = userService.get(Long.valueOf(user_id));
            Boolean needTip = checkNeedTip(user);
            return BaseOutput.success().setData(needTip);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("微信用户绑定查询失败", e);
            BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.failure("error");
    }

    /**
     * wx查询微信是否绑定用户
     *
     * @param open_id
     * @return
     */
    @ApiOperation(value = "wx查询微信是否绑定用户", notes = "微信绑定用户")
    @GetMapping(value = "/validateWeChatBindUser.api")
    public BaseOutput validateWeChatBindUser(@RequestParam String open_id) {
        logger.info("微信用户绑定查询，open_id:" + open_id);
        try {
            User user = DTOUtils.newDTO(User.class);
            user.setOpenId(open_id);
            List<User> userList = userService.listByExample(user);
            Boolean isBind = false;
            //微信已绑定
            if (!userList.isEmpty()) {
                isBind = true;
            }
            return BaseOutput.success().setData(isBind);
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("微信用户绑定查询失败", e);
            BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.failure("error");
    }

    /**
     * /confirmBindWeChatTip.api
     *
     * @param user_id
     * @return
     */
    @ApiOperation(value = "wx用户绑定确认提示", notes = "wx用户绑定确认提示")
    @GetMapping(value = "/confirmBindWeChatTip.api")
    public BaseOutput confirmBindWeChatTip(@RequestParam String user_id) {
        logger.info("wx用户绑定确认提示，user_id:" + user_id);
        try {
            userService.confirmBindWeChatTip(user_id);
            return BaseOutput.success();
        } catch (TraceBizException e) {
            return BaseOutput.failure(e.getMessage());
        } catch (Exception e) {
            logger.error("wx用户绑定确认提示", e);
            BaseOutput.failure(e.getMessage());
        }
        return BaseOutput.failure("error");
    }

    /**
     * 检查是否需要弹出提示
     *
     * @param user
     * @return
     */
    private Boolean checkNeedTip(User user) {
        if (StringUtils.isBlank(user.getOpenId())) {
            Date confirm_date = user.getConfirmDate();
            //未确认
            if (null == confirm_date) {
                return true;
            }
            //当前日期在确认一天后则弹出提示
            if (DateUtils.getCurrentDate().after(DateUtils.addDays(confirm_date, 1))) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 用户获取个人信息【接口已通】
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "用户获取个人信息【接口已通】", notes = "用户获取个人信息")
    @RequestMapping(value = "/getUserInfo.api", method = RequestMethod.GET)
    @InterceptConfiguration
    public BaseOutput<UserOutput> getUserInfoByUserId(@RequestParam Long userId) {
        try {
            UserOutput user = userService.getUserByUserId(userId);
            if (user == null) {
                return BaseOutput.failure("用户不存在");
            }
            // //初始状态获取个人信息不展示名称
            // if (user.getValidateState() == ValidateStateEnum.CERTREQ.getCode()){
            // user.setName("");
            // }
            return BaseOutput.success().setData(user);
        } catch (Exception e) {
            logger.error("get", e);
            return BaseOutput.failure();
        }
    }
}
