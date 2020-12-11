package com.dili.trace.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.exception.TraceBizException;
import com.dili.common.util.MD5Util;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.ss.util.DateUtils;
import com.dili.trace.api.components.ManageSystemComponent;
import com.dili.trace.api.components.SessionRedisService;
import com.dili.trace.api.input.UserInput;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.api.output.UserQrOutput;
import com.dili.trace.dao.UserMapper;
import com.dili.trace.domain.*;
import com.dili.trace.dto.MessageInputDto;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.enums.*;
import com.dili.trace.glossary.*;
import com.dili.trace.service.*;
import com.dili.trace.util.QRCodeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import one.util.streamex.StreamEx;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    public UserMapper getActualDao() {
        return (UserMapper) getDao();
    }

    // @Autowired
    // RedisUtil redisUtil;
    @Autowired
    UserPlateService userPlateService;
    @Autowired
    UsualAddressService usualAddressService;
    @Autowired
    EventMessageService eventMessageService;
    @Autowired
    RegisterBillService registerBillService;
    @Autowired
    DefaultConfiguration defaultConfiguration;
    @Autowired
    QrCodeService qrCodeService;
    @Value("${current.baseWebPath}")
    private String baseWebPath;
    @Autowired
    UserQrHistoryService userQrHistoryService;
    @Autowired
    TallyAreaNoService tallyAreaNoService;
    @Autowired
    SMSService sMSService;
    @Autowired
    SessionRedisService sessionRedisService;
    @Autowired
    IWxAppService wxAppService;

    @Autowired
    MessageService messageService;

    @Autowired
    UserStoreService userStoreService;

    @Autowired
    ManageSystemComponent manageSystemComponent;

    @Autowired
    SysConfigService sysConfigService;

    @Resource
    private UserTallyAreaService userTallyAreaService;

    @Resource
    UserHistoryService userHistoryService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(User user, UserTypeEnum userType, String originalPassword) {
        user.setState(EnabledStateEnum.ENABLED.getCode());
        user.setPassword(MD5Util.md5(originalPassword));
        user.setIsDelete(0L);
        user.setYn(YesOrNoEnum.YES.getCode());
        user.setUserType(userType.getCode());

//		// 验证验证码是否正确
//		if (flag) {
//			checkVerificationCode(user.getPhone(), user.getCheckCode());
//		}

        // 验证理货区号是否已注册
        if (StringUtils.isNotBlank(user.getTallyAreaNos())) {
            existsTallyAreaNo(null, Arrays.asList(user.getTallyAreaNos().split(",")));
        }
        // 验证手机号是否已注册
        if (existsAccount(user.getPhone())) {
            throw new TraceBizException("手机号已注册");
        }
        // 验证身份证号是否已注册
        if (StringUtils.isNotBlank(user.getCardNo()) && existsCardNo(user.getCardNo())) {
            throw new TraceBizException("身份证号已注册");
        }
        this.usualAddressService.increaseUsualAddressTodayCount(UsualAddressTypeEnum.USER, user.getSalesCityId());
        insertSelective(user);
        // 更新用户理货区
        updateUserTallyArea(user.getId(), Arrays.asList(StringUtils.trimToEmpty(user.getTallyAreaNos()).split(",")));
        // 增加车牌信息
//        LOGGER.info("输入车牌:{}",user.getPlates());
        List<String> plateList = this.parsePlate(user.getPlates());
//        LOGGER.info("解析车牌:{}",plateList.toString());
        if (!plateList.isEmpty()) {
            UserPlate up = this.userPlateService.findUserPlateByPlates(plateList).stream().findFirst().orElse(null);
            if (up != null) {
                throw new TraceBizException("车牌[" + up.getPlate() + "]已被其他用户使用");
            }
            this.userPlateService.deleteAndInsertUserPlate(user.getId(), plateList);
        }
        this.userHistoryService.insertUserHistoryForNewUser(user.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(User user, Boolean flag) {
        // 验证验证码是否正确
        if (flag) {
            user.setSource(UpStreamSourceEnum.REGISTER.getCode());
            this.sMSService.checkVerificationCode(user.getPhone(), user.getCheckCode());
        } else {
            user.setSource(UpStreamSourceEnum.DOWN.getCode());
        }

        // 验证手机号是否已注册
        if (existsAccount(user.getPhone())) {
            throw new TraceBizException("手机号已注册");
            // return;
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(this.defaultConfiguration.getPassword());// TODO 默认密码
        }
        user.setPassword(MD5Util.md5(user.getPassword()));

        if (StringUtils.isEmpty(user.getCardNo())) {
            user.setCardNo("");
        }

        if (StringUtils.isEmpty(user.getAddr())) {
            user.setAddr("");
        }
        user.setValidateState(ValidateStateEnum.CERTREQ.getCode());

        // // 验证身份证号是否已注册
        // if (existsCardNo(user.getCardNo())) {
        // throw new TraceBusinessException("身份证号已注册");
        // }
        // this.usualAddressService.increaseUsualAddressTodayCount(UsualAddressTypeEnum.USER,
        // user.getSalesCityId());
        String tallyAreaNos = this.tallyAreaNoService.parseAndConvertTallyAreaNos(user.getTallyAreaNos());
        user.setTallyAreaNos(tallyAreaNos);
        if (StringUtils.isBlank(user.getMarketName()) || Objects.isNull(user.getMarketId())) {
            throw new TraceBizException("用户市场为空");
//            user.setMarketId(Long.valueOf(MarketEnum.AQUATIC_TYPE.getCode()));
//            user.setMarketName("杭州水产");
        }
        insertSelective(user);
        this.tallyAreaNoService.saveOrUpdateTallyAreaNo(user.getId(), tallyAreaNos);
        // // 增加车牌信息
        // // LOGGER.info("输入车牌:{}",user.getPlates());
        // List<String> plateList = this.parsePlate(user.getPlates());
        // // LOGGER.info("解析车牌:{}",plateList.toString());
        // if (!plateList.isEmpty()) {
        // UserPlate up =
        // this.userPlateService.findUserPlateByPlates(plateList).stream().findFirst().orElse(null);
        // if (up != null) {
        // throw new TraceBusinessException("车牌[" + up.getPlate() + "]已被其他用户使用");
        // }
        // this.userPlateService.deleteAndInsertUserPlate(user.getId(), plateList);
        // }
        // this.userHistoryService.insertUserHistoryForNewUser(user.getId());
        // 新增用户店铺信息
        addUserStore(user);
        this.updateUserQrItem(user.getId());
    }

    private void addUserStore(User user) {
        UserStore userStore = new UserStore();
        userStore.setStoreName(user.getName());

        //店铺名重复则添加后缀
        List<UserStore> storeList = userStoreService.listByExample(userStore);
        if (!storeList.isEmpty()) {
            userStore.setStoreName(getUserStoreNameUnique(userStore.getStoreName()));
        }
        userStore.setUserId(user.getId());
        userStore.setCreated(new Date());
        userStore.setModified(new Date());
        userStoreService.insert(userStore);
    }

    private String getUserStoreNameUnique(String storeName) {
        int max = 100, min = 1;
        int ran2 = (int) (Math.random() * (max - min) + min);
        String newStoreName = storeName + "-" + String.valueOf(ran2);
        UserStore userStore = new UserStore();
        userStore.setStoreName(newStoreName);
        List<UserStore> storeList = userStoreService.listByExample(userStore);
        if (!storeList.isEmpty()) {
            return getUserStoreNameUnique(storeName);
        } else {
            return newStoreName;
        }
    }

    @Override
    public void updateUser(User user) {
        // 手机号验重
        if (StringUtils.isNotBlank(user.getPhone())) {
            List<User> users = getUserByExistsAccount(user.getPhone());
            if (CollectionUtils.isNotEmpty(users)) {
                users.forEach(o -> {
                    if (!o.getId().equals(user.getId()) && o.getPhone().equals(user.getPhone())) {
                        throw new TraceBizException("手机号已注册");
                    }
                });
            }
        }

        // 验证身份证号是否已注册
        if (StringUtils.isNotBlank(user.getCardNo())) {
            User query = DTOUtils.newDTO(User.class);
            query.setCardNo(user.getCardNo());
            query.setYn(YesOrNoEnum.YES.getCode());
            List<User> users = listByExample(query);
            if (CollectionUtils.isNotEmpty(users)) {
                users.forEach(o -> {
                    if (!o.getId().equals(user.getId()) && o.getCardNo().equals(user.getCardNo())) {
                        throw new TraceBizException("身份证号已注册");
                    }
                });
            }
        }

        User userPO = get(user.getId());
        if (!YesOrNoEnum.YES.getCode().equals(userPO.getYn())) {
            throw new TraceBizException("数据已被删除");
        }

        // LOGGER.info("输入车牌:{}",user.getPlates());
        List<String> plateList = this.parsePlate(user.getPlates());
        // LOGGER.info("解析车牌:{}",plateList.toString());
        if (!plateList.isEmpty()) {
            UserPlate up = this.userPlateService.findUserPlateByPlates(plateList).stream().filter(p -> {
                return !p.getUserId().equals(userPO.getId());
            }).findFirst().orElse(null);
            if (up != null) {
                throw new TraceBizException("车牌[" + up.getPlate() + "]已被其他用户使用");
            }
        }
        if (StringUtils.isBlank(user.getMarketName())) {
            user.setMarketName(userPO.getMarketName());
        }
        Integer validateAllow = 40;
        Integer needPush = 1;
        String tallyAreaNos = this.tallyAreaNoService.parseAndConvertTallyAreaNos(user.getTallyAreaNos());
        user.setTallyAreaNos(tallyAreaNos);
        this.userPlateService.deleteAndInsertUserPlate(userPO.getId(), plateList);
        user.setModified(new Date());
        if (validateAllow.equals(userPO.getValidateState())) {
            user.setIsPush(needPush);
        }
        updateSelective(user);
        this.tallyAreaNoService.saveOrUpdateTallyAreaNo(userPO.getId(), tallyAreaNos);
        this.usualAddressService.increaseUsualAddressTodayCount(UsualAddressTypeEnum.USER, userPO.getSalesCityId(),
                user.getSalesCityId());

        this.sessionRedisService.updateUser(this.get(user.getId()));

        //发送消息
        if (!validateAllow.equals(user.getValidateState())) {
            sendMessageByManage(user.getName(), user.getId(), user.getMarketId());
        }
        // this.updateUserQrItem(user.getId());

    }

    /**
     * 发送消息给管理员提示审核
     *
     * @param userName
     * @param userId
     */
    private void sendMessageByManage(String userName, Long userId, Long marketId) {
        // 审核通过增加消息
        MessageInputDto messageInputDto = new MessageInputDto();
        messageInputDto.setCreatorId(userId);
        messageInputDto.setMessageType(MessageTypeEnum.USERREGISTER.getCode());
        messageInputDto.setSourceBusinessType(MessageStateEnum.BUSINESS_TYPE_USER.getCode());
        messageInputDto.setSourceBusinessId(userId);
        messageInputDto.setEventMessageContentParam(new String[]{userName});
        messageInputDto.setReceiverType(MessageReceiverEnum.MESSAGE_RECEIVER_TYPE_MANAGER.getCode());

        List<com.dili.uap.sdk.domain.User> manageList = manageSystemComponent.findUserByUserResource("user/index.html#list", marketId);
        Set<Long> managerIdSet = new HashSet<>();
        StreamEx.of(manageList).nonNull().forEach(s -> {
            //没有判断用户状态
            managerIdSet.add(s.getId());
        });
        Long[] managerId = managerIdSet.toArray(new Long[managerIdSet.size()]);
        messageInputDto.setReceiverIdArray(managerId);
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("userName", userName);
        messageInputDto.setSmsContentParam(smsMap);
        messageService.addMessage(messageInputDto);
    }

    private List<String> parsePlate(String plates) {
        List<String> plateList = new ArrayList<>();
        if (StringUtils.isBlank(plates)) {
            return plateList;
        }
        if (JSON.isValid(plates) && JSON.isValidArray(plates)) {
            JSON.parseArray(plates).stream().filter(Objects::nonNull).map(String::valueOf)
                    .collect(Collectors.toCollection(() -> plateList));
        } else {
            plateList.add(plates);
        }

        return plateList.stream().filter(StringUtils::isNotBlank).map(String::trim).collect(toList());

    }

    @Override
    public User login(String phone, String encryptedPassword) {
        User query = DTOUtils.newDTO(User.class);
        query.setPhone(phone);
        query.setYn(YesOrNoEnum.YES.getCode());
        User po = listByExample(query).stream().findFirst().orElse(null);
        if (po == null) {
            throw new TraceBizException("手机号未注册");
        }
        if (EnabledStateEnum.DISABLED.getCode().equals(po.getState())) {
            throw new TraceBizException("手机号已禁用");
        }
        if (!po.getPassword().equals(encryptedPassword)) {
            throw new TraceBizException("密码错误");
        }
        return po;
    }

    @Transactional
    @Override
    public void resetPassword(User user) {
        User query = DTOUtils.newDTO(User.class);
        query.setPhone(user.getPhone());
        query.setYn(YesOrNoEnum.YES.getCode());
        User po = listByExample(query).stream().findFirst().orElse(null);
        if (po == null) {
            throw new TraceBizException("用户不存在");
        }
        User condition = DTOUtils.newDTO(User.class);
        condition.setId(po.getId());
        condition.setVersion(po.getVersion());
        po.setPassword(user.getPassword());
        po.setVersion(po.getVersion() + 1);
        int i = updateSelectiveByExample(po, condition);
        if (i != 1) {
            throw new TraceBizException("数据已变更,请稍后重试");
        }
    }

    @Transactional
    @Override
    public void renewPassword(User user, String smscode) {
        this.sMSService.checkResetPasswordSmsCode(user.getPhone(), smscode);
        User query = DTOUtils.newDTO(User.class);
        query.setPhone(user.getPhone());
        query.setYn(YesOrNoEnum.YES.getCode());
        User po = listByExample(query).stream().findFirst().orElse(null);
        if (po == null) {
            throw new TraceBizException("用户不存在");
        }
        User condition = DTOUtils.newDTO(User.class);
        condition.setId(po.getId());
        condition.setVersion(po.getVersion());
        po.setPassword(MD5Util.md5(user.getPassword()));
        po.setVersion(po.getVersion() + 1);
        int i = updateSelectiveByExample(po, condition);
        if (i != 1) {
            throw new TraceBizException("数据已变更,请稍后重试");
        }
    }

    @Transactional
    @Override
    public void changePassword(User user) {
        User po = get(user.getId());
        if (po == null) {
            throw new TraceBizException("用户不存在");
        }
        if (!po.getPassword().equals(user.getOldPassword())) {
            throw new TraceBizException("原密码错误");
        }
        User condition = DTOUtils.newDTO(User.class);
        condition.setId(po.getId());
        condition.setVersion(po.getVersion());
        po.setPassword(user.getPassword());
        po.setVersion(po.getVersion() + 1);
        int i = updateSelectiveByExample(po, condition);
        if (i != 1) {
            throw new TraceBizException("数据已变更,请稍后重试");
        }
    }

    /**
     * 检测手机号是否存在
     *
     * @param phone
     * @return true 存在 false 不存在
     */
    @Override
    public boolean existsAccount(String phone) {
        return !CollUtil.isEmpty(getUserByExistsAccount(phone));
    }

    @Override
    public List<User> getUserByExistsAccount(String phone) {
        User query = DTOUtils.newDTO(User.class);
        query.setPhone(phone);
        query.setYn(YesOrNoEnum.YES.getCode());
        return listByExample(query);
    }

    @Override
    @Transactional
    public BaseOutput updateEnable(Long id, Boolean enable) {
        // long tt =
        // this.redisUtil.getRedisTemplate().getExpire(ExecutionConstants.WAITING_DISABLED_USER_PREFIX);
        User user = get(id);
        if (user == null) {
            return BaseOutput.failure("数据不存在");
        }
        if (!YesOrNoEnum.YES.getCode().equals(user.getYn())) {
            return BaseOutput.failure("数据已被删除");
        }
        Integer needPush = 1;
        user.setModified(new Date());
        user.setIsPush(needPush);
        if (enable) {
            user.setState(EnabledStateEnum.ENABLED.getCode());
            this.updateSelective(user);
            this.sessionRedisService.updateUser(this.get(user.getId()));
            // this.redisUtil.getRedisTemplate().opsForSet().remove(ExecutionConstants.WAITING_DISABLED_USER_PREFIX,
            // id);
        } else {
            user.setState(EnabledStateEnum.DISABLED.getCode());
            this.updateSelective(user);
            this.sessionRedisService.removeUser(this.get(user.getId()));
            // this.redisUtil.getRedisTemplate().opsForSet().add(ExecutionConstants.WAITING_DISABLED_USER_PREFIX,
            // id);
        }
        return BaseOutput.success("操作成功");
    }

    private Optional<String> andCondition(UserListDto dto) {
        List<String> strList = new ArrayList<String>();
        if (dto != null && dto.getHasBusinessLicense() != null) {
            if (dto.getHasBusinessLicense()) {
                strList.add(" (business_license_url is not null and business_license_url<>'') ");
            } else {
                strList.add(" (business_license_url is null or business_license_url='') ");
            }
        }

        if (dto != null && StringUtils.isNotBlank(dto.getPlates())) {
            strList.add(" id in (select user_id from user_plate where plate like '"
                    + dto.getPlates().trim().toUpperCase() + "%')");
        }
        if (!strList.isEmpty()) {
            return Optional.of(String.join(" and ", strList));
        }
        return Optional.empty();
    }

    @Override
    public EasyuiPageOutput listEasyuiPageByExample(UserListDto dto) throws Exception {
        // 经营户卡号查询，不足6位，左边补0
        if (!StringUtils.isEmpty(dto.getThirdPartyCode())) {
            dto.setThirdPartyCode(StringUtils.leftPad(dto.getThirdPartyCode(), 6, "0"));
        }
        this.andCondition(dto).ifPresent(str -> {
            dto.mset(IDTO.AND_CONDITION_EXPR, str);
        });
        dto.setYn(1);
        EasyuiPageOutput out = this.listEasyuiPageByExample(dto, true);
        List<User> users = out.getRows();
        List<Long> userIdList = users.stream().map(o -> {
            return o.getId();
        }).collect(toList());
        Map<Long, List<UserPlate>> userPlateMap = this.userPlateService.findUserPlateByUserIdList(userIdList);
        List<User> userList = users.stream().map(u -> {
            // Long userId = (Long) u.get("id");
            Long userId = u.getId();
            if (userPlateMap.containsKey(userId)) {
                String plates = userPlateMap.get(userId).stream().map(UserPlate::getPlate)
                        .collect(Collectors.joining(","));
               // u.put("plates", plates);
                u.setPlates(plates);
            } else {
              //  u.put("plates", "");
                u.setPlates("");
            }
            return u;
        }).collect(toList());
        out.setRows(userList);

        return out;
    }

    @Override
    public BaseOutput deleteUser(Long id) {
        if (id == null) {
            return BaseOutput.failure("参数错误");
        }
        User user = this.get(id);
        if (user == null) {
            return BaseOutput.failure("数据不存在");
        }

        List<String> tallyAreaNos = Arrays.asList(StringUtils.trimToEmpty(user.getTallyAreaNos()).split(","));

        user.setState(EnabledStateEnum.DISABLED.getCode());
        user.setYn(YesOrNoEnum.NO.getCode());
        user.setIsDelete(user.getId());
        user.setOpenId("");
        user.setModified(new Date());
        this.updateSelective(user);

        // 删除用户车牌信息
        UserPlate up = DTOUtils.newDTO(UserPlate.class);
        up.setUserId(user.getId());
        this.userPlateService.deleteByExample(up);
        this.sessionRedisService.removeUser(this.get(user.getId()));
        return BaseOutput.success("操作成功");

        // UserTallyArea example=DTOUtils.newDTO(UserTallyArea.class);
        // example.setUserId(user.getId());
        // this.userTallyAreaService.deleteByExample(example);
        // user.setTallyAreaNos(null);
        // user.setState(EnabledStateEnum.DISABLED.getCode());
        // this.updateExact(user);

    }

    @Override
    public BaseOutput<List<UserOutput>> countGroupByValidateState(User user) {
        // if (user == null) {
        // return BaseOutput.failure("参数错误");
        // }
        return BaseOutput.success().setData(getActualDao().countGroupByValidateState(user));
    }

    @Override
    public BasePage<UserOutput> pageUserByQuery(UserInput user) {
        PageHelper.startPage(user.getPage(), user.getRows());
        List<UserOutput> list = getActualDao().listUserByQuery(user);
        Page<User> page = (Page) list;
        BasePage<UserOutput> result = new BasePage<UserOutput>();
        result.setDatas(list);
        result.setPage(page.getPageNum());
        result.setRows(page.getPageSize());
        result.setTotalItem(page.getTotal());
        result.setTotalPage(page.getPages());
        result.setStartIndex(page.getStartRow());
        return result;
    }

    @Override
    public BaseOutput verifyUserCert(UserInput input, OperatorUser operatorUser) {
        if (input == null || input.getId() == null) {
            return BaseOutput.failure("参数错误");
        }
        User user = get(input.getId());
        if (ValidateStateEnum.UNCERT.getCode() != user.getValidateState()) {
            return BaseOutput.failure("当前状态不能审核");
        }
        Integer needPush = 1;
        // 审核通过
        if (ValidateStateEnum.PASSED.getCode() == input.getValidateState()) {
            sendVerifyCertMessage(user, MessageTypeEnum.REGISTERPASS.getCode(), null, operatorUser);
            user.setIsPush(needPush);
            user.setIsActive(needPush);
        } else if (ValidateStateEnum.NOPASS.getCode() == input.getValidateState()) {
            // 审核不通过
            sendVerifyCertMessage(user, MessageTypeEnum.REGISTERFAILURE.getCode(), input.getRefuseReason(), operatorUser);
        } else {
            String message = "系统出现异常";
            return BaseOutput.failure(message);
        }

        user.setValidateState(input.getValidateState());
        user.setModified(new Date());
        int retRows = update(user);
        if (retRows > 0) {
            return BaseOutput.success("用户资料审核申请已通过");
        } else {
            return BaseOutput.failure();
        }
    }

    private void sendVerifyCertMessage(User user, Integer messageType, String operateReason, OperatorUser operatorUser) {
        // 审核通过增加消息
        MessageInputDto messageInputDto = new MessageInputDto();
        messageInputDto.setCreatorId(operatorUser.getId());
        messageInputDto.setMessageType(messageType);
        messageInputDto.setReceiverIdArray(new Long[]{user.getId()});
        messageInputDto.setEventMessageContentParam(new String[]{operateReason});

        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("userName", user.getName());
        messageService.addMessage(messageInputDto);
    }


    private void updateUserQrItem(Long userId) {
        if (userId == null) {
            return;
        }
        this.userQrHistoryService.createUserQrHistoryForUserRegist(userId, UserQrStatusEnum.BLACK.getCode());
    }

    @Override
    public List<User> findUserByNameOrPhoneOrTallyNo(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return Lists.newArrayList();
        }
        Example e = new Example(User.class);
        e.or().orLike("tallyAreaNos", "%" + keyword.trim() + "%").orLike("name", "%" + keyword.trim() + "%")
                .orLike("phone", "%" + keyword.trim() + "%");
        return this.getDao().selectByExample(e);
    }

    @Override
    public UserQrOutput getUserQrCode(Long userId) throws Exception {
        User userItem = this.get(userId);
        if (userItem == null) {
            throw new TraceBizException("数据不存在");
        }
        String content = this.baseWebPath + "/user?userId=" + userId;

        Integer getARgb = UserQrStatusEnum.fromCode(userItem.getQrStatus()).map(UserQrStatusEnum::getARgb)
                .orElse(UserQrStatusEnum.BLACK.getARgb());
        String base64Img = this.qrCodeService.getBase64QrCode(content, 400, 400, getARgb);
        UserQrOutput qrOutput = new UserQrOutput();
        qrOutput.setBase64QRImg(base64Img);
        qrOutput.setUpdated(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        qrOutput.setUserId(userId);
        return qrOutput;
    }

    @Override
    public List<User> findUserByQrStatusList(List<Integer> qrStatusList) {

        Example e = new Example(User.class);
        e.and().andIn("qrStatus", qrStatusList);
        return this.getDao().selectByExample(e);
    }

    @Override
    public Integer countUser(User user) {
        return this.getActualDao().selectCount(user);
    }

    @Override
    public User wxLogin(String openid) {
        User query = DTOUtils.newDTO(User.class);
        query.setOpenId(openid);
        query.setYn(YesOrNoEnum.YES.getCode());
        User po = listByExample(query).stream().findFirst().orElse(null);
        if (po == null) {
            throw new TraceBizException("用户未注册");
        }
        if (EnabledStateEnum.DISABLED.getCode().equals(po.getState())) {
            throw new TraceBizException("手机号已禁用");
        }
        return po;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String wxRegister(User user) throws JsonProcessingException {
        // 验证手机号是否已注册
        if (existsAccount(user.getPhone())) {
            throw new TraceBizException("手机号已注册");
        }
        //验证openid是否已注册
        if (existsOpenId(user.getOpenId())) {
            throw new TraceBizException("微信已绑定用户");
        }
        user.setPassword(MD5Util.md5(this.defaultConfiguration.getPassword()));
        user.setCardNo("");
        user.setAddr("");
        user.setValidateState(ValidateStateEnum.CERTREQ.getCode());
        String tallyAreaNos = this.tallyAreaNoService.parseAndConvertTallyAreaNos(user.getTallyAreaNos());
        user.setTallyAreaNos(tallyAreaNos);
        if (StringUtils.isBlank(user.getMarketName()) || Objects.isNull(user.getMarketId())) {
            throw new TraceBizException("用户市场为空");
        }
        user.setSource(UpStreamSourceEnum.REGISTER.getCode());
        insertSelective(user);
        this.tallyAreaNoService.saveOrUpdateTallyAreaNo(user.getId(), tallyAreaNos);
        this.updateUserQrItem(user.getId());
        return this.defaultConfiguration.getPassword();
    }

    @Override
    public void userBindWeChat(String openid, Long user_id) {
        //验证微信是否已绑定用户
        User query = DTOUtils.newDTO(User.class);
        query.setOpenId(openid);
        if (null != getActualDao().selectOne(query)) {
            throw new TraceBizException("微信已绑定用户");
        }
        User user = get(user_id);
        if (null != user && StringUtils.isNotBlank(user.getOpenId())) {
            throw new TraceBizException("用户已绑定微信");
        }
        user.setOpenId(openid);
        update(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void confirmBindWeChatTip(String user_id) {
        User user = get(Long.valueOf(user_id));
        user.setConfirmDate(new Date());
        update(user);
    }

    @Override
    public List<UserOutput> listUserByStoreName(Long userId, String queryCondition, Long marketId) {
        return getActualDao().listUserByStoreName(userId, queryCondition, marketId);
    }

    @Override
    public UserOutput getUserByUserId(Long userId) {
        return getActualDao().getUserByUserId(userId);
    }

    private boolean existsOpenId(String openid) {
        if (StringUtils.isBlank(openid)) {
            throw new TraceBizException("注册用户openid为空");
        }
        User openuser = DTOUtils.newDTO(User.class);
        openuser.setOpenId(openid);
        openuser.setYn(YesOrNoEnum.YES.getCode());
        return null != getActualDao().selectOne(openuser);
    }

    private WxApp getWxAppInfo(String appId) {
        if (StringUtils.isBlank(appId)) {
            return null;
        }
        WxApp wxApp = new WxApp();
        wxApp.setAppId(appId);
        List<WxApp> list = wxAppService.list(wxApp);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public UserQrOutput getUserQrCodeWithName(Long userId) throws Exception {
        UserQrOutput qrOutput = new UserQrOutput();
        qrOutput.setUpdated(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        qrOutput.setUserId(userId);
        String content = this.baseWebPath + "/user?userId=" + userId;
        User user = this.get(userId);
        qrOutput.setUserName(user.getName());
        UserStore userStoreParam = new UserStore();
        userStoreParam.setUserId(userId);
        UserStore userStore = StreamEx.of(userStoreService.list(userStoreParam)).nonNull().findFirst().orElse(null);
        if (userStore != null) {
            qrOutput.setUserName(userStore.getStoreName());
        }
        byte[] bytes = QRCodeUtil.encode(content, this.getUserQrCode(userId).getBase64QRImg(), false, qrOutput.getUserName());
        String base64Img = QRCodeUtil.base64Image(bytes);
        qrOutput.setBase64QRImg(base64Img);
        return qrOutput;
    }

    @Override
    public void updateUserIsPushFlag(Integer isPush, List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            LOGGER.error("updateUserIsPushFlag :userIdList IS NULL");
            return;
        }
        if (null == isPush) {
            LOGGER.error("updateUserIsPushFlag :isPush IS NULL");
            return;
        }
        getActualDao().updateUserIsPushFlag(isPush, userIdList);
    }

    @Override
    public void updateUserActiveByTime() {
        String optType = SysConfigTypeEnum.OPERATION_LIMIT_DAY.getCode();
        String optCategory = SysConfigTypeEnum.OPERATION_LIMIT_DAY.getCode();
        SysConfig sysConfig = new SysConfig();
        sysConfig.setOptType(optType);
        sysConfig.setOptCategory(optCategory);
        List<SysConfig> sysConfigList = sysConfigService.listByExample(sysConfig);
        //未配置活跃限定天数，则不计算活跃
        if (CollectionUtils.isEmpty(sysConfigList)) {
            return;
        }
        //活跃限定天数为0，则不计算活跃
        String val = sysConfigList.get(0).getOptValue();
        int limitDay = Integer.valueOf(val);
        if (limitDay <= 0) {
            return;
        }
        limitDay = 0 - limitDay;
        Map<String, Object> activeMap = processActiveMap(limitDay);
        //patch活跃
        getActualDao().updateUserActiveByBill(activeMap);
        getActualDao().updateUserActiveByBuyer(activeMap);
        getActualDao().updateUserActiveBySeller(activeMap);
        //patch不活跃
        updateUserUnActive(activeMap);
    }

    /**
     * 更新用户去活跃
     *
     * @param map
     */
    private void updateUserUnActive(Map<String, Object> map) {
        List<User> unActiveListBill = getActualDao().getActiveUserListByBill(map);
        List<User> unActiveListSeller = getActualDao().getActiveUserListBySeller(map);
        CopyOnWriteArrayList<Long> billList = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<Long> sellList = new CopyOnWriteArrayList<>();
        //去活跃 未激活、禁用用户
        Integer activeFlag = 1;
        User user = DTOUtils.newDTO(User.class);
        user.setIsActive(activeFlag);
        user.mset(IDTO.AND_CONDITION_EXPR, " ( state=0 or yn=-1 )");
        List<User> userList = listByExample(user);
        List<Long> sUserList = StreamEx.of(userList).nonNull().map(User::getId).collect(toList());
        if (CollectionUtils.isNotEmpty(sUserList)) {
            LOGGER.info("---用户状态去活跃集合 relustList---" + JSON.toJSONString(sUserList));
            getActualDao().updateUserUnActiveFlag(sUserList);
        }
        //无报备单用户
        if (CollectionUtils.isNotEmpty(unActiveListBill)) {
            StreamEx.of(unActiveListBill).nonNull().forEach(b -> {
                billList.add(b.getId());
            });
        }
        //无销售交易用户
        if (CollectionUtils.isNotEmpty(unActiveListSeller)) {
            StreamEx.of(unActiveListSeller).nonNull().forEach(b -> {
                sellList.add(b.getId());
            });
        }
        //用户已存在报备单
        if (CollectionUtils.isEmpty(billList)) {
            return;
        }
        //用户已销售交易
        if (CollectionUtils.isEmpty(unActiveListSeller)) {
            return;
        }

        List<Long> resultList = billList.stream().filter(r -> sellList.contains(r)).collect(toList());
        LOGGER.info("---去活跃集合 relustList---" + JSON.toJSONString(resultList));
        if (CollectionUtils.isNotEmpty(resultList)) {
            getActualDao().updateUserUnActiveFlag(resultList);
        }
    }

    private Map<String, Object> processActiveMap(int limitDay) {
        Map<String, Object> activeMap = new HashMap<>(16);
        Date limitDate = new Date();
        limitDate = DateUtils.addDays(limitDate, limitDay);
        String limitDateStr = DateUtils.format(limitDate, "yyyy-MM-dd HH:mm:ss");
        activeMap.put("limitTime", limitDateStr);
        return activeMap;
    }

    @Override
    public BasePage<User>  findUserByKeyword(UserListDto input) {
        PageHelper.startPage(input.getPage(), input.getRows());
        List<User> userList = new ArrayList<User>();
        if (StringUtils.isBlank(input.getThirdPartyCode()) && StringUtils.isBlank(input.getPhone()) && StringUtils.isBlank(input.getName())) {
            userList = new ArrayList<User>();
        } else {
            Example e = new Example(User.class);
            if (StringUtils.isNotBlank(input.getThirdPartyCode())) {
                e.and().andEqualTo("thirdPartyCode", input.getThirdPartyCode());
            }
            if (StringUtils.isNotBlank(input.getPhone())) {
                e.and().andLike("phone", "%" + input.getPhone().trim() + "%");
            }
            if (StringUtils.isNotBlank(input.getName())) {
                e.and().andLike("name", "%" + input.getName().trim() + "%");
            }

//            e.or().orLike("thirdPartyCode", "%" + input.getKeyword().trim() + "%")
//                    .orLike("name", "%" + input.getName().trim() + "%")
//                    .orLike("phone", "%" + input.getPhone().trim() + "%");

            e.and().andEqualTo("state", EnabledStateEnum.ENABLED.getCode());
            e.and().andEqualTo("yn", YesOrNoEnum.YES.getCode());
            // e.and().andEqualTo("isActive", YesOrNoEnum.YES.getCode());
            e.and().andEqualTo("marketId", input.getMarketId());
            userList =  this.getDao().selectByExample(e);
        }

        Page<User> page = (Page) userList;
        BasePage<User> result = new BasePage<User>();
        result.setDatas(userList);
        result.setPage(page.getPageNum());
        result.setRows(page.getPageSize());
        result.setTotalItem(page.getTotal());
        result.setTotalPage(page.getPages());
        result.setStartIndex(page.getStartRow());
        return result;
    }

    @Override
    public User findByTallyAreaNo(String tallyAreaNo) {
        UserTallyArea query = DTOUtils.newDTO(UserTallyArea.class);
        query.setTallyAreaNo(tallyAreaNo);
        UserTallyArea userTallyArea = userTallyAreaService.list(query).stream().findFirst().orElse(null);
        if (null == userTallyArea) {
            return null;
        }

        User userQuery = DTOUtils.newDTO(User.class);
        userQuery.setId(userTallyArea.getUserId());
        userQuery.setState(EnabledStateEnum.ENABLED.getCode());
        return list(userQuery).stream().findFirst().orElse(null);
    }

    @Override
    public List<DTO> queryByTallyAreaNo(String likeTallyAreaNo) {

        UserListDto userListDto = DTOUtils.newDTO(UserListDto.class);
        userListDto.setLikeTallyAreaNos(likeTallyAreaNo);

        List<User> userItemList = this.listByExample(userListDto);
        Map<Long, List<String>> userIdMap = StreamEx.of(userItemList).toMap(User::getId, u -> {
            return Arrays.asList(StringUtils.trimToEmpty(u.getTallyAreaNos()).split(","));
        });

        Map<Long, String> userIdNameMap = StreamEx.of(userItemList).toMap(User::getId, User::getName);

        List<Long> userIdList = StreamEx.ofKeys(userIdMap).toList();
        Map<Long, List<UserPlate>> userIdPlateMap = this.userPlateService.findUserPlateByUserIdList(userIdList);

        List<DTO> data = StreamEx.ofValues(userIdPlateMap).flatMap(List::stream).flatMap(up -> {

            return userIdMap.getOrDefault(up.getUserId(), Collections.emptyList()).stream().map(tno -> {

                DTO dto = new DTO();
                dto.put("userName", userIdNameMap.getOrDefault(up.getUserId(), ""));
                dto.put("plate", up.getPlate());
                dto.put("tallyAreaNo", tno);
                return dto;

            });
        }).toList();

        return data;

    }

    /**
     * 创建场外委托用户
     *
     * @param user
     * @return
     */
    @Transactional
    public User createCommissionUser(User user) {
        if (user == null || StringUtils.isAnyBlank(user.getPhone(), user.getPassword(), user.getAckPassword())) {
            throw new TraceBizException("参数错误");
        }
        if (!user.getPassword().equals(user.getAckPassword())) {
            throw new TraceBizException("两次输入密码不一致");
        }
//		user.setPlates(null);
//		user.setTallyAreaNos(null);
        this.register(user, UserTypeEnum.COMMISSION_USER, user.getPassword());
        return user;
    }

    /**
     * 检测理货区号是否已注册
     *
     * @param userId
     * @param tallyAreaNos
     */
    public void existsTallyAreaNo(Long userId, List<String> tallyAreaNos) {
        tallyAreaNos.forEach(tallyAreaNo -> {
            UserTallyArea query = DTOUtils.newDTO(UserTallyArea.class);
            query.setTallyAreaNo(tallyAreaNo);
            UserTallyArea userTallyArea = userTallyAreaService.listByExample(query).stream().findFirst().orElse(null);
            if (null != userTallyArea && !userTallyArea.getUserId().equals(userId)) {
                throw new TraceBizException("理货区号【" + tallyAreaNo + "】已被注册");
            }
        });
    }

    /**
     * 检测身份证号是否存在
     *
     * @param cardNo
     * @return true 存在 false 不存在
     */
    public boolean existsCardNo(String cardNo) {
        User query = DTOUtils.newDTO(User.class);
        query.setCardNo(cardNo);
        query.setYn(YesOrNoEnum.YES.getCode());
        return !CollUtil.isEmpty(listByExample(query));
    }

    /**
     * 更新用户理货区
     *
     * @param userId
     * @param tallyAreaNos
     */
    private void updateUserTallyArea(Long userId, List<String> tallyAreaNos) {
        if (null != userId) {
            UserTallyArea query = DTOUtils.newDTO(UserTallyArea.class);
            query.setUserId(userId);
            userTallyAreaService.deleteByExample(query);

            List<UserTallyArea> userTallyAreas = StreamEx.ofNullable(tallyAreaNos).nonNull()
                    .flatCollection(Function.identity()).filter(StringUtils::isNotBlank).map(tallyAreaNo -> {
                        UserTallyArea userTallyArea = DTOUtils.newDTO(UserTallyArea.class);
                        userTallyArea.setUserId(userId);
                        userTallyArea.setTallyAreaNo(tallyAreaNo);
                        return userTallyArea;
                    }).toList();

            if (!userTallyAreas.isEmpty()) {
                userTallyAreaService.batchInsert(userTallyAreas);
            }
        }

    }

    /**
     * 获取无照片的经营户
     * @return
     */
    @Override
    public List<User> getUserByCredentialUrl(User user) {
        return getActualDao().getUserByCredentialUrl(user);
    }
}