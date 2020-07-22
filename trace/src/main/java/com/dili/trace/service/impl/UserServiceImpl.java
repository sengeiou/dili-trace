package com.dili.trace.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.dili.common.config.DefaultConfiguration;
import com.dili.common.entity.ExecutionConstants;
import com.dili.common.exception.TraceBusinessException;
import com.dili.common.service.RedisService;
import com.dili.common.util.MD5Util;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.BasePage;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.dto.DTO;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.dto.IDTO;
import com.dili.trace.api.input.UserInput;
import com.dili.trace.api.output.UserOutput;
import com.dili.trace.api.output.UserQrOutput;
import com.dili.trace.dao.UserMapper;
import com.dili.trace.domain.EventMessage;
import com.dili.trace.domain.User;
import com.dili.trace.domain.UserPlate;
import com.dili.trace.dto.OperatorUser;
import com.dili.trace.dto.UserListDto;
import com.dili.trace.enums.MessageStateEnum;
import com.dili.trace.enums.ValidateStateEnum;
import com.dili.trace.glossary.EnabledStateEnum;
import com.dili.trace.glossary.UpStreamSourceEnum;
import com.dili.trace.glossary.UserQrStatusEnum;
import com.dili.trace.glossary.UserTypeEnum;
import com.dili.trace.glossary.UsualAddressTypeEnum;
import com.dili.trace.glossary.YnEnum;
import com.dili.trace.service.EventMessageService;
import com.dili.trace.service.QrCodeService;
import com.dili.trace.service.RUserTallyAreaService;
import com.dili.trace.service.RegisterBillService;
import com.dili.trace.service.SMSService;
import com.dili.trace.service.TallyAreaNoService;
import com.dili.trace.service.UserPlateService;
import com.dili.trace.service.UserQrHistoryService;
import com.dili.trace.service.UserService;
import com.dili.trace.service.UsualAddressService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2019-07-26 09:20:35.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService {

    public UserMapper getActualDao() {
        return (UserMapper) getDao();
    }

    @Resource
    private RedisService redisService;

    @Resource
    UserPlateService userPlateService;
    @Resource
    UsualAddressService usualAddressService;
    @Resource
    EventMessageService eventMessageService;
    @Resource
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(User user, Boolean flag) {
        // 验证验证码是否正确
        if (flag) {
            this.sMSService.checkVerificationCode(user.getPhone(), user.getCheckCode());
            user.setSource(UpStreamSourceEnum.REGISTER.getCode());
        } else {
            user.setSource(UpStreamSourceEnum.DOWN.getCode());
        }

        // 验证手机号是否已注册
        if (existsAccount(user.getPhone())) {
            throw new TraceBusinessException("手机号已注册");
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
        user.setValidateState(ValidateStateEnum.UNCERT.getCode());

        // // 验证身份证号是否已注册
        // if (existsCardNo(user.getCardNo())) {
        // throw new TraceBusinessException("身份证号已注册");
        // }
        // this.usualAddressService.increaseUsualAddressTodayCount(UsualAddressTypeEnum.USER,
        // user.getSalesCityId());
        String tallyAreaNos = this.tallyAreaNoService.parseAndConvertTallyAreaNos(user.getTallyAreaNos());
        user.setTallyAreaNos(tallyAreaNos);
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
        this.updateUserQrItem(user.getId());
    }

    @Override
    public void updateUser(User user) {
        // 手机号验重
        if (StringUtils.isNotBlank(user.getPhone())) {
            List<User> users = getUserByExistsAccount(user.getPhone());
            if (CollectionUtils.isNotEmpty(users)) {
                users.forEach(o -> {
                    if (!o.getId().equals(user.getId()) && o.getPhone().equals(user.getPhone())) {
                        throw new TraceBusinessException("手机号已注册");
                    }
                });
            }
        }

        // 验证身份证号是否已注册
        if (StringUtils.isNotBlank(user.getCardNo())) {
            User query = DTOUtils.newDTO(User.class);
            query.setCardNo(user.getCardNo());
            query.setYn(YnEnum.YES.getCode());
            List<User> users = listByExample(query);
            if (CollectionUtils.isNotEmpty(users)) {
                users.forEach(o -> {
                    if (!o.getId().equals(user.getId()) && o.getCardNo().equals(user.getCardNo())) {
                        throw new TraceBusinessException("身份证号已注册");
                    }
                });
            }
        }

        User userPO = get(user.getId());
        if (!YnEnum.YES.getCode().equals(userPO.getYn())) {
            throw new TraceBusinessException("数据已被删除");
        }

        // LOGGER.info("输入车牌:{}",user.getPlates());
        List<String> plateList = this.parsePlate(user.getPlates());
        // LOGGER.info("解析车牌:{}",plateList.toString());
        if (!plateList.isEmpty()) {
            UserPlate up = this.userPlateService.findUserPlateByPlates(plateList).stream().filter(p -> {
                return !p.getUserId().equals(userPO.getId());
            }).findFirst().orElse(null);
            if (up != null) {
                throw new TraceBusinessException("车牌[" + up.getPlate() + "]已被其他用户使用");
            }
        }

        String tallyAreaNos = this.tallyAreaNoService.parseAndConvertTallyAreaNos(user.getTallyAreaNos());
        user.setTallyAreaNos(tallyAreaNos);
        this.userPlateService.deleteAndInsertUserPlate(userPO.getId(), plateList);
        updateSelective(user);
        this.tallyAreaNoService.saveOrUpdateTallyAreaNo(userPO.getId(), tallyAreaNos);
        this.usualAddressService.increaseUsualAddressTodayCount(UsualAddressTypeEnum.USER, userPO.getSalesCityId(),
                user.getSalesCityId());
        // this.updateUserQrItem(user.getId());

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

        return plateList.stream().filter(StringUtils::isNotBlank).map(String::trim).collect(Collectors.toList());

    }

    @Override
    public User login(String phone, String encryptedPassword) {
        User query = DTOUtils.newDTO(User.class);
        query.setPhone(phone);
        query.setYn(YnEnum.YES.getCode());
        User po = listByExample(query).stream().findFirst().orElse(null);
        if (po == null) {
            throw new TraceBusinessException("手机号未注册");
        }
        if (EnabledStateEnum.DISABLED.getCode().equals(po.getState())) {
            throw new TraceBusinessException("手机号已禁用");
        }
        if (!po.getPassword().equals(encryptedPassword)) {
            throw new TraceBusinessException("密码错误");
        }
        return po;
    }

    @Transactional
    @Override
    public void resetPassword(User user) {
        User query = DTOUtils.newDTO(User.class);
        query.setPhone(user.getPhone());
        query.setYn(YnEnum.YES.getCode());
        User po = listByExample(query).stream().findFirst().orElse(null);
        if (po == null) {
            throw new TraceBusinessException("用户不存在");
        }
        User condition = DTOUtils.newDTO(User.class);
        condition.setId(po.getId());
        condition.setVersion(po.getVersion());
        po.setPassword(user.getPassword());
        po.setVersion(po.getVersion() + 1);
        int i = updateSelectiveByExample(po, condition);
        if (i != 1) {
            throw new TraceBusinessException("数据已变更,请稍后重试");
        }
    }

    @Transactional
    @Override
    public void renewPassword(User user,String smscode) {
        this.sMSService.checkResetPasswordSmsCode(user.getPhone(), smscode);
        User query = DTOUtils.newDTO(User.class);
        query.setPhone(user.getPhone());
        query.setYn(YnEnum.YES.getCode());
        User po = listByExample(query).stream().findFirst().orElse(null);
        if (po == null) {
            throw new TraceBusinessException("用户不存在");
        }
        User condition = DTOUtils.newDTO(User.class);
        condition.setId(po.getId());
        condition.setVersion(po.getVersion());
        po.setPassword(MD5Util.md5(user.getPassword()));
        po.setVersion(po.getVersion() + 1);
        int i = updateSelectiveByExample(po, condition);
        if (i != 1) {
            throw new TraceBusinessException("数据已变更,请稍后重试");
        }
    }

    @Transactional
    @Override
    public void changePassword(User user) {
        User po = get(user.getId());
        if (po == null) {
            throw new TraceBusinessException("用户不存在");
        }
        if (!po.getPassword().equals(user.getOldPassword())) {
            throw new TraceBusinessException("原密码错误");
        }
        User condition = DTOUtils.newDTO(User.class);
        condition.setId(po.getId());
        condition.setVersion(po.getVersion());
        po.setPassword(user.getPassword());
        po.setVersion(po.getVersion() + 1);
        int i = updateSelectiveByExample(po, condition);
        if (i != 1) {
            throw new TraceBusinessException("数据已变更,请稍后重试");
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
        query.setYn(YnEnum.YES.getCode());
        return listByExample(query);
    }

    @Override
    @Transactional
    public BaseOutput updateEnable(Long id, Boolean enable) {
        long tt = redisService.getExpire(ExecutionConstants.WAITING_DISABLED_USER_PREFIX);
        User user = get(id);
        if (user == null) {
            return BaseOutput.failure("数据不存在");
        }
        if (!YnEnum.YES.getCode().equals(user.getYn())) {
            return BaseOutput.failure("数据已被删除");
        }
        if (enable) {
            user.setState(EnabledStateEnum.ENABLED.getCode());
            this.updateSelective(user);

            redisService.setRemove(ExecutionConstants.WAITING_DISABLED_USER_PREFIX, id);
        } else {
            user.setState(EnabledStateEnum.DISABLED.getCode());
            this.updateSelective(user);
            redisService.sSet(ExecutionConstants.WAITING_DISABLED_USER_PREFIX, id);
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
        this.andCondition(dto).ifPresent(str -> {
            dto.mset(IDTO.AND_CONDITION_EXPR, str);
        });
        dto.setYn(1);
        EasyuiPageOutput out = this.listEasyuiPageByExample(dto, true);
        List<DTO> users = out.getRows();
        List<Long> userIdList = users.stream().map(o -> {
            return (Long) o.get("id");
        }).collect(Collectors.toList());
        Map<Long, List<UserPlate>> userPlateMap = this.userPlateService.findUserPlateByUserIdList(userIdList);
        List<DTO> userList = users.stream().map(u -> {
            Long userId = (Long) u.get("id");
            if (userPlateMap.containsKey(userId)) {
                String plates = userPlateMap.get(userId).stream().map(UserPlate::getPlate)
                        .collect(Collectors.joining(","));
                u.put("plates", plates);
            } else {
                u.put("plates", "");
            }
            return u;
        }).collect(Collectors.toList());
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

        long tt = redisService.getExpire(ExecutionConstants.WAITING_DISABLED_USER_PREFIX);
        List<String> tallyAreaNos = Arrays.asList(StringUtils.trimToEmpty(user.getTallyAreaNos()).split(","));

        user.setState(EnabledStateEnum.DISABLED.getCode());
        user.setYn(YnEnum.NO.getCode());
        user.setIsDelete(user.getId());
        this.updateSelective(user);

        redisService.sSet(ExecutionConstants.WAITING_DISABLED_USER_PREFIX, id);

        // 删除用户车牌信息
        UserPlate up = DTOUtils.newDTO(UserPlate.class);
        up.setUserId(user.getId());
        this.userPlateService.deleteByExample(up);

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
        result.setTotalItem(Integer.parseInt(String.valueOf(page.getTotal())));
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
        String message;
        if (ValidateStateEnum.PASSED.getCode() == input.getValidateState()) {// 审核通过
            message = "用户资料审核申请已通过";
        } else if (ValidateStateEnum.NOPASS.getCode() == input.getValidateState()) {// 审核不通过
            message = "用户资料审核申请未通过";
        } else {
            message = "系统出现异常";
            return BaseOutput.failure(message);
        }

        user.setValidateState(input.getValidateState());
        int retRows = update(user);
        if (retRows > 0) {
            sendVerifyCertMessage(user, message, input.getRefuseReason(), operatorUser);
            return BaseOutput.success(message);
        } else {
            return BaseOutput.failure();
        }
    }

    private void sendVerifyCertMessage(User user, String message, String operateReason, OperatorUser operatorUser) {
        // 增加消息
        EventMessage eventMessage = new EventMessage();
        eventMessage.setTitle(message);
        eventMessage.setOverview(operateReason);

        eventMessage.setCreator(operatorUser.getName());
        eventMessage.setCreatorId(operatorUser.getId());
        eventMessage.setReceiver(user.getName());
        eventMessage.setReceiverId(user.getId());
        eventMessage.setReceiverType(UserTypeEnum.USER.getCode());

        eventMessage.setSourceBusinessId(user.getId());
        eventMessage.setSourceBusinessType(MessageStateEnum.BUSINESS_TYPE_USER.getCode());

        eventMessageService.addMessage(eventMessage);
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
            throw new TraceBusinessException("数据不存在");
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
    public List<User> findUserQrStatusChangedList() {

        Example e = new Example(User.class);
        e.and().andCondition("pre_qr_status<>qr_status");
        return this.getDao().selectByExample(e);
    }

}